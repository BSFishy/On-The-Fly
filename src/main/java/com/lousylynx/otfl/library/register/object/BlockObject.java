package com.lousylynx.otfl.library.register.object;

import com.lousylynx.otfl.api.register.ObjectType;
import com.lousylynx.otfl.api.register.RegistryObject;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

// TODO: finish
public class BlockObject extends RegistryObject {

    @Getter
    private final Block block;

    @Getter
    @Setter
    private ResourceLocation resource;

    public BlockObject(Block block) {
        super(ObjectType.BLOCK);

        this.block = block;
    }

    public BlockObject(Block block, ResourceLocation resource) {
        this(block);

        this.resource = resource;
    }

    @Override
    public void register() {
        if (resource == null) {
            GameRegistry.register(block);
            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        } else {
            GameRegistry.register(block, resource);
            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }
}
