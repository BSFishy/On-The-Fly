package com.lousylynx.otfl.library.register.object;

import com.lousylynx.otfl.api.register.ObjectType;
import com.lousylynx.otfl.api.register.RegistryObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

// TODO: finish
public class EntityObject extends RegistryObject {
    public EntityObject() {
        super(ObjectType.ENTITY);
    }

    @Override
    public boolean hasResource() {
        return false;
    }

    @Override
    public ResourceLocation getResource() {
        return null;
    }

    @Override
    public IForgeRegistryEntry<?> getObject() {
        return null;
    }
}
