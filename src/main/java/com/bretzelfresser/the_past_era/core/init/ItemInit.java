package com.bretzelfresser.the_past_era.core.init;

import com.bretzelfresser.the_past_era.ThePastEra;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThePastEra.MODID);
    public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(ForgeRegistries.ITEMS, ThePastEra.MODID);

    public static final RegistryObject<MobBucketItem> GOOBAR_BUCKET = ITEMS.register("bucket_of_goobar", () -> new MobBucketItem(EntityInit.GOOBAR::get, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().tab(ModItemGroups.FISH)));
    public static final RegistryObject<MobBucketItem> SEA_MOSQUITO_BUCKET = ITEMS.register("bucket_of_sea_mosquito", () -> new MobBucketItem(EntityInit.SEA_MOSQUITO::get, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().tab(ModItemGroups.FISH)));

    public static final RegistryObject<Item> RAW_GOOBAR = ITEMS.register("raw_goobar", () -> new Item(new Item.Properties().tab(ModItemGroups.FISH).food(FoodInit.RAW_GOOBAR)));
    public static final RegistryObject<Item> COOKED_GOOBAR = ITEMS.register("cooked_goobar", () -> new Item(new Item.Properties().tab(ModItemGroups.FISH).food(FoodInit.COOKED_GOOBAR)));


    //Spawn Eggs
    public static final RegistryObject<ForgeSpawnEggItem> GOOBAR_SPAWN_EGG = SPAWN_EGGS.register("goobar_spawn_egg", () -> new ForgeSpawnEggItem(EntityInit.GOOBAR::get, 0x343741, 0xffff3e, new Item.Properties().tab(ModItemGroups.FISH)));

}
