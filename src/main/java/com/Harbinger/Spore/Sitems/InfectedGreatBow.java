package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.Fluids.BileLiquid;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeWeaponData;
import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Seffects;
import com.Harbinger.Spore.core.Senchantments;
import com.Harbinger.Spore.core.Sitems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class InfectedGreatBow extends BowItem implements SporeWeaponData {
    private static final ResourceLocation BONUS_REACH_MODIFIER_UUID = ResourceLocation.fromNamespaceAndPath(Spore.MODID,"attack_reach");
    public InfectedGreatBow() {
        super(new Item.Properties().durability(SConfig.SERVER.bow_durability.get()));
        Sitems.BIOLOGICAL_ITEMS.add(this);
        Sitems.TINTABLE_ITEMS.add(this);
    }
    @Override
    public boolean isValidRepairItem(ItemStack itemstack, ItemStack repairitem) {
        return Objects.equals(Sitems.BIOMASS.get(), repairitem.getItem());
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                i = EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
                if (i < 0) {
                    return;
                }

                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel) {
                        ServerLevel serverlevel = (ServerLevel)level;
                        if (!list.isEmpty()) {
                            this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                        }
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }

    }

    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        float f = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
        float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float)(projectileItems.size() - 1);
        float f2 = (float)((projectileItems.size() - 1) % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for(int i = 0; i < projectileItems.size(); ++i) {
            ItemStack itemstack = (ItemStack)projectileItems.get(i);
            if (!itemstack.isEmpty()) {
                float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
                f3 = -f3;
                Projectile projectile = this.createProjectile(level, shooter, weapon, itemstack, isCrit);
                if (projectile instanceof Arrow arrow){
                    abstractEffects(itemstack,arrow,shooter.level());
                }
                this.shootProjectile(shooter, projectile, i, velocity, inaccuracy, f4, target);
                level.addFreshEntity(projectile);
                weapon.hurtAndBreak(this.getDurabilityUse(itemstack), shooter, LivingEntity.getSlotForHand(hand));
                if (weapon.isEmpty()) {
                    break;
                }
            }
        }
    }

    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return this.getDynamicAttributeModifiers(stack);
    }
    public ItemAttributeModifiers getDynamicAttributeModifiers(ItemStack stack) {
        double extraDamage = calculateTrueDamage(stack, SConfig.SERVER.bow_melee_damage.get()) + modifyDamage(stack, SConfig.SERVER.bow_melee_damage.get());
        double extraRecharge = -2.4 + modifyRecharge(stack);
        double extraReach = 1 + modifyRange(stack);

        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, extraDamage, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, extraRecharge, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(BONUS_REACH_MODIFIER_UUID, extraReach, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }
    public void abstractEffects(ItemStack stack, Arrow arrow,Level level){
        if (Senchantments.hasEnchant(level,stack,Senchantments.CORROSIVE_POTENCY)){
            arrow.addEffect(new MobEffectInstance(Seffects.CORROSION,200,1));
        }
        if (Senchantments.hasEnchant(level,stack,Senchantments.GASTRIC_SPEWAGE)){
            for (MobEffectInstance instance : BileLiquid.bileEffects())
                arrow.addEffect(instance);
        }
        if (getVariant(stack) == SporeToolsMutations.ROTTEN){
            arrow.addEffect(new MobEffectInstance(MobEffects.WITHER,100,0));
        }
        if (getVariant(stack) == SporeToolsMutations.TOXIC){
            arrow.addEffect(new MobEffectInstance(MobEffects.POISON,100,1));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        if (!tooHurt(stack)){
            components.add(Component.translatable("spore.item.hurt").withStyle(ChatFormatting.RED));
        }
        if (Screen.hasShiftDown()){
            if (getAdditionalDamage(stack) > 0){
                components.add(Component.literal(Component.translatable("spore.item.damage_increase").getString() + getAdditionalDamage(stack) + "%"));
            }if (getMaxAdditionalDurability(stack) > 0){
                components.add(Component.literal(Component.translatable("spore.item.durability_increase").getString()+ getMaxAdditionalDurability(stack) + "%"));
            }if (getAdditionalDurability(stack) > 0){
                components.add(Component.literal(Component.translatable("spore.item.additional_durability").getString()+ getAdditionalDurability(stack)));
            }if (getEnchantmentValue(stack) > 1){
                components.add(Component.literal(Component.translatable("spore.item.enchant").getString()+ getEnchantmentValue(stack)));
            }
            if (getVariant(stack) != SporeToolsMutations.DEFAULT){
                components.add(Component.literal(Component.translatable("spore.item.mutation").getString()+Component.translatable(getVariant(stack).getName()).getString()));
            }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return super.isBarVisible(stack) || getAdditionalDurability(stack) > 0;
    }
    @Override
    public int getBarColor(ItemStack stack) {
        if (getAdditionalDurability(stack) > 0){
            return Mth.hsvToRgb( 240.0F, 100.0F, 100.0F);
        }else{
            return super.getBarColor(stack);
        }
    }
    @Override
    public int getEnchantmentValue(ItemStack stack) {
        int luck = getLuck(stack);
        return luck > 0 ? luck : 1;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (!tooHurt(stack) && entity instanceof Player player){
            player.getCooldowns().addCooldown(this,60);
        }
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity living, LivingEntity entity) {
        if (tooHurt(stack)){
            hurtTool(stack,entity,1);
        }
        doEntityHurtAfterEffects(stack,living,entity);
        return super.hurtEnemy(stack, living, entity);
    }

}
