package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeBaseArmor;
import com.Harbinger.Spore.core.SAttributes;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Sitems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.List;

public class UpgradedInfectedExoskeleton extends SporeBaseArmor implements CustomModelArmorData{
    private static final ResourceLocation location = ResourceLocation.parse("spore:textures/armor/living_armor_set_mk.png");
    private final Holder<MobEffect> effectHolder;
    public UpgradedInfectedExoskeleton(Type type) {
        super(type, new int[]{SConfig.SERVER.boots_up_durability.get(), SConfig.SERVER.pants_up_durability.get(),
                        SConfig.SERVER.chestplate_up_durability.get() ,SConfig.SERVER.helmet_up_durability.get()},
                new int[]{SConfig.SERVER.boots_up_protection.get(), SConfig.SERVER.pants_up_protection.get(),
                        SConfig.SERVER.chestplate_up_protection.get(),SConfig.SERVER.helmet_protection.get()},
                SConfig.SERVER.armor_toughness.get()+2,
                SConfig.SERVER.knockback_resistance.get());
        effectHolder = Seffects.SYMBIOSIS;
    }



    @Override
    public void tickArmor(Player living, Level level) {
        this.geteffect(living);
    }

    public void geteffect(LivingEntity entity) {
        MobEffectInstance instance = entity.getEffect(effectHolder);
        if (entity.tickCount % 20 == 0){
            int val = getEffectMod(entity);
            if (instance != null && instance.getDuration() < 60){
                if (val != -1){
                    entity.addEffect(new MobEffectInstance(effectHolder, 200, val, (false), (false)));
                }
            }
            if (instance == null){
                if (val != -1){
                    entity.addEffect(new MobEffectInstance(effectHolder, 200, val, (false), (false)));
                }
            }
        }
    }
    private int getEffectMod(LivingEntity living){
        int i = 0;
        ItemStack helmet = living.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = living.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = living.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = living.getItemBySlot(EquipmentSlot.FEET);
        if (helmet.equals(ItemStack.EMPTY) || chest.equals(ItemStack.EMPTY) || legs.equals(ItemStack.EMPTY) || feet.equals(ItemStack.EMPTY)){return -1;}
        if (helmet.getItem().equals(Sitems.INF_UP_HELMET.get())){i=i+2;}
        if (chest.getItem().equals(Sitems.INF_UP_CHESTPLATE.get())){i=i+2;}
        if (legs.getItem().equals(Sitems.INF_UP_PANTS.get())){i=i+2;}
        if (feet.getItem().equals(Sitems.INF_UP_BOOTS.get())){i=i+2;}
        if (helmet.getItem().equals(Sitems.INF_HELMET.get())){i++;}
        if (chest.getItem().equals(Sitems.INF_CHEST.get())){i++;}
        if (legs.getItem().equals(Sitems.INF_PANTS.get())){i++;}
        if (feet.getItem().equals(Sitems.INF_BOOTS.get())){i++;}
        return  i < 4 ? -1 : i > 7 ? 1 : 0;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return location;
    }

    public static  class InfectedUpChestplate extends UpgradedInfectedExoskeleton{
        public InfectedUpChestplate() {
            super(Type.CHESTPLATE);
        }

        public static boolean isFlyEnabled(ItemStack stack) {
            return stack.getDamageValue() < stack.getMaxDamage() - 10;
        }


        @Override
        public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
            return InfectedUpChestplate.isFlyEnabled(stack);
        }

