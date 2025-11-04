package com.Harbinger.Spore.core;


import com.Harbinger.Spore.Recipes.GraftingRecipe;
import com.Harbinger.Spore.Recipes.InjectionRecipe;
import com.Harbinger.Spore.Recipes.SurgeryRecipe;
import com.Harbinger.Spore.Recipes.WombRecipe;
import com.Harbinger.Spore.Spore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class Srecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Spore.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Spore.MODID);

    public static final Supplier<RecipeSerializer<SurgeryRecipe>> SURGERY_SERIALIZER =
            SERIALIZERS.register("surgery", () -> SurgeryRecipe.SurgeryRecipeSerializer.INSTANCE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<SurgeryRecipe>> SURGERY_TYPE =
            TYPES.register("surgery", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "surgery";
                }
            });



    public static final Supplier<RecipeSerializer<GraftingRecipe>> GRAFTING_SERIALIZER =
            SERIALIZERS.register("grafting", () -> GraftingRecipe.GraftingRecipeSerializer.INSTANCE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<GraftingRecipe>> GRAFTING_TYPE =
            TYPES.register("grafting", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "grafting";
                }
            });


    public static final Supplier<RecipeSerializer<InjectionRecipe>> INJECTION_SERIALIZER =
            SERIALIZERS.register("injection", () -> InjectionRecipe.InjectionRecipeSerializer.INSTANCE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<InjectionRecipe>> INJECTION_TYPE =
            TYPES.register("injection", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "injection";
                }
            });

    public static final Supplier<RecipeSerializer<WombRecipe>> WOMB_SERIALIZER =
            SERIALIZERS.register("assimilation", () -> WombRecipe.WombRecipeSerializer.INSTANCE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<WombRecipe>> WOMB_TYPE =
            TYPES.register("assimilation", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "assimilation";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
