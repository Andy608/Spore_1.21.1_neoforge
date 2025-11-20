package com.Harbinger.Spore.ExtremelySusThings;

import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.Sentities.TrueCalamity;
import com.Harbinger.Spore.core.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Utilities {
    public static final List<BlockState> biomass = createBiomass();
    public static void explodeCircle(ServerLevel level, Entity owner, BlockPos pos, double range, float damage,double blockHardness,Predicate<Entity> predicate) {
        explodeCircle(level,owner,pos,range,damage, ParticleTypes.EXPLOSION_EMITTER,false,blockHardness,predicate);
    }
    public static void explodeCircle(ServerLevel level,
                                     Entity owner,
                                     BlockPos pos,
                                     double range,
                                     float damage,
                                     ParticleOptions particleTypes,
                                     boolean dropItems,
                                     double blockHardness,
                                     Predicate<Entity> predicate) {

        RandomSource random = RandomSource.create();

        for (int i = 0; i <= 2 * range; ++i) {
            for (int j = 0; j <= 2 * range; ++j) {
                for (int k = 0; k <= 2 * range; ++k) {
                    double distance = Mth.sqrt(
                            (float) ((i - range) * (i - range) +
                                    (j - range) * (j - range) +
                                    (k - range) * (k - range))
                    );

                    if (distance < range + 0.5) {
                        BlockPos blockpos = pos.offset(i - (int) range, j - (int) range, k - (int) range);
                        BlockState state = level.getBlockState(blockpos);
                        float hardness = state.getDestroySpeed(level, blockpos);

                        if (hardness >= 0 && hardness <= blockHardness && EventHooks.canEntityGrief(level, owner)) {
                            level.removeBlock(blockpos, dropItems);

                            if (random.nextFloat() < 0.3F) {
                                float offset = random.nextFloat() * 0.05f;
                                level.sendParticles(
                                        particleTypes,
                                        blockpos.getX() + 0.5,
                                        blockpos.getY() + 0.5,
                                        blockpos.getZ() + 0.5,
                                        1,
                                        offset, 0, offset,
                                        1
                                );
                            }
                        }
                    }
                }
            }
        }

        AABB searchBox = AABB.ofSize(new Vec3(pos.getX(), pos.getY(), pos.getZ()), range * 2, range * 2, range * 2);
        List<Entity> entities = level.getEntities(owner, searchBox, predicate);

        for (Entity entity : entities) {
            if (owner instanceof LivingEntity living) {
                entity.hurt(level.damageSources().mobAttack(living), damage);
            } else {
                entity.hurt(level.damageSources().generic(), damage);
            }
        }

        level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS);
    }

    public static void convertBlocks(ServerLevel level,
                                     Entity owner,
                                     BlockPos pos,
                                     double range,
                                     BlockState newState) {

        RandomSource random = RandomSource.create();

        for (int i = 0; i <= 2 * range; ++i) {
            for (int j = 0; j <= 2 * range; ++j) {
                for (int k = 0; k <= 2 * range; ++k) {
                    double distance = Mth.sqrt(
                            (float) ((i - range) * (i - range) +
                                    (j - range) * (j - range) +
                                    (k - range) * (k - range))
                    );

                    if (distance < range + 0.5) {
                        BlockPos blockpos = pos.offset(i - (int) range, j - (int) range, k - (int) range);

                        if (EventHooks.canEntityGrief(level, owner) && random.nextFloat() < 0.2f) {
                            BlockState current = level.getBlockState(blockpos);
                            if (current.isAir() && level.getBlockState(blockpos.below()).isSolidRender(level, blockpos)) {
                                level.setBlock(blockpos, newState, 3);
                            }
                        }
                    }
                }
            }
        }
    }

    public static final Predicate<LivingEntity> TARGET_SELECTOR_PREDICATE = (entity) -> {
        if (entity instanceof UtilityEntity || entity instanceof TrueCalamity){
            return false;
        }else if ((entity instanceof AbstractFish || entity instanceof Animal) && !SConfig.SERVER.at_an.get()){
            return false;
        }else if (!SConfig.SERVER.blacklist.get().isEmpty()){
            for(String string : SConfig.SERVER.blacklist.get()){
                if (string.endsWith(":") && entity.getEncodeId() != null){
                    String[] mod = string.split(":");
                    String[] iterations = entity.getEncodeId().split(":");
                    if (Objects.equals(mod[0], iterations[0])){
                        return false;
                    }
                }
            }
            return !SConfig.SERVER.blacklist.get().contains(entity.getEncodeId());
        }
        return true;
    };
    public static BooleanCache<LivingEntity> TARGET_SELECTOR = new BooleanCache<>(8, TARGET_SELECTOR_PREDICATE);

     public static List<Item> helmetList() {
         List<Item> values = new ArrayList<>();

         for (String idString : com.Harbinger.Spore.core.SConfig.SERVER.gas_masks.get()) {
             try {
                 ResourceLocation id = ResourceLocation.tryParse(idString);
                 if (id == null) {
                     continue;
                 }
                 Optional<Item> optional = BuiltInRegistries.ITEM.getOptional(id);
                 optional.ifPresent(values::add);

             } catch (Exception e) {
                 System.err.println("[Spore] Invalid item ID in gas_masks config: " + idString);
             }
         }

         return values;
     }
     public static ItemStack tryToCreateStack(ResourceLocation location){
         Optional<Item> optional = BuiltInRegistries.ITEM.getOptional(location);
         return optional.map(ItemStack::new).orElse(ItemStack.EMPTY);
     }
     public static EntityType<?> tryToCreateEntity(ResourceLocation location){
        Optional<EntityType<?>> optional = BuiltInRegistries.ENTITY_TYPE.getOptional(location);
        return optional.isEmpty() ? Sentities.KNIGHT.get() : optional.get();
     }
    public static Block tryToCreateBlock(ResourceLocation location){
        Optional<Block> optional = BuiltInRegistries.BLOCK.getOptional(location);
        return optional.orElse(Blocks.AIR);
    }
    public static Holder<MobEffect> tryToCreateEffect(ResourceLocation location){
        Optional<Holder<MobEffect>> optional = Optional.of(wrapHolder(BuiltInRegistries.MOB_EFFECT.get(location)));
        return optional.orElse(Seffects.MYCELIUM);
    }
    public static Holder<Potion> tryToCreatePotion(ResourceLocation location){
        if (location == null) {
            return Potions.AWKWARD;
        }
        try {
            Potion potion = BuiltInRegistries.POTION.get(location);
            if (potion != null) {
                return BuiltInRegistries.POTION.wrapAsHolder(potion);
            }
            return Potions.AWKWARD;
        } catch (Exception e) {
            return Potions.AWKWARD;
        }
    }
    public static Attribute tryToCreateAttribute(ResourceLocation location){
        Optional<Attribute> optional = BuiltInRegistries.ATTRIBUTE.getOptional(location);
        return optional.orElse(Attributes.ATTACK_KNOCKBACK.value());
    }
    public static List<Holder<Block>> tryToCreateBlockFromTag(Level level, ResourceLocation location){
        List<Holder<Block>> values = new ArrayList<>();
        HolderGetter<Block> blockGetter = level.registryAccess().lookupOrThrow(Registries.BLOCK);
        TagKey<Block> tagKey = TagKey.create(BuiltInRegistries.BLOCK.key(), location);
        HolderSet.Named<Block> tagSet = blockGetter.get(tagKey).orElse(null);
        if (tagSet == null){
            return values;
        }else {
            values = tagSet.stream().toList();
        }
        return values;
    }
    public static List<BlockState> biomass(){
        return biomass;
    }
    private static List<BlockState> createBiomass(){
        List<BlockState> states = new ArrayList<>();
        states.add(Sblocks.BIOMASS_BLOCK.get().defaultBlockState());
        states.add(Sblocks.SICKEN_BIOMASS_BLOCK.get().defaultBlockState());
        states.add(Sblocks.CALCIFIED_BIOMASS_BLOCK.get().defaultBlockState());
        states.add(Sblocks.MEMBRANE_BLOCK.get().defaultBlockState());
        states.add(Sblocks.ROOTED_BIOMASS.get().defaultBlockState());
        states.add(Sblocks.ROOTED_MYCELIUM.get().defaultBlockState());
        states.add(Sblocks.GASTRIC_BIOMASS.get().defaultBlockState());
        return states;
    }

    public static Vec3 generatePositionAway(Vec3 origin, double distance) {
        Random random = new Random();
        double theta = random.nextDouble() * 2 * Math.PI; // Random angle around the z-axis (0 to 2π)
        double phi = Math.acos(2 * random.nextDouble() - 1); // Random angle from the z-axis (0 to π)
        // Convert spherical coordinates to Cartesian coordinates for the offset
        double offsetX = Math.sin(phi) * Math.cos(theta) * distance;
        double offsetY = Math.sin(phi) * Math.sin(theta) * distance;
        double offsetZ = Math.cos(phi) * distance;
        // Generate the new position
        return new Vec3(origin.x + offsetX, origin.y + offsetY,origin.z + offsetZ);
    }

    public static List<Item> getItemsFromTag(String namespace, String tagName) {
        TagKey<Item> tagKey = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(namespace, tagName));
        return BuiltInRegistries.ITEM.getTag(tagKey)
                .map(holderSet -> holderSet.stream().map(Holder::value).collect(Collectors.toList()))
                .orElse(List.of());  // Return an empty list if no items are found
    }

    public static void doCustomModifiersAfterEffects(LivingEntity attacker, LivingEntity victim) {
        if (attacker == null || victim == null) return;

        AttributeInstance corrosion = attacker.getAttribute(SAttributes.CORROSIVES);
        if (corrosion != null && corrosion.getValue() >= 1) {
            int level = (int) corrosion.getValue() - 1;
            victim.addEffect(new MobEffectInstance(Seffects.CORROSION, 300, level), attacker);
        }

        AttributeInstance toxic = attacker.getAttribute(SAttributes.TOXICITY);
        if (toxic != null && toxic.getValue() >= 1) {
            int level = (int) toxic.getValue() - 1;
            victim.addEffect(new MobEffectInstance(Holder.direct(MobEffects.POISON.value()), 400, level), attacker);
        }

        AttributeInstance local = attacker.getAttribute(SAttributes.LOCALIZATION);
        if (local != null && local.getValue() >= 1) {
            int level = (int) local.getValue() - 1;
            victim.addEffect(new MobEffectInstance(Seffects.MARKER, 600, level), attacker);
        }

        AttributeInstance grind = attacker.getAttribute(SAttributes.GRINDING);
        if (grind != null && grind.getValue() >= 1) {
            double level = grind.getValue();
            victim.getArmorSlots().forEach(itemStack -> {
                itemStack.setDamageValue(itemStack.getDamageValue() + (int) (10 * level));
            });
        }
    }

    public static int mixColors(Map<Integer, Float> colorsAndWeights) {
        float totalWeight = 0f;
        float r = 0f;
        float g = 0f;
        float b = 0f;

        for (Map.Entry<Integer, Float> entry : colorsAndWeights.entrySet()) {
            int color = entry.getKey();
            float weight = entry.getValue();

            r += ((color >> 16) & 0xFF) * weight;
            g += ((color >> 8) & 0xFF) * weight;
            b += (color & 0xFF) * weight;

            totalWeight += weight;
        }

        if (totalWeight == 0) {
            return 0xFFFFFF; // default to white if no colors
        }

        r /= totalWeight;
        g /= totalWeight;
        b /= totalWeight;

        return ((int) r << 16) | ((int) g << 8) | (int) b;
    }

    public static Holder<MobEffect> wrapHolder(MobEffect effect){
      return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
    }
}
