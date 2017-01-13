package com.lousylynx.otfl.library.register.object;

import com.lousylynx.otfl.api.register.ObjectType;
import com.lousylynx.otfl.api.register.RegistryObject;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

// TODO: finish
public class ItemObject extends RegistryObject {

    @Getter
    private final Item block;

    @Getter
    @Setter
    private ResourceLocation resource;

    public ItemObject(Item block) {
        super(ObjectType.BLOCK);

        this.block = block;
    }

    public ItemObject(Item block, ResourceLocation resource) {
        this(block);

        this.resource = resource;
    }

    @Override
    public void register() {
        if (resource == null) {
            GameRegistry.register(block);
        } else {
            GameRegistry.register(block, resource);
        }
    }
}
