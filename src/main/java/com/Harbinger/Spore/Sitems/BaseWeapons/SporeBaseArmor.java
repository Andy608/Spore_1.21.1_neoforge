package com.Harbinger.Spore.Sitems.BaseWeapons;

import com.Harbinger.Spore.core.Senchantments;
import com.Harbinger.Spore.core.Sitems;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public abstract class SporeBaseArmor extends ArmorItem implements SporeArmorData {
    protected static final ResourceLocation[] BASE_ARMOR_ID = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("base_armor_boots"),
            ResourceLocation.withDefaultNamespace("base_armor_leggings"),
            ResourceLocation.withDefaultNamespace("base_armor_chestplate"),
            ResourceLocation.withDefaultNamespace("base_armor_helmet")
    };
    protected static final ResourceLocation[] BASE_TOUGHNESS_ID = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("base_toughness_boots"),
            ResourceLocation.withDefaultNamespace("base_toughness_leggings"),
            ResourceLocation.withDefaultNamespace("base_toughness_chestplate"),
            ResourceLocation.withDefaultNamespace("base_toughness_helmet")
    };
    protected static final ResourceLocation[] BASE_KNOCKBACK_ID = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("base_knockback_boots"),
            ResourceLocation.withDefaultNamespace("base_knockback_leggings"),
            ResourceLocation.withDefaultNamespace("base_knockback_chestplate"),
            ResourceLocation.withDefaultNamespace("base_knockback_helmet")
    };
    protected static final ResourceLocation[] BASE_SPEED_ID = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("base_speed_boots"),
            ResourceLocation.withDefaultNamespace("base_speed_leggings"),
            ResourceLocation.withDefaultNamespace("base_speed_chestplate"),
            ResourceLocation.withDefaultNamespace("base_speed_helmet")
    };
    protected static final ResourceLocation[] BASE_SWIM_ID = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("base_swim_boots"),
            ResourceLocation.withDefaultNamespace("base_swim_leggings"),
            ResourceLocation.withDefaultNamespace("base_swim_chestplate"),
            ResourceLocation.withDefaultNamespace("base_swim_helmet")
    };
    protected static final ResourceLocation[] BASE_STEP_ID = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("base_step_boots"),
            ResourceLocation.withDefaultNamespace("base_step_leggings"),
            ResourceLocation.withDefaultNamespace("base_step_chestplate"),
            ResourceLocation.withDefaultNamespace("base_step_helmet")
    };

    protected final int[] protection;
    protected final float toughness;
    protected float knockback;

    public SporeBaseArmor(Type type, int[] durability, int[] protection, float toughness, float knockback) {
        super(ArmorMaterials.LEATHER, type, new Item.Properties().durability(durability[type.getSlot().getIndex()]));
        Sitems.TINTABLE_ITEMS.add(this);
        Sitems.BIOLOGICAL_ITEMS.add(this);
        this.protection = protection;
        this.toughness = toughness;
        this.knockback = knockback;
    }
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return tooHurt(stack) ? this.getDynamicAttributeModifiers(stack) : super.getDefaultAttributeModifiers(stack);
    }
    public ItemAttributeModifiers getDynamicAttributeModifiers(ItemStack stack) {
        EquipmentSlot slot = this.type.getSlot();
        int index = this.type.getSlot().getIndex();
        double baseArmor = calculateTrueDefense(stack, protection[index]) + modifyProtection(stack, protection[index]);
        double baseToughness = calculateTrueToughness(stack, toughness) + modifyToughness(stack, toughness);
        double knockbackRes = this.knockback + modifyKnockbackResistance(stack, knockback);

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(BASE_ARMOR_ID[index], baseArmor, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.bySlot(slot))
                .add(Attributes.ARMOR_TOUGHNESS,
                        new AttributeModifier(BASE_TOUGHNESS_ID[index], baseToughness, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.bySlot(slot));

        if (knockbackRes > 0.0F) {
            builder.add(Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(BASE_KNOCKBACK_ID[index], knockbackRes * 0.1f, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(slot));
        }
        if (this.getVariant(stack) == SporeArmorMutations.DROWNED){
            builder.add(Attributes.WATER_MOVEMENT_EFFICIENCY,
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
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.parse("spore:textures/entity/empty.png");
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {

        int durabilityLeft = stack.getMaxDamage() - stack.getDamageValue();
        if (durabilityLeft - amount <= 11 && entity != null) {
            entity.playSound(Ssounds.INFECTED_GEAR_BREAK.get());
        }

        if (tooHurt(stack)) {

            if (getAdditionalDurability(stack) > 0) {
                hurtExtraDurability(stack, amount, entity);
                return amount;
            }

            return super.damageItem(stack, calculateDurabilityLost(stack, amount), entity, onBroken);
        }

        return super.damageItem(stack, amount, entity, onBroken);
    }



    public void tickArmor(Player living, Level level){

    }

    public int calculateDurabilityLost(ItemStack stack, int value) {
        if (this.getVariant(stack) == SporeArmorMutations.CHARRED) {
            return value * 2;
        }
        return value;
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return Holder.direct(Ssounds.INFECTED_GEAR_EQUIP.get());
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        int luck = getLuck(stack);
        return luck > 0 ? luck : 1;
    }

    public double modifyProtection(ItemStack stack, double value) {
        if (this.getVariant(stack) == SporeArmorMutations.REINFORCED) {
            return value * 0.2f;
        }
        if (this.getVariant(stack) == SporeArmorMutations.SKELETAL) {
            return value * -0.2f;
        }
        return 0;
    }

    public double modifyToughness(ItemStack stack, double value) {
        if (this.getVariant(stack) == SporeArmorMutations.SKELETAL) {
            return 1;
        }
        return 0;
    }

    public double modifyKnockbackResistance(ItemStack stack, double value) {
        return 0;
    }

    public float calculateAdditionalDamage(DamageSource source, ItemStack stack, float value) {
        if (this.getVariant(stack) == SporeArmorMutations.CHARRED && source.is(DamageTypeTags.IS_FIRE)) {
            return -value * 0.25f;
        }
        if (this.getVariant(stack) == SporeArmorMutations.DROWNED && source.is(DamageTypeTags.IS_FIRE)) {
            return value * 0.25f;
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        if (!tooHurt(stack)) {
            components.add(Component.translatable("spore.item.hurt").withStyle(ChatFormatting.RED));
        }
        if (Screen.hasShiftDown()) {
            if (getAdditionalProtection(stack) > 0) {
                components.add(Component.literal(Component.translatable("spore.item.armor_increase").getString() + getAdditionalProtection(stack) + "%"));
            }
            if (getAdditionalToughness(stack) > 0) {
                components.add(Component.literal(Component.translatable("spore.item.toughness_increase").getString() + getAdditionalToughness(stack) + "%"));
            }
            if (getMaxAdditionalDurability(stack) > 0) {
                components.add(Component.literal(Component.translatable("spore.item.durability_increase").getString() + getMaxAdditionalDurability(stack) + "%"));
            }
            if (getAdditionalDurability(stack) > 0) {
                components.add(Component.literal(Component.translatable("spore.item.additional_durability").getString() + getAdditionalDurability(stack)));
            }
            if (getEnchantmentValue(stack) > 0) {
                components.add(Component.literal(Component.translatable("spore.item.enchant").getString() + getEnchantmentValue(stack)));
            }
            if (getVariant(stack) != SporeArmorMutations.DEFAULT) {
                components.add(Component.literal(Component.translatable("spore.item.mutation").getString() + Component.translatable(getVariant(stack).getName()).getString()));
            }
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack itemStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && Senchantments.hasEnchant(player.level(),stack,Senchantments.VORACIOUS_MAW) && stack.getDamageValue() > 0){
            if (itemStack.getFoodProperties(player) == null){
                return false;
            }
            stack.setDamageValue(getDamage(stack)-50);
            itemStack.shrink(1);
            player.playNotifySound(SoundEvents.GENERIC_EAT, SoundSource.AMBIENT, 1f, 1f);
            return true;
        }
        boolean shouldOverride = clickAction == ClickAction.SECONDARY
                && itemStack.getItem() == Sitems.SYRINGE.get()
                && getVariant(stack) != SporeArmorMutations.DEFAULT;

        if (shouldOverride) {
            this.setVariant(SporeArmorMutations.DEFAULT, stack);
            itemStack.shrink(1);
            player.playNotifySound(Ssounds.SYRINGE_SUCK.get(), SoundSource.AMBIENT, 1f, 1f);
        }

        return shouldOverride;
    }
}
