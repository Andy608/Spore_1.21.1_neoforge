package com.Harbinger.Spore.Sevents;

import com.Harbinger.Spore.ExtremelySusThings.ChunkLoadRequest;
import com.Harbinger.Spore.ExtremelySusThings.ChunkLoaderHelper;
import com.Harbinger.Spore.ExtremelySusThings.SporeSavedData;
import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Sentities.BaseEntities.*;
import com.Harbinger.Spore.Sentities.BasicInfected.InfectedDrowned;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Protector;
import com.Harbinger.Spore.Sentities.Organoids.Proto;
import com.Harbinger.Spore.Sentities.Utility.ScentEntity;
import com.Harbinger.Spore.Sitems.BaseWeapons.LootModifierWeapon;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeBaseArmor;
import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.*;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.*;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Spore.MODID)
public class HandlerEvents {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        DespawnSystem.tickMobCleaner(event.getServer());
        ChunkLoaderHelper.tick();
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            ChunkLoadRequest.loadChunkData(level);
        }
    }
    @SubscribeEvent
    public static void TickEvents(PlayerTickEvent.Pre event){
        LivingTickEvent.TickEffects(event);
        Player player = event.getEntity();
            Level level = player.level();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    ItemStack armorStack = player.getItemBySlot(slot);
                    if (!armorStack.isEmpty() && armorStack.getItem() instanceof SporeBaseArmor sporeBaseArmor) {
                        if (!level.isClientSide) {
                            sporeBaseArmor.tickArmor(player,level);
                        }
                    }
                }
            }
        Holder<MobEffect> madnessHolder = Seffects.MADNESS;
            MobEffectInstance effectInstance = player.getEffect(madnessHolder);
            if (effectInstance != null && effectInstance.getDuration() == 1) {
                int amplifier = effectInstance.getAmplifier();
                if (amplifier > 0) {
                    player.removeEffect(madnessHolder);
                    player.addEffect(new MobEffectInstance(madnessHolder, 12000, amplifier - 1));
                }
            }
    }

    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                SblockEntities.SURGERY_TABLE_ENTITY.get(),
                (blockEntity, context) -> blockEntity.getItemHandler()
        );
    }
    @SubscribeEvent
    private static void registerAttributes(EntityAttributeCreationEvent event){
        MobAttributes.registerAttributes(event);
    }

    @SubscribeEvent
    public static void Command(RegisterCommandsEvent event){
        Commands.Command(event);
    }
    @SubscribeEvent
    public static void TickEvents(EntityTickEvent.Pre event){
        LivingTickEvent.TickEvents(event);
    }
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        Infection.onEntityDeath(event);
    }

    @SubscribeEvent
    public static void DefenseBypass(LivingDamageEvent.Pre event) {
        DamageHandeling.DefenseBypass(event);
    }

    @SubscribeEvent
    public static void NoSleep(CanPlayerSleepEvent event){
        ServerPlayer player = event.getEntity();
        if(player.hasEffect(Seffects.UNEASY)){
            player.displayClientMessage(Component.translatable("uneasy.message"),true);
            event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof Snowball) {
            if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) event.getRayTraceResult()).getEntity();
                if (entity instanceof LivingEntity living) {
                    if (living.canFreeze()) living.setTicksFrozen(living.getTicksFrozen() + 100);
                }
            }
        }
    }

    @SubscribeEvent
    public static void DiscardProto(EntityLeaveLevelEvent event){
        if (event.getEntity() instanceof Protector protector){
            SporeSavedData.removeProtector(protector);
        }
        if (event.getEntity() instanceof Proto proto && event.getLevel() instanceof ServerLevel level){
            SporeSavedData.removeHivemind(level,proto);
        }
    }

    @SubscribeEvent
    public static void LoadCalamity(EntityEvent.EnteringSection event){
        if (event.getEntity() instanceof Calamity calamity && calamity.level() instanceof ServerLevel level && calamity.getSearchArea() != BlockPos.ZERO){
            SectionPos OldChunk = event.getOldPos();
            SectionPos NewChunk = event.getNewPos();
            if (SConfig.SERVER.calamity_chunk.get() && event.didChunkChange() && OldChunk != NewChunk ){
                if (calamity.getSearchArea() != BlockPos.ZERO){
                    ChunkPos chunk = NewChunk.chunk();
                    UUID ownerId = calamity.getUUID();
                    String id = "calamity_" + ownerId + "_" + chunk.toString();
                    ChunkLoadRequest request = new ChunkLoadRequest(
                            level.dimension(),
                            new ChunkPos[]{chunk},
                            0,
                            id,
                            20 * 30,
                            ownerId
                    );
                    ChunkLoaderHelper.addRequest(request);
                }
            }
        }
        if (event.getEntity() instanceof Proto proto && proto.level() instanceof ServerLevel level && SConfig.SERVER.proto_chunk.get()){
            SectionPos OldChunk = event.getOldPos();
            SectionPos NewChunk = event.getNewPos();
            if (event.didChunkChange() && OldChunk != NewChunk){
                ChunkPos chunk = NewChunk.chunk();
                UUID ownerId = proto.getUUID();
                String id = "hivemind_" + ownerId + "_" + chunk.toString();
                ChunkLoadRequest request = new ChunkLoadRequest(
                        level.dimension(),
                        new ChunkPos[]{chunk},
                        0,
                        id,
                        20 * 60 * 10,
                        ownerId
                );
                ChunkLoaderHelper.addRequest(request);
            }
        }
    }
    @SubscribeEvent
    public static void FallProt(LivingFallEvent event){
        if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() == Sitems.INF_UP_BOOTS.get()){
            event.setDistance(event.getDistance()-25);
        }
    }
    @SubscribeEvent
    public static void onLivingSpawned(EntityJoinLevelEvent event) {
        if (event != null) {
            if (event.getEntity() instanceof Protector protector) {
                SporeSavedData.addProtector(protector);
            }
            if (event.getEntity() instanceof Proto proto && event.getLevel() instanceof ServerLevel serverLevel) {
                SporeSavedData.addHivemind(serverLevel, proto);
            }
            if (event.getEntity() instanceof PathfinderMob mob) {

                for (String string : SConfig.SERVER.attack.get()) {
                    if (string.endsWith(":")) {
                        String[] mod = string.split(":");
                        String[] iterations = mob.getEncodeId().split(":");
                        if (Objects.equals(mod[0], iterations[0])) {
                            mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Infected.class, false));
                            mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Calamity.class, false));
                            mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Organoid.class, false));
                        }
                    } else {
                        if (SConfig.SERVER.attack.get().contains(mob.getEncodeId())) {
                            mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Infected.class, false));
                            mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Calamity.class, false));
                            mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Organoid.class, false));
                        }
                    }
                }
                for (String string : SConfig.SERVER.flee.get()) {
                    if (string.endsWith(":")) {
                        String[] mod = string.split(":");
                        String[] iterations = mob.getEncodeId().split(":");
                        if (Objects.equals(mod[0], iterations[0])) {
                            mob.goalSelector.addGoal(4, new AvoidEntityGoal<>(mob, Infected.class, 6.0F, 1.0D, 0.9D));
                            mob.goalSelector.addGoal(4, new AvoidEntityGoal<>(mob, UtilityEntity.class, 8.0F, 1.0D, 0.9D));
                        }
                    } else {
                        if (SConfig.SERVER.flee.get().contains(mob.getEncodeId())) {
                            mob.goalSelector.addGoal(4, new AvoidEntityGoal<>(mob, Infected.class, 6.0F, 1.0D, 0.9D));
                            mob.goalSelector.addGoal(4, new AvoidEntityGoal<>(mob, UtilityEntity.class, 8.0F, 1.0D, 0.9D));

                        }
                    }
                }
            }
        }
    }
    private static List<EntityType<?>> blacklist(){
        List<EntityType<?>> values = new ArrayList<>();
        values.add(Sentities.PLAGUED.get());
        values.add(Sentities.LACERATOR.get());
        values.add(Sentities.BIOBLOOB.get());
        values.add(Sentities.SAUGLING.get());
        return values;
    }
    @SubscribeEvent
    public static void SpawnPlacement(RegisterSpawnPlacementsEvent event){
        for (DeferredHolder<?,?> type : Sentities.SPORE_ENTITIES.getEntries()){
            EntityType<?> entityType = (EntityType<?>) type.get();
            if (blacklist().contains(entityType)){continue;}
            try {
                event.register((EntityType<UtilityEntity>) entityType, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,UtilityEntity::checkMonsterInfectedRules, RegisterSpawnPlacementsEvent.Operation.AND);
            } catch (Exception e) {
                String id = entityType.getDescriptionId();
                Spore.LOGGER.warn("Could not apply custom placement {}: {}", id, e.getMessage());
            }
        }
    }
    @SubscribeEvent
    public static void FishingAnInfectedDrowned(ItemFishedEvent event){
        if (event != null){
            if (Math.random() < 0.05 && event.getHookEntity().isOpenWaterFishing()){
                InfectedDrowned infectedDrowned = new InfectedDrowned(Sentities.INF_DROWNED.get(),event.getEntity().level());
                infectedDrowned.moveTo(event.getHookEntity().getX(),event.getHookEntity().getY(),event.getHookEntity().getZ());
                infectedDrowned.setKills(1);
                infectedDrowned.setTarget(event.getEntity());
                event.getEntity().level().addFreshEntity(infectedDrowned);
            }
        }
    }

    @SubscribeEvent
    public static void ExplosiveBite(LivingEntityUseItemEvent.Finish event){
        if (event != null && !event.getEntity().level().isClientSide){
            Item item = event.getItem().getItem();
            if (item == Sitems.ROASTED_TUMOR.get() && Math.random() < 0.2){
                LivingEntity entity = event.getEntity();
                entity.level().explode(null,entity.getX(),entity.getY(),entity.getZ(),0.5f, Level.ExplosionInteraction.NONE);
            }
            if (item == Sitems.MILKY_SACK.get()){
                LivingEntity entity = event.getEntity();
                List<MobEffectInstance> effectsToRemove = new ArrayList<>();
                entity.getActiveEffects().forEach(mobEffectInstance -> {
                    if (!mobEffectInstance.getEffect().value().isBeneficial()) {
                        effectsToRemove.add(mobEffectInstance);
                    }
                });
                effectsToRemove.forEach(mobEffectInstance -> entity.removeEffect(mobEffectInstance.getEffect()));
            }
        }
    }

    @SubscribeEvent
    public static void ProtectFromEffect(MobEffectEvent.Applicable event)
    {
        LivingEntity living = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        if (instance != null){
            if (instance == Seffects.MYCELIUM && Utilities.helmetList().contains(living.getItemBySlot(EquipmentSlot.HEAD).getItem())){
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
            if (living.getItemBySlot(EquipmentSlot.HEAD).getItem() == Sitems.INF_UP_HELMET.get() && instance == Seffects.MADNESS && instance.getAmplifier() < 1){
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }
}
