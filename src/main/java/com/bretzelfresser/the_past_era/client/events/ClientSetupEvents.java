package com.bretzelfresser.the_past_era.client.events;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.client.model.GoobarModel;
import com.bretzelfresser.the_past_era.client.model.SeaMosquitoModel;
import com.bretzelfresser.the_past_era.common.entity.Goobar;
import com.bretzelfresser.the_past_era.common.entity.SeaMosquito;
import com.bretzelfresser.the_past_era.core.init.EntityInit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Mod.EventBusSubscriber(modid = ThePastEra.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event){
        event.put(EntityInit.GOOBAR.get(), Goobar.createAttributes().build());
        event.put(EntityInit.SEA_MOSQUITO.get(), SeaMosquito.createAttributes().build());
    }

    @SubscribeEvent
    public static void clientSetup(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.GOOBAR.get(), createGeoRenderer(new GoobarModel()));
        event.registerEntityRenderer(EntityInit.SEA_MOSQUITO.get(), createGeoRenderer(new SeaMosquitoModel()));
    }

    public static <T extends LivingEntity & IAnimatable> EntityRendererProvider<T> createGeoRenderer(AnimatedGeoModel<T> model) {
        return m -> new SimpleGeoRenderer<>(m, model);
    }

    protected static class SimpleGeoRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {

        public SimpleGeoRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
            super(renderManager, modelProvider);
        }
    }


}
