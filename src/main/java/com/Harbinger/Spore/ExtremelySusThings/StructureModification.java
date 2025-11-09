package com.Harbinger.Spore.ExtremelySusThings;

import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.SConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.world.ModifiableStructureInfo;
import net.neoforged.neoforge.common.world.StructureModifier;

public class StructureModification implements StructureModifier {

    public static final MapCodec<StructureModification> CODEC = MapCodec.unit(StructureModification::new);

    public static MapCodec<StructureModification> makeCodec() {
        return CODEC;
    }

    @Override
    public MapCodec<? extends StructureModifier> codec() {
        return CODEC;
    }

    @Override
    public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (phase != Phase.ADD) return;

        TagKey<Structure> labTag = TagKey.create(
                Registries.STRUCTURE,
                ResourceLocation.fromNamespaceAndPath(Spore.MODID, "laboratories")
        );

        if (!structure.is(labTag)) {
            return;
        }

        if (SConfig.SERVER.structure_spawns.get().isEmpty()) {
            return;
        }

        for (String entry : SConfig.SERVER.structure_spawns.get()) {
            String[] parts = entry.split("\\|");

            if (parts.length != 4) {
                Spore.LOGGER.warn("Invalid structure spawn config entry: {}", entry);
                continue;
            }

            ResourceLocation entityId = ResourceLocation.parse(parts[0]);
            EntityType<?> entity = Utilities.tryToCreateEntity(entityId);

            if (entity == null) {
                Spore.LOGGER.warn("Unknown entity type: {}", parts[0]);
                continue;
            }

            try {
                int weight = Integer.parseInt(parts[1]);
                int min = Integer.parseInt(parts[2]);
                int max = Integer.parseInt(parts[3]);

                builder.getStructureSettings()
                        .getOrAddSpawnOverrides(entity.getCategory())
                        .addSpawn(new MobSpawnSettings.SpawnerData(entity, weight, min, max));

            } catch (NumberFormatException e) {
                Spore.LOGGER.error("Invalid number in structure spawn config: {}", entry, e);
            }
        }
    }
}
