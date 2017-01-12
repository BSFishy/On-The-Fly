package com.lousylynx.otfl.api.register;

import com.lousylynx.otfl.api.OtflException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class BasicRegister {

    /**
     * A list of all of the registered objects
     */
    private final List<RegistryObject> registeredObjects = new ArrayList<>();
    private final ObjectType type;

    /**
     * Registers a {@link RegistryObject} to be in the runtime
     *
     * @param object the object to register
     */
    public void register(RegistryObject object) throws OtflException {
        if (object.getType().instanceOf(type.getType())) {
            registeredObjects.add(object);
        } else {
            throw new OtflException("An object of the incorrect type was attempted to be added");
        }
    }

    /**
     * Updates all of the registered objects to be added to the runtime
     */
    public void update() throws OtflException {
        for (RegistryObject object : registeredObjects) {
            if (object.getType().instanceOf(getType().getType())) {
                if(!object.isAdded()) {
                    object.register();
                    object.setAdded(true);
                }
            } else {
                throw new OtflException("An object of the incorrect type was attempted to be added");
            }
        }
    }
}
