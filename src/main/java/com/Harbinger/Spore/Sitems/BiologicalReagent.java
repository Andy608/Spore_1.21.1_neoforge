package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.Senchantments;
import com.Harbinger.Spore.core.Ssounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.List;

public class BiologicalReagent extends BaseItem {
    private final AcceptedTypes type;
    private final ResourceKey<Enchantment> location;
    public BiologicalReagent(AcceptedTypes types, ResourceKey<Enchantment> location) {
        super(new Properties());
        type = types;
        this.location = location;
    }
    public static final TagKey<Item> ALL_TYPES = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Spore.MODID,"enchantable_items"));
    public static final TagKey<Item> WEAPON_TYPES = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Spore.MODID,"enchantable_weapon_items"));
    public static final TagKey<Item> ARMOR_TYPES_TYPES = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Spore.MODID,"enchantable_armor_items"));
    public AcceptedTypes getType(){return type;}
    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    public ResourceKey<Enchantment> getAppliedEnchantment(){
        return location;
    }

    public boolean testSlotCompat(ItemStack stack){
        if (type == AcceptedTypes.ALL_TYPES){
            return stack.is(ALL_TYPES);
        }else if (type == AcceptedTypes.WEAPON_TYPES){
            return stack.is(WEAPON_TYPES);
        }else if (type == AcceptedTypes.ARMOR_TYPES){
            return stack.is(ARMOR_TYPES_TYPES);
        }
        return false;
    }

    private double chance(){
        return 0.2;
    }
    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        ItemStack stack = slot.getItem();
        LevelAccessor accessor = player.level();
        if (testSlotCompat(stack)  && !Senchantments.hasEnchant(accessor,itemStack,getAppliedEnchantment())){
            if (getAppliedEnchantment() != null && clickAction == ClickAction.SECONDARY){
                player.playNotifySound(Ssounds.REAGENT.get(), SoundSource.AMBIENT,1F,1F);
                Senchantments.EnchantItem(accessor,stack,getAppliedEnchantment());
                itemStack.setCount(itemStack.getCount() -1);
                if (Math.random() < chance()){
                    ResourceKey<Enchantment> curse = Senchantments.curses.get(player.getRandom().nextInt(Senchantments.curses.size()));
                    Senchantments.EnchantItem(accessor,stack,curse);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, list, tooltipFlag);
        list.add(Component.translatable(type.getId()).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("item.reagent.line1"));
        list.add(Component.translatable("enchantment."+getAppliedEnchantment().location().toLanguageKey()));
        list.add(Component.translatable("item.reagent.line2").withStyle(ChatFormatting.BLACK));
        list.add(Component.translatable("universal_shift_rightclick").withStyle(ChatFormatting.YELLOW));
    }

    public enum AcceptedTypes{
        ALL_TYPES("spore.name.reagent_type1"),
        WEAPON_TYPES("spore.name.reagent_type2"),
        ARMOR_TYPES("spore.name.reagent_type3");
        private final String id;
        AcceptedTypes(String ids){
            id = ids;
        }
        public String getId(){
            return id;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()){
            if (level.isClientSide) {
                com.Harbinger.Spore.Client.ClientModEvents.openInjectionScreen(player);
            }
        }
        ItemStack stack = player.getItemInHand(hand);
        return InteractionResultHolder.success(stack);
    }
}
