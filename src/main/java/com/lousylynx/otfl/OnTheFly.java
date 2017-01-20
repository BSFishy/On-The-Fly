package com.lousylynx.otfl;

import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.library.OtflLibrary;
import com.lousylynx.otfl.library.util.FMLInjector;
import com.lousylynx.otfl.library.util.WorldEvents;
import com.lousylynx.otfl.test.RunTests;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = OnTheFly.MODID, version = OnTheFly.VERSION)
public class OnTheFly {
    public static final String MODID = "otfl";
    public static final String VERSION = "0.1";

    public static final boolean DEBUG = true;

    private static Logger logger = LogManager.getLogger(MODID);

    @Instance
    public static OnTheFly INSTANCE;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OtflLibrary.setPreInit(true);

        FMLInjector.inject();

        try {
            OtflLibrary.init();
        } catch (OtflException e) {
            e.printStackTrace();
        }

        if (DEBUG) {
            RunTests.preInit();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        OtflLibrary.setPreInit(false);
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

    public static void log(String message) {
        logf(Level.INFO, message);
    }

    public static void logf(Level level, String message, Object... replacements) {
        logger.log(level, "[OnTheFlyLibrary] " + String.format(message, (Object[]) replacements));
    }

    @EventHandler
    public void fmlMissingMappingsEvent(FMLMissingMappingsEvent event) {
        WorldEvents.instance().fmlMissingMappingsEvent(event);
    }
}
