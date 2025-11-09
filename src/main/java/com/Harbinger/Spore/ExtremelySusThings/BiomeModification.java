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

import java.util.List;

public class BiomeModification implements BiomeModifier {

    public static final MapCodec<BiomeModification> CODEC = MapCodec.unit(BiomeModification::new);

    public static MapCodec<BiomeModification> makeCodec() {
        return CODEC;
    }

    public BiomeModification() {
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC; // ✅ Correct & safe
    }

    // ✅ Add mob spawns based on config
    private void addSpawns(ModifiableBiomeInfo.BiomeInfo.Builder builder, int modifier) {
        List<? extends String> spawnEntries = SConfig.SERVER.spawns.get();

        for (String entry : spawnEntries) {
            String[] parts = entry.split("\\|");
            if (parts.length != 4) {
                Spore.LOGGER.warn("Invalid spawn entry: {}", entry);
                continue;
            }

            ResourceLocation id = ResourceLocation.parse(parts[0]);
            EntityType<?> type = Utilities.tryToCreateEntity(id);

            if (type == null) {
                Spore.LOGGER.warn("Unknown entity type: {}", parts[0]);
                continue;
            }

            try {
                int weight = Integer.parseInt(parts[1]) + modifier;
                int min = Integer.parseInt(parts[2]);
                int max = Integer.parseInt(parts[3]);

                builder.getMobSpawnSettings()
                        .getSpawner(type.getCategory())
                        .add(new MobSpawnSettings.SpawnerData(type, weight, min, max));

            } catch (NumberFormatException e) {
                Spore.LOGGER.error("Invalid number in spawn config: {}", entry, e);
            }
        }
    }

    // ✅ Checks blacklist tags and direct biome IDs
    private boolean isBiomeBlacklisted(Holder<Biome> biome) {
        for (String blacklisted : SConfig.SERVER.dimension_blacklist.get()) {
            ResourceLocation loc = ResourceLocation.parse(blacklisted);
            TagKey<Biome> tag = TagKey.create(Registries.BIOME, loc);

            if (biome.is(tag) || biome.is(loc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) return;

        if (isBiomeBlacklisted(biome)) return;

        int biomeModifier = biome.is(Tags.Biomes.IS_MUSHROOM) ? 20 : 0;

        for (String allowedBiome : SConfig.SERVER.dimension_parameters.get()) {
            ResourceLocation loc = ResourceLocation.parse(allowedBiome);
            TagKey<Biome> tag = TagKey.create(Registries.BIOME, loc);

            if (biome.is(tag) || biome.is(loc)) {
                addSpawns(builder, biomeModifier);
                break;
            }
        }
    }
}