        @Override
        public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
            if (!entity.level().isClientSide) {
                int nextFlightTick = flightTicks + 1;
                if (nextFlightTick % 10 == 0) {
                    if (nextFlightTick % 20 == 0) {
                        stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
                        if (entity instanceof Player player){
                            player.causeFoodExhaustion(0.1F);
                        }
                    }
                    entity.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ELYTRA_GLIDE);
                }
            }
            return true;
        }


        public int getEnchantmentValue() {
            return 2;
        }

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
            super.inventoryTick(stack, level, entity, slotId, isSelected);
            if (!(entity instanceof LivingEntity living)) return;
            if (living.getItemBySlot(EquipmentSlot.CHEST).equals(stack)) {
                if (living.horizontalCollision && living.isCrouching()) {
                    Vec3 currentMovement = living.getDeltaMovement();

                    if (currentMovement.y < 0.15D) {
                        Vec3 climbVec = new Vec3(currentMovement.x, 0.15D, currentMovement.z);
                        living.setDeltaMovement(climbVec);
                    }
                }
            }
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, components, tooltipFlag);
            if (Screen.hasShiftDown()){
                components.add(Component.translatable("item.armor.shift").withStyle(ChatFormatting.DARK_RED));
            } else {
                components.add(Component.translatable("item.armor.normal").withStyle(ChatFormatting.GOLD));
            }
        }
    }

    public static class InfectedUpHelmet extends UpgradedInfectedExoskeleton{
        public InfectedUpHelmet() {
            super(Type.HELMET);
        }

        @Override
        public void tickArmor(Player player, Level level) {
            super.tickArmor(player, level);
            if (player.tickCount % 10 == 0 && player.isCrouching()){
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,600,0,false,false));
            }
            if (player.tickCount % 20 == 0 && player.isInWater()){
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING,200,0,false,false));
            }
        }
    }

    public static class InfectedUpPants extends UpgradedInfectedExoskeleton{
        public InfectedUpPants() {
            super(Type.LEGGINGS);
        }

        @Override
        public ItemAttributeModifiers getDynamicAttributeModifiers(ItemStack stack) {
            EquipmentSlot slot = this.type.getSlot();
            int index = this.type.getSlot().getIndex();
            double baseArmor = calculateTrueDefense(stack, protection[index]) + modifyProtection(stack, protection[index]);
            double baseToughness = calculateTrueToughness(stack, toughness) + modifyToughness(stack, toughness);
            double knockbackRes = this.knockback + modifyKnockbackResistance(stack, knockback);
            float speed = (this.getVariant(stack) == SporeArmorMutations.REINFORCED || this.getVariant(stack) == SporeArmorMutations.SKELETAL) ? this.getVariant(stack) == SporeArmorMutations.REINFORCED ? -0.01f : 0.01f : 0f;
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder()
                    .add(Attributes.ARMOR,
                            new AttributeModifier(BASE_ARMOR_ID[index], baseArmor, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.bySlot(slot))
                    .add(Attributes.ARMOR_TOUGHNESS,
                            new AttributeModifier(BASE_TOUGHNESS_ID[index], baseToughness, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.bySlot(slot))
                    .add(Attributes.STEP_HEIGHT,
                            new AttributeModifier(BASE_STEP_ID[index], 0.75, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.bySlot(slot))
                    .add(Attributes.MOVEMENT_SPEED,
                            new AttributeModifier(BASE_SPEED_ID[index], 0.02 + speed, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.bySlot(slot));

            if (knockbackRes > 0.0F) {
                builder.add(Attributes.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(BASE_KNOCKBACK_ID[index], knockbackRes * 0.1f, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.bySlot(slot));
            }
            if (this.getVariant(stack) == SporeArmorMutations.DROWNED){
                builder.add(NeoForgeMod.SWIM_SPEED,
                        new AttributeModifier(BASE_SWIM_ID[index], 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.bySlot(slot));
            }
            if (this.getVariant(stack) == SporeArmorMutations.REINFORCED || this.getVariant(stack) == SporeArmorMutations.SKELETAL) {
                double speedMod = (this.getVariant(stack) == SporeArmorMutations.REINFORCED) ? -0.01 : 0.01;
                builder.add(Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(BASE_SPEED_ID[index], speedMod, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.bySlot(slot));
            }
            return builder.build();
        }
        @Override
        public void tickArmor(Player player, Level level) {
            super.tickArmor(player, level);
            if (player.tickCount % 30 == 0){
                player.addEffect(new MobEffectInstance(MobEffects.JUMP,40,1,false,false));
            }
        }
    }

    public static class InfectedUpBoots extends UpgradedInfectedExoskeleton{
        public InfectedUpBoots() {
            super(Type.BOOTS);
        }

        @Override
        public ItemAttributeModifiers getDynamicAttributeModifiers(ItemStack stack) {
            EquipmentSlot slot = this.type.getSlot();
            int index = this.type.getSlot().getIndex();
            double baseArmor = calculateTrueDefense(stack, protection[slot.getIndex()]) + modifyProtection(stack, protection[slot.getIndex()]);
            double baseToughness = calculateTrueToughness(stack, toughness) + modifyToughness(stack, toughness);
            double knockbackRes = this.knockback + modifyKnockbackResistance(stack, knockback);
            double underwaterSpeed = this.getVariant(stack) == SporeArmorMutations.DROWNED ? 0.75 : 0.5;
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder()
                    .add(Attributes.ARMOR,
                            new AttributeModifier(BASE_ARMOR_ID[index], baseArmor, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.bySlot(slot))
                    .add(Attributes.ARMOR_TOUGHNESS,
                            new AttributeModifier(BASE_TOUGHNESS_ID[index], baseToughness, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.bySlot(slot))
                    .add(NeoForgeMod.SWIM_SPEED,
                    new AttributeModifier(BASE_SWIM_ID[index], underwaterSpeed, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                    EquipmentSlotGroup.bySlot(slot));

            if (knockbackRes > 0.0F) {
                builder.add(Attributes.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(BASE_KNOCKBACK_ID[index], knockbackRes * 0.1f, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.bySlot(slot));
            }
            if (this.getVariant(stack) == SporeArmorMutations.REINFORCED || this.getVariant(stack) == SporeArmorMutations.SKELETAL) {
                double speedMod = (this.getVariant(stack) == SporeArmorMutations.REINFORCED) ? -0.01 : 0.01;
                builder.add(Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(BASE_SPEED_ID[index], speedMod, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.bySlot(slot));
            }

            return builder.build();
        }
    }
}
