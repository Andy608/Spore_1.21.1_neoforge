package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.ExtremelySusThings.ClientAdvancementTracker;
import com.Harbinger.Spore.ExtremelySusThings.Package.RequestAdvancementPacket;
import com.Harbinger.Spore.ExtremelySusThings.SporePacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class OrganItem extends BaseItem {
    private final String info;
    private final String advancementIds;

    public OrganItem(String value, String advancementId) {
        super(new Properties());
        this.info = value;
        this.advancementIds = advancementId;
    }

    public String getAdvancementIds() {
        return advancementIds;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Entity entity = Minecraft.getInstance().getCameraEntity();
        if (entity instanceof Player player) {
            if (ClientAdvancementTracker.hasAdvancement(advancementIds)) {
                tooltipComponents.add(Component.translatable(info).withStyle(ChatFormatting.GOLD));
            } else {
                tooltipComponents.add(Component.translatable("spore.scanner.organ.default").withStyle(ChatFormatting.RED));
            }
            SporePacketHandler.sendToServer(new RequestAdvancementPacket(advancementIds,player.getId()));
        }else {
            tooltipComponents.add(Component.translatable("spore.scanner.organ.default").withStyle(ChatFormatting.RED));
        }
    }
}