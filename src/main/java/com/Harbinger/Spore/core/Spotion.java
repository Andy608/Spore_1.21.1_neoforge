package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Spore;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Spotion {
    public static final DeferredRegister<Potion> POTIONS
            = DeferredRegister.create(Registries.POTION, Spore.MODID);
    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }


    public static Holder<Potion> MYCELIUM_POTION = POTIONS.register("mycelium_potion",
            () -> new Potion(new MobEffectInstance(Seffects.MYCELIUM, 3600, 1)));
    public static Holder<Potion> MARKER_POTION = POTIONS.register("marker_potion",
            () -> new Potion(new MobEffectInstance(Seffects.MARKER, 3600, 1)));
    public static Holder<Potion> CORROSION_POTION = POTIONS.register("corrosion_potion",
            () -> new Potion(new MobEffectInstance(Seffects.CORROSION, 3600, 0)));
    public static Holder<Potion> CORROSION_POTION_STRONG = POTIONS.register("corrosion_potion_strong",
            () -> new Potion(new MobEffectInstance(Seffects.CORROSION, 3600, 3)));
}
