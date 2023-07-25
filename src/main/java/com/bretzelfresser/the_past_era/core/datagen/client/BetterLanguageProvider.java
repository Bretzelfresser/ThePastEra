package com.bretzelfresser.the_past_era.core.datagen.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import javax.json.JsonString;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class BetterLanguageProvider implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<String, String> data = new TreeMap<>();
    private final Set<String> entities, blocks, items, itemStacks, enchantments, effects, itemGroups;
    private final DataGenerator gen;
    private final String modid;
    private final String locale;

    public BetterLanguageProvider(DataGenerator gen, String modid, String locale) {
        this.gen = gen;
        this.modid = modid;
        this.locale = locale;
        entities = new HashSet<>();
        blocks = new HashSet<>();
        items = new HashSet<>();
        enchantments = new HashSet<>();
        effects = new HashSet<>();
        itemGroups = new HashSet<>();
        itemStacks = new HashSet<>();
    }

    protected abstract void addTranslations();

    @Override
    public void run(HashCache cache) throws IOException {
        addTranslations();
        if (!data.isEmpty())
            save(cache, this.gen.getOutputFolder().resolve("assets/" + modid + "/lang/" + locale + ".json"));
    }

    private void save(HashCache cache, Path target) throws IOException {
        String data = GSON.toJson(this.data);
        data = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(data); // Escape unicode after the fact so that it's not double escaped by GSON
        String hash = DataProvider.SHA1.hashUnencodedChars(data).toString();
        if (!Objects.equals(cache.getHash(target), hash) || !Files.exists(target)) {
            Files.createDirectories(target.getParent());

            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(target)) {
                GSON.toJson(createData().getAsJsonObject(), bufferedwriter);
            }
        }

        cache.putNew(target, hash);
    }

    protected JsonObject createData() {
        JsonObject obj = new JsonObject();
        this.items.forEach(i -> {
            obj.addProperty(i, this.data.get(i));
            this.data.remove(i);
        });
        if (items.size() > 0 && data.size() > 0)
            obj.addProperty("----------", "----------");

        this.itemStacks.forEach(i -> {
            obj.addProperty(i, this.data.get(i));
            this.data.remove(i);
        });
        if (itemStacks.size() > 0 && data.size() > 0)
            obj.addProperty("---------", "-----------");

        this.blocks.forEach(i -> {
            obj.addProperty(i, this.data.get(i));
            this.data.remove(i);
        });
        if (blocks.size() > 0 && data.size() > 0)
            obj.addProperty("-----------", "---------");

        this.itemGroups.forEach(i -> {
            obj.addProperty(i, this.data.get(i));
            this.data.remove(i);
        });
        if (itemGroups.size() > 0 && data.size() > 0)
            obj.addProperty("--------", "------------");

        this.entities.forEach(i -> {
            obj.addProperty(i, this.data.get(i));
            this.data.remove(i);
        });
        if (entities.size() > 0 && data.size() > 0)
            obj.addProperty("------------", "--------");

        this.enchantments.forEach(i -> {
            obj.addProperty(i, this.data.get(i));
            this.data.remove(i);
        });
        if (enchantments.size() > 0 && data.size() > 0)
            obj.addProperty("-------------", "-------");

        this.effects.forEach(i -> {
            obj.addProperty(i, this.data.get(i));
            this.data.remove(i);
        });
        if (effects.size() > 0 && data.size() > 0)
            obj.addProperty("-------", "-------------");

        this.data.entrySet().forEach(e -> {
            obj.addProperty(e.getKey(), e.getValue());
        });


        return obj;
    }

    protected void comment(JsonObject obj, String comment) {
        obj.addProperty("_comment", comment);
    }

    @Override
    public String getName() {
        return "Languages: " + locale;
    }

    public void add(Block key, String name) {
        blocks.add(key.getDescriptionId());
        add(key.getDescriptionId(), name);
    }

    public void add(EntityType<?> key, String name) {
        this.entities.add(key.getDescriptionId());
        add(key.getDescriptionId(), name);
    }

    public void add(MobEffect key, String name) {
        effects.add(key.getDescriptionId());
        add(key.getDescriptionId(), name);
    }

    public void add(Enchantment key, String name) {
        this.enchantments.add(key.getDescriptionId());
        add(key.getDescriptionId(), name);
    }

    public void add(Item key, String name) {
        this.items.add(key.getDescriptionId());
        add(key.getDescriptionId(), name);
    }

    public void add(ItemStack key, String name) {
        this.itemStacks.add(key.getDescriptionId());
        add(key.getDescriptionId(), name);
    }

    public void add(CreativeModeTab tab, String name) {
        if (tab.getDisplayName() instanceof TranslatableComponent) {
            this.itemGroups.add(((TranslatableComponent) tab.getDisplayName()).getKey());
            add(((TranslatableComponent) tab.getDisplayName()).getKey(), name);
        } else {
            throw new IllegalArgumentException("this just works with TranslatableComponents");
        }
    }

    public void add(String key, String value) {
        if (data.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                nextTitleCase = true;
                titleCase.append(" ");
                continue;
            }

            if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}
