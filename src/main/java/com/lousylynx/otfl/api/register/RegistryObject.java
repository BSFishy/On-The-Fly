package com.lousylynx.otfl.api.register;

import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.library.register.object.BlockObject;
import com.lousylynx.otfl.library.register.object.ItemObject;
import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

@Data
public abstract class RegistryObject {

    private final ObjectType type;

    /**
     * If the object has a {@link ResourceLocation}
     *
     * @return if the object has a {@link ResourceLocation}
     */
    public abstract boolean hasResource();

    /**
     * Get the {@link ResourceLocation} if
     * the object has one
     *
     * @return the object's {@link ResourceLocation}
     */
    public abstract ResourceLocation getResource();

    /**
     * Get the object that this class represents
     *
     * @return the object this class represents
     */
    public abstract IForgeRegistryEntry<?> getObject();

    /**
     * Get a {@link RegistryObject} from a {@link IForgeRegistryEntry}
     *
     * @param impl the {@link IForgeRegistryEntry} to get the object from
     * @return the {@link RegistryObject}
     * @throws OtflException if the {@link IForgeRegistryEntry} is not yet supported
     */
    public static RegistryObject fromRegistryEntry(IForgeRegistryEntry<?> impl) throws OtflException {
        if (impl instanceof Block) {
            return new BlockObject((Block) impl);
        } else if (impl instanceof Item) {
            return new ItemObject((Item) impl);
        } else {
            throw new OtflException("The object %s(%s) is not supported yet", impl, impl.getClass().getPackage());
        }
    }
}
