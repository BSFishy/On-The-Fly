package com.lousylynx.otfl.library.register.object;

import com.lousylynx.otfl.api.register.ObjectType;
import com.lousylynx.otfl.api.register.RegistryObject;
import com.lousylynx.otfl.library.OtflLibrary;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

// TODO: finish
public class BlockObject extends RegistryObject {

    @Getter
    private final Block block;

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
    public void register(int flags) {
        OtflLibrary.instance().getAddManager().add(this, flags);
        OtflLibrary.instance().getAddManager().add(getItemObject());
    }

    public Item getItem() {
        return new ItemBlock((Block) getObject()).setRegistryName(getObject().getRegistryName());
    }

    public ItemObject getItemObject() {
        return new ItemObject(getItem());
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
        return block;
    }
}
