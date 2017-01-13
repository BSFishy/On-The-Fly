package com.lousylynx.otfl.api.register;

import com.lousylynx.otfl.api.OtflException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class BasicRegister<T extends RegistryObject> {

    /**
     * A list of all of the registered objects
     */
    private final List<T> registeredObjects = new ArrayList<>();

    /**
     * Registers a {@link RegistryObject} to be in the runtime
     *
     * @param object the object to register
     */
    public void register(T object) throws OtflException {
        registeredObjects.add(object);
    }

    /**
     * Updates all of the registered objects to be added to the runtime
     */
    public void update() throws OtflException {
        for (RegistryObject object : registeredObjects) {
            if (!object.isAdded()) {
                object.register();
                object.setAdded(true);
            }
        }
    }
}
