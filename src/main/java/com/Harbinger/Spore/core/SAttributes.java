package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Spore;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


public class SAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, Spore.MODID);
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
    private static String constructLang(String value) {
        return "attribute.name." + Spore.MODID + "." + value;
    }
    public static final Holder<Attribute> TOXICITY = ATTRIBUTES.register("toxicity",
            () -> new RangedAttribute(constructLang("toxicity"), 0.0D, 0.0D, 64.0D)
                    .setSyncable(true));
    public static final Holder<Attribute> REJUVENATION = ATTRIBUTES.register("rejuvenation",
            () -> new RangedAttribute(constructLang("rejuvenation"), 0.0D, 0.0D, 64.0D)
                    .setSyncable(true));
    public static final Holder<Attribute> LOCALIZATION = ATTRIBUTES.register("localization",
            () -> new RangedAttribute(constructLang("localization"), 0.0D, 0.0D, 64.0D)
                    .setSyncable(true));
    public static final Holder<Attribute> LACERATION = ATTRIBUTES.register("laceration",
            () -> new RangedAttribute(constructLang("laceration"), 0.0D, 0.0D, 64.0D)
                    .setSyncable(true));
    public static final Holder<Attribute> CORROSIVES = ATTRIBUTES.register("corrosives",
            () -> new RangedAttribute(constructLang("corrosives"), 0.0D, 0.0D, 64.0D)
                    .setSyncable(true));
    public static final Holder<Attribute> BALLISTIC = ATTRIBUTES.register("ballistic",
            () -> new RangedAttribute(constructLang("ballistic"), 0.0D, 0.0D, 64.0D)
                    .setSyncable(true));
    public static final Holder<Attribute> GRINDING = ATTRIBUTES.register("grinding",
            () -> new RangedAttribute(constructLang("grinding"), 0.0D, 0.0D, 64.0D)
                    .setSyncable(true));
}