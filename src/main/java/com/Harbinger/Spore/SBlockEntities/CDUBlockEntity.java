package com.Harbinger.Spore.SBlockEntities;


import com.Harbinger.Spore.ExtremelySusThings.Utilities;
import com.Harbinger.Spore.Sblocks.CDUBlock;
import com.Harbinger.Spore.Screens.CDUMenu;
import com.Harbinger.Spore.Sentities.Utility.InfectionTendril;
import com.Harbinger.Spore.Sentities.Utility.ScentEntity;
import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.SblockEntities;
import com.Harbinger.Spore.core.Sblocks;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CDUBlockEntity extends BlockEntity implements MenuProvider {
    private static final TagKey<Block> foliage = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.parse("removable_foliage"));
    public final int maxFuel = SConfig.DATAGEN.cryo_time.get();
    public int fuel;
    private final List<StoreDouble> blockMap;
    private final List<BlockState> biomass;
    public CDUBlockEntity(BlockPos pos, BlockState state) {
        super(SblockEntities.CDU.get(), pos, state);
        blockMap = fabricateBlocks();
        biomass = stateList();
    }
    record StoreDouble(Block value1, Block value2){}

    private List<StoreDouble> fabricateBlocks(){
        List<StoreDouble> blocks = new ArrayList<>();
        for (String str : SConfig.DATAGEN.block_cleaning.get()){
            String[] string = str.split("\\|" );
            Block blockCon1 = Utilities.tryToCreateBlock(ResourceLocation.parse(string[0]));
            Block blockCon2 = Utilities.tryToCreateBlock(ResourceLocation.parse(string[1]));
            if (blockCon1 != Blocks.AIR && blockCon2 != Blocks.AIR){
                blocks.add(new StoreDouble(blockCon1,blockCon2));
            }
        }
        return blocks;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("fuel",this.getFuel());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.setFuel(tag.getInt("fuel"));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithFullMetadata(registries);
    }

    public void setFuel(int i){
        this.fuel = i;
    }
    public int getFuel(){
        return this.fuel;
    }

    public List<BlockState> stateList(){
        List<BlockState> states = new ArrayList<>();
        states.add(Sblocks.BIOMASS_BLOCK.get().defaultBlockState());
        states.add(Sblocks.SICKEN_BIOMASS_BLOCK.get().defaultBlockState());
        states.add(Sblocks.CALCIFIED_BIOMASS_BLOCK.get().defaultBlockState());
        states.add(Sblocks.MEMBRANE_BLOCK.get().defaultBlockState());
        states.add(Sblocks.ROOTED_BIOMASS.get().defaultBlockState());
        states.add(Sblocks.GASTRIC_BIOMASS.get().defaultBlockState());
        states.add(Sblocks.ROOTED_MYCELIUM.get().defaultBlockState());
        return states;
    }
    public void cleanInfection(BlockPos blockPos){
        int range =2* SConfig.DATAGEN.cryo_range.get();
        AABB aabb = AABB.ofSize(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()), range, range, range);
        if (level == null){
            return;
        }
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);

        for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
            BlockState state = level.getBlockState(blockpos);
            if (state.is(foliage)){
                if (Math.random() < 0.2)
                    level.removeBlock(blockpos,false);
            }
            if (state == Sblocks.REMAINS.get().defaultBlockState()){
                level.setBlock(blockpos,Sblocks.FROZEN_REMAINS.get().defaultBlockState(),3);
            }
            if (Math.random() < 0.2 && !blockMap.isEmpty()){
                for (StoreDouble storeDouble : blockMap){
                    if (storeDouble.value1 == state.getBlock()){
                        level.setBlock(blockpos,storeDouble.value2.defaultBlockState(),3);
                    }
                }
            }
            if (Math.random() < 0.1){
                if (biomass.contains(state)){
                    level.setBlock(blockpos,Sblocks.FROST_BURNED_BIOMASS.get().defaultBlockState(),3);
                }
                if (state == Sblocks.BILE.get().defaultBlockState()){
                    level.setBlock(blockpos,Sblocks.CRUSTED_BILE.get().defaultBlockState(),3);
                }
            }
            if (Math.random() < 0.001 && SConfig.DATAGEN.cryo_snow.get()){
                BlockState blockState1 = level.getBlockState(blockpos.above());
                if (state.isSolidRender(level,blockPos) && blockState1.isAir()){
                    RandomSource randomSource = RandomSource.create();
                    int layer = randomSource.nextInt(1,4);
                    BlockState snowState = Blocks.SNOW.defaultBlockState();
                    if (snowState.getBlock().getStateDefinition().getProperty("layers") instanceof IntegerProperty property)
                        level.setBlock(blockpos.above(),snowState.setValue(property,layer),3);
                }
            }
        }
        for (Entity entity : entities){
            if (entity instanceof LivingEntity livingEntity &&
                    (livingEntity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES))){
                livingEntity.setTicksFrozen(livingEntity.getTicksFrozen()+100);
                float damage = getDamageAfterArmor((float) (SConfig.DATAGEN.cryo_damage.get() *1f),livingEntity);
                livingEntity.hurt(livingEntity.damageSources().freeze(), damage);
            }
            if (entity instanceof ScentEntity || entity instanceof InfectionTendril){
                entity.discard();
            }
        }
    }
    public static float getDamageAfterArmor(float damage, LivingEntity target) {
        double armor = target.getArmorValue();
        AttributeInstance instance = target.getAttribute(Attributes.ARMOR_TOUGHNESS);
        double toughness = instance == null ? 0 : instance.getValue();
        float f = 2.0F + (float)toughness / 4.0F;
        float armorFactor = Math.min(20.0F, (float)armor);

        return damage * (1.0F - armorFactor / (armorFactor + f));
    }

    public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, CDUBlockEntity e) {
        if (CDUBlock.isCDUUsable(blockPos,e.level)){
            if (e.getFuel() > 0 && !level.isClientSide){
                e.fuel--;
                if (e.getFuel() % 100 == 0){
                    e.cleanInfection(blockPos);
                }
                if (e.getFuel() % 80 == 0){
                    level.playSound(null,blockPos, Ssounds.CDU_AMBIENT.value(), SoundSource.BLOCKS,1,1);
                }
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.spore.cdu");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CDUMenu(i,inventory);
    }
}
