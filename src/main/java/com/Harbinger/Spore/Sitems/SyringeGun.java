package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.Client.AnimationTrackers.SGAnimationTracker;
import com.Harbinger.Spore.Client.AnimationTrackers.SGReloadAnimationTracker;
import com.Harbinger.Spore.Sentities.Projectile.SyringeProjectile;
import com.Harbinger.Spore.Sitems.Agents.ArmorSyringe;
import com.Harbinger.Spore.Sitems.Agents.WeaponSyringe;
import com.Harbinger.Spore.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.core.NonNullList;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SyringeGun extends BaseItem2 implements CustomModelArmorData {
    private static final ResourceLocation TEXTURE = ResourceLocation.parse("spore:textures/item/syringe_gun.png");

    public static final List<Item> AMMO = List.of(
            Sitems.SYRINGE.get(),
            Sitems.VAMPIRIC_SYRINGE.get(),
            Sitems.CALCIFIED_SYRINGE.get(),
            Sitems.BEZERK_SYRINGE.get(),
            Sitems.TOXIC_SYRINGE.get(),
            Sitems.ROTTEN_SYRINGE.get(),
            Sitems.REINFORCED_SYRINGE.get(),
            Sitems.SKELETAL_SYRINGE.get(),
            Sitems.DROWNED_SYRINGE.get(),
            Sitems.CHARRED_SYRINGE.get()
    );

    public SyringeGun() {
        super(new Properties().stacksTo(1).durability(SConfig.SERVER.syringe_durability.get())
                .component(SdataComponents.CHAMBER.get(), 0) // currentChamber
                .component(SdataComponents.RELOAD_TIMER.get(), 0) // reloadTimer
                .component(SdataComponents.SHOOT_COOLDOWN.get(), 0) // shootCooldown
                .component(SdataComponents.RELOADING.get(), false)); // reloading
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    private boolean isValidAmmo(ItemStack stack) {
        return !stack.isEmpty() && AMMO.contains(stack.getItem());
    }

    /** ---------------- MAGAZINE / CLIP ---------------- */

    private NonNullList<ItemStack> getMagazine(ItemStack gun) {
        ItemContainerContents contents = gun.get(DataComponents.CONTAINER);
        if (contents != null) {
            NonNullList<ItemStack> magazine = NonNullList.withSize(4, ItemStack.EMPTY);
            List<ItemStack> items = contents.stream().toList();
            for (int i = 0; i < Math.min(items.size(), 4); i++) {
                magazine.set(i, items.get(i));
            }
            return magazine;
        }
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    public NonNullList<Integer> getClip(ItemStack gun) {
        NonNullList<Integer> clip = NonNullList.withSize(4, 0);
        NonNullList<ItemStack> magazine = getMagazine(gun);
        for (int i = 0; i < magazine.size(); i++) {
            ItemStack ammo = magazine.get(i);
            if (!ammo.isEmpty()) {
                clip.set(i, encodeColors(ammo));
            }
        }
        return clip;
    }

    private void saveMagazine(ItemStack gun, NonNullList<ItemStack> magazine) {
        gun.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(magazine));
    }

    public void setMagazine(ItemStack gun, ItemStack ammo, int slot) {
        NonNullList<ItemStack> magazine = getMagazine(gun);
        if (isValidAmmo(ammo)) {
            magazine.set(slot, ammo.copy());
        }
        saveMagazine(gun, magazine);
    }

    public void removeMagazine(ItemStack gun, int slot) {
        NonNullList<ItemStack> magazine = getMagazine(gun);
        magazine.set(slot, ItemStack.EMPTY);
        saveMagazine(gun, magazine);
    }

    private int encodeColors(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        if (stack.getItem().equals(Sitems.SYRINGE.get())) return -1;
        if (stack.getItem() instanceof WeaponSyringe w) return w.getColor();
        if (stack.getItem() instanceof ArmorSyringe a) return a.getColor();
        return 0;
    }

    /** ---------------- STATE HANDLING ---------------- */

    private int getCurrentChamber(ItemStack gun) {
        return gun.getOrDefault(SdataComponents.CHAMBER, 0);
    }

    private void setCurrentChamber(ItemStack gun, int value) {
        gun.set(SdataComponents.CHAMBER, value);
    }

    private int getReloadTimer(ItemStack gun) {
        // Use a custom component for reload timer to avoid conflicts
        return gun.getOrDefault(SdataComponents.RELOAD_TIMER.get(), 0);
    }

    private void setReloadTimer(ItemStack gun, int value) {
        gun.set(SdataComponents.RELOAD_TIMER.get(), value);
    }

    private boolean isReloading(ItemStack gun) {
        return gun.getOrDefault(SdataComponents.RELOADING, false);
    }

    private void setReloading(ItemStack gun, boolean value) {
        gun.set(SdataComponents.RELOADING, value);
    }

    private int getShootCooldown(ItemStack gun) {
        // Use a custom component for shoot cooldown
        return gun.getOrDefault(SdataComponents.SHOOT_COOLDOWN.get(), 0);
    }

    private void setShootCooldown(ItemStack gun, int value) {
        gun.set(SdataComponents.SHOOT_COOLDOWN.get(), value);
    }

    /** ---------------- LOGIC ---------------- */

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!(entity instanceof Player player)) return;

        boolean inHand = player.getMainHandItem() == stack || player.getOffhandItem() == stack;
        if (inHand && isReloading(stack)) {
            if (getReloadTimer(stack) == 5){
                player.playNotifySound(Ssounds.SYRINGE_SPIN.get(), SoundSource.AMBIENT,0.5f,1);
            }
            if (getReloadTimer(stack) > 0) setReloadTimer(stack, getReloadTimer(stack) - 1);
            else {
                reloadOne(stack, player);
                setReloadTimer(stack, 10);
            }
        }

        if (getShootCooldown(stack) > 0){
            if (getShootCooldown(stack) == 5 && level.isClientSide){
                int chamber = getCurrentChamber(stack);
                player.playNotifySound(Ssounds.SYRINGE_SPIN.get(), SoundSource.AMBIENT,0.5f,1);
                SGReloadAnimationTracker.triggerRotationToChamber(player,chamber,10);
            }
            setShootCooldown(stack, getShootCooldown(stack) - 1);
        }
    }

    public void startReload(ItemStack stack) {
        setReloading(stack, true);
        setReloadTimer(stack, 10);
    }

    private void reloadOne(ItemStack gun, Player player) {
        NonNullList<ItemStack> magazine = getMagazine(gun);
        for (int i = 0; i < magazine.size(); i++) {
            if (magazine.get(i).isEmpty()) {
                ItemStack ammo = findAmmo(player);
                if (!ammo.isEmpty()) {
                    if (player.level().isClientSide) {
                        SGReloadAnimationTracker.triggerRotationToChamber(player,i,10);
                        player.playNotifySound(Ssounds.SYRINGE_RELOAD.get(), SoundSource.AMBIENT, 0.5f, 1);
                    } else {
                        ItemStack taken = ammo.split(1);
                        setMagazine(gun, taken, i);
                    }
                } else {
                    setReloading(gun, false);
                    break;
                }
                return;
            }
        }
        setReloading(gun, false);
    }

    private ItemStack findAmmo(Player player) {
        ItemStack offhand = player.getOffhandItem();
        if (isValidAmmo(offhand)) return offhand;
        if (player.getAbilities().instabuild)
            return new ItemStack(Sitems.SYRINGE.get());
        for (ItemStack invStack : player.getInventory().items) {
            if (isValidAmmo(invStack)) return invStack;
        }
        return ItemStack.EMPTY;
    }

    public boolean shoot(ItemStack gun, Player player, Level level, InteractionHand hand) {
        if (getShootCooldown(gun) > 0) return false;

        NonNullList<ItemStack> magazine = getMagazine(gun);
        int chamber = getCurrentChamber(gun);
        ItemStack ammo = magazine.get(chamber);

        if (!ammo.isEmpty()) {
            if (!level.isClientSide) {
                int enchantment = EnchantmentHelper.getItemEnchantmentLevel(Senchantments.getEnchantment(player.level(),Enchantments.POWER),gun);
                float power = enchantment > 0 ? enchantment * 1.5f : 0;
                SyringeProjectile arrow = new SyringeProjectile(level, player, SConfig.SERVER.syringe_damage.get() + power, ammo);
                Vec3 vec3 = (new Vec3(0.0D, 0.0D, hand == InteractionHand.MAIN_HAND ? 0.2 : -0.2)).yRot(-player.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
                arrow.moveTo(player.position().add(vec3.x,1.4,vec3.z));
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
                level.addFreshEntity(arrow);
                player.playNotifySound(Ssounds.SYRINGE_SHOOT.get(), SoundSource.AMBIENT,0.5f,1);
            } else {
                SGAnimationTracker.trigger(player);
            }
            removeMagazine(gun, chamber);
            setCurrentChamber(gun, (chamber + 1) % 4);
            setShootCooldown(gun, 10);
            return true;
        } else {
            player.playNotifySound(SoundEvents.LEVER_CLICK, SoundSource.AMBIENT,1,1);
            setCurrentChamber(gun, (chamber + 1) % 4);
            triggerMagazineRotation(chamber, player);
        }
        return false;
    }

    private void triggerMagazineRotation(int chamber, Player player) {
        if (player.level().isClientSide) {
            SGReloadAnimationTracker.triggerRotationToChamber(player,chamber,10);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack gun = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (player.isShiftKeyDown()) {
            if (!isReloading(gun)) startReload(gun);
            return InteractionResultHolder.consume(gun);
        }

        if (getShootCooldown(gun) > 0 || isReloading(gun)) {
            return InteractionResultHolder.fail(gun);
        }

        if (shoot(gun, player, level, hand)) {
            gun.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            return InteractionResultHolder.consume(gun);
        }

        startReload(gun);
        return InteractionResultHolder.fail(gun);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        for (ItemStack ammo : getMagazine(stack)) {
            if (!ammo.isEmpty()) {
                tooltipComponents.add(ammo.copy().getDisplayName());
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack itemStack) {
        return itemStack.is(Sitems.CIRCUIT_BOARD.get());
    }
}