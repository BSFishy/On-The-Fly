package com.lousylynx.otfl.test;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class RunTests {
    public static void start(FMLServerStartingEvent event) {
        event.registerServerCommand(new RegisterCommand());
    }
}
