package com.Harbinger.Spore.core;
import com.Harbinger.Spore.Spore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ScreativeTab {
    public static final DeferredRegister<CreativeModeTab> SPORE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Spore.MODID);

    public static final Supplier<CreativeModeTab> SPORE = SPORE_TABS.register("spore",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.spore"))
                    .icon(() -> new ItemStack(Sitems.MUTATED_HEART.get()))
                    .displayItems((parameters, output) -> {
                        Sitems.BIOLOGICAL_ITEMS.forEach(output::accept);
                    })
                    .build()
    );

    public static final Supplier<CreativeModeTab> SPORE_T = SPORE_TABS.register("spore_t",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.spore_t"))
                    .icon(() -> new ItemStack(Sitems.SCANNER.get()))
                    .displayItems((parameters, output) -> {
                        Sitems.TECHNOLOGICAL_ITEMS.forEach(output::accept);
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        SPORE_TABS.register(eventBus);
    }
}