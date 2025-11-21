package com.Harbinger.Spore.Sentities.Organoids;


import com.Harbinger.Spore.Sentities.AI.CalamitiesAI.ScatterShotRangedGoal;
import com.Harbinger.Spore.Sentities.BaseEntities.Organoid;
import com.Harbinger.Spore.Sentities.Projectile.AdaptableProjectile;
import com.Harbinger.Spore.Sentities.VariantKeeper;
import com.Harbinger.Spore.Sentities.Variants.BulletParameters;
import com.Harbinger.Spore.Sentities.Variants.UsurperVariants;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Usurper extends Organoid implements RangedAttackMob , VariantKeeper {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Usurper.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIMER = SynchedEntityData.defineId(Usurper.class, EntityDataSerializers.INT);
    public Usurper(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    @Override
    public int getEmerge_tick(){
        return 60;
    }
    @Override
    public int getBorrow_tick() {
        return 100;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, SConfig.SERVER.usurper_hp.get() * SConfig.SERVER.global_health.get())
                .add(Attributes.ARMOR, SConfig.SERVER.usurper_armor.get() * SConfig.SERVER.global_armor.get())
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("timer",entityData.get(TIMER));
        tag.putInt("Variant",this.getTypeVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(TIMER, tag.getInt("timer"));
        entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TIMER,0);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        this.addTargettingGoals();
        this.goalSelector.addGoal(2,new ScatterShotRangedGoal(this,0,40,32,1,4));
        this.goalSelector.addGoal(3 ,new RandomLookAroundGoal(this));
        super.registerGoals();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide){
            if (this.getTarget() == null && this.entityData.get(TIMER) < 1200){
                this.entityData.set(TIMER,this.entityData.get(TIMER) + 1);
            }else if (this.entityData.get(TIMER) >= 1200){
                tickBurrowing();
            }
        }
    }

    @Override
    public List<? extends String> getDropList() {
        return SConfig.DATAGEN.usurper_loot.get();
    }

    @Override
    public void tickBurrowing(){
        int burrowing = this.entityData.get(BORROW);
        if (burrowing > this.getBorrow_tick()) {
            this.discard();
            burrowing = -1;
        }
        this.entityData.set(BORROW, burrowing + 1);
    }
    @Override
    public boolean hurt(DamageSource source, float value) {
        if (this.isEmerging()){
            return false;
        }
        return super.hurt(source, value);
    }
    @Override
    public boolean isNoAi() {
        return this.isBurrowing() || this.isEmerging();
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float p_33318_) {
        BulletParameters parameters = Util.getRandom(BulletParameters.values(),this.random);
        AdaptableProjectile projectile = new AdaptableProjectile(parameters,this.level(),this);
        double dx = livingEntity.getX() - this.getX();
        double dy = livingEntity.getY() + livingEntity.getEyeHeight();
        double dz = livingEntity.getZ() - this.getZ();
        projectile.moveTo(this.getX(), this.getY()+1.2D ,this.getZ());
        projectile.shoot(dx, dy - projectile.getY() + Math.hypot(dx, dz) * 0.001F, dz, 1.5f, 3.0F);
        this.playSound(Ssounds.SPIT.get());
        level().addFreshEntity(projectile);
    }
    protected SoundEvent getAmbientSound() {
        return Ssounds.USURPER_AMBIENT.get();
    }

    protected SoundEvent getDeathSound() {
        return Ssounds.INF_DAMAGE.get();
    }


    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        UsurperVariants variant = Util.getRandom(UsurperVariants.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }


    public UsurperVariants getVariant() {
        return UsurperVariants.byId(this.getTypeVariant() & 255);
    }

    public int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }
    @Override
    public void setVariant(int i) {
        this.entityData.set(DATA_ID_TYPE_VARIANT,i > UsurperVariants.values().length || i < 0 ? 0 : i);
    }

    @Override
    public int amountOfMutations() {
        return UsurperVariants.values().length;
    }

    private void setVariant(UsurperVariants variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public String getMutation() {
        if (getTypeVariant() != 0){
            return this.getVariant().getName();
        }
        return super.getMutation();
    }
}
