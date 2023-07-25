package com.bretzelfresser.the_past_era.client.model;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.common.entity.SeaMosquito;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SeaMosquitoModel extends AnimatedGeoModel<SeaMosquito> {
    @Override
    public ResourceLocation getModelLocation(SeaMosquito object) {
        return ThePastEra.modLoc("geo/sea_mosquito.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SeaMosquito object) {
        return ThePastEra.modLoc("textures/entity/sea_mosquito.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SeaMosquito animatable) {
        return ThePastEra.modLoc("animations/sea_mosquito.animation.json");
    }
}
