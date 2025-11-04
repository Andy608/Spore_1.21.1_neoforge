package com.Harbinger.Spore.Sitems;


import com.Harbinger.Spore.Sitems.BaseWeapons.DamagePiercingModifier;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsBaseItem;
import com.Harbinger.Spore.core.SConfig;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class InfectedMace extends SporeToolsBaseItem implements DamagePiercingModifier {
    public InfectedMace() {
        super(SConfig.SERVER.mace_damage.get(), 2f, 3, SConfig.SERVER.mace_durability.get(), 1,createToolProperties(BlockTags.MINEABLE_WITH_AXE,1));
    }
    private static Tool createToolProperties(TagKey<Block> block, double mining) {
        return new Tool(List.of(Tool.Rule.minesAndDrops(block, (float) mining)), 1.0F, 1);
    }
    @Override
    public float getMinimalDamage(float damage) {
        return SConfig.SERVER.mace_damage.get() * 0.15f;
    }

    @Override
    public boolean doesExtraKnockBack() {
        return true;
    }
}
