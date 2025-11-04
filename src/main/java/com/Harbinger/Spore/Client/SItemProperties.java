package com.Harbinger.Spore.Client;


import com.Harbinger.Spore.Sitems.*;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Sitems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SItemProperties {
    public static void addCustomItemProperties() {
        makeBow(Sitems.INFECTED_BOW.get());
        makeTrident(Sitems.INFECTED_SPEAR.get());
        makeCrossbow(Sitems.INFECTED_CROSSBOW.get());
        makeDecayedLimbs(Sitems.DECAYED_LIMBS.get());
        makeSickle(Sitems.SICKLE.get());
        makeCleaver(Sitems.CLEAVER.get());
        makeShield(Sitems.SHIELD.get());
    }
    private static void makeBow(Item item) {
        ItemProperties.register(item, ResourceLocation.parse("pull"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) {
                return 0.0F;
            } else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float)(p_174635_.getUseDuration(p_174637_) -
                        p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemProperties.register(item, ResourceLocation.parse("pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) -> {
            return p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F;
        });
    }
    private static void makeTrident(Item item) {
        ItemProperties.register(item, ResourceLocation.parse("throw"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) {
                return 0.0F;
            } else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float)(p_174635_.getUseDuration(p_174637_) -
                        p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(item, ResourceLocation.parse("throwing"), (p_174585_, p_174586_, p_174587_, p_174588_) -> {
            return p_174587_ != null && p_174587_.isUsingItem() && p_174587_.getUseItem() == p_174585_ ? 1.0F : 0.0F;
        });

    }
    private static void makeCrossbow(Item item) {
        ItemProperties.register(item, ResourceLocation.parse("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(stack) ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(stack,entity);
            }
        });

        ItemProperties.register(item, ResourceLocation.parse("pulling"), (stack, level, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
        });

        ItemProperties.register(item, ResourceLocation.parse("charged"), (stack, level, entity, seed) -> {
            return entity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
        });

        ItemProperties.register(item, ResourceLocation.parse("firework"), (stack, level, entity, seed) -> {
            if (entity != null && CrossbowItem.isCharged(stack)) {
                var chargedProjectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
                return chargedProjectiles != null && chargedProjectiles.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
            }
            return 0.0F;
        });
    }
    private static void makeDecayedLimbs(Item item){
        ItemProperties.register(item, ResourceLocation.parse("decayed"), (p_174585_, p_174586_, p_174587_, p_174588_) -> {
            return p_174587_ instanceof Player player && player.hasEffect(Seffects.MADNESS) ? 1.0F : 0.0F;
        });
    }
    private static void makeSickle(Item item){
        ItemProperties.register(item, ResourceLocation.parse("thrown"), (p_174585_, p_174586_, p_174587_, p_174588_) -> {
            return p_174585_.getItem() instanceof InfectedSickle sickle && sickle.getThrownSickle(p_174585_) ? 1.0F : 0.0F;
        });
    }
    private static void makeCleaver(Item item){
        ItemProperties.register(item, ResourceLocation.parse("swipe"), (stack, p_174586_, player, p_174588_) -> {
            return stack.getItem() instanceof InfectedCleaver && player != null && player.isUsingItem() ? 1.0F : 0.0F;
        });
    }
    private static void makeShield(Item item){
        ItemProperties.register(item, ResourceLocation.parse("use"), (stack, p_174586_, player, p_174588_) -> {
            return stack.getItem() instanceof InfectedShield && player != null && player.isUsingItem() ? 1.0F : 0.0F;
        });
    }

    public static class SyringeGunClient implements IClientItemExtensions {
        public static final SyringeGunClient INSTANCE = new SyringeGunClient();
        @Override
        public HumanoidModel.@org.jetbrains.annotations.Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            ItemStack stack = entityLiving.getItemInHand(hand);
            if (stack.getItem() instanceof SyringeGun){
               return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }
            return IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
        }
    }
    public static class BileClientExtension implements IClientFluidTypeExtensions {
        public static final BileClientExtension INSTANCE = new BileClientExtension();
        public static final ResourceLocation FLUID_STILL = ResourceLocation.parse("spore:block/bile_static");
        public static final ResourceLocation FLUID_FLOWING = ResourceLocation.parse("spore:block/bile_flow");
        public static final ResourceLocation OVERLAY = ResourceLocation.parse("spore:textures/extra/bile_overlay.png");

        @Override
        public ResourceLocation getStillTexture() {
            return FLUID_STILL;
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return FLUID_FLOWING;
        }

        @Override
        public @Nullable ResourceLocation getOverlayTexture() {
            return OVERLAY;
        }
    }
}
