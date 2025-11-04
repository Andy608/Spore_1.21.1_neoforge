package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Screens.*;
import com.Harbinger.Spore.Spore;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SMenu {
    public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(Registries.MENU, Spore.MODID);
    public static void register(IEventBus eventBus) {
        MENU.register(eventBus);
    }
    private static final FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

    public static final Supplier<MenuType<ContainerMenu>> CONTAINER = MENU.register("container",   () -> new MenuType<>((windowId, inv) -> new ContainerMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<SurgeryMenu>> SURGERY_MENU = MENU.register("surgery_menu", () -> new MenuType<>((windowId, inv) -> new SurgeryMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<InjectionMenu>> INJECTION_MENU = MENU.register("injection_menu", () -> new MenuType<>((windowId, inv) -> new InjectionMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<InjectionRecipeMenu>> INJECTION_RECIPE_MENU = MENU.register("injection_recipe_menu", () -> new MenuType<>(InjectionRecipeMenu::new, FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<AssimilationMenu>> ASSIMILATION_MENU = MENU.register("assimilation_menu", () -> new MenuType<>((windowId, inv) -> new AssimilationMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<IncubatorMenu>> INCUBATOR_MENU = MENU.register("incubator_menu", () -> new MenuType<>((windowId, inv) -> new IncubatorMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<ZoaholicMenu>> ZOAHOLIC_MENU = MENU.register("zoaholic_menu", () -> new MenuType<>(ZoaholicMenu::new, FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<SurgeryRecipeMenu>> SURGERY_RECIPE_MENU = MENU.register("surgery_recipe_menu", () -> new MenuType<>((windowId, inv) -> new SurgeryRecipeMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<CDUMenu>> CDU_MENU = MENU.register("cdu_menu", () -> new MenuType<>((windowId, inv) -> new CDUMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<CabinetMenu>> CABINET_MENU = MENU.register("cabinet_menu", () -> new MenuType<>((windowId, inv) -> new CabinetMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<GraftingMenu>> GRAFTING_MENU = MENU.register("grafting_menu", () -> new MenuType<>((windowId, inv) -> new GraftingMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<GraftingRecipeMenu>> GRAFTING_RECIPE_MENU = MENU.register("grafting_recipe_menu", () -> new MenuType<>((windowId, inv) -> new GraftingRecipeMenu(windowId, inv,buf), FeatureFlags.VANILLA_SET));

}
