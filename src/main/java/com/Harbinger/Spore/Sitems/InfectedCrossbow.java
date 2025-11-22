package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.Fluids.BileLiquid;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeWeaponData;
import com.Harbinger.Spore.core.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class InfectedCrossbow extends CrossbowItem implements SporeWeaponData {
    public InfectedCrossbow() {
        super(new Properties().durability(SConfig.SERVER.crossbow_durability.get()));
        Sitems.BIOLOGICAL_ITEMS.add(this);
        Sitems.TINTABLE_ITEMS.add(this);
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemstack, ItemStack repairitem) {
        return Objects.equals(Sitems.BIOMASS.get(), repairitem.getItem());
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d0 = target.getX() - shooter.getX();
            double d1 = target.getZ() - shooter.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = target.getY(0.3333333333333333) - projectile.getY() + d2 * 0.20000000298023224;
            vector3f = getProjectileShotVector(shooter, new Vec3(d0, d3, d1), angle);
        } else {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(angle * 0.017453292F), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = shooter.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }
        projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), (float) (velocity * SConfig.SERVER.crossbow_arrow_damage_multiplier.get()), inaccuracy);

        float f = getShotPitch(shooter.getRandom(), index);
        shooter.level().playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, shooter.getSoundSource(), 1.0F, f);

    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        if (ammo.is(Items.FIREWORK_ROCKET)) {
            return new FireworkRocketEntity(level, ammo, shooter, shooter.getX(), shooter.getEyeY() - 0.15000000596046448, shooter.getZ(), true);
        } else {
            Projectile projectile = super.createProjectile(level, shooter, weapon, ammo, isCrit);
            if (projectile instanceof AbstractArrow abstractArrow) {
                abstractArrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
                if (abstractArrow instanceof Arrow arrow){
                    abstractEffects(weapon,arrow,shooter.level());
                }
            }
            return projectile;
        }
    }

    private static Vector3f getProjectileShotVector(LivingEntity shooter, Vec3 distance, float angle) {
        Vector3f vector3f = distance.toVector3f().normalize();
        Vector3f vector3f1 = (new Vector3f(vector3f)).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            vector3f1 = (new Vector3f(vector3f)).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = (new Vector3f(vector3f)).rotateAxis(1.5707964F, vector3f1.x, vector3f1.y, vector3f1.z);
        return (new Vector3f(vector3f)).rotateAxis(angle * 0.017453292F, vector3f2.x, vector3f2.y, vector3f2.z);
    }

    private static float getShotPitch(RandomSource random, int index) {
        return index == 0 ? 1.0F : getRandomShotPitch((index & 1) == 1, random);
    }
    private static float getRandomShotPitch(boolean isHighPitched, RandomSource random) {
        float f = isHighPitched ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        if (!tooHurt(stack)){
            components.add(Component.translatable("spore.item.hurt").withStyle(ChatFormatting.RED));
        }
        if (Screen.hasShiftDown()){
            if (getAdditionalDamage(stack) > 0){
                components.add(Component.literal(Component.translatable("spore.item.damage_increase").getString() + getAdditionalDamage(stack) + "%"));
            }if (getMaxAdditionalDurability(stack) > 0){
                components.add(Component.literal(Component.translatable("spore.item.durability_increase").getString()+ getMaxAdditionalDurability(stack) + "%"));
            }if (getAdditionalDurability(stack) > 0){
                components.add(Component.literal(Component.translatable("spore.item.additional_durability").getString()+ getAdditionalDurability(stack)));
            }if (getEnchantmentValue(stack) > 1){
                components.add(Component.literal(Component.translatable("spore.item.enchant").getString()+ getEnchantmentValue(stack)));
            }
            if (getVariant(stack) != SporeToolsMutations.DEFAULT){
                components.add(Component.literal(Component.translatable("spore.item.mutation").getString()+Component.translatable(getVariant(stack).getName()).getString()));
            }
        }
    }

    public boolean useOnRelease(ItemStack p_150801_) {
        return p_150801_.is(this);
    }

    public int getDefaultProjectileRange() {
        return 8;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        int luck = getLuck(stack);
        return luck > 0 ? luck : 1;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (!tooHurt(stack) && entity instanceof Player player){
            player.getCooldowns().addCooldown(this,60);
        }
        return super.onEntitySwing(stack, entity, hand);
    }

    public static void abstractEffects(ItemStack stack, Arrow arrow,Level level){
        if (Senchantments.hasEnchant(level,stack,Senchantments.CORROSIVE_POTENCY)){
            arrow.addEffect(new MobEffectInstance(Seffects.CORROSION,200,1));
        }
        if (Senchantments.hasEnchant(level,stack,Senchantments.GASTRIC_SPEWAGE)){
            for (MobEffectInstance instance : BileLiquid.bileEffects())
                arrow.addEffect(instance);
        }
        if (getMutation(stack) == SporeToolsMutations.TOXIC){
            arrow.addEffect(new MobEffectInstance(MobEffects.POISON,100,0));
        }
        if (getMutation(stack) == SporeToolsMutations.ROTTEN){
            arrow.addEffect(new MobEffectInstance(MobEffects.WITHER,100,1));
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity living, LivingEntity entity) {
        if (tooHurt(stack)){
            hurtTool(stack,entity,1);
        }
        doEntityHurtAfterEffects(stack,living,entity);
        return super.hurtEnemy(stack, living, entity);
    }
    private static boolean tooFreakyHurt(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 10;
    }

    private static SporeToolsMutations getMutation(ItemStack stack){
        return SporeToolsMutations.byId(stack.getOrDefault(SdataComponents.WEAPON_MUTATION.get(), 0) & 255);
    }

    private static double calculateTrueDamageCrossBow(ItemStack stack, double meleeDamage) {
        double value = stack.getOrDefault(SdataComponents.ADDITIONAL_DAMAGE.get(), 0.0f) * 0.01;
        if (value > 0) {
            return meleeDamage + (meleeDamage * value);
        }
        return meleeDamage;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack itemStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && Senchantments.hasEnchant(player.level(),stack,Senchantments.VORACIOUS_MAW) && stack.getDamageValue() > 0){
            if (itemStack.getFoodProperties(player) == null){
                return false;
            }
            stack.setDamageValue(getDamage(stack)-50);
            itemStack.shrink(1);
            player.playNotifySound(SoundEvents.GENERIC_EAT, SoundSource.AMBIENT, 1f, 1f);
            return true;
        }
        boolean shouldOverride = clickAction == ClickAction.SECONDARY
                && itemStack.getItem() == Sitems.SYRINGE.get()
                && getVariant(stack) != SporeToolsMutations.DEFAULT;

        if (shouldOverride) {
            this.setVariant(SporeToolsMutations.DEFAULT, stack);
            itemStack.shrink(1);
            player.playNotifySound(Ssounds.SYRINGE_SUCK.value(), SoundSource.AMBIENT, 1f, 1f);
        }

        return shouldOverride;
    }
}

