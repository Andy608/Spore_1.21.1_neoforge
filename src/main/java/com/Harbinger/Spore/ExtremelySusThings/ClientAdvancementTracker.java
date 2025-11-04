package com.Harbinger.Spore.ExtremelySusThings;

import net.neoforged.api.distmarker.Dist;

import java.util.HashMap;
import java.util.Map;

@net.neoforged.api.distmarker.OnlyIn(Dist.CLIENT)
public class ClientAdvancementTracker {
    private static final Map<String, Boolean> advancementCache = new HashMap<>();

    public static void setAdvancement(String advancementId, boolean hasIt) {
        advancementCache.put(advancementId, hasIt);
    }

    public static boolean hasAdvancement(String advancementId) {
        return advancementCache.getOrDefault(advancementId, false);
    }
}