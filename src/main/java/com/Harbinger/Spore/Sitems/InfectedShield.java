package com.Harbinger.Spore.Sitems;


import com.Harbinger.Spore.Fluids.BileLiquid;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsBaseItem;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsMutations;
import com.Harbinger.Spore.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class InfectedShield extends SporeToolsBaseItem implements Equipable{
    public static final int MAX_CHARGE = 25;
    public InfectedShield() {
        super(SConfig.SERVER.shield_damage.get(), 0, 1, SConfig.SERVER.shield_durability.get(), 0, null);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public UseAnim getUseAnimation(ItemStack p_43105_) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }


    public InteractionResultHolder<ItemStack> use(Level p_43099_, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (tooHurt(itemstack)){
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SHIELD_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.OFFHAND;
    }
    public void setCharge(ItemStack stack,int value){
        stack.set(SdataComponents.SHIELD_CHARGE.get(),value);
    }
    public int getCharge(ItemStack stack){
        return stack.getOrDefault(SdataComponents.SHIELD_CHARGE.get(),0);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        int current = this.getCharge(stack);
        if (current >= InfectedShield.MAX_CHARGE && entity != null) {
            this.triggerBash(entity, stack);
            setCharge(stack,0);
        }
        int durabilityLeft = stack.getMaxDamage() - stack.getDamageValue();
        if (durabilityLeft-amount <= 11 && entity != null){
            entity.playSound(Ssounds.INFECTED_GEAR_BREAK.get());
        }
        if (tooHurt(stack)){
            this.setCharge(stack,++current);
            if (getAdditionalDurability(stack) > 0){
                hurtExtraDurability(stack,amount,entity);
                return 0;
            }else{
                return super.damageItem(stack, calculateDurabilityLostForMutations(amount,stack), entity, onBroken);
            }
        }else{
            return 0;
        }
    }

    public void triggerBash(LivingEntity player, ItemStack stack) {
        if (!(player.level().isClientSide)) {
            double radius = 5.0;
            Vec3 look = player.getLookAngle();
            AABB area = player.getBoundingBox().expandTowards(look.scale(radius)).inflate(2.0);

            List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, area,
                    e -> e != player && e.isAlive() && player.hasLineOfSight(e));

            for (LivingEntity target : entities) {
                ((ServerLevel) target.level()).sendParticles(Sparticles.SPORE_IMPACT.get(), target.getX(), target.getY()+1, target.getZ(), 1, 0, 0, 0, 0);
                Vec3 direction = target.position().subtract(player.position()).normalize();
                target.hurtMarked = true;
                target.knockback(getVariant(stack) == SporeToolsMutations.CALCIFIED ? 2.5f : 1.5F, -direction.x, -direction.z);
                target.hurt(player.damageSources().generic(),SConfig.SERVER.shield_damage.get());
                abstractEffects(stack,target);
                if (getVariant(stack) == SporeToolsMutations.TOXIC){
                    target.addEffect(new MobEffectInstance(MobEffects.POISON, 200,0));
                }
                if (getVariant(stack) == SporeToolsMutations.ROTTEN){
                    target.addEffect(new MobEffectInstance(MobEffects.POISON, 200,0));
                }
            }
            if (getVariant(stack) == SporeToolsMutations.VAMPIRIC){
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400,0));
            }
            if (getVariant(stack) == SporeToolsMutations.BEZERK){
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200,1));
            }
            player.level().playSound(null, player.blockPosition(), Ssounds.SHIELD_BASH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            this.hurtTool(stack,player,1);
        }
    }
    public void abstractEffects(ItemStack stack, LivingEntity arrow){
        if (Senchantments.hasEnchant(arrow.level(),stack,Senchantments.CORROSIVE_POTENCY)){
            arrow.addEffect(new MobEffectInstance(Seffects.CORROSION,200,1));}
        if (Senchantments.hasEnchant(arrow.level(),stack,Senchantments.GASTRIC_SPEWAGE)){
            for (MobEffectInstance instance : BileLiquid.bileEffects())
                arrow.addEffect(instance);}
        if (Senchantments.hasEnchant(arrow.level(),stack,Senchantments.CRYOGENIC_ASPECT) && arrow.canFreeze()){
            arrow.setTicksFrozen(arrow.getTicksFrozen()+300);}
        arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,200,0));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        components.add(Component.literal("Charge "+getCharge(stack)+"/"+MAX_CHARGE));
    }
}
