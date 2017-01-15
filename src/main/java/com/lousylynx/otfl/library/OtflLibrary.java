package com.lousylynx.otfl.library;

import com.lousylynx.otfl.api.OtflApi;
import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.api.OtflFlags;
import com.lousylynx.otfl.api.register.RegistryObject;
import com.lousylynx.otfl.library.register.BlockRegister;
import com.lousylynx.otfl.library.register.EntityRegister;
import com.lousylynx.otfl.library.register.ItemRegister;
import com.lousylynx.otfl.library.util.AddingManager;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtflLibrary extends OtflApi {

    private static OtflLibrary INSTANCE;
    private AddingManager addManager;
    @Getter
    private List<ResourceLocation> addedObjectNames = new ArrayList<>();
    @Getter
    private Map<ResourceLocation, RegistryObject> addedObjects = new HashMap<>();

    private OtflLibrary() {
        addManager = new AddingManager();
    }

    @Override
    public void initialize() {
        addRegister(new ItemRegister());
        addRegister(new BlockRegister());
        addRegister(new EntityRegister());
    }

    public static OtflLibrary instance() {
        if (INSTANCE == null) {
            try {
                init();
            } catch (OtflException e) {
                e.printStackTrace();
            }
        }

        return INSTANCE;
    }

    @Override
    public void register(RegistryObject object) {
        register(object, OtflFlags.Registration.USE_FOUND);
    }

    @Override
    public void register(IForgeRegistryEntry<?> object) {
        try {
            instance().register(RegistryObject.fromRegistryEntry(object));
        } catch (OtflException e) {
            e.printStackTrace();
        }
    }

    public void register(RegistryObject object, int flags){
        instance().addManager.add(object, flags);
    }

    public void register(IForgeRegistryEntry<?> object, int flags) {
        try {
            instance().register(RegistryObject.fromRegistryEntry(object), flags);
        } catch (OtflException e) {
            e.printStackTrace();
        }
    }

    /**
     * This updates the {@link OtflLibrary#addedObjectNames} and
     * {@link OtflLibrary#addedObjects} fields. This should only
     * be done unless you know exactly what you're doing.
     *
     * @param object the object to update
     */
    public void updateObject(RegistryObject object) {
        if (!instance().addedObjectNames.contains(object.getObject().getRegistryName()))
            instance().addedObjectNames.add(object.getObject().getRegistryName());

        instance().addedObjects.put(object.getObject().getRegistryName(), object);
    }

    /**
     * Get a {@link RegistryObject} from its name from the list
     * of registered objects
     *
     * @param name the name of the object
     * @return the object
     */
    @Nullable
    public RegistryObject getRegistryObjectFromName(String name) {
        ResourceLocation loc = new ResourceLocation(name);
        if (!instance().addedObjectNames.contains(loc))
            return null;

        return instance().addedObjects.get(loc);
    }

    /**
     * Initialize the library
     *
     * @throws OtflException if the library has already been initialized
     */
    public static void init() throws OtflException {
        checkInit();

        INSTANCE = new OtflLibrary();
        INSTANCE.initialize();
    }

    private static void checkInit() throws OtflException {
        if (INSTANCE != null)
            throw new OtflException("On The Fly Library cannot be initialized multiple times");
    }
}
