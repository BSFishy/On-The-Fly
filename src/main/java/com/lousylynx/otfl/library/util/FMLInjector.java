package com.lousylynx.otfl.library.util;


import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.lousylynx.otfl.OnTheFly;
import com.lousylynx.otfl.api.register.RegistryObject;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.*;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class injects itself into the Minecraft source code,
 * to retrieve objects required to fix specific errors
 * that have to do with registries.
 */
public class FMLInjector {

    private static boolean injected = false;
    @Getter
    private static Method finderMethod = null;
    @Getter
    private static Method addMethod = null;

    /**
     * This method should never be called by an addon to the api.
     * This is the method that begins the injection into the code.
     * This method is called by the OnTheFly main class,
     * and will throw an error if you try to call it again.
     */
    public static void inject() {
        Preconditions.checkArgument(!injected, "Cannot inject multiple times");
        injected = true;

        Class<PersistentRegistryManager> persistentRegistry = PersistentRegistryManager.class;
        for (Method m : persistentRegistry.getDeclaredMethods()) {
            if (m.getName().equals("findRegistry")) {
                m.setAccessible(true);

                finderMethod = m;
                OnTheFly.log("Successfully injected into PersistentRegistryManager");
                break;
            }
        }

        if (finderMethod == null) {
            OnTheFly.log("There was an error injecting into PersistentRegistryManager");
        }

        injectPersistentRegistry();
        Class<FMLControlledNamespacedRegistry> controlledRegistry = FMLControlledNamespacedRegistry.class;
        for (Method m : controlledRegistry.getDeclaredMethods()) {
            if (m.getName().equals("add")) {
                m.setAccessible(true);

                addMethod = m;
                OnTheFly.log("Successfully injected into FMLControlledNamespacedRegistry");
                break;
            }
        }

        if (addMethod == null) {
            OnTheFly.log("There was an error injecting into FMLControlledNamespacedRegistry");
        }
    }

    /**
     * This gets the availability map for a specific registry.
     * The registry <b>must</b> be an instance of {@link FMLControlledNamespacedRegistry}
     *
     * @param registry the registry to get the map from
     * @return the availability map from that specific class
     */
    public static BitSet getAvailabilityMap(IForgeRegistry<?> registry) {
        Preconditions.checkArgument(registry instanceof FMLControlledNamespacedRegistry, "The registry must be a controlled namespaced registry");

        FMLControlledNamespacedRegistry r = (FMLControlledNamespacedRegistry) registry;

        return (BitSet) getAvailabilityMapField(r);
    }

