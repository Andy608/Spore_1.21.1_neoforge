package com.Harbinger.Spore.Sitems.Agents;

import com.Harbinger.Spore.Recipes.SporeForcedRecipes.InjectionSuctionRecipe;
import com.Harbinger.Spore.Sitems.BaseItem2;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;


public class Syringe extends BaseItem2 {
    public Syringe() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()){
            if (level.isClientSide) {
                com.Harbinger.Spore.Client.ClientModEvents.openInjectionScreen(player);
            }
        }
        ItemStack stack = player.getItemInHand(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity living, InteractionHand hand) {
        Level level = player.level();
        InjectionSuctionRecipe.Recipe match = InjectionSuctionRecipe.getUsableRecipe(level, living);
        if (match != null) {
            ItemStack result = new ItemStack(match.output());
            if (!result.isEmpty()) {
                player.playNotifySound(Ssounds.SYRINGE_SUCK.get(), SoundSource.AMBIENT, 1F, 1F);
                living.hurt(level.damageSources().playerAttack(player), 1f);

                if (!player.getInventory().add(result.copy())) {
                    player.drop(result.copy(), false);
                }
                itemStack.shrink(1);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
        }
        return InteractionResult.PASS;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("universal_shift_rightclick").withStyle(ChatFormatting.YELLOW));
    }
}
