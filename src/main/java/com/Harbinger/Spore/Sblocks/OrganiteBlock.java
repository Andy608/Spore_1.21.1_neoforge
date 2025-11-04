package com.Harbinger.Spore.Sblocks;


import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Seffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class OrganiteBlock extends Block {
    public OrganiteBlock() {
        super(Properties.of().strength(6f,4f).sound(SoundType.SLIME_BLOCK));
    }


    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        super.tick(state, level, blockPos, randomSource);
        AABB searchbox = AABB.ofSize(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 35, 35, 35);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchbox);
        for (Entity entity1 : entities) {
            if (entity1 instanceof LivingEntity entity && !(entity instanceof Infected || entity instanceof UtilityEntity || SConfig.SERVER.blacklist.get().contains(entity.getEncodeId()) || Utilities.helmetList().contains(entity.getItemBySlot(EquipmentSlot.HEAD).getItem()))) {
                entity.addEffect(new MobEffectInstance(Seffects.MYCELIUM ,200,0));
                entity.addEffect(new MobEffectInstance(Seffects.MARKER ,400,0));
            }
        }
        level.scheduleTick(blockPos, this, 100);
    }

    @Override
    public void onPlace(BlockState blockstate, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(blockstate, level, pos, oldState, moving);
        level.scheduleTick(pos, this, 100);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        RandomSource random = RandomSource.create();
        for (String str : SConfig.DATAGEN.organite_loot.get()){
            String[] string = str.split("\\|" );
            Item item1 = Utilities.tryToCreateStack(ResourceLocation.parse(string[0])).getItem();
            ItemStack itemStack = new ItemStack(item1);
            int m;
            int minimalV = Integer.parseUnsignedInt(string[2]);
            int maxV = Integer.parseUnsignedInt(string[3]);
            if (minimalV == maxV){
                m = maxV;
            } else {
                try {
                    m = random.nextInt(minimalV, maxV);
                } catch (Exception e){
                    m = 1;
                }
            }

            if (!itemStack.equals(ItemStack.EMPTY) && Math.random() < (Integer.parseUnsignedInt(string[1]) / 100F)) {
                itemStack.setCount(m);
                ItemEntity item = new ItemEntity(level, pos.getX() , pos.getY(),pos.getZ(),itemStack);
                item.setPickUpDelay(10);
                level.addFreshEntity(item);}
        }
        return super.onDestroyedByPlayer(state, level, pos, player, false, fluid);
    }
}
