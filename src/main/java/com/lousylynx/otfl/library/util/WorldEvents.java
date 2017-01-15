package com.lousylynx.otfl.library.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEvents {

    private static WorldEvents instance;

    public static void register() {
        if (instance == null)
            MinecraftForge.EVENT_BUS.register(instance = new WorldEvents());
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        event.getWorld().getWorldInfo();
    }
}
