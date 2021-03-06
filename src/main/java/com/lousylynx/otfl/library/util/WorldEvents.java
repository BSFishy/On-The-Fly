package com.lousylynx.otfl.library.util;

import com.lousylynx.otfl.OnTheFly;
import com.lousylynx.otfl.api.OtflFlags;
import com.lousylynx.otfl.api.register.RegistryObject;
import com.lousylynx.otfl.library.OtflLibrary;
import com.lousylynx.otfl.library.register.object.BlockObject;
import com.lousylynx.otfl.library.register.object.ItemObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a class full of events that have to do
 * with fixing various things having to do with
 * loading worlds, saving worlds, etc.
 */
public class WorldEvents {

    private static WorldEvents instance;
    private final List<RegistryObject> remappedObjects = new ArrayList<>();

    /**
     * Register this class of events to
     * the {@link MinecraftForge#EVENT_BUS}
     */
    public static void register() {
        if (instance == null)
            MinecraftForge.EVENT_BUS.register(instance = new WorldEvents());
    }

    /**
     * Get the instance of this class
     *
     * @return the instance of this class
     */
    public static WorldEvents instance() {
        if (instance == null)
            register();

        return instance;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        try {
            if (!remappedObjects.isEmpty()) {
                Iterator<RegistryObject> objectIterator = remappedObjects.iterator();

                while (objectIterator.hasNext()) {
                    RegistryObject object = objectIterator.next();

                    FMLInjector.removeAlias(object);
                    objectIterator.remove();
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This event is called whenever there is a mapping
     * missing. This is normally caused by this mod, so
     * it checks all of the registered objects, to see
     * if it <b>is</b> one of the registered objects,
     * and if it is, it remaps it.<br><br>
     * <b>You should never be calling this method
     * yourself. This method is called by the OTFL
     * mod itself.</b>
     *
     * @param event the missing mappings event
     */
    public void fmlMissingMappingsEvent(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.getAll()) {
            if (OtflLibrary.instance().getAddedObjectNames().contains(new ResourceLocation(mapping.name))) {
                RegistryObject object = OtflLibrary.instance().getRegistryObjectFromName(mapping.name);
                if (remappedObjects.contains(object))
                    continue;

                if (object instanceof ItemObject || object instanceof BlockObject) {
                    object.register(OtflFlags.Registration.USE_GAMEDATA);

                    try {
                        FMLInjector.setMissingMappingData(mapping, FMLMissingMappingsEvent.Action.REMAP, object.getObject());
                        if (object instanceof BlockObject)
                            FMLInjector.setMissingMappingData(mapping, FMLMissingMappingsEvent.Action.REMAP, ((BlockObject) object).getItemObject());

                        remappedObjects.add(object);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        OnTheFly.logf(Level.WARN, "Unable to remap %s", mapping.name);
                        e.printStackTrace();
                        return;
                    }

                    OnTheFly.logf(Level.INFO, "Successfully remapped %s", mapping.name);
                } else {
                    OnTheFly.logf(Level.WARN, "Unable to remap %s. It is not an item or a block.", mapping.name);
                }
            }
        }
    }
}
