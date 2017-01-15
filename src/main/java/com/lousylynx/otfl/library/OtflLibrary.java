package com.lousylynx.otfl.library;

import com.lousylynx.otfl.api.OtflApi;
import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.api.register.RegistryObject;
import com.lousylynx.otfl.library.register.BlockRegister;
import com.lousylynx.otfl.library.register.EntityRegister;
import com.lousylynx.otfl.library.register.ItemRegister;
import com.lousylynx.otfl.library.util.AddingManager;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class OtflLibrary extends OtflApi {

    private static OtflLibrary INSTANCE;
    private static AddingManager addManager;

    private OtflLibrary() {
        addManager = new AddingManager();
    }

    @Override
    public void initialize() {
        addRegister(new ItemRegister());
        addRegister(new BlockRegister());
        addRegister(new EntityRegister());
    }

    public static OtflLibrary instance() {
        return INSTANCE;
    }

    @Override
    public void register(RegistryObject object) {
        addManager.add(object);
    }

    @Override
    public void register(IForgeRegistryEntry<?> object) {
        try {
            register(RegistryObject.fromRegistryEntry(object));
        } catch (OtflException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the library
     *
     * @throws OtflException if the library has already been initialized
     */
    public static void init() throws OtflException {
        checkInit();

        INSTANCE = new OtflLibrary();
        INSTANCE.initialize();
    }

    private static void checkInit() throws OtflException {
        if (INSTANCE != null)
            throw new OtflException("On The Fly Library cannot be initialized multiple times");
    }
}
