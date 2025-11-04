package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Spore;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Sparticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Spore.MODID);

    public static final Supplier<SimpleParticleType> SPORE_PARTICLE =
            PARTICLE_TYPES.register("spore_particle", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> ACID_PARTICLE =
            PARTICLE_TYPES.register("acid_particle", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> BLOOD_PARTICLE =
            PARTICLE_TYPES.register("blood_particle", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> SPORE_SLASH =
            PARTICLE_TYPES.register("spore_slash", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> SPORE_IMPACT =
            PARTICLE_TYPES.register("spore_impact", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> VOMIT =
            PARTICLE_TYPES.register("vomit", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> VOMIT_BONE =
            PARTICLE_TYPES.register("vomit_bone", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> VOMIT_ORES =
            PARTICLE_TYPES.register("vomit_ores", () -> new SimpleParticleType(true));
    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
