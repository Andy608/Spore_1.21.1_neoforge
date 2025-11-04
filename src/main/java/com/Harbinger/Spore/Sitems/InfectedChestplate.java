
package com.Harbinger.Spore.Sitems;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class InfectedChestplate extends InfectedExoskeleton{
    public InfectedChestplate() {
        super(Type.CHESTPLATE);
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

    @Override
    public void tickArmor(Player player, Level level) {
        super.tickArmor(player, level);
        if (player.horizontalCollision && player.isCrouching()) {
            Vec3 initialVec = player.getDeltaMovement();
            Vec3 climbVec = new Vec3(initialVec.x, 0.2D, initialVec.z);
            player.setDeltaMovement(climbVec.x * 0.91D,
                    climbVec.y * 0.98D, climbVec.z * 0.91D);
        }
    }
}
