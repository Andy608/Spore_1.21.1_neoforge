package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Spore;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelAccessor;

import java.util.List;

public class Senchantments {
    private static final RandomSource randomSource = RandomSource.create();
    public static final ResourceKey<Enchantment> SYMBIOTIC_RECONSTITUTION = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Spore.MODID,"symbiotic_reconstitution"));
    public static final ResourceKey<Enchantment> CRYOGENIC_ASPECT = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Spore.MODID,"cryogenic_aspect"));
    public static final ResourceKey<Enchantment> GASTRIC_SPEWAGE = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Spore.MODID,"gastric_spewage"));
    public static final ResourceKey<Enchantment> CORROSIVE_POTENCY = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Spore.MODID,"corrosive_potency"));
    public static final ResourceKey<Enchantment> SERRATED_THORNS = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Spore.MODID,"serrated_thorns"));
    public static final ResourceKey<Enchantment> VORACIOUS_MAW = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Spore.MODID,"voracious_maw"));

    public static final ResourceKey<Enchantment> UNWAVERING_NATURE = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Spore.MODID,"unwavering_nature"));
    public static final ResourceKey<Enchantment> MUTAGENIC_REACTANT = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Spore.MODID,"mutagenic_reactant"));

    public static void EnchantItem(LevelAccessor level, ItemStack stack, ResourceKey<Enchantment>  resourceKey)
    {
        Holder<Enchantment> enchantmentHolder = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(resourceKey);
        stack.enchant(enchantmentHolder,1);
    }
    public static boolean hasEnchant(LevelAccessor level, ItemStack stack, ResourceKey<Enchantment>  resourceKey){
        return stack.getEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(resourceKey)) != 0;
    }
    public static Holder<Enchantment> getEnchantment(LevelAccessor level,ResourceKey<Enchantment>  resourceKey){
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(resourceKey);
    }

    public static final List<ResourceKey<Enchantment>> curses = List.of(Senchantments.UNWAVERING_NATURE,Senchantments.MUTAGENIC_REACTANT);


}
