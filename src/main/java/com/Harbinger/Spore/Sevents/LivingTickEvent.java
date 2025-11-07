package com.Harbinger.Spore.Sevents;

import com.Harbinger.Spore.Effect.SporeEffectsHandler;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

import java.util.ArrayList;
import java.util.List;

public class LivingTickEvent {
    private static final TagKey<Block> tag = BlockTags.create(ResourceLocation.parse("spore:fungal_blocks"));
    public static void TickEvents(LivingEvent event){
        LivingEntity living = event.getEntity();
        for (MobEffectInstance instance : living.getActiveEffects()){
            int amp = instance.getAmplifier();
            MobEffect effect = instance.getEffect().value();
            if (effect instanceof SporeEffectsHandler handler){
                if (handler.isDurationEffectTick(instance.getDuration(),amp)){
                    handler.triggerEffects(living,amp);
                }
            }
        }
        if (living instanceof Player player){
            MobEffectInstance effectInstance = player.getEffect(Seffects.MADNESS);
            if (effectInstance != null && effectInstance.getDuration() == 1){
                int level = effectInstance.getAmplifier();
                if (level > 0){
                    effectInstance.update(new MobEffectInstance(Seffects.MADNESS,12000,level-1));
                }
            }
            if (player.tickCount % 400 == 0 && player.level().isClientSide){
                AABB aabb = player.getBoundingBox().inflate(5);
                List<BlockPos> list = new ArrayList<>();
                for (BlockPos blockPos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))){
                    if (player.level().getBlockState(blockPos).is(tag)){
                        list.add(blockPos);
                    }
                }
                if (list.size() > 4){
                    player.playSound(Ssounds.AREA_AMBIENT.get());
                }
            }
        }
    }
}
