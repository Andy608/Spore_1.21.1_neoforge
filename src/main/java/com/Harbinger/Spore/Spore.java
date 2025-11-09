package com.Harbinger.Spore;

import com.Harbinger.Spore.ExtremelySusThings.BiomeModification;
import com.Harbinger.Spore.ExtremelySusThings.SporePacketHandler;
import com.Harbinger.Spore.ExtremelySusThings.StructureModification;
import com.Harbinger.Spore.core.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.StructureModifier;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

@Mod(Spore.MODID)
public class Spore {
    public static final String MODID = "spore";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Spore(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(SporePacketHandler::registerPackets);
        Sblocks.register(modEventBus);
        SdataComponents.register(modEventBus);
        Sitems.register(modEventBus);
        ScreativeTab.register(modEventBus);
        Ssounds.register(modEventBus);
        Seffects.register(modEventBus);
        SAttributes.register(modEventBus);
        Sparticles.register(modEventBus);
        Sentities.register(modEventBus);
        Spotion.register(modEventBus);
        SblockEntities.register(modEventBus);
        Srecipes.register(modEventBus);
        SMenu.register(modEventBus);
        Ssounds.registerSong(modEventBus);
        Sfluids.SPORE_FLUID.register(modEventBus);
        SticketType.init();
        modContainer.registerConfig(ModConfig.Type.STARTUP, SConfig.SERVER_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, SConfig.DATAGEN_SPEC);
        final DeferredRegister<MapCodec<? extends BiomeModifier>> biomeModifiers =
                DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Spore.MODID);
        biomeModifiers.register(modEventBus);
        biomeModifiers.register("inf_spawns", BiomeModification::makeCodec);
        final DeferredRegister<MapCodec<? extends StructureModifier>> structureModifiers = DeferredRegister.create(NeoForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, Spore.MODID);
        structureModifiers.register(modEventBus);
        structureModifiers.register("spore_structure_spawns", StructureModification::makeCodec);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        System.out.print("Initializing "+MODID);
    }
}
