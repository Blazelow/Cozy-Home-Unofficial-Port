package net.luckystudio.cozyhome.client;

import com.google.common.collect.Sets;
import net.luckystudio.cozyhome.CozyHome;
import java.util.Set;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
public class ModEntityModelLayers {

    private static final String MAIN = "main";
    private static final Set<ModelLayerLocation> LAYERS = Sets.newHashSet();

    public static final ModelLayerLocation SEAT = registerMain("seat");
    public static final ModelLayerLocation TELESCOPE = registerMain("telescope");
    public static final ModelLayerLocation SOFA = registerMain("sofa");
    public static final ModelLayerLocation COUCH_CUSHION = registerMain("couch_cushion");
    public static final ModelLayerLocation CHAIR = registerMain("chair");
    public static final ModelLayerLocation CUSHION = registerMain("cushion");
    public static final ModelLayerLocation SOFA_CUSHION = registerMain("sofa_cushion");
    public static final ModelLayerLocation GRANDFATHER_CLOCK = registerMain("grandfather_clock");
    public static final ModelLayerLocation WALL_CLOCK = registerMain("wall_clock");
    public static final ModelLayerLocation BATHTUB_LIQUID = registerMain("bathtub_liquid");

    private static ModelLayerLocation registerMain(String id) {
        return register(id, MAIN);
    }

    private static ModelLayerLocation register(String id, String layer) {
        ModelLayerLocation entityModelLayer = create(id, layer);
        if (!LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            return entityModelLayer;
        }
    }

    private static ModelLayerLocation create(String id, String layer) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CozyHome.MOD_ID, id), layer);
    }

    // Registering EntityModelLayers
    public static void registerEntityModelLayers(){
        CozyHome.LOGGER.info("Registering ModBlockEntityModels for " + CozyHome.MOD_ID);
    }
}
