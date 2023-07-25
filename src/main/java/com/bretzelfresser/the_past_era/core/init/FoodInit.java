package com.bretzelfresser.the_past_era.core.init;

import net.minecraft.world.food.FoodProperties;

public class FoodInit {


    public static final FoodProperties RAW_GOOBAR = new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).build();
    public static final FoodProperties COOKED_GOOBAR = new FoodProperties.Builder().nutrition(2).saturationMod(0.8F).build();
}