    private static Object getAvailabilityMapField(FMLControlledNamespacedRegistry registry) {
        Class<? extends FMLControlledNamespacedRegistry> clazz = registry.getClass();

        try {
            Field mapTmp = clazz.getDeclaredField("availabilityMap");
            mapTmp.setAccessible(true);

            return mapTmp.get(registry);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a registry from a registry object.
     *
     * @param entry the object to get the registry from
     * @return the registry
     */
    public static IForgeRegistry<?> findRegistry(IForgeRegistryEntry<?> entry) {
        try {
            Object value = getFinderMethod().invoke(null, entry);
            return (IForgeRegistry) value;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add a object to the registries.
     * This should normally only be used from within
     * the OTFL code, unless you know exactly what you are doing.
     *
     * @param id    the id of the object
     * @param name  the {@link ResourceLocation} of the object to add
     * @param thing the object to add
     * @return the resulting id of the registered object
     */
    public static int add(int id, ResourceLocation name, Object thing) {
        try {
            IForgeRegistryEntry castedObj = (IForgeRegistryEntry) thing;
            if (castedObj.getRegistryName() == null) {
                OnTheFly.logf(Level.ERROR, "Attempt to register object without having set a registry name %s (type %s)", thing, thing.getClass().getName());
                throw new IllegalArgumentException(String.format("No registry name set for object %s (%s)", new Object[]{thing, thing.getClass().getName()}));
            }

            IForgeRegistry r = findRegistry(castedObj);
            return (int) getAddMethod().invoke(r, id, name, thing);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void injectPersistentRegistry() {
        boolean success = true;
        try {
            for (FMLControlledNamespacedRegistry<?> r : getAllRegistries()) {
                if (r == null) {
                    success = false;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            OnTheFly.log("There was an issue with injecting into registries");
            e.printStackTrace();
            return;
        }

        if (success) {
            OnTheFly.log("Successfully injected into registries");
        } else {
            OnTheFly.log("There was an issue with injecting into registries");
        }
    }

    /**
     * Get all of the registries for everything in the game
     *
     * @return a {@link List} of all of the registries
     * @throws NoSuchFieldException   if there is no "registries" field in the registry
     * @throws IllegalAccessException if there is an error with accessing the "registries" field
     */
    public static Set<FMLControlledNamespacedRegistry<?>> getAllRegistries() throws NoSuchFieldException, IllegalAccessException {
        return getAllRegistriesAndNames().values();
    }

    /**
     * Get all of the registries and their {@link ResourceLocation} names
     *
     * @return a {@link BiMap} of all of the registries and their names
     * @throws NoSuchFieldException   if there is an error getting the registries
     * @throws IllegalAccessException if there is an error getting the registries
     */
    public static BiMap<ResourceLocation, FMLControlledNamespacedRegistry<?>> getAllRegistriesAndNames() throws NoSuchFieldException, IllegalAccessException {
        Class<PersistentRegistryManager> prm = PersistentRegistryManager.class;
        BiMap<ResourceLocation, FMLControlledNamespacedRegistry<?>> returnValue = HashBiMap.create();
        for (Class c : prm.getDeclaredClasses()) {
            if (c.getName().equals("net.minecraftforge.fml.common.registry.PersistentRegistryManager$PersistentRegistry")) {
                for (Object e : c.getEnumConstants()) {
                    Class val = e.getClass();

                    Field registries = val.getDeclaredField("registries");
                    registries.setAccessible(true);

                    BiMap<ResourceLocation, FMLControlledNamespacedRegistry<?>> value = (BiMap<ResourceLocation, FMLControlledNamespacedRegistry<?>>) registries.get(e);

                    returnValue.putAll(value);
                }
            }
        }

        return returnValue;
    }

    /**
     * Get the {@link FMLControlledNamespacedRegistry#minId} field value
     * from an instance
     *
     * @param registry the instance of the class to get the value from
     * @return the value of the {@link FMLControlledNamespacedRegistry#minId} field
     * @throws NoSuchFieldException   if there was an error getting the value
     * @throws IllegalAccessException if there was an error getting the value
     */
    public static int getMinId(FMLControlledNamespacedRegistry<?> registry) throws NoSuchFieldException, IllegalAccessException {
        Class<FMLControlledNamespacedRegistry> registryClass = FMLControlledNamespacedRegistry.class;

        Field minId = registryClass.getDeclaredField("minId");
        minId.setAccessible(true);

        return (int) minId.get(registry);
    }

    // TODO: don't inject here, inject into GameData#iBlockRegistry etc
    public static void setMissingMappingData(MissingMapping i, FMLMissingMappingsEvent.Action a, Object t) throws NoSuchFieldException, IllegalAccessException{
        Class<MissingMapping> missingMappingClass = MissingMapping.class;

        Field action = missingMappingClass.getDeclaredField("action");
        action.setAccessible(true);

        Field target = missingMappingClass.getDeclaredField("target");
        target.setAccessible(true);

        action.set(i, a);

        target.set(i, t);
    }

    private static GameData getGameData() throws NoSuchFieldException, IllegalAccessException {
        Class<GameData> clazz = GameData.class;

        Field instance = clazz.getDeclaredField("mainData");
        instance.setAccessible(true);

        return (GameData) instance.get(null);
    }

    /**
     * This will get the {@link GameData#iBlockRegistry}
     * field from the {@link GameData} class
     * @return the {@link GameData#iBlockRegistry} field value
     * @throws NoSuchFieldException if there was an error getting the field
     * @throws IllegalAccessException if there was an error getting the field
     */
    public static FMLControlledNamespacedRegistry<?> getGameDataBlockRegistry() throws NoSuchFieldException, IllegalAccessException {
        GameData instance = getGameData();
        Class<? extends GameData> clazz = instance.getClass();

        Field registry = clazz.getDeclaredField("iBlockRegistry");
        registry.setAccessible(true);

        return (FMLControlledNamespacedRegistry<?>) registry.get(instance);
    }

    /**
     * This will get the {@link GameData#iItemRegistry}
     * field from the {@link GameData} class
     * @return the {@link GameData#iItemRegistry} field value
     * @throws NoSuchFieldException if there was an error getting the field
     * @throws IllegalAccessException if there was an error getting the field
     */
    public static FMLControlledNamespacedRegistry<?> getGameDataItemRegistry() throws NoSuchFieldException, IllegalAccessException {
        GameData instance = getGameData();
        Class<? extends GameData> clazz = instance.getClass();

        Field registry = clazz.getDeclaredField("iItemRegistry");
        registry.setAccessible(true);

        return (FMLControlledNamespacedRegistry<?>) registry.get(instance);
    }

    /**
     * This method will remove a {@link RegistryObject}
     * from any aliases it might have.
     * @param object the {@link RegistryObject} to remove the aliases from
     * @throws NoSuchFieldException if there was an error changing the value
     * @throws IllegalAccessException if there was an error changing the value
     */
    public static void removeAlias(RegistryObject object) throws NoSuchFieldException, IllegalAccessException {
        IForgeRegistry<?> registryTmp = findRegistry(object.getObject());
        Preconditions.checkArgument(registryTmp instanceof  FMLControlledNamespacedRegistry<?>, "The registry is not the right type");

        FMLControlledNamespacedRegistry<?> registry = (FMLControlledNamespacedRegistry) registryTmp;
        Class<FMLControlledNamespacedRegistry> clazz = FMLControlledNamespacedRegistry.class;

        Field aliasesField = clazz.getDeclaredField("aliases");
        aliasesField.setAccessible(true);

        Map<ResourceLocation, ResourceLocation> aliases = (Map<ResourceLocation, ResourceLocation>) aliasesField.get(registry);
        aliases.remove(object.getObject().getRegistryName());

        aliasesField.set(registry, aliases);
    }
}
