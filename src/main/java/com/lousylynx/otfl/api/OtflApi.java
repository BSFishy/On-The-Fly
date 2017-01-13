package com.lousylynx.otfl.api;

import com.lousylynx.otfl.api.register.BasicRegister;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class OtflApi {

    private List<BasicRegister> registers = new ArrayList<>();

    /**
     * Add the initial contents to the list of registers
     */
    public abstract void initialize();

    /**
     * Updates the registers, to add the registered objects
     */
    public void update() throws OtflException {
        for (BasicRegister register : registers) {
            register.update();
        }
    }

    /**
     * Adds a register to the list of registers
     *
     * @param register the register to add
     */
    public void addRegister(BasicRegister register) {
        registers.add(register);
    }
}
