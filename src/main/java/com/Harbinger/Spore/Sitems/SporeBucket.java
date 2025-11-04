package com.Harbinger.Spore.Sitems;

import com.Harbinger.Spore.core.Sitems;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

public class SporeBucket extends BucketItem {

    public SporeBucket(Fluid content, Properties properties) {
        super(content, properties);
        Sitems.TECHNOLOGICAL_ITEMS.add(this);
    }
}
