package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Sitems.SyringeGun;
import com.Harbinger.Spore.Spore;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SdataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Spore.MODID);

    public static final Supplier<DataComponentType<Float>> ADDITIONAL_PROTECTION = DATA_COMPONENTS.register(
            "additional_protection", () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).build());

    public static final Supplier<DataComponentType<Float>> ADDITIONAL_TOUGHNESS = DATA_COMPONENTS.register(
            "additional_toughness", () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).build());

    public static final Supplier<DataComponentType<Integer>> ENCHANTING_LUCK = DATA_COMPONENTS.register(
            "enchanting_luck", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> ARMOR_MUTATION = DATA_COMPONENTS.register(
            "armor_mutation", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> MAX_ADDITIONAL_DURABILITY = DATA_COMPONENTS.register(
            "max_additional_durability", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> EXTRA_DURABILITY = DATA_COMPONENTS.register(
            "extra_durability", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Float>> ADDITIONAL_DAMAGE = DATA_COMPONENTS.register(
            "additional_damage", () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).build());

    public static final Supplier<DataComponentType<Integer>> WEAPON_EXTRA_DURABILITY = DATA_COMPONENTS.register(
            "weapon_extra_durability", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> WEAPON_MUTATION = DATA_COMPONENTS.register(
            "weapon_mutation", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> SHIELD_CHARGE = DATA_COMPONENTS.register(
            "shield_charge", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> CDU_FUEL = DATA_COMPONENTS.register(
            "cdu_fuel", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> INCUBATOR_FUEL = DATA_COMPONENTS.register(
            "incubator_fuel", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Boolean>> SICKLE_THROW = DATA_COMPONENTS.register(
            "sickle_throw", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build());

    public static final Supplier<DataComponentType<Integer>> PCI_AMMO = DATA_COMPONENTS.register(
            "pci_ammo", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> RELOAD_TIMER = DATA_COMPONENTS.register(
            "reload_timer", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> SHOOT_COOLDOWN = DATA_COMPONENTS.register(
            "shoot_cooldown", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> CHAMBER = DATA_COMPONENTS.register(
            "chamber", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Boolean>> RELOADING = DATA_COMPONENTS.register(
            "reloading_gun", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build());


    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}