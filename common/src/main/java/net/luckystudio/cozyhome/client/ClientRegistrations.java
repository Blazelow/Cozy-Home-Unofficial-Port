package net.luckystudio.cozyhome.client;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import net.luckystudio.cozyhome.CozyHome;
import net.luckystudio.cozyhome.block.custom.clocks.grandfather_clock.GrandfatherClockModel;
import net.luckystudio.cozyhome.block.custom.clocks.wall_clock.WallClockModel;
import net.luckystudio.cozyhome.block.custom.seatable.chairs.ChairModel;
import net.luckystudio.cozyhome.block.custom.seatable.couches.CouchCushionModel;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaCushionModel;
import net.luckystudio.cozyhome.block.custom.seatable.sofas.SofaModel;
import net.luckystudio.cozyhome.block.custom.telescope.TelescopeModel;
import net.luckystudio.cozyhome.entity.model.CushionModel;
import net.luckystudio.cozyhome.entity.model.SeatEntityModel;
import net.luckystudio.cozyhome.item.renderer.BathtubSpecialRenderer;
import net.luckystudio.cozyhome.item.renderer.ChairSpecialRenderer;
import net.luckystudio.cozyhome.item.renderer.SofaSpecialRenderer;
import net.luckystudio.cozyhome.item.renderer.WallClockSpecialRenderer;

/** The client registrations that are identical on every loader; the loader module supplies the actual registry. */
public class ClientRegistrations {

    public interface LayerRegistrar {
        void register(ModelLayerLocation layer, Supplier<LayerDefinition> definition);
    }

    public interface SpecialRendererRegistrar {
        void register(Identifier id, MapCodec<? extends SpecialModelRenderer.Unbaked<?>> codec);
    }

    public static void layerDefinitions(LayerRegistrar registrar) {
        registrar.register(ModEntityModelLayers.SEAT, SeatEntityModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.TELESCOPE, TelescopeModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.SOFA, SofaModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.SOFA_CUSHION, SofaCushionModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.COUCH_CUSHION, CouchCushionModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.CHAIR, ChairModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.CUSHION, CushionModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.GRANDFATHER_CLOCK, GrandfatherClockModel::getTexturedModelData);
        registrar.register(ModEntityModelLayers.WALL_CLOCK, WallClockModel::getTexturedModelData);
    }

    /** Item model types used by assets/cozyhome/items/*.json for the items that are drawn from code. */
    public static void specialModelRenderers(SpecialRendererRegistrar registrar) {
        registrar.register(CozyHome.id("chair"), ChairSpecialRenderer.Unbaked.CODEC);
        registrar.register(CozyHome.id("sofa"), SofaSpecialRenderer.Unbaked.CODEC);
        registrar.register(CozyHome.id("wall_clock"), WallClockSpecialRenderer.Unbaked.CODEC);
        registrar.register(CozyHome.id("bathtub"), BathtubSpecialRenderer.Unbaked.CODEC);
    }
}
