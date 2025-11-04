package com.Harbinger.Spore.Recipes;

import com.Harbinger.Spore.SBlockEntities.SurgeryTableBlockEntity;
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

public record GraftingRecipe(NonNullList<Ingredient> inputItems, ItemStack output) implements Recipe<GraftingRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public boolean matches(GraftingRecipeInput simpleContainer, Level level) {
        if (level.isClientSide() || simpleContainer.size() < 24) {
            return false;
        }
        for (int i = SurgeryTableBlockEntity.GRATING_ITEM_ONE; i < SurgeryTableBlockEntity.GRATING_ITEM_TWO+1; i++) {
            if (!inputItems.get(i).test(simpleContainer.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(GraftingRecipeInput craftingContainer, HolderLookup.Provider provider) {
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
        return Srecipes.GRAFTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Srecipes.GRAFTING_TYPE.get();
    }
    public record SlottedIngredient(int slot, Ingredient ingredient) {
        public static final MapCodec<GraftingRecipe.SlottedIngredient> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Codec.INT.fieldOf("slot").forGetter(GraftingRecipe.SlottedIngredient::slot),
                        Ingredient.CODEC_NONEMPTY.fieldOf("item").forGetter(GraftingRecipe.SlottedIngredient::ingredient)
                ).apply(inst, GraftingRecipe.SlottedIngredient::new)
        );
    }
    public static class GraftingRecipeSerializer implements RecipeSerializer<GraftingRecipe> {
        public static final GraftingRecipeSerializer INSTANCE = new GraftingRecipeSerializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Spore.MODID, "grafting");
        public static final MapCodec<GraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                GraftingRecipe.SlottedIngredient.CODEC.codec().listOf().fieldOf("ingredients").xmap(
                        list -> {
                            NonNullList<Ingredient> nonNullList = NonNullList.withSize(25, Ingredient.EMPTY);
                            for (GraftingRecipe.SlottedIngredient slotted : list) {
                                int slot = Mth.clamp(slotted.slot(), 0, 24);
                                nonNullList.set(slot, slotted.ingredient());
                            }
                            return nonNullList;
                        },
                        ingredients -> ingredients.stream()
                                .map(ing -> new GraftingRecipe.SlottedIngredient(ingredients.indexOf(ing), ing))
                                .toList()
                ).forGetter(GraftingRecipe::getIngredients),
                ItemStack.CODEC.fieldOf("result").forGetter(GraftingRecipe::output)
        ).apply(inst, GraftingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, GraftingRecipe> STREAM_CODEC =
                new StreamCodec<RegistryFriendlyByteBuf, GraftingRecipe>() {
                    @Override
                    public GraftingRecipe decode(RegistryFriendlyByteBuf buffer) {
                        int size = buffer.readVarInt();
                        NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
                        for (int i = 0; i < size; i++) {
                            inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
                        }
                        ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
                        return new GraftingRecipe(inputs, result);
                    }

                    @Override
                    public void encode(RegistryFriendlyByteBuf buffer, GraftingRecipe recipe) {
                        buffer.writeVarInt(recipe.inputItems().size());
                        for (Ingredient ingredient : recipe.inputItems()) {
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
                        }
                        ItemStack.STREAM_CODEC.encode(buffer, recipe.output());
                    }
                };
        @Override
        public MapCodec<GraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
