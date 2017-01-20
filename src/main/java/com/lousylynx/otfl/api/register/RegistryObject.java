package com.lousylynx.otfl.api.register;

import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.api.OtflFlags;
import com.lousylynx.otfl.library.OtflLibrary;
import com.lousylynx.otfl.library.register.object.BlockObject;
import com.lousylynx.otfl.library.register.object.ItemObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

@Data
public abstract class RegistryObject {

    private final ObjectType type;

    @Getter
    @Setter
    private int id = -1;

    /**
     * This registers this instance of this object to the registry
     */
    public void register() {
        register(OtflFlags.Registration.USE_FOUND);
    }

    /**
     * @param flags
     */
    public abstract void register(int flags);

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
        RegistryObject tmp = OtflLibrary.instance().getAddedObjects().get(impl.getRegistryName());
        if (impl instanceof Block) {
            return tmp != null ? tmp : new BlockObject((Block) impl);
        } else if (impl instanceof Item) {
            return tmp != null ? tmp : new ItemObject((Item) impl);
        } else if (tmp != null) {
            return tmp;
        } else {
            throw new OtflException("The object %s(%s) is not supported yet", impl, impl.getClass().getPackage());
        }
    }
}
