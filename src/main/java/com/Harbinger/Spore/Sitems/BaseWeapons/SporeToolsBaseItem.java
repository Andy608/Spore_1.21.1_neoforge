package com.Harbinger.Spore.Sitems.BaseWeapons;

import com.Harbinger.Spore.Sitems.BaseItem;
import com.Harbinger.Spore.core.Senchantments;
import com.Harbinger.Spore.core.Sitems;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SporeToolsBaseItem extends BaseItem implements SporeWeaponData , IItemExtension {
    protected final double meleeDamage;
    protected final double meleeReach;
    protected final double meleeRecharge;
    protected final int miningLevel;
    public static final ResourceLocation BASE_ATTACK_REACH_ID = ResourceLocation.withDefaultNamespace("base_attack_reach");
    public SporeToolsBaseItem(double meleeDamage, double meleeReach, double meleeRecharge, int durability, int miningLevel, Tool toolComponentData) {
        super(new Item.Properties().stacksTo(1).durability(durability).component(DataComponents.TOOL,toolComponentData));
        this.meleeDamage = meleeDamage;
        this.meleeReach = meleeReach;
        this.meleeRecharge = meleeRecharge;
        this.miningLevel = miningLevel;
        Sitems.TINTABLE_ITEMS.add(this);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack itemStack) {
        return super.isValidRepairItem(stack, itemStack) || itemStack.getItem() == Sitems.BIOMASS.get();
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return tooHurt(stack) ? this.getDynamicAttributeModifiers(stack) : super.getDefaultAttributeModifiers(stack);
    }

    public ItemAttributeModifiers getDynamicAttributeModifiers(ItemStack stack) {
        double extraDamage = calculateTrueDamage(stack, meleeDamage) + modifyDamage(stack, meleeDamage);
        double extraRecharge = -meleeRecharge + modifyRecharge(stack);
        double extraReach = meleeReach + modifyRange(stack);

        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, extraDamage, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, extraRecharge, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(BASE_ATTACK_REACH_ID, extraReach, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState blockState) {
        return tooHurt(stack) ? this.miningLevel : 1.0F;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        int durabilityLeft = stack.getMaxDamage() - stack.getDamageValue();
        if (durabilityLeft - amount <= 11) {
            entity.playSound(Ssounds.INFECTED_GEAR_BREAK.get());
        }
        if (durabilityLeft < 10) return 0;
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        int luck = getLuck(stack);
        return luck > 0 ? luck : 1;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (!tooHurt(stack) && entity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 60);
        }
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity living, LivingEntity entity) {
        if (tooHurt(stack)) {
            hurtTool(stack, entity, 1);
        }
        doEntityHurtAfterEffects(stack, living, entity);
        return super.hurtEnemy(stack, living, entity);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity living) {
        if (!tooHurt(stack)) {
            return false;
        }
        return super.mineBlock(stack, level, state, pos, living);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return super.isBarVisible(stack) || getAdditionalDurability(stack) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (getAdditionalDurability(stack) > 0) {
            return Mth.hsvToRgb(240.0F, 100.0F, 100.0F);
        } else {
            return super.getBarColor(stack);
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        if (!tooHurt(stack)) {
            components.add(Component.translatable("spore.item.hurt").withStyle(ChatFormatting.RED));
        }
        if (Screen.hasShiftDown()) {
            if (getAdditionalDamage(stack) > 0) {
                components.add(Component.literal(Component.translatable("spore.item.damage_increase").getString() + getAdditionalDamage(stack) + "%"));
            }
            if (getMaxAdditionalDurability(stack) > 0) {
                components.add(Component.literal(Component.translatable("spore.item.durability_increase").getString() + getMaxAdditionalDurability(stack) + "%"));
            }
            if (getAdditionalDurability(stack) > 0) {
                components.add(Component.literal(Component.translatable("spore.item.additional_durability").getString() + getAdditionalDurability(stack)));
            }
            if (getEnchantmentValue(stack) > 1) {
                components.add(Component.literal(Component.translatable("spore.item.enchant").getString() + getEnchantmentValue(stack)));
            }
            if (getVariant(stack) != SporeToolsMutations.DEFAULT) {
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
                && getVariant(stack) != SporeToolsMutations.DEFAULT;

        if (shouldOverride) {
            this.setVariant(SporeToolsMutations.DEFAULT, stack);
            itemStack.shrink(1);
            player.playNotifySound(Ssounds.SYRINGE_SUCK.get(), SoundSource.AMBIENT, 1f, 1f);
        }

        return shouldOverride;
    }
}
