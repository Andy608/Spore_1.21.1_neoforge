package com.Harbinger.Spore.Sitems;


import com.Harbinger.Spore.Sentities.Projectile.ThrownBoomerang;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeSwordBase;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class InfectedBoomerang extends SporeSwordBase {
    public InfectedBoomerang() {
        super(SConfig.SERVER.boomerang_damage.get(), 1f, 3f, SConfig.SERVER.boomerang_durability.get());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ItemStack thrownCopy = stack.copy();

            ThrownBoomerang boomerang = new ThrownBoomerang(level, player, thrownCopy, getVariant(stack).getColor());
            boomerang.setPos(player.getEyePosition());
            boomerang.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 0.75F);

            if (player.getAbilities().instabuild) {
                boomerang.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            level.addFreshEntity(boomerang);
            level.playSound(null, player, Ssounds.INFECTED_WEAPON_THROW.get(), SoundSource.PLAYERS, 1.5F, 0.9F);

            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }


}
