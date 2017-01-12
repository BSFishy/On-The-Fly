package com.lousylynx.otfl.api.register;

import com.lousylynx.otfl.library.register.object.BlockObject;

public enum ObjectType {

    BLOCK(BlockObject.class), ITEM(null), ENTITY(null);

    private final Class<? extends RegistryObject> type;

    ObjectType(Class<? extends RegistryObject> type) {
        this.type = type;
    }

    public Class<? extends RegistryObject> getType() {
        return type;
    }

    public boolean instanceOf(Class<?> clazz) {
        return clazz.isInstance(type);
    }
}