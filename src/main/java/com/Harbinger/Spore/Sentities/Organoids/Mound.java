package com.Harbinger.Spore.Sentities.Organoids;

import com.Harbinger.Spore.ExtremelySusThings.SporeSavedData;
import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.SBlockEntities.ContainerBlockEntity;
import com.Harbinger.Spore.SBlockEntities.LivingStructureBlocks;
import com.Harbinger.Spore.Sentities.AI.HurtTargetGoal;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.Organoid;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.Sentities.FoliageSpread;
import com.Harbinger.Spore.Sentities.Signal;
import com.Harbinger.Spore.Sentities.Utility.InfectionTendril;
import com.Harbinger.Spore.core.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Mound extends Organoid implements FoliageSpread {
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(Mound.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COUNTER = SynchedEntityData.defineId(Mound.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_AGE = SynchedEntityData.defineId(Mound.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> STRUCTURE = SynchedEntityData.defineId(Mound.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LINKED = SynchedEntityData.defineId(Mound.class, EntityDataSerializers.BOOLEAN);

    private final int maxCounter = SConfig.SERVER.mound_cooldown.get();
    private int attackCooldown = 0;

    private static final int AGE_INCREMENT_INTERVAL = 20;
    private static final int ATTACK_CLOUD_DURATION = 300;
    private static final int ATTACK_CLOUD_COOLDOWN = 300;
    private static final int MYCELIUM_EFFECT_DURATION = 600;
    private static final int MAX_TENDRILS = 4;
    private static final float STRUCTURE_PLACEMENT_CHANCE = 0.1f;
    private static final double MIN_STRUCTURE_DISTANCE_SQ = 80.0;

    public Mound(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, SConfig.SERVER.mound_hp.get() * SConfig.SERVER.global_health.get())
                .add(Attributes.ARMOR, SConfig.SERVER.mound_armor.get() * SConfig.SERVER.global_armor.get())
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        EntityDimensions baseDimensions = super.getDefaultDimensions(pose);
        return baseDimensions.scale(Math.max(1.0f, this.getAge()));
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return Math.max(0, super.calculateFallDamage(fallDistance, damageMultiplier) - 30);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return this.getLinked() && this.getMaxAge() <= 2;
    }

    @Override
    public List<? extends String> getDropList() {
        return SConfig.DATAGEN.mound_loot.get();
    }

    @Override
    public int getEmerge_tick() {
        return 40;
    }

    @Override
    public boolean isCloseCombatant() {
        return true;
    }


    public int getAge() {
        return entityData.get(AGE);
    }

    public void setAge(int age) {
        entityData.set(AGE, age);
    }

    public int getCounter() {
        return entityData.get(COUNTER);
    }

    public void setCounter(int counter) {
        entityData.set(COUNTER, counter);
    }

    public int getMaxAge() {
        return entityData.get(MAX_AGE);
    }

    public void setMaxAge(int maxAge) {
        entityData.set(MAX_AGE, maxAge);
    }

    public boolean getLinked() {
        return entityData.get(LINKED);
    }

    public void setLinked(boolean linked) {
        entityData.set(LINKED, linked);
    }

    public int getAgeCounter() {
        return this.getPersistentData().getInt("age");
    }


    @Override
    public void tick() {
        super.tick();

        if (this.tickCount % AGE_INCREMENT_INTERVAL == 0) {
            handleAgeIncrement();
            handleInfectionSpread();
            handleParticleEffects();
        }

        if (this.isAlive() && attackCooldown > 0) {
            attackCooldown--;
        }
    }

    private void handleAgeIncrement() {
        if (this.isAlive() && getAge() < getMaxAge()) {
            int currentAgeCounter = getAgeCounter() + 1;
            this.getPersistentData().putInt("age", currentAgeCounter);

            if (currentAgeCounter >= SConfig.SERVER.mound_age.get()) {
                this.getPersistentData().putInt("age", 0);
                setAge(getAge() + 1);
            }
        }
    }

    private void handleInfectionSpread() {
        if (getCounter() < maxCounter) {
            setCounter(getCounter() + 1);
        }

        if (this.isAlive() && getCounter() >= maxCounter && !level().isClientSide) {
            double range = getInfectionRange();
            SpreadInfection(level(), range, this.getOnPos());
            setCounter(0);

            if (this.random.nextInt(10) == 0 && getAge() >= 3 && canSpawnTendrils()) {
                spawnTendril();
            }
        }
    }

    private double getInfectionRange() {
        return switch (getAge()) {
            case 2 -> SConfig.SERVER.mound_range_age2.get();
            case 3 -> SConfig.SERVER.mound_range_age3.get();
            case 4 -> SConfig.SERVER.mound_range_age4.get();
            default -> SConfig.SERVER.mound_range_default.get();
        };
    }

    private void handleParticleEffects() {
        if (getCounter() > (maxCounter - 2) && getCounter() < maxCounter && this.level() instanceof ServerLevel serverLevel) {
            spawnPuffParticles(serverLevel);
        }

        if (getCounter() == (maxCounter - 2)) {
            this.playSound(Ssounds.PUFF.value());
        }
    }

    private void spawnPuffParticles(ServerLevel serverLevel) {
        double x = this.getX() + (random.nextFloat() - 0.2) * 0.2;
        double y = this.getY() + (random.nextFloat() - 0.5) * 5.0;
        double z = this.getZ() + (random.nextFloat() - 0.2) * 0.2;
        serverLevel.sendParticles(Sparticles.SPORE_PARTICLE.get(), x, y, z, 9, 0, 0, 0, 1);
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (attackCooldown == 0) {
            spawnDefensiveCloud();
            attackCooldown = ATTACK_CLOUD_COOLDOWN;
        }
        return super.hurt(source, amount);
    }

    private void spawnDefensiveCloud() {
        if (!this.level().isClientSide) {
            AreaEffectCloud cloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            cloud.setOwner(this);
            cloud.setRadius(2.0F);
            cloud.setDuration(ATTACK_CLOUD_DURATION);
            cloud.setRadiusPerTick(((1.5F * getAge()) - cloud.getRadius()) / (float) cloud.getDuration());
            cloud.addEffect(new MobEffectInstance(Seffects.MYCELIUM, 200, 1));
            this.level().addFreshEntity(cloud);
            this.playSound(Ssounds.PUFF.value(), 0.5f, 0.5f);
        }
    }

    @Override
    public void die(DamageSource source) {
        if (this.getLinked() && this.getAge() > 3 && source.getEntity() != null) {
            if (!isFrozen()) {
                alertNearbyProtos(source);
            }
        }

        for (int i = 0; i <= this.getAge(); i++) {
            super.die(source);
        }
    }

    private boolean isFrozen() {
        return this.isInPowderSnow || this.Cold() ||
                (this.getLastDamageSource() != null && this.getLastDamageSource().is(DamageTypes.FREEZE));
    }

    private void alertNearbyProtos(DamageSource source) {
        if (level() instanceof ServerLevel serverLevel){
            List<Proto> protos = SporeSavedData.getHiveminds(serverLevel);
            if (protos.isEmpty()){
                return;
            }
            for (Proto proto : protos){
                if (proto.distanceTo(this) <= SConfig.SERVER.proto_range.get()){
                    int y = source.getDirectEntity() != null ? (int) source.getDirectEntity().getY() : (int) this.getY();
                    BlockPos relativePos = new BlockPos(this.getOnPos().getX(),y,this.getOnPos().getZ());
                    proto.setSignal(new Signal(true, relativePos));
                    break;
                }
            }
        }
    }


    private boolean canSpawnTendrils() {
        AABB searchBox = this.getBoundingBox().inflate(SConfig.SERVER.mound_tendril_checker.get());
        List<InfectionTendril> existingTendrils = level().getEntitiesOfClass(InfectionTendril.class, searchBox);
        return existingTendrils.size() <= MAX_TENDRILS;
    }

    private void spawnTendril() {
        AABB searchBox = this.getBoundingBox().inflate(SConfig.SERVER.mound_tendril_checker.get());

        for (BlockPos pos : BlockPos.betweenClosed(
                Mth.floor(searchBox.minX), Mth.floor(searchBox.minY), Mth.floor(searchBox.minZ),
                Mth.floor(searchBox.maxX), Mth.floor(searchBox.maxY), Mth.floor(searchBox.maxZ))) {

            BlockState blockState = level().getBlockState(pos);

            if (isValidTendrilTarget(pos, blockState)) {
                InfectionTendril tendril = new InfectionTendril(Sentities.TENDRIL.get(), level());
                tendril.setAgeM(this.getMaxAge() - 1);
                tendril.setSearchArea(pos);
                tendril.setPos(this.getX(), this.getY() + 0.5, this.getZ());
                level().addFreshEntity(tendril);
                break;
            }
        }
    }

    private boolean isValidTendrilTarget(BlockPos pos, BlockState state) {
        return isStructureBlock(pos) ||
                (isChestWithFood(pos) && SConfig.SERVER.tendril_chest.get()) ||
                (state.is(Sblocks.REMAINS.get()) && SConfig.SERVER.tendril_corpse.get()) ||
                (state.is(Blocks.SPAWNER) && SConfig.SERVER.tendril_spawner.get());
    }

    private boolean isChestWithFood(BlockPos pos) {
        BlockEntity blockEntity = this.level().getBlockEntity(pos);
        return blockEntity instanceof Container container &&
                !(container instanceof ContainerBlockEntity) &&
                container.hasAnyMatching(item -> item.getFoodProperties(this) != null);
    }

    private boolean isStructureBlock(BlockPos pos) {
        BlockEntity blockEntity = this.level().getBlockEntity(pos);
        return blockEntity instanceof LivingStructureBlocks;
    }


    @Override
    public void additionPlacers(Level level, BlockPos pos, double range) {
        AABB area = this.getBoundingBox().inflate(range);

        for (BlockPos blockPos : BlockPos.betweenClosed(
                Mth.floor(area.minX), Mth.floor(area.minY), Mth.floor(area.minZ),
                Mth.floor(area.maxX), Mth.floor(area.maxY), Mth.floor(area.maxZ))) {

            if (random.nextFloat() < STRUCTURE_PLACEMENT_CHANCE &&
                    canPlaceStructure(level, blockPos) &&
                    this.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > MIN_STRUCTURE_DISTANCE_SQ) {

                placeStructureBlock(level, blockPos.above());
                entityData.set(STRUCTURE, false);
                break;
            }
        }
    }

    @Override
    public void additionIgnoreConfigPlacers(Level level, BlockPos pos, double range) {
        AABB area = this.getBoundingBox().inflate(range);

        // Structure placement
        for (BlockPos blockPos : BlockPos.betweenClosed(
                Mth.floor(area.minX), Mth.floor(area.minY), Mth.floor(area.minZ),
                Mth.floor(area.maxX), Mth.floor(area.maxY), Mth.floor(area.maxZ))) {

            if (random.nextFloat() < STRUCTURE_PLACEMENT_CHANCE &&
                    canPlaceStructure(level, blockPos) &&
                    this.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > MIN_STRUCTURE_DISTANCE_SQ) {

                placeStructureBlock(level, blockPos.above());
                entityData.set(STRUCTURE, false);
                break;
            }
        }

        // Helmet effect application
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            if (Utilities.TARGET_SELECTOR.Test(entity) &&
                    Utilities.helmetList().contains(entity.getItemBySlot(EquipmentSlot.HEAD).getItem())) {
                entity.addEffect(new MobEffectInstance(Seffects.MYCELIUM, MYCELIUM_EFFECT_DURATION, 1));
            }
        }
    }

    private boolean canPlaceStructure(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        BlockState aboveState = level.getBlockState(pos.above());
        return aboveState.isAir() &&
                blockState.isSolidRender(level, pos) &&
                entityData.get(STRUCTURE) &&
                getAge() >= getMaxAge();
    }

    private void placeStructureBlock(Level level, BlockPos pos) {
        List<Holder<Block>> structureBlocks = Utilities.tryToCreateBlockFromTag(level, ResourceLocation.parse("spore:block_st"));
        if (!structureBlocks.isEmpty()) {
            BlockState blockState = structureBlocks.get(random.nextInt(structureBlocks.size())).value().defaultBlockState();
            level.setBlock(pos, blockState, Block.UPDATE_ALL);
        }
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("age", getAge());
        tag.putInt("counter", getCounter());
        tag.putInt("max_age", getMaxAge());
        tag.putBoolean("structure", entityData.get(STRUCTURE));
        tag.putBoolean("linked", getLinked());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(AGE, tag.getInt("age"));
        entityData.set(COUNTER, tag.getInt("counter"));
        entityData.set(MAX_AGE, tag.getInt("max_age"));
        entityData.set(STRUCTURE, tag.getBoolean("structure"));
        entityData.set(LINKED, tag.getBoolean("linked"));
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AGE, 1);
        builder.define(COUNTER, 0);
        builder.define(MAX_AGE, 4);
        builder.define(STRUCTURE, true);
        builder.define(LINKED, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (AGE.equals(dataAccessor)) {
            updateAttributes();
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    private void updateAttributes() {
        double health = SConfig.SERVER.mound_hp.get() * getAge() * SConfig.SERVER.global_health.get();
        double armor = SConfig.SERVER.mound_armor.get() * getAge() * SConfig.SERVER.global_armor.get();

        AttributeInstance healthAttr = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance armorAttr = this.getAttribute(Attributes.ARMOR);

        if (healthAttr != null) healthAttr.setBaseValue(health);
        if (armorAttr != null) armorAttr.setBaseValue(armor);
    }

    // ========== AI & SPAWNING ==========

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(2, new HurtTargetGoal(this,
                enemy -> !(SConfig.SERVER.blacklist.get().contains(enemy.getEncodeId()) || enemy instanceof UtilityEntity),
                Infected.class).setAlertOthers(Infected.class));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setDefaultLinkage(level.getLevel());
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public void setDefaultLinkage(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            SporeSavedData data = SporeSavedData.getDataLocation(serverLevel);
            if (data != null && data.getAmountOfHiveminds() >= SConfig.SERVER.proto_spawn_world_mod.get()) {
                this.setLinked(true);
            }
        }
    }
}