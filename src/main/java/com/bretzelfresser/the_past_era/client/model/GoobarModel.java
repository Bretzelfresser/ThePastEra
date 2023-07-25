package com.bretzelfresser.the_past_era.client.model;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.common.entity.Goobar;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GoobarModel extends AnimatedGeoModel<Goobar> {
    @Override
    public ResourceLocation getModelLocation(Goobar object) {
        return ThePastEra.modLoc("geo/goobar.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Goobar object) {
        return object.getGoobarType().getTexture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Goobar animatable) {
        return ThePastEra.modLoc("animations/goobar.animation.json");
    }
}
