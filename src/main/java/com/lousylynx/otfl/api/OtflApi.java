package com.lousylynx.otfl.api;

import com.lousylynx.otfl.api.register.BasicRegister;
import com.lousylynx.otfl.api.register.RegistryObject;
import lombok.Data;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class OtflApi {

    private List<BasicRegister> registers = new ArrayList<>();

    /**
     * Add the initial contents to the list of registers
     */
    public abstract void initialize();

    public abstract void register(RegistryObject object);

    /**
     * Register an object from an {@link IForgeRegistryEntry}. <br>
     * That means that it can be anything registerable from
     * the OTFL library, such as an item or a block.
     *
     * @param object the {@link IForgeRegistryEntry} to register
     */
    public abstract void register(IForgeRegistryEntry<?> object);
/*
    /*
     * Updates the registers, to add the registered objects
     *//*
    public void update() throws OtflException {
        for (BasicRegister register : registers) {
            register.update();
        }
    }*/


    /**
     * Register a {@link BasicRegister}. This should only
     * be done if there is a custom object that should
     * be able to be registered.
     *
     * @param register the {@link BasicRegister} to register
     */
    public void addRegister(BasicRegister register) {
        registers.add(register);
    }

    /**
     * Get a list of all of the registered registers
     *
     * @return the list of registers
     */
    public List<BasicRegister> getRegisters() {
        return registers;
    }
}
