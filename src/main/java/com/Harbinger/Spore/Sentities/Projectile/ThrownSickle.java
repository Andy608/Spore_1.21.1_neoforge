package com.Harbinger.Spore.Sentities.Projectile;

import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Fluids.BileLiquid;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeWeaponData;
import com.Harbinger.Spore.Sitems.InfectedSickle;
import com.Harbinger.Spore.core.*;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class ThrownSickle extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSickle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ThrownSickle.class, EntityDataSerializers.INT);
    private ItemStack spearItem = new ItemStack(Sitems.SICKLE.get());
    private boolean dealtDamage;
    private SickelState state = SickelState.FLYING;
    private Entity hookedEntity = null;
    private Vec3 hookedBlockPos = null;

    public ThrownSickle(Level level, LivingEntity livingEntity, ItemStack stack, int color) {
        super(Sentities.THROWN_SICKEL.get(), level);
        this.setOwner(livingEntity);
        this.spearItem = stack.copy();
        this.entityData.set(ID_FOIL, stack.hasFoil());
        this.entityData.set(COLOR, color);
    }

    public ThrownSickle(Level level) {
        super(Sentities.THROWN_SICKEL.get(), level);
    }

    public ThrownSickle(EntityType<ThrownSickle> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_FOIL, false);
        builder.define(COLOR, 0);
    }

    public ItemStack getSpearItem() {
        return spearItem.copy();
    }

    public int getColor() { return entityData.get(COLOR); }

    @Override
    public void tick() {
        if (this.state == SickelState.HOOKED_IN_ENTITY && hookedEntity != null && hookedEntity.isAlive()) {
            this.setPos(hookedEntity.getX(), hookedEntity.getY() + (hookedEntity.getBbHeight() * 0.5), hookedEntity.getZ());
        }
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity living) {
            ItemStack stack = living.getMainHandItem();
            if (this.distanceTo(living) > 30.0f || !(stack.getItem() instanceof InfectedSickle)) {
                if (stack.getItem() instanceof InfectedSickle sickle) {
                    sickle.setThrownSickle(stack, false);
                }
                this.discard();
            }
        } else {
            this.discard();
        }
        super.tick();
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 from, Vec3 to) {
        return this.dealtDamage ? null : super.findHitEntity(from, to);
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        Entity entity = hit.getEntity();
        int sharpnessLevel = EnchantmentHelper.getTagEnchantmentLevel(Senchantments.getEnchantment(level(),Enchantments.SHARPNESS),this.spearItem);
        int fireAspectLevel = EnchantmentHelper.getTagEnchantmentLevel(Senchantments.getEnchantment(level(),Enchantments.FIRE_ASPECT),this.spearItem);
        float baseDamage = SConfig.SERVER.sickle_damage.get() + (0.5F * sharpnessLevel);
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent soundevent = Ssounds.INFECTED_WEAPON_HIT_ENTITY.get();
        if (entity.hurt(damagesource, baseDamage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity && level() instanceof ServerLevel serverLevel) {
                if (entity1 instanceof LivingEntity ownerLiving) {
                    EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damagesource);
                    if (spearItem.getItem() instanceof SporeWeaponData data) {
                        data.abstractMutationBuffs(livingEntity, ownerLiving, spearItem, data);
                    }
                }
                if (fireAspectLevel > 0) {
                    entity.setRemainingFireTicks(80 * fireAspectLevel);
                }
                abstractEffects(this.spearItem, livingEntity);
                this.doPostHurtEffects(livingEntity);
            }
        }
        this.hookedEntity = entity;
        this.state = SickelState.HOOKED_IN_ENTITY;
        this.playSound(soundevent, 1.0F, 1.0F);
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.hookedBlockPos = result.getLocation();
        this.state = SickelState.HOOKED_BLOCK;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return Ssounds.INFECTED_WEAPON_HIT_BLOCK.get();
    }

    public SickelState getHookState() {
        return this.state;
    }

    public Entity getHookedEntity() {
        return this.hookedEntity;
    }

    public Vec3 getHookedBlockPos() {
        return this.hookedBlockPos;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        // Read sickle data using new 1.21.1 method
        if (tag.contains("Sickle", CompoundTag.TAG_COMPOUND)) {
            CompoundTag sickleTag = tag.getCompound("Sickle");
            this.spearItem = ItemStack.parse(this.registryAccess(), sickleTag).orElse(new ItemStack(Sitems.SICKLE.get()));
        }

        this.dealtDamage = tag.getBoolean("DealtDamage");
        if (this.getOwner() != null) {
            tag.putUUID("OwnerUUID", this.getOwner().getUUID());
        }
        this.entityData.set(COLOR, tag.getInt("color"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        // Save sickle data using new 1.21.1 method
        tag.put("Sickle", this.spearItem.save(this.registryAccess(), new CompoundTag()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
        tag.putInt("color", this.entityData.get(COLOR));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        Entity entity = this.getOwner();
        if (entity != null) {
            UUID uuid1 = entity.getUUID();
            if (!this.level().isClientSide) {
                Entity entity1 = ((ServerLevel) this.level()).getEntity(uuid1);
                if (entity1 instanceof LivingEntity livingEntity) {
                    this.setOwner(livingEntity);
                }
            }
        }
    }

    @Override
    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
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

    public enum SickelState {
        FLYING,
        HOOKED_IN_ENTITY,
        HOOKED_BLOCK;

        private SickelState() {
        }
    }
}