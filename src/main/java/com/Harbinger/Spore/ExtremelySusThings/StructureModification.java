package com.Harbinger.Spore.ExtremelySusThings;


import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.SConfig;
import com.mojang.serialization.Codec;
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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class StructureModification implements StructureModifier {
    public static final DeferredHolder<MapCodec<? extends StructureModifier>, MapCodec<? extends StructureModifier>> SERIALIZER =
            DeferredHolder.create(NeoForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS,
                    ResourceLocation.fromNamespaceAndPath(Spore.MODID, "spore_structure_spawns"));

    @Override
    public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (structure.is(TagKey.create(Registries.STRUCTURE,ResourceLocation.fromNamespaceAndPath(Spore.MODID,"laboratories")))){
            if (!SConfig.SERVER.structure_spawns.get().isEmpty()){
                for (String str : SConfig.SERVER.structure_spawns.get()) {
                    String[] string = str.split("\\|");
                    EntityType<?> entity = Utilities.tryToCreateEntity(ResourceLocation.parse(string[0]));
                    if (entity != null){
                        builder.getStructureSettings().getOrAddSpawnOverrides(entity.getCategory()).addSpawn(new MobSpawnSettings.SpawnerData(entity, Integer.parseUnsignedInt(string[1]),
                                Integer.parseUnsignedInt(string[2]), Integer.parseUnsignedInt(string[3])));
                    }
                }
            }
        }
    }

    public static MapCodec<? extends StructureModifier>makeCodec() {
        return MapCodec.unit(StructureModification::new);
    }

    @Override
    public MapCodec<? extends StructureModifier> codec() {
        return SERIALIZER.get();
    }

}
