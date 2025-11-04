package com.Harbinger.Spore.Sentities.Projectile;

import com.Harbinger.Spore.Fluids.BileLiquid;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeWeaponData;
import com.Harbinger.Spore.core.*;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThrownSpear extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.INT);
    private ItemStack spearItem = new ItemStack(Sitems.INFECTED_SPEAR.get());
    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;


    public ThrownSpear(Level p_37569_, ItemStack stack,LivingEntity owner,int color) {
        super(Sentities.THROWN_SPEAR.get(), p_37569_);
        this.setOwner(owner);
        this.spearItem = stack.copy();
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(stack));
        this.entityData.set(ID_FOIL, stack.hasFoil());
        this.entityData.set(COLOR, color);
    }

    public ThrownSpear(Level level) {
        super(Sentities.THROWN_SPEAR.get(),level);
    }

    public ThrownSpear(EntityType<ThrownSpear> thrownSpearEntityType, Level level) {
        super(thrownSpearEntityType,level);
    }
    public int getColor(){return entityData.get(COLOR);}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte)0);
        builder.define(ID_FOIL, false);
        builder.define(COLOR, 0);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double)i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    protected ItemStack getPickupItem() {
        return this.spearItem.copy();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return this.spearItem.copy();
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_37575_, Vec3 p_37576_) {
        return this.dealtDamage ? null : super.findHitEntity(p_37575_, p_37576_);
    }

    protected void onHitEntity(EntityHitResult hit) {
        Entity entity = hit.getEntity();
        float f = SConfig.SERVER.spear_damage.get() + (0.5F * EnchantmentHelper.getItemEnchantmentLevel(Senchantments.getEnchantment(level(),Enchantments.SHARPNESS), this.spearItem));
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent soundevent = Ssounds.INFECTED_WEAPON_HIT_ENTITY.get();
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingEntity) {
                if (entity1 instanceof LivingEntity ownerLiving) {
                    if (spearItem.getItem() instanceof SporeWeaponData data){
                        data.abstractMutationBuffs(livingEntity,ownerLiving,spearItem,data);
                    }
                }
                int j = EnchantmentHelper.getItemEnchantmentLevel(Senchantments.getEnchantment(level(),Enchantments.FIRE_ASPECT), this.spearItem);
                if (j > 0) {
                    entity.setRemainingFireTicks(80 * j);
                }
                abstractEffects(this.spearItem,livingEntity);

                this.doPostHurtEffects(livingEntity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        this.playSound(soundevent, f1, 1.0F);
    }


    protected boolean tryPickup(Player p_150196_) {
        return super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem());
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return Ssounds.INFECTED_WEAPON_HIT_BLOCK.get();
    }

    public void playerTouch(Player p_37580_) {
        if (this.ownedBy(p_37580_) || this.getOwner() == null) {
            super.playerTouch(p_37580_);
        }

    }
    private byte getLoyaltyFromItem(ItemStack stack) {
        Level var3 = this.level();
        byte var10000;
        if (var3 instanceof ServerLevel serverlevel) {
            var10000 = (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, this), 0, 127);
        } else {
            var10000 = 0;
        }

        return var10000;
    }
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(this.getPickupItemStackOrigin()));
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }
    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }

    public static void abstractEffects(ItemStack stack, LivingEntity livingEntity){
        if (Senchantments.hasEnchant(livingEntity.level(),stack,Senchantments.CORROSIVE_POTENCY)){
            livingEntity.addEffect(new MobEffectInstance(Seffects.CORROSION,200,1));
        }
        if (Senchantments.hasEnchant(livingEntity.level(),stack,Senchantments.GASTRIC_SPEWAGE)){
            for (MobEffectInstance instance : BileLiquid.bileEffects())
                livingEntity.addEffect(instance);
        }
        if (Senchantments.hasEnchant(livingEntity.level(),stack,Senchantments.CRYOGENIC_ASPECT)){
            livingEntity.setTicksFrozen(livingEntity.getTicksFrozen()+300);
        }
    }

}
