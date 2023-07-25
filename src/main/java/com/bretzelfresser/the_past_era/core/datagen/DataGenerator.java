package com.bretzelfresser.the_past_era.core.datagen;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.core.datagen.client.ModBlockModelsProvider;
import com.bretzelfresser.the_past_era.core.datagen.client.ModEnglishLangProvider;
import com.bretzelfresser.the_past_era.core.datagen.client.ModItemModelsProvider;
import com.bretzelfresser.the_past_era.core.datagen.server.ModLootTablesProvider;
import com.bretzelfresser.the_past_era.core.datagen.server.ModRecipeProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ThePastEra.MODID)
public class DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator gen = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeClient())
            gatherClientData(gen, helper);
        if (event.includeServer())
            gatherServerData(gen, helper);
        try {
            gen.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void gatherClientData(net.minecraft.data.DataGenerator gen, ExistingFileHelper helper) {
        gen.addProvider(new ModItemModelsProvider(gen, helper));
        gen.addProvider(new ModBlockModelsProvider(gen, helper));
    }

    private static void gatherServerData(net.minecraft.data.DataGenerator gen, ExistingFileHelper helper) {
        gen.addProvider(new ModRecipeProvider(gen));
        gen.addProvider(new ModEnglishLangProvider(gen));
        gen.addProvider(new ModLootTablesProvider(gen));
    }


}
