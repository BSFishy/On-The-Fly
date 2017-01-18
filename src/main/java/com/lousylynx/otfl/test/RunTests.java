package com.lousylynx.otfl.test;

import com.lousylynx.otfl.library.OtflLibrary;
import com.lousylynx.otfl.library.register.object.BlockObject;
import com.lousylynx.otfl.library.register.object.ItemObject;
import com.lousylynx.otfl.test.object.ExampleBlock;
import com.lousylynx.otfl.test.object.ExampleItem;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class RunTests {
    public static void start(FMLServerStartingEvent event) {
        event.registerServerCommand(new RegisterCommand());
    }

    public static void preInit() {
        OtflLibrary.instance().add(new BlockObject(new ExampleBlock()));
        OtflLibrary.instance().add(new ItemObject(new ExampleItem()));
    }
}
