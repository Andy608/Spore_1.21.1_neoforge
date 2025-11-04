package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.core.Sitems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

public class SporeSpawnEgg extends SpawnEggItem {
    private final SpawnEggType type;

    public SporeSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, SpawnEggType type1) {
        super(type.get(), backgroundColor, -1, new Properties());
        this.type = type1;
        Sitems.BIOLOGICAL_ITEMS.add(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(type.getName());
        super.appendHoverText(stack, context, components, flag);
    }
}
