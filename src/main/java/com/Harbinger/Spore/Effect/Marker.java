package com.Harbinger.Spore.Effect;

import com.Harbinger.Spore.core.Seffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class Marker extends MobEffect {
    public Marker() {
        super(MobEffectCategory.NEUTRAL, 8412043);
    }

    //@Override
  //  public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        //int j = pLivingEntity.getEffect(Seffects.MARKER.get()).getDuration();
        // if (!(pLivingEntity instanceof Infected || pLivingEntity instanceof UtilityEntity)){
       // AABB boundingBox = pLivingEntity.getBoundingBox().inflate(16 * (pAmplifier + 1));
       // List<Entity> entities = pLivingEntity.level().getEntities(pLivingEntity, boundingBox);

       // for (Entity entity : entities) {
       //     if(entity instanceof Infected livingEntity) {
       //         livingEntity.addEffect( new MobEffectInstance(Seffects.MARKER.get() , j * 2,pAmplifier + 1, false,false));
       //     }
       // }
      //  return true;
      //  }
      //  return false;
  //  }

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
