package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Spore;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Sfluids {
    public static final DeferredRegister<Fluid> SPORE_FLUID = DeferredRegister.create(Registries.FLUID, Spore.MODID);

    private static BaseFlowingFluid.Properties bileProperties() {
        return new BaseFlowingFluid.Properties(
                () -> BILE_FLUID_TYPE, // FluidType reference
                BILE_FLUID_SOURCE,
                BILE_FLUID_FLOWING
        )
                .bucket(Sitems.BUCKET_OF_BILE)
                .block(Sblocks.BILE);
    }
    public static final FluidType BILE_FLUID_TYPE = new FluidType(FluidType.Properties.create()
            .lightLevel(5)
            .density(1024)
            .viscosity(1024)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
    ) {};

    public static final DeferredHolder<Fluid, FlowingFluid> BILE_FLUID_SOURCE = SPORE_FLUID.register("bile",
            () -> new BaseFlowingFluid.Source(bileProperties()));

    public static final DeferredHolder<Fluid, FlowingFluid> BILE_FLUID_FLOWING = SPORE_FLUID.register("bile_flowing",
            () -> new BaseFlowingFluid.Flowing(bileProperties()));

    public static void postInit() {
        FluidInteractionRegistry.addInteraction(BILE_FLUID_TYPE, new FluidInteractionRegistry.InteractionInformation(
                (FluidInteractionRegistry.HasFluidInteraction) Fluids.WATER, // Use Fluids.WATER instead of ForgeMod.WATER_TYPE
                fluidState -> Sblocks.CRUSTED_BILE.get().defaultBlockState()
        ));

        FluidInteractionRegistry.addInteraction(BILE_FLUID_TYPE, new FluidInteractionRegistry.InteractionInformation(
                (FluidInteractionRegistry.HasFluidInteraction) Fluids.LAVA,
                fluidState -> Sblocks.CRUSTED_BILE.get().defaultBlockState()
        ));
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        postInit();
    }
}