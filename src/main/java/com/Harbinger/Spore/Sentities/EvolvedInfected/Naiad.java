package com.Harbinger.Spore.Sentities.EvolvedInfected;

import com.Harbinger.Spore.Sentities.AI.CustomMeleeAttackGoal;
import com.Harbinger.Spore.Sentities.AI.HurtTargetGoal;
import com.Harbinger.Spore.Sentities.AI.HybridPathNavigation;
import com.Harbinger.Spore.Sentities.BaseEntities.EvolvedInfected;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.EvolvingInfected;
import com.Harbinger.Spore.Sentities.Hyper.Inquisitor;
import com.Harbinger.Spore.Sentities.MovementControls.WaterXlandMovement;
import com.Harbinger.Spore.Sentities.WaterInfected;
import com.Harbinger.Spore.core.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Naiad extends EvolvedInfected implements WaterInfected {
    public static final EntityDataAccessor<BlockPos> TERRITORY = SynchedEntityData.defineId(Naiad.class, EntityDataSerializers.BLOCK_POS);
    public Naiad(EntityType<? extends EvolvedInfected> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new WaterXlandMovement(this);
        this.navigation = new HybridPathNavigation(this,this.level());
    }

    @Override
    protected void addRegularGoals() {
        super.addRegularGoals();
        this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return 4.0 + entity.getBbWidth() * entity.getBbWidth();}});

        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FindWaterTerritoryGoal(this));
        this.goalSelector.addGoal(7, new BreakBoatsGoal(this,1.2));
    }

    @Override
    protected void addTargettingGoals(){
        this.goalSelector.addGoal(2, new HurtTargetGoal(this , livingEntity -> {return TARGET_SELECTOR.test(livingEntity);}, Infected.class).setAlertOthers(Infected.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>
                (this, LivingEntity.class,  true,livingEntity -> {return livingEntity instanceof Player || SConfig.SERVER.whitelist.get().contains(livingEntity.getEncodeId());}){
            @Override
            protected AABB getTargetSearchArea(double targetDistance) {
                return this.mob.getBoundingBox().inflate(targetDistance);
            }
        });
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>
                (this, LivingEntity.class,  true, livingEntity -> {return SConfig.SERVER.at_mob.get() && TARGET_SELECTOR.test(livingEntity);}){
            @Override
            protected AABB getTargetSearchArea(double targetDistance) {
                return this.mob.getBoundingBox().inflate(targetDistance);
            }
        });
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TERRITORY,BlockPos.ZERO);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("TerritoryX",entityData.get(TERRITORY).getX());
        tag.putInt("TerritoryY",entityData.get(TERRITORY).getY());
        tag.putInt("TerritoryZ",entityData.get(TERRITORY).getZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        int tX = tag.getInt("TerritoryX");
        int tY = tag.getInt("TerritoryY");
        int tZ = tag.getInt("TerritoryZ");
        entityData.set(TERRITORY,new BlockPos(tX,tY,tZ));
    }
    public BlockPos getTerritory(){
        return entityData.get(TERRITORY);
    }
    public void setTerritory(BlockPos pos){
        entityData.set(TERRITORY,pos);
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    public void travel(Vec3 p_32858_) {
        if (this.isEffectiveAi() && this.isInFluidType()) {
            this.moveRelative(0.1F, p_32858_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.85D));
        } else {
            super.travel(p_32858_);
        }
    }

    @Override
    public List<? extends String> getDropList() {
        return SConfig.DATAGEN.inf_knight_loot.get();
    }
    @Override
    public DamageSource getCustomDamage(LivingEntity entity) {
        if (Math.random() < 0.3){
            return SdamageTypes.knight_damage(this);
        }
        return super.getCustomDamage(entity);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, SConfig.SERVER.knight_hp.get() * SConfig.SERVER.global_health.get())
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ATTACK_DAMAGE, SConfig.SERVER.knight_damage.get() * SConfig.SERVER.global_damage.get())
                .add(Attributes.ARMOR, SConfig.SERVER.knight_armor.get() * SConfig.SERVER.global_armor.get())
                .add(Attributes.FOLLOW_RANGE, 32);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (entity.isInFluidType()){
            if (entity instanceof LivingEntity living){
                living.addEffect(new MobEffectInstance(Seffects.MARKER,200,1));
            }
            entity.setDeltaMovement(getDeltaMovement().add(0f,-1f,0));
        }
        return super.doHurtTarget(entity);
    }
    protected SoundEvent getAmbientSound() {
        return Ssounds.DROWNED_AMBIENT.value();
    }

    protected SoundEvent getDeathSound() {
        return Ssounds.INF_DAMAGE.value();
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.DROWNED_STEP;
    }

    protected void playStepSound(BlockPos p_34316_, BlockState p_34317_) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
    public static class FindWaterTerritoryGoal extends Goal {
        private final Naiad naiad;
        private BlockPos targetPos;
        private int cooldown = 0;

        public FindWaterTerritoryGoal(Naiad naiad) {
            this.naiad = naiad;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            BlockPos territory = naiad.getTerritory();
            if (territory.equals(BlockPos.ZERO)) {
                return true;
            }

            double distanceSqr = territory.distToCenterSqr(naiad.position());
            if (distanceSqr > 400) {
                cooldown = 40;
                return true;
            }

            return false;
        }

        @Override
        public void start() {
            Level level = naiad.level();
            BlockPos currentTerritory = naiad.getTerritory();

            if (currentTerritory.equals(BlockPos.ZERO)) {
                // Find new territory
                targetPos = findNearestWaterBiome(level, naiad.blockPosition());
                if (targetPos != null) {
                    naiad.setTerritory(targetPos);
                }
            } else {
                // Use existing territory
                targetPos = currentTerritory;
            }

            if (targetPos != null) {
                naiad.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return targetPos != null &&
                    !naiad.getNavigation().isDone() &&
                    targetPos.distToCenterSqr(naiad.position()) > 9;
        }

        @Override
        public void stop() {
            targetPos = null;
            naiad.getNavigation().stop();
        }

        private BlockPos findNearestWaterBiome(Level level, BlockPos origin) {
            int range = 128;
            int step = 4;

            for (int r = 4; r <= range; r += step) {
                for (int i = 0; i < r * 2; i += step) {
                    BlockPos pos1 = origin.offset(r, 0, i - r);
                    BlockPos pos2 = origin.offset(-r, 0, i - r);
                    BlockPos pos3 = origin.offset(i - r, 0, r);
                    BlockPos pos4 = origin.offset(i - r, 0, -r);

                    BlockPos[] positions = {pos1, pos2, pos3, pos4};

                    for (BlockPos pos : positions) {
                        if (level.isLoaded(pos)) {
                            var biome = level.getBiome(pos);
                            if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN) || biome.is(BiomeTags.IS_RIVER)) {
                                BlockPos surfacePos = findWaterSurface(level, pos);
                                return surfacePos != null ? surfacePos : pos;
                            }
                        }
                    }
                }
            }
            return null;
        }

        private BlockPos findWaterSurface(Level level, BlockPos pos) {
            for (int y = level.getSeaLevel(); y > level.getMinBuildHeight(); y--) {
                BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
                if (!level.getFluidState(checkPos).isEmpty() &&
                        level.getBlockState(checkPos.above()).isAir()) {
                    return checkPos;
                }
            }
            return null;
        }
    }
    public static class BreakBoatsGoal extends Goal {
        private final Naiad naiad;
        private Boat targetBoat;
        private int breakTime;
        private final double speedModifier;

        public BreakBoatsGoal(Naiad naiad, double speedModifier) {
            this.naiad = naiad;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (naiad.getTarget() != null) return false;

            // Find nearby boats
            List<Boat> boats = naiad.level().getEntitiesOfClass(Boat.class,
                    naiad.getBoundingBox().inflate(16.0),
                    boat -> boat != null && !boat.isRemoved());

            if (boats.isEmpty()) return false;

            // Find the closest boat
            Boat closestBoat = null;
            double closestDistance = Double.MAX_VALUE;

            for (Boat boat : boats) {
                double distance = naiad.distanceToSqr(boat);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestBoat = boat;
                }
            }

            if (closestBoat != null) {
                this.targetBoat = closestBoat;
                return true;
            }

            return false;
        }

        @Override
        public void start() {
            this.breakTime = 0;
            if (targetBoat != null) {
                naiad.getNavigation().moveTo(targetBoat, speedModifier);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return targetBoat != null &&
                    !targetBoat.isRemoved() &&
                    naiad.distanceToSqr(targetBoat) <= 256.0 && // 16 blocks max distance
                    naiad.getTarget() == null; // Don't break boats while attacking something
        }

        @Override
        public void tick() {
            if (targetBoat == null || targetBoat.isRemoved()) {
                return;
            }

            naiad.getLookControl().setLookAt(targetBoat, 30.0F, 30.0F);

            double distance = naiad.distanceToSqr(targetBoat);
            if (distance > 9.0) { // 3 blocks
                naiad.getNavigation().moveTo(targetBoat, speedModifier);
            } else {
                naiad.getNavigation().stop();

                breakTime++;
                if (breakTime >= 20) {
                    breakBoat();
                }
            }
        }

        @Override
        public void stop() {
            this.targetBoat = null;
            this.breakTime = 0;
            naiad.getNavigation().stop();
        }

        private void breakBoat() {
            if (targetBoat != null && !targetBoat.isRemoved()) {
                Level level = naiad.level();

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SPLASH,
                            targetBoat.getX(), targetBoat.getY() + 0.5, targetBoat.getZ(),
                            10, 0.5, 0.5, 0.5, 0.1);

                    serverLevel.playSound(null, targetBoat.blockPosition(),
                            SoundEvents.WOOD_BREAK, SoundSource.HOSTILE, 1.0F, 1.0F);
                }

                if (!targetBoat.hasCustomName() && naiad.getRandom().nextFloat() < 0.8F) {
                    targetBoat.spawnAtLocation(new ItemStack(Items.STICK));
                }

                targetBoat.discard();

                stop();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
    @Override
    public boolean hasLineOfSight(Entity entity) {
        if (entity instanceof LivingEntity living && living.isInWater() && living.getHealth() < living.getMaxHealth()){
            return true;
        }
        return super.hasLineOfSight(entity);
    }
}
