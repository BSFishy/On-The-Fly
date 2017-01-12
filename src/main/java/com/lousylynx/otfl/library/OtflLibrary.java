package com.lousylynx.otfl.library;

import com.lousylynx.otfl.api.OtflApi;
import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.api.register.BasicRegister;
import com.lousylynx.otfl.library.register.BlockRegister;
import com.lousylynx.otfl.library.register.EntityRegister;
import com.lousylynx.otfl.library.register.ItemRegister;

import java.util.List;

public class OtflLibrary extends OtflApi {

    private static OtflLibrary INSTANCE;

    private OtflLibrary() { }

    @Override
    public void initialize() {
        addRegister(new ItemRegister());
        addRegister(new BlockRegister());
        addRegister(new EntityRegister());
    }

    /**
     * Initialize the library
     * @throws OtflException if the library has already been initialized
     */
    public static void init() throws OtflException {
        checkInit();

        INSTANCE = new OtflLibrary();
        INSTANCE.initialize();
    }

    /**
     * Get the list of all of the registries
     * @return the list of registries
     * @throws OtflException if the library has already been initialized
     */
    public static List<BasicRegister> getRegisteries() throws OtflException {
        checkInit();

        return INSTANCE.getRegisters();
    }

    public static void addRegistry(BasicRegister register) throws OtflException {
        checkInit();

        INSTANCE.addRegister(register);
    }

    /**
     * Update the api, to register the objects
     * @throws OtflException if the library has already been initialized
     */
    public static void register() throws OtflException {
        checkInit();

        INSTANCE.update();
    }

    private static void checkInit() throws OtflException {
        if(INSTANCE != null)
            throw new OtflException("On The Fly Library cannot be initialized multiple times");
    }
}
