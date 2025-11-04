package com.Harbinger.Spore.Sentities.Projectile;


import com.Harbinger.Spore.Recipes.EntityContainer;
import com.Harbinger.Spore.Recipes.InjectionRecipe;
import com.Harbinger.Spore.Sitems.Agents.AbstractSyringe;
import com.Harbinger.Spore.core.Sentities;
import com.Harbinger.Spore.core.Sitems;
import com.Harbinger.Spore.core.Srecipes;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Optional;

public class SyringeProjectile extends AbstractArrow {
    private static final String SYRINGE_COMPONENT = "syringe_item";
    private ItemStack itemStack;
    public SyringeProjectile(Level level) {
        super(Sentities.THROWN_SYRINGE.get(), level);
        itemStack = new ItemStack(Sitems.SYRINGE.get());
    }
    public SyringeProjectile(Level level,LivingEntity living,float damage,ItemStack stack){
        super(Sentities.STINGER.get(), level);
        this.setOwner(living);
        this.setBaseDamage(damage);
        this.setItemStack(stack);
    }

    public void setItemStack(ItemStack stack){
        itemStack = stack;
    }
    @Override
    protected ItemStack getPickupItem() {
        return itemStack;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return itemStack;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Syringe", 10)) {
            this.itemStack = ItemStack.parse(this.registryAccess(), tag.getCompound("Syringe"))
                    .orElse(ItemStack.EMPTY);
        }
        else if (tag.contains(SYRINGE_COMPONENT, 10)) {
            this.itemStack = ItemStack.parse(this.registryAccess(), tag.getCompound(SYRINGE_COMPONENT))
                    .orElse(ItemStack.EMPTY);
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!this.itemStack.isEmpty()) {
            tag.put(SYRINGE_COMPONENT, this.itemStack.save(this.registryAccess()));
        }
    }
    public Optional<RecipeHolder<InjectionRecipe>> getRecipe(Level level, Entity entity){
        EntityContainer container = new EntityContainer(entity);
        return level.getRecipeManager().getRecipeFor(Srecipes.INJECTION_TYPE.get(), container, level);
    }
    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity living && !level().isClientSide && canHitEntity(living)){
            if (itemStack.getItem().equals(Sitems.SYRINGE.get())){
                Optional<RecipeHolder<InjectionRecipe>> match = this.getRecipe(level(),living);
                if (match.isPresent() && Math.random() < 0.5){
                    ItemStack stack = match.get().value().getResultItem(null);
                    if (stack == null){
                        return;
                    }
                    ItemEntity itemEntity = new ItemEntity(level(),entity.getX(),entity.getY(),entity.getZ(),stack);
                    level().addFreshEntity(itemEntity);
                }
                this.playSound(Ssounds.SYRINGE_SUCK.get());
            }
            if (itemStack.getItem() instanceof AbstractSyringe syringe){
                syringe.useSyringe(itemStack,living);
                this.playSound(Ssounds.SYRINGE_GUN_INJECT.get());
            }
            living.hurt(level().damageSources().mobProjectile(this,(LivingEntity) getOwner()), (float) getBaseDamage());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        discard();
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return !entity.equals(getOwner()) && super.canHitEntity(entity);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return Ssounds.SYRINGE_GUN_INJECT.get();
    }

}
