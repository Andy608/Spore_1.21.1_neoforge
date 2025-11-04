package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class Biomass extends BaseItem2 {
    public Biomass() {
        super(new Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).effect(new MobEffectInstance(com.Harbinger.Spore.core.Seffects.MYCELIUM,100,0),0.5f).build()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (Screen.hasShiftDown()){
            tooltipComponents.add(Component.translatable("item.line.shift").withStyle(ChatFormatting.DARK_RED));
        } else {
            tooltipComponents.add(Component.translatable("item.line.normal").withStyle(ChatFormatting.GOLD));
        }
    }
}
