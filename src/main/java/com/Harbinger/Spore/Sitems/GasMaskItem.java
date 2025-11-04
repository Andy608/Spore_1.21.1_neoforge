package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.core.Sitems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GasMaskItem extends ArmorItem implements CustomModelArmorData{
    private final ResourceLocation location = ResourceLocation.parse("spore:textures/armor/gas_mask.png");
    public GasMaskItem() {
        super(Holder.direct(new ArmorMaterial(
                Map.of(),
                0,
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.EMPTY,
                List.of(),
                0,
                0
        )), Type.HELMET, new Properties().stacksTo(1));
        Sitems.TECHNOLOGICAL_ITEMS.add(this);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        return 0;
    }


    @Override
    public ResourceLocation getTextureLocation() {
        return location;
    }
}
