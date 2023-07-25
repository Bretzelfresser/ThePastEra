package com.bretzelfresser.the_past_era.core.init;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.common.entity.Goobar;
import com.bretzelfresser.the_past_era.common.entity.SeaMosquito;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EntityInit {


    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ThePastEra.MODID);

    public static final RegistryObject<EntityType<Goobar>> GOOBAR = register("goobar", () -> EntityType.Builder.of(Goobar::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.4F).clientTrackingRange(4));
    public static final RegistryObject<EntityType<SeaMosquito>> SEA_MOSQUITO = register("sea_mosquito", () -> EntityType.Builder.of(SeaMosquito::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.4F).clientTrackingRange(4));

    public static final <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder){
        return ENTITIES.register(name, () -> builder.get().build(ThePastEra.modLoc(name).toString()));
    }
}
