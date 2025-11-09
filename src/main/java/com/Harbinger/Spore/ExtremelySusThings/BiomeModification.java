package com.Harbinger.Spore.ExtremelySusThings;

import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.SConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class BiomeModification implements net.neoforged.neoforge.common.world.BiomeModifier {
    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<? extends BiomeModifier>> SERIALIZER =
            DeferredHolder.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS,
                    ResourceLocation.fromNamespaceAndPath(Spore.MODID, "inf_spawns"));

    public BiomeModification() {}

    private void addSpawns(ModifiableBiomeInfo.BiomeInfo.Builder builder, int modifier) {
        List<? extends String> spawnEntries = SConfig.SERVER.spawns.get();

        for (String entry : spawnEntries) {
            String[] parts = entry.split("\\|");
            if (parts.length != 4) {
                Spore.LOGGER.warn("Invalid spawn config entry: {}", entry);
                continue;
            }

            ResourceLocation entityId = ResourceLocation.parse(parts[0]);
            EntityType<?> entityType = Utilities.tryToCreateEntity(entityId);

            if (entityType == null) {
                Spore.LOGGER.warn("Unknown entity type: {}", parts[0]);
                continue;
            }

            try {
                int weight = Integer.parseUnsignedInt(parts[1]) + modifier;
                int minCount = Integer.parseUnsignedInt(parts[2]);
                int maxCount = Integer.parseUnsignedInt(parts[3]);

                builder.getMobSpawnSettings()
                        .getSpawner(entityType.getCategory())
                        .add(new MobSpawnSettings.SpawnerData(entityType, weight, minCount, maxCount));

            } catch (NumberFormatException e) {
                Spore.LOGGER.error("Invalid spawn config number format in: {}", entry, e);
            }
        }
    }

    private boolean isBiomeBlacklisted(Holder<Biome> biome) {
        for (String blacklisted : SConfig.SERVER.dimension_blacklist.get()) {
            ResourceLocation blacklistedLocation = ResourceLocation.parse(blacklisted);
            TagKey<Biome> blacklistTag = TagKey.create(Registries.BIOME, blacklistedLocation);
            if (biome.is(blacklistTag) || biome.is(blacklistedLocation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void modify(Holder<Biome> holder, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) return;

        if (isBiomeBlacklisted(holder)) return;

        int biomeModifier = holder.is(Tags.Biomes.IS_MUSHROOM) ? 20 : 0;

        for (String allowedBiome : SConfig.SERVER.dimension_parameters.get()) {
            ResourceLocation biomeLocation =  ResourceLocation.parse(allowedBiome);
            TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, biomeLocation);

            if (holder.is(biomeTag) || holder.is(biomeLocation)) {
                addSpawns(builder, biomeModifier);
                break;
            }
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }


    public static MapCodec<BiomeModification> makeCodec() {
        return MapCodec.unit(BiomeModification::new);
    }
}
