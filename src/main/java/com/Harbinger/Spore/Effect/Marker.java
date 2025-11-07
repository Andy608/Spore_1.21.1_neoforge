package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Marker extends MobEffect implements SporeEffectsHandler{
    public Marker() {
        super(MobEffectCategory.NEUTRAL, 8412043);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        int j = pLivingEntity.getEffect(Seffects.MARKER).getDuration();
        if (!(pLivingEntity instanceof UtilityEntity)){
        AABB boundingBox = pLivingEntity.getBoundingBox().inflate(16 * (pAmplifier + 1));
        List<Entity> entities = pLivingEntity.level().getEntities(pLivingEntity, boundingBox);

        for (Entity entity : entities) {
            if(entity instanceof Infected livingEntity) {
                livingEntity.addEffect( new MobEffectInstance(Seffects.MARKER , j * 2,pAmplifier + 1, false,false));
            }
        }
        return true;
        }
        return false;
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        if (this == Seffects.MARKER.value()) {
            int i = 80 >> intensity;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        }
        return false;
    }
}
