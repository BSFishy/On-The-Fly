package com.lousylynx.otfl.api.register;

import lombok.Data;

@Data
public abstract class RegistryObject {

    private final ObjectType type;
    private boolean added = false;

    /**
     * Register the object to the runtime
     */
    public abstract void register();
}
