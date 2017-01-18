package com.lousylynx.otfl.library.register.object;

import com.lousylynx.otfl.api.register.ObjectType;
import com.lousylynx.otfl.api.register.RegistryObject;
import com.lousylynx.otfl.library.OtflLibrary;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

// TODO: finish
public class ItemObject extends RegistryObject {

    @Getter
    private final Item item;

    @Setter
    private ResourceLocation resource;

    public ItemObject(Item item) {
        super(ObjectType.BLOCK);

        this.item = item;
    }

    public ItemObject(Item block, ResourceLocation resource) {
        this(block);

        this.resource = resource;
    }

    @Override
    public void register(int flags) {
        OtflLibrary.instance().getAddManager().add(this, flags);
    }

    @Override
    public boolean hasResource() {
        return resource != null;
    }

    @Override
    public ResourceLocation getResource() {
        return hasResource() ? resource : null;
    }

    @Override
    public IForgeRegistryEntry<?> getObject() {
        return item;
    }
}
