package com.bretzelfresser.the_past_era.core.datagen.client;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.core.init.EntityInit;
import com.bretzelfresser.the_past_era.core.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class ModEnglishLangProvider extends BetterLanguageProvider {
    public ModEnglishLangProvider(DataGenerator gen) {
        super(gen, ThePastEra.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ItemInit.ITEMS.getEntries().stream().map(RegistryObject::get).forEach(item -> add(item, toTitleCase(item.getRegistryName().getPath())));
        ItemInit.SPAWN_EGGS.getEntries().stream().map(RegistryObject::get).forEach(item -> add(item, toTitleCase(item.getRegistryName().getPath())));
        EntityInit.ENTITIES.getEntries().stream().forEach(r ->  add(r.get(), toTitleCase(r.getId().getPath())));
    }
}
