package com.bretzelfresser.the_past_era;

import com.bretzelfresser.the_past_era.core.init.EntityInit;
import com.bretzelfresser.the_past_era.core.init.ItemInit;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ThePastEra.MODID)
public class ThePastEra {

    public static final String MODID = "the_past_era";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation modLoc(String path){
        return new ResourceLocation(MODID, path);
    }

    public ThePastEra() {

        GeckoLib.initialize();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ItemInit.ITEMS.register(bus);
        ItemInit.SPAWN_EGGS.register(bus);

        EntityInit.ENTITIES.register(bus);

    }


}
