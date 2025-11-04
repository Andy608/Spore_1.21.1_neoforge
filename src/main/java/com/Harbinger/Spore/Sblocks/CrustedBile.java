package com.Harbinger.Spore.Sblocks;

import com.Harbinger.Spore.core.Sitems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrustedBile extends Block implements BucketPickup {
    public CrustedBile() {
        super(Properties.of().strength(2f,2f).sound(SoundType.SLIME_BLOCK));
    }


    @Override
    public ItemStack pickupBlock(@Nullable Player player, LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        levelAccessor.removeBlock(blockPos,false);
        return new ItemStack(Sitems.BUCKET_OF_BILE.get());
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.SLIME_HURT);
    }
}
