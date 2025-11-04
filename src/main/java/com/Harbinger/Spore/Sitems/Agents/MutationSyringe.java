package com.Harbinger.Spore.Sitems.Agents;

import com.Harbinger.Spore.Sentities.VariantKeeper;
import com.Harbinger.Spore.Sitems.BaseItem2;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorData;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeWeaponData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MutationSyringe extends BaseItem2 {
    public MutationSyringe() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickAction, Player player) {
        ItemStack itemStack = slot.getItem();
        if (itemStack.getItem() instanceof SporeArmorData armor_data && clickAction == ClickAction.SECONDARY){
            int i = armor_data.getVariant(itemStack).getId();
            i = SporeArmorMutations.values().length > i ? i+1 : 0;
            armor_data.setVariant(SporeArmorMutations.byId(i & 255),itemStack);
            return true;
        }
        if (itemStack.getItem() instanceof SporeWeaponData weaponData && clickAction == ClickAction.SECONDARY){
            int i = weaponData.getVariant(itemStack).getId();
            i = SporeToolsMutations.values().length > i ? i+1 : 0;
            weaponData.setVariant(SporeToolsMutations.byId(i & 255),itemStack);
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("CREATIVE ONLY").withStyle(ChatFormatting.RED));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand hand) {
        if (living instanceof VariantKeeper keeper){
            keeper.increaseVariant();
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, living, hand);
    }
}
