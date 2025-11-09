package com.Harbinger.Spore.Sentities.BaseEntities;

import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Senchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn;

public class UtilityEntity extends PathfinderMob {
    protected UtilityEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
    public List<? extends String> getDropList(){
        return null;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        DamageSource damagesource = this.damageSources().mobAttack(this);
        Level var5 = this.level();
        if (var5 instanceof ServerLevel serverlevel) {
            f = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, f);
        }

        boolean flag = entity.hurt(getCustomDamage(this), f);
        if (flag) {
            float f1 = this.getKnockback(entity, damagesource);
            if (f1 > 0.0F && entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                livingentity.knockback((double)(f1 * 0.5F), (double)Mth.sin(this.getYRot() * 0.017453292F), (double)(-Mth.cos(this.getYRot() * 0.017453292F)));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            Level var7 = this.level();
            if (var7 instanceof ServerLevel) {
                ServerLevel serverlevel1 = (ServerLevel)var7;
                EnchantmentHelper.doPostAttackEffects(serverlevel1, entity, damagesource);
            }

            this.setLastHurtMob(entity);
            this.playAttackSound();
        }

        return flag;
    }

    public DamageSource getCustomDamage(LivingEntity entity){
        return this.damageSources().mobAttack(entity);
    }

    public static boolean checkMonsterInfectedRules(EntityType<? extends Infected> p_219014_, ServerLevelAccessor levelAccessor, MobSpawnType type, BlockPos pos, RandomSource source) {
        if (levelAccessor.getDifficulty() != Difficulty.PEACEFUL){
            return furtherSpawnParameters(p_219014_,levelAccessor,type,pos,source);
        }
        return false;
    }
    private static boolean furtherSpawnParameters(EntityType<? extends Infected> p_219014_,ServerLevelAccessor levelAccessor, MobSpawnType type, BlockPos pos, RandomSource source){
        MinecraftServer server = levelAccessor.getServer();
        if (server != null){
            if (server.getPlayerList().getPlayers().isEmpty()){
                return false;
            }
        }
        return isDarkEnoughToSpawn(levelAccessor, pos, source) && checkMobSpawnRules(p_219014_, levelAccessor, type, pos, source);
    }
    public Predicate<LivingEntity> TARGET_SELECTOR = (entity) -> {
        return Utilities.TARGET_SELECTOR.Test(entity);
    };

    protected void addTargettingGoals(){
        this.goalSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers(Infected.class,Utilities.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>
                (this, LivingEntity.class,  true,livingEntity -> {return livingEntity instanceof Player || SConfig.SERVER.whitelist.get().contains(livingEntity.getEncodeId());}){
            @Override
            protected AABB getTargetSearchArea(double value) {
                return this.mob.getBoundingBox().inflate(value, value, value);
            }
        });
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>
                (this, LivingEntity.class,  true, livingEntity -> {return SConfig.SERVER.at_mob.get() && TARGET_SELECTOR.test(livingEntity);}){
            @Override
            protected AABB getTargetSearchArea(double value) {
                return this.mob.getBoundingBox().inflate(value, value, value);
            }
        });
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        if (getDropList() == null){return;}
        int val = 0;
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            Holder<Enchantment> lootingHolder = Senchantments.getEnchantment(attacker.level(),Enchantments.LOOTING);
            val = weapon.getEnchantmentLevel(lootingHolder);
        }
        if (!getDropList().isEmpty()){
            for (String str : getDropList()){
                String[] string = str.split("\\|" );
                ItemStack itemStack = Utilities.tryToCreateStack(ResourceLocation.parse(string[0]));
                int m = 1;
                if (Integer.parseUnsignedInt(string[2]) == Integer.parseUnsignedInt(string[3])){
                    int o = Integer.parseUnsignedInt(string[3]);
                    m = val > 0 ? random.nextInt(o,o+val) : o;

                } else {if (Integer.parseUnsignedInt(string[2]) >= 1 && Integer.parseUnsignedInt(string[2]) >= 1){
                    int v1 = Integer.parseUnsignedInt(string[2]);
                    int v2 = Integer.parseUnsignedInt(string[3]);
                    float e = m * (0.15f * val);
                    int i = e > val ? (int) e : val;
                    m = random.nextInt(v1, v2+i);
                }}
                int value = Integer.parseUnsignedInt(string[1])+(val*10);
                if (Math.random() < (value / 100F)) {
                    itemStack.setCount(m);
                    ItemEntity item = new ItemEntity(level(), this.getX() , this.getY(),this.getZ(),itemStack);
                    item.setPickUpDelay(10);
                    level().addFreshEntity(item);}}
        }
    }

    protected boolean Cold(){
        BlockPos pos = new BlockPos(this.getBlockX(),this.getBlockY(),this.getBlockZ());
        Biome biome = level().getBiome(pos).value();
        return SConfig.SERVER.weaktocold.get() && biome.getBaseTemperature() <= 0.2;
    }

    public String getMutation(){
        return null;
    }
}
