package com.Harbinger.Spore.Sitems;


import com.Harbinger.Spore.Sentities.Projectile.ThrownSickle;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeSwordBase;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SdataComponents;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class InfectedSickle extends SporeSwordBase {
    public InfectedSickle() {
        super(SConfig.SERVER.sickle_damage.get(), 2f, 2, SConfig.SERVER.sickle_durability.get());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity living, LivingEntity entity) {
        if (getThrownSickle(stack)){return false;}
        return super.hurtEnemy(stack, living, entity);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_HOE) ? 2F:1F;
    }

    @Override
    public ItemAttributeModifiers getDynamicAttributeModifiers(ItemStack stack) {
        return super.getDynamicAttributeModifiers(stack);
    }

    @Override
    public boolean reversedKnockback() {
        return true;
    }

    public void setThrownSickle(ItemStack stack,boolean value){
        stack.set(SdataComponents.SICKLE_THROW,value);
    }
    public boolean getThrownSickle(ItemStack stack){
        return stack.getOrDefault(SdataComponents.SICKLE_THROW.get(),false);
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }


    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        ItemStack itemstack = player.getMainHandItem();
        if (!level.isClientSide && tooHurt(itemstack)) {
            List<ThrownSickle> projectiles = level.getEntitiesOfClass(ThrownSickle.class, player.getBoundingBox().inflate(32),
                    s -> s.getOwner() == player && !s.isRemoved());
            setThrownSickle(itemstack, false);

            if (!projectiles.isEmpty()) {
                ThrownSickle sickle = projectiles.getFirst();
                if (sickle.getHookState() == ThrownSickle.SickelState.HOOKED_IN_ENTITY && sickle.getHookedEntity() != null) {
                    Entity hooked = sickle.getHookedEntity();

                    if (hooked instanceof LivingEntity le && le.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE).getValue() > 0.5) {
                        pullEntityToward(player, hooked.position());
                    } else {
                        pullEntityToward(hooked, player.position());
                    }

                } else if (sickle.getHookState() == ThrownSickle.SickelState.HOOKED_BLOCK && sickle.getHookedBlockPos() != null) {
                    pullEntityToward(player, sickle.getHookedBlockPos());
                }
                sickle.discard();
            } else {
                player.startUsingItem(hand);
                return InteractionResultHolder.success(itemstack);
            }
        }

        return InteractionResultHolder.pass(itemstack);
    }

    private void pullEntityToward(Entity toMove, Vec3 targetPos) {
        Vec3 direction = targetPos.subtract(toMove.position()).normalize();
        double strength = 4D;
        Vec3 velocity = direction.multiply(strength,strength/2,strength);
        toMove.setDeltaMovement(velocity);
        toMove.hurtMarked = true;
    }
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int T) {
        if (entity instanceof Player player && !getThrownSickle(stack)) {
            int i = this.getUseDuration(stack,entity) - T;
            if (i >= 10 && !level.isClientSide) {
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                ThrownSickle thrownSpear = new ThrownSickle(level, player, stack,getVariant(stack).getColor());
                thrownSpear.setPos(player.getEyePosition());
                thrownSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2F , 0.75F);
                level.addFreshEntity(thrownSpear);
                this.setThrownSickle(stack,true);
                level.playSound(null, thrownSpear, Ssounds.INFECTED_WEAPON_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

}
