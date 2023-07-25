package com.bretzelfresser.the_past_era.core.datagen.server;

import com.bretzelfresser.the_past_era.core.init.EntityInit;
import com.bretzelfresser.the_past_era.core.init.ItemInit;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTablesProvider extends LootTableProvider {
    public ModLootTablesProvider(DataGenerator p_124437_) {
        super(p_124437_);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(ModEntityLoot::new, LootContextParamSets.ENTITY));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((id, table) -> LootTables.validate(validationtracker, id, table));
    }

    protected static class ModEntityLoot extends EntityLoot{

        private List<EntityType<?>> knowEntities = new ArrayList<>();

        @Override
        protected void addTables() {
            add(EntityInit.GOOBAR.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ItemInit.RAW_GOOBAR.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0f, 1f)))))
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemRandomChanceCondition.randomChance(0.05F))));
        }

        @Override
        protected void add(EntityType<?> type, LootTable.Builder p_124373_) {
            this.knowEntities.add(type);
            super.add(type, p_124373_);
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return this.knowEntities;
        }
    }
}
