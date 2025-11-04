package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Effect.*;
import com.Harbinger.Spore.Spore;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Seffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(Registries.MOB_EFFECT, Spore.MODID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    public static final Holder<MobEffect> MYCELIUM = MOB_EFFECTS.register("mycelium_ef",
            Mycelium::new);

    public static final Holder<MobEffect> MADNESS = MOB_EFFECTS.register("madness",
            Madness::new);

    public static final Holder<MobEffect> STARVATION = MOB_EFFECTS.register("starvation",
            Starvation::new);

    public static final Holder<MobEffect> UNEASY = MOB_EFFECTS.register("uneasy",
            Uneasy::new);

    public static final Holder<MobEffect> MARKER = MOB_EFFECTS.register("marker",
            () -> new Marker().addAttributeModifier(Attributes.FOLLOW_RANGE,
                    ResourceLocation.fromNamespaceAndPath(Spore.MODID, "marker_follow_range"), 0.5F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final Holder<MobEffect> CORROSION = MOB_EFFECTS.register("corrosion",
            () -> new Corrosion().addAttributeModifier(Attributes.ARMOR,
                    ResourceLocation.fromNamespaceAndPath(Spore.MODID, "corrosion_armor"), -0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final Holder<MobEffect> SYMBIOSIS = MOB_EFFECTS.register("symbiosis",
            () -> new Symbiosis().addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "symbiosis_movement_speed"), 0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_SPEED,
                            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "symbiosis_attack_speed"), 0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE,
                            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "symbiosis_attack_damage"), 4F, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.MAX_HEALTH,
                            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "symbiosis_max_health"), 6F, AttributeModifier.Operation.ADD_VALUE));
}