package com.Harbinger.Spore.Effect;


import com.Harbinger.Spore.Sentities.Utility.Illusion;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Sentities;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;

public class Madness extends MobEffect implements SporeEffectsHandler{
    public Madness() {
        super(MobEffectCategory.HARMFUL, 419435);
    }


    public void feelingWatched(ServerPlayer player){
        player.displayClientMessage(Component.translatable("vigil.message"),true);
    }

    public void SummonIllusion(LivingEntity entity, ServerLevel serverLevel, boolean value, int targetId){
        int x = entity.getRandom().nextInt(-6,6);
        int z = entity.getRandom().nextInt(-6,6);
        Illusion illusion = new Illusion(Sentities.ILLUSION.get(), serverLevel);
        illusion.setSeeAble(false);
        illusion.setAdvanced(value);
        illusion.setTargetId(targetId);
        DifficultyInstance difficultyInstance = serverLevel.getCurrentDifficultyAt(entity.blockPosition());
        illusion.moveTo(entity.getX() + x,entity.getY(),entity.getZ()+z);
        illusion.finalizeSpawn(serverLevel, difficultyInstance, MobSpawnType.MOB_SUMMONED,null);
        serverLevel.addFreshEntity(illusion);
    }

    public void playClientSounds(LivingEntity entity){
        entity.playSound(Ssounds.MADNESS.value());
    }


    public boolean isDurationEffectTick(int duration, int intensity) {
        return duration % 80 == 0;
    }

    @Override
    public void triggerEffects(LivingEntity entity, int intense) {
        if (Math.random() < (SConfig.SERVER.chance_hallucination_spawn.get() * 0.01) && entity.level() instanceof ServerLevel serverLevel){
            if (intense > 1 && intense < 4){
                SummonIllusion(entity,serverLevel,false,entity.getId());
            }
            if (intense >= 4){
                SummonIllusion(entity,serverLevel,true,entity.getId());
            }
        }
        if (Math.random() < 0.1){
            this.playClientSounds(entity);
        }
        if (Math.random() < 0.1 && intense > 0 && entity instanceof ServerPlayer player){
            this.feelingWatched(player);
        }
    }
}