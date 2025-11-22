package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Ssounds;
import com.Harbinger.Spore.Sentities.Projectile.ThrownKnife;
import com.Harbinger.Spore.Sitems.BaseWeapons.LootModifierWeapon;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeSwordBase;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class InfectedKnife extends SporeSwordBase implements LootModifierWeapon {
    public InfectedKnife() {
        super(SConfig.SERVER.knife_damage.get(), 0, -1, SConfig.SERVER.knife_durability.get());
    }

    @Override
    public int getLootingLevel() {
        return 1;
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
        ItemStack itemstack = player.getItemInHand(hand);
        if (tooHurt(itemstack)){
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int T) {
        if (entity instanceof Player player) {
            int i = this.getUseDuration(stack,entity) - T;
            if (i >= 10 && !level.isClientSide) {
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

                ThrownKnife thrownSpear = new ThrownKnife(level, player, stack,getVariant(stack).getColor());
                thrownSpear.setPos(player.getEyePosition());
                thrownSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2F , 0.75F);
                if (player.getAbilities().instabuild) {
                    thrownSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
                level.addFreshEntity(thrownSpear);
                level.playSound(null, thrownSpear, Ssounds.INFECTED_WEAPON_THROW.value(), SoundSource.PLAYERS, 1.5F, 0.9F);
                if (!player.getAbilities().instabuild) {
                    player.getInventory().removeItem(stack);
                }
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }
}
