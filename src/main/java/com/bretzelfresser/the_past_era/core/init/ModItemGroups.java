package com.bretzelfresser.the_past_era.core.init;

import com.bretzelfresser.the_past_era.ThePastEra;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class ModItemGroups {

    public static final ModItemGroup FISH = new ModItemGroup("fish", () -> Items.TROPICAL_FISH);

    protected static class ModItemGroup extends CreativeModeTab{

        protected final Supplier<ItemLike> icon;

        public ModItemGroup(String label, Supplier<ItemLike> icon) {
            super("item_group." + ThePastEra.MODID + "." + label);
            this.icon = icon;
        }

        @Override
        public ItemStack makeIcon() {
            return this.icon.get().asItem().getDefaultInstance();
        }
    }
}
