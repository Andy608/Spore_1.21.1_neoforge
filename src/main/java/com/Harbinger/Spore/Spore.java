package com.Harbinger.Spore;

import com.Harbinger.Spore.ExtremelySusThings.SporePacketHandler;
import com.Harbinger.Spore.core.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
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
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        System.out.print("Initializing "+MODID);
    }
}
