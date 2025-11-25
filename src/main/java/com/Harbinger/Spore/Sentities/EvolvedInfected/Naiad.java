package com.Harbinger.Spore.Sentities.EvolvedInfected;

import com.Harbinger.Spore.Sentities.AI.CustomMeleeAttackGoal;
import com.Harbinger.Spore.Sentities.AI.HybridPathNavigation;
import com.Harbinger.Spore.Sentities.BaseEntities.EvolvedInfected;
import com.Harbinger.Spore.Sentities.EvolvingInfected;
import com.Harbinger.Spore.Sentities.Hyper.Inquisitor;
import com.Harbinger.Spore.Sentities.MovementControls.WaterXlandMovement;
import com.Harbinger.Spore.Sentities.WaterInfected;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SdamageTypes;
import com.Harbinger.Spore.core.Sentities;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
        this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1.5, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return 4.0 + entity.getBbWidth() * entity.getBbWidth();}});

        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FindWaterTerritoryGoal(this, 16, 8));
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
        private final double searchRadius;
        private final int waterSizeRequired;

        public FindWaterTerritoryGoal(Naiad naiad, double searchRadius, int waterSizeRequired) {
            this.naiad = naiad;
            this.searchRadius = searchRadius;
            this.waterSizeRequired = waterSizeRequired;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return naiad.getTerritory().equals(BlockPos.ZERO);
        }

        @Override
        public void start() {
            Level level = naiad.level();
            BlockPos origin = naiad.blockPosition();

            BlockPos found = findWaterBody(level, origin);

            if (found != null) {
                naiad.setTerritory(found);
                return;
            }

            BlockPos biomeTarget = findNearestWaterBiome(level, origin);
            if (biomeTarget != null && naiad.tickCount % 20 == 0) {
                naiad.getNavigation().moveTo(biomeTarget.getX(), biomeTarget.getY(), biomeTarget.getZ(), 1.0);
            }
        }

        private BlockPos findWaterBody(Level level, BlockPos center) {
            int radius = (int)searchRadius;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.offset(x, 0, z);

                    if (level.getFluidState(pos).isSource()) {
                        int connected = floodFillWater(level, pos, waterSizeRequired);

                        if (connected >= waterSizeRequired) {
                            return pos;
                        }
                    }
                }
            }
            return null;
        }

        // Simple water flood-fill counter
        private int floodFillWater(Level level, BlockPos start, int limit) {
            HashSet<BlockPos> visited = new HashSet<>();
            ArrayDeque<BlockPos> queue = new ArrayDeque<>();
            queue.add(start);

            while (!queue.isEmpty() && visited.size() < limit) {
                BlockPos pos = queue.poll();
                if (visited.contains(pos)) continue;
                visited.add(pos);

                for (Direction dir : Direction.values()) {
                    BlockPos next = pos.relative(dir);
                    if (!visited.contains(next) && level.getFluidState(next).isSource()) {
                        queue.add(next);
                    }
                }
            }
            return visited.size();
        }

        private BlockPos findNearestWaterBiome(Level level, BlockPos origin) {
            int check = 128;

            for (int r = 8; r < check; r += 8) {
                for (int x = -r; x <= r; x += 8) {
                    for (int z = -r; z <= r; z += 8) {
                        BlockPos pos = origin.offset(x, 0, z);

                        var biome = level.getBiome(pos);

                        if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN) || biome.is(BiomeTags.IS_RIVER)) {
                            return pos;
                        }
                    }
                }
            }
            return null;
        }
    }

}
