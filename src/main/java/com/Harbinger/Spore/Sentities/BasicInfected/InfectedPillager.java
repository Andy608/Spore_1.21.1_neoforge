package com.Harbinger.Spore.Sentities.BasicInfected;

import com.Harbinger.Spore.Sentities.AI.CustomMeleeAttackGoal;
import com.Harbinger.Spore.Sentities.ArmedInfected;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.EvolvingInfected;
import com.Harbinger.Spore.Sentities.Variants.ScamperVariants;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfectedPillager extends Infected implements CrossbowAttackMob , EvolvingInfected, ArmedInfected {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(InfectedPillager.class, EntityDataSerializers.BOOLEAN);

    public InfectedPillager(EntityType<? extends Infected> type, Level level) {
        super(type, level);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_33280_) {
        return p_33280_ == Items.CROSSBOW;
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean p_33302_) {
        this.entityData.set(IS_CHARGING_CROSSBOW, p_33302_);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CHARGING_CROSSBOW, false);
    }

    @Override
    public List<? extends String> getDropList() {
        return SConfig.DATAGEN.inf_pillager_loot.get();
    }
    @Override
    public boolean startRiding(Entity entity) {
        if (!this.getMainHandItem().equals(new ItemStack(Items.CROSSBOW))){
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        }
        return super.startRiding(entity);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F));
        this.goalSelector.addGoal(2 , new CustomMeleeAttackGoal(this ,1.4,true){
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
            return 3.0 + entity.getBbWidth() * entity.getBbWidth();
        }});
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, SConfig.SERVER.inf_pil_hp.get() * SConfig.SERVER.global_health.get())
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, SConfig.SERVER.inf_pil_damage.get() * SConfig.SERVER.global_damage.get())
                .add(Attributes.ARMOR, SConfig.SERVER.inf_pil_armor.get() * SConfig.SERVER.global_armor.get())
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.ATTACK_KNOCKBACK, 1);

    }
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }


    protected void populateDefaultEquipmentSlots(RandomSource p_219059_, DifficultyInstance p_219060_) {
        this.setItemSlot(EquipmentSlot.MAINHAND,Math.random() < 0.5 ? new ItemStack(Items.CROSSBOW) : ItemStack.EMPTY);
    }


    public void performRangedAttack(LivingEntity p_33272_, float p_33273_) {
        this.performCrossbowAttack(this, 1.6F);
    }


    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.populateDefaultEquipmentSlots(this.random, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    protected SoundEvent getAmbientSound() {
        return Ssounds.INF_PILLAGER_AMBIENT.get();
    }

    protected SoundEvent getHurtSound() {
        return Ssounds.INF_DAMAGE.get();
    }

    protected SoundEvent getDeathSound() {
        return Ssounds.INF_DAMAGE.get();
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    protected void playStepSound() {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
    @Override
    public void baseTick() {
        super.baseTick();
        tickEvolution(this,SConfig.SERVER.pil_ev.get(), ScamperVariants.VILLAGER);
    }

    @Override
    public String origin() {
        return "minecraft:pillager";
    }
}
