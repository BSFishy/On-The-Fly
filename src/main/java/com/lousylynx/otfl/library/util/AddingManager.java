package com.lousylynx.otfl.library.util;

import com.google.common.base.Preconditions;
import com.lousylynx.otfl.OnTheFly;
import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.api.register.RegistryObject;
import com.lousylynx.otfl.library.OtflLibrary;
import com.lousylynx.otfl.library.register.object.BlockObject;
import com.lousylynx.otfl.library.register.object.ItemObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import org.apache.logging.log4j.Level;

import java.util.BitSet;

/**
 * This is mainly used in the OTFL library, and
 * should never be called anywhere else, unless
 * there is a special exception to add custom
 * objects, or something else.
 */
public class AddingManager {

    public AddingManager() {
    }

    /**
     * Add an object to the game, and replace the object
     * if it has already been registered
     *
     * @param object the object to add
     */
    public void add(RegistryObject object) {
        IForgeRegistry<?> registryTmp = FMLInjector.findRegistry(object.getObject());
        Preconditions.checkArgument(registryTmp instanceof FMLControlledNamespacedRegistry, "The registry is not an FMLControlledNamespacedRegistry");

        FMLControlledNamespacedRegistry registry = (FMLControlledNamespacedRegistry) registryTmp;

        int desiredId = object.getId() >= 0 ? object.getId() : registry.getId(object.getObject());
        BitSet availabilityMap = FMLInjector.getAvailabilityMap(registry);
        if (shouldReplace(availabilityMap, desiredId)) {
            availabilityMap.set(desiredId, false);
        }

        int idToUse;
        try {
            idToUse = desiredId >= 0 ? desiredId : availabilityMap.nextClearBit(FMLInjector.getMinId(registry));

            FMLInjector.add(idToUse, object.getObject().getRegistryName(), object.getObject());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            OnTheFly.logf(Level.ERROR, "There was an error registering the object: %s(%s)", object.getObject().getClass().getName(), object.getObject().getClass().getPackage());
            e.printStackTrace();
            return;
        }

        if (object.getId() != idToUse) {
            OnTheFly.logf(Level.INFO, "The object %s got a new id. Previous id: %s New id: %s", object.getObject().getRegistryName(), object.getId(), idToUse);
            object.setId(idToUse);
        }

        OtflLibrary.instance().updateObject(object);
        OnTheFly.logf(Level.INFO, "Successfully registered %s(%s) with the ID of %s", object.getObject().getRegistryName(), object.getObject().getClass().getName(),
                idToUse);
    }

    private boolean shouldReplace(BitSet availabilityMap, int desiredId) {
        return desiredId >= 0 && availabilityMap.get(desiredId);
    }

    /**
     * Get a {@link ResourceLocation} in the {@link PersistentRegistryManager} class
     * from a {@link RegistryObject}
     *
     * @param object the {@link RegistryObject} to get the {@link ResourceLocation} from
     * @return a {@link ResourceLocation} in the {@link PersistentRegistryManager} class
     * @throws OtflException if the {@link RegistryObject} specified is not implemented yet
     */
    public static ResourceLocation getNameFromObject(RegistryObject object) throws OtflException {
        if (object instanceof BlockObject) {
            return PersistentRegistryManager.BLOCKS;
        } else if (object instanceof ItemObject) {
            return PersistentRegistryManager.ITEMS;
        } else {
            throw new OtflException("The object %s(%s) is not implemented yet", object.getObject().getClass().getName(), object.getObject().getClass().getPackage());
        }
    }
}
