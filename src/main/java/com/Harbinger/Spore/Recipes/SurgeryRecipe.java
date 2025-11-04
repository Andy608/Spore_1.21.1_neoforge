package com.Harbinger.Spore.Recipes;

import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.Srecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record SurgeryRecipe(NonNullList<Ingredient> inputItems, ItemStack output) implements Recipe<SurgeryRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public boolean matches(SurgeryRecipeInput container, Level level) {
        if (level.isClientSide() || container.size() < 16) {
            return false;
        }
        for (int i = 0; i < 16; i++) {
            if (!inputItems.get(i).test(container.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(SurgeryRecipeInput container, HolderLookup.Provider provider) {
        return output == null ? ItemStack.EMPTY : output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Srecipes.SURGERY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Srecipes.SURGERY_TYPE.get();
    }
    public record SlottedIngredient(int slot, Ingredient ingredient) {
        public static final MapCodec<SlottedIngredient> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Codec.INT.fieldOf("slot").forGetter(SlottedIngredient::slot),
                        Ingredient.CODEC_NONEMPTY.fieldOf("item").forGetter(SlottedIngredient::ingredient)
                ).apply(inst, SlottedIngredient::new)
        );
    }
    public static class SurgeryRecipeSerializer implements RecipeSerializer<SurgeryRecipe> {
        public static final SurgeryRecipeSerializer INSTANCE = new SurgeryRecipeSerializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Spore.MODID, "surgery");
        public static final MapCodec<SurgeryRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SurgeryRecipe.SlottedIngredient.CODEC.codec().listOf().fieldOf("ingredients").xmap(
                        list -> {
                            NonNullList<Ingredient> nonNullList = NonNullList.withSize(16, Ingredient.EMPTY);
                            for (SurgeryRecipe.SlottedIngredient slotted : list) {
                                int slot = Mth.clamp(slotted.slot(), 0, 15);
                                nonNullList.set(slot, slotted.ingredient());
                            }
                            return nonNullList;
                        },
                        ingredients -> ingredients.stream()
                                .map(ing -> new SlottedIngredient(ingredients.indexOf(ing), ing))
                                .toList()
                ).forGetter(SurgeryRecipe::getIngredients),
                ItemStack.CODEC.fieldOf("result").forGetter(SurgeryRecipe::output)
        ).apply(inst, SurgeryRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SurgeryRecipe> STREAM_CODEC =
                new StreamCodec<RegistryFriendlyByteBuf, SurgeryRecipe>() {
                    @Override
                    public SurgeryRecipe decode(RegistryFriendlyByteBuf buffer) {
                        int size = buffer.readVarInt();
                        NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
                        for (int i = 0; i < size; i++) {
                            inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
                        }
                        ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
                        return new SurgeryRecipe(inputs, result);
                    }

                    @Override
                    public void encode(RegistryFriendlyByteBuf buffer, SurgeryRecipe recipe) {
                        buffer.writeVarInt(recipe.inputItems().size());
                        for (Ingredient ingredient : recipe.inputItems()) {
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
                        }
                        ItemStack.STREAM_CODEC.encode(buffer, recipe.output());
                    }
                };
        @Override
        public MapCodec<SurgeryRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SurgeryRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }


}