package com.Harbinger.Spore.Recipes;

import com.Harbinger.Spore.Sentities.VariantKeeper;
import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.Srecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record WombRecipe(List<Pair> entityPairs, String attribute, ResourceLocation icon) implements Recipe<EntityContainer> {

    public String getAttribute() {
        return attribute;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    @Override
    public boolean matches(EntityContainer entityContainer, Level level) {
        if (level.isClientSide) {
            return false;
        }
        EntityType<?> entityType = entityContainer.entity().getType();

        for (Pair pair : entityPairs) {
            EntityType<?> expectedType = EntityType.byString(pair.entityId()).orElse(null);
            if (expectedType == null) continue;

            if (entityContainer.entity() instanceof VariantKeeper keeper) {
                if (keeper.getTypeVariant() == pair.type() && entityType.equals(expectedType)) {
                    return true;
                }
            } else {
                if (entityType.equals(expectedType)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(EntityContainer entityContainer, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Srecipes.WOMB_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Srecipes.WOMB_TYPE.get();
    }

    public record Pair(String entityId, int type) {
        public static final Codec<Pair> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("entity").forGetter(Pair::entityId),
                        Codec.INT.fieldOf("entity_type").forGetter(Pair::type)
                ).apply(instance, Pair::new)
        );
    }
    public static class WombRecipeSerializer implements RecipeSerializer<WombRecipe> {
        public static final WombRecipeSerializer INSTANCE = new WombRecipeSerializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Spore.MODID, "assimilation");
        public static final MapCodec<WombRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Pair.CODEC.listOf().fieldOf("entities").forGetter(WombRecipe::entityPairs),
                        Codec.STRING.fieldOf("attribute").forGetter(WombRecipe::attribute),
                        ResourceLocation.CODEC.fieldOf("icon").forGetter(WombRecipe::icon)
                ).apply(instance, WombRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, WombRecipe> STREAM_CODEC =
                StreamCodec.of(
                        (buffer, recipe) -> {
                            buffer.writeInt(recipe.entityPairs().size());
                            for (Pair pair : recipe.entityPairs()) {
                                buffer.writeUtf(pair.entityId());
                                buffer.writeInt(pair.type());
                            }
                            buffer.writeUtf(recipe.attribute());
                            ResourceLocation.STREAM_CODEC.encode(buffer, recipe.icon());
                        },
                        buffer -> {
                            int size = buffer.readInt();
                            List<Pair> entityPairs = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                String entityId = buffer.readUtf();
                                int type = buffer.readInt();
                                entityPairs.add(new Pair(entityId, type));
                            }

                            String attribute = buffer.readUtf();
                            ResourceLocation icon = ResourceLocation.STREAM_CODEC.decode(buffer);
                            return new WombRecipe(entityPairs, attribute, icon);
                        }
                );

        @Override
        public MapCodec<WombRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WombRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}