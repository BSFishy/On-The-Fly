package com.lousylynx.otfl;

import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.library.OtflLibrary;
import com.lousylynx.otfl.test.RunTests;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = OnTheFly.MODID, version = OnTheFly.VERSION)
public class OnTheFly {
    public static final String MODID = "otfl";
    public static final String VERSION = "0.1";

    public static final boolean DEBUG = true;

    @Instance
    public static OnTheFly INSTANCE;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            OtflLibrary.init();
        } catch (OtflException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        if (DEBUG) {
            RunTests.start(event);
        }
    }
}
