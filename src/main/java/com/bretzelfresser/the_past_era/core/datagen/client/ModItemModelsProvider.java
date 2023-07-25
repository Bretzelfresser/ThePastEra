package com.bretzelfresser.the_past_era.core.datagen.client;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.core.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelsProvider extends ItemModelProvider {

    public final ModelFile generated = getExistingFile(mcLoc("item/generated"));
    public final ModelFile spawnEgg = getExistingFile(mcLoc("item/template_spawn_egg"));

    public ModItemModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ThePastEra.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simple(ItemInit.GOOBAR_BUCKET.get(), ItemInit.RAW_GOOBAR.get(), ItemInit.COOKED_GOOBAR.get());
        ItemInit.SPAWN_EGGS.getEntries().stream().map(RegistryObject::get).forEach(this::spawnEgg);
    }

    private void simple(Item... items) {
        for (Item item : items) {
            String name = item.getRegistryName().getPath();
            getBuilder(name).parent(generated).texture("layer0", "item/" + name);
        }
    }

    private void spawnEgg(Item... items) {
        for (Item item : items) {
            String name = item.getRegistryName().getPath();
            getBuilder(name).parent(spawnEgg);
        }
    }
}
