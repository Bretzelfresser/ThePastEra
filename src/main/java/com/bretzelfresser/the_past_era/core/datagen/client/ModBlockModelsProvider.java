package com.bretzelfresser.the_past_era.core.datagen.client;

import com.bretzelfresser.the_past_era.ThePastEra;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockModelsProvider extends BlockModelProvider {
    public ModBlockModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ThePastEra.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
