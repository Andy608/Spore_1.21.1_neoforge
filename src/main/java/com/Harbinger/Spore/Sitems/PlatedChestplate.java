
package com.Harbinger.Spore.Sitems;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PlatedChestplate extends PlatedExoskeleton {
    public PlatedChestplate() {
        super(Type.CHESTPLATE);
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
