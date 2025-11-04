package com.Harbinger.Spore.Sitems;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeDiggerTools;
import com.Harbinger.Spore.core.SConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class InfectedCombatShovel extends SporeDiggerTools {
    protected static final Map<Block, BlockState> FLATTENABLES;

    public InfectedCombatShovel() {
        super(SConfig.SERVER.shovel_damage.get(), 3f, 2f, SConfig.SERVER.shovel_durability.get(), 5, BlockTags.MINEABLE_WITH_SHOVEL);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = context.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(context, ItemAbilities.SHOVEL_FLATTEN, false);
            BlockState blockstate2 = null;
            if (blockstate1 != null && level.isEmptyBlock(blockpos.above())) {
                level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!level.isClientSide()) {
                    level.levelEvent(null, 1009, blockpos, 0);
                }

                CampfireBlock.dowse(context.getPlayer(), level, blockpos, blockstate);
                blockstate2 = blockstate.setValue(CampfireBlock.LIT, false);
            }

            if (blockstate2 != null) {
                if (!level.isClientSide) {
                    level.setBlock(blockpos, blockstate2, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, blockstate2));
                    if (player != null) {
                        // Fixed the hurtAndBreak call - removed the undefined 'error' parameter
                        context.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean canMultiBreak(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity living) {
        return !living.isCrouching();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Updated MobEffectInstance to use holder
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
        return super.hurtEnemy(stack, target, attacker);
    }

    static {
        FLATTENABLES = Maps.newHashMap(ImmutableMap.<Block, BlockState>builder()
                .put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.defaultBlockState())
                .put(Blocks.DIRT, Blocks.DIRT_PATH.defaultBlockState())
                .put(Blocks.PODZOL, Blocks.DIRT_PATH.defaultBlockState())
                .put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.defaultBlockState())
                .put(Blocks.MYCELIUM, Blocks.DIRT_PATH.defaultBlockState())
                .put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.defaultBlockState())
                .build());
    }
}