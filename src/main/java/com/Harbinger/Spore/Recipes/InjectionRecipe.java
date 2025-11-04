package com.Harbinger.Spore.Recipes;

import com.Harbinger.Spore.Sentities.VariantKeeper;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record InjectionRecipe(String entityId, int type, ItemStack result) implements Recipe<EntityContainer> {

    public String getEntityId() {
        return entityId;
    }

    public int getEntityType() {
        return type;
    }

    @Override
    public boolean matches(EntityContainer entityContainer, Level level) {
        if (level.isClientSide) {
            return false;
        }
        EntityType<?> entityType = entityContainer.entity().getType();
        EntityType<?> expectedType = EntityType.byString(entityId).orElse(null);
        if (expectedType == null) return false;

        if (entityContainer.entity() instanceof VariantKeeper keeper) {
            return keeper.getTypeVariant() == this.getEntityType() && entityType.equals(expectedType);
        }
        return entityType.equals(expectedType);
    }

    @Override
    public ItemStack assemble(EntityContainer entityContainer, HolderLookup.Provider provider) {
        return result == null ? ItemStack.EMPTY : result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result == null ? ItemStack.EMPTY : result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Srecipes.INJECTION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Srecipes.INJECTION_TYPE.get();
    }


    public static class InjectionRecipeSerializer implements RecipeSerializer<InjectionRecipe> {
        public static final InjectionRecipeSerializer INSTANCE = new InjectionRecipeSerializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Spore.MODID, "injection");

        public static final MapCodec<InjectionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.fieldOf("entity").forGetter(InjectionRecipe::entityId),
                        Codec.INT.fieldOf("entity_type").forGetter(InjectionRecipe::type),
                        ItemStack.CODEC.fieldOf("output").forGetter(InjectionRecipe::result)
                ).apply(instance, InjectionRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, InjectionRecipe> STREAM_CODEC =
                StreamCodec.of(
                        (buffer, recipe) -> {
                            buffer.writeUtf(recipe.entityId());
                            buffer.writeVarInt(recipe.getEntityType());
                            ItemStack.STREAM_CODEC.encode(buffer, recipe.result());
                        },
                        buffer -> {
                            String id = buffer.readUtf();
                            int type = buffer.readVarInt();
                            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
                            return new InjectionRecipe(id, type, result);
                        }
                );

        @Override
        public MapCodec<InjectionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InjectionRecipe> streamCodec() {
            return STREAM_CODEC;
        }

    }
}