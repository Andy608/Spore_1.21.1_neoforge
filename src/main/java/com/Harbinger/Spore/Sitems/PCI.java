package com.Harbinger.Spore.Sitems;


import com.Harbinger.Spore.Client.AnimationTrackers.PCIAnimationTracker;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SdataComponents;
import com.Harbinger.Spore.core.Sitems;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.List;

public class PCI extends BaseItem2 implements CustomModelArmorData {
    protected final ResourceLocation BONUS_DAMAGE_MODIFIER_UUID = ResourceLocation.parse("pci_damage");
    protected final ResourceLocation BONUS_REACH_MODIFIER_UUID = ResourceLocation.parse("pci_reach");
    protected final ResourceLocation ATTACK_SPEED_MODIFIER_UUID = ResourceLocation.parse("pci_speed");
    private static final ResourceLocation TEXTURE = ResourceLocation.parse("spore:textures/item/pci.png");
    private static final int RELOAD_TICKS = 60;

    public PCI() {
        super(new Properties().stacksTo(1).durability(SConfig.SERVER.pci_durability.get()));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack itemStack) {
        return itemStack.is(Sitems.CIRCUIT_BOARD.get());
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(Attributes.ATTACK_DAMAGE,new AttributeModifier(BONUS_DAMAGE_MODIFIER_UUID,SConfig.SERVER.pci_damage.get()-1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        builder.add(Attributes.ENTITY_INTERACTION_RANGE,new AttributeModifier(BONUS_REACH_MODIFIER_UUID,2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        builder.add(Attributes.ATTACK_SPEED,new AttributeModifier(ATTACK_SPEED_MODIFIER_UUID,-2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        return builder.build();
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }

    public void setCharge(ItemStack stack, int value) {
        stack.set(SdataComponents.PCI_AMMO,value);
    }

    public int getCharge(ItemStack stack) {
        return stack.getOrDefault(SdataComponents.PCI_AMMO,0);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (entity.level().isClientSide && entity instanceof Player player && !player.getCooldowns().isOnCooldown(this)) {
            PCIAnimationTracker.trigger(player);
        }
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && player.getCooldowns().isOnCooldown(this)){
            return false;
        }
        if (getCharge(stack) > 0){
            if (target instanceof Infected infected && infected.getLinked()){infected.setLinked(false);}
        }
        if (attacker instanceof Player player){
            playSound(player);
        }
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }
    public void playSound(Player player){
        player.playNotifySound(Ssounds.PCI_INJECT.value(), SoundSource.AMBIENT,1f,1f);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getCharge(stack) < SConfig.SERVER.pci_max_charge.get()/2){
            lookForAmmo(player,stack);
        }
        return super.use(level, player, hand);
    }

    private boolean lookForAmmo(Player player, ItemStack itemStack) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == Sitems.ICE_CANISTER.get() && stack.getCount() > 0) {
                stack.shrink(1);
                setCharge(itemStack, SConfig.SERVER.pci_max_charge.get());
                player.getCooldowns().addCooldown(this,RELOAD_TICKS);
                player.playNotifySound(Ssounds.CDU_INSERT.value(), SoundSource.AMBIENT,1f,1f);
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("pci.line.normal").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal(getCharge(stack)+"/"+SConfig.SERVER.pci_max_charge.get()).withStyle(ChatFormatting.DARK_BLUE));
    }
}
