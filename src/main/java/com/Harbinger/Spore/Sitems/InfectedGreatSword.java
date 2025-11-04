package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.Sitems.BaseWeapons.SporeSwordBase;
import com.Harbinger.Spore.core.SConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class InfectedGreatSword extends SporeSwordBase {
    private final ResourceLocation BONUS_ARMOR_MODIFIER_UUID = ResourceLocation.withDefaultNamespace("base_armor");
    private final ResourceLocation BONUS_TOUGHNESS_MODIFIER_UUID = ResourceLocation.withDefaultNamespace("base_toughness");
    public InfectedGreatSword() {
        super(SConfig.SERVER.greatsword_damage.get(), 2.5f, 3F, SConfig.SERVER.greatsword_durability.get());
    }


    @Override
    public ItemAttributeModifiers getDynamicAttributeModifiers(ItemStack stack) {
        double extraDamage = calculateTrueDamage(stack, meleeDamage) + modifyDamage(stack, meleeDamage);
        double extraRecharge = -meleeRecharge + modifyRecharge(stack);
        double extraReach = meleeReach + modifyRange(stack);

        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, extraDamage, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, extraRecharge, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(BASE_ATTACK_REACH_ID, extraReach, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND).add(Attributes.ARMOR,
                        new AttributeModifier(BONUS_ARMOR_MODIFIER_UUID, SConfig.SERVER.greatsword_armor.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ARMOR_TOUGHNESS,
                        new AttributeModifier(BONUS_TOUGHNESS_MODIFIER_UUID, SConfig.SERVER.greatsword_toughness.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }
}
