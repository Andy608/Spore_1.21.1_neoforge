package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Screens.*;
import com.Harbinger.Spore.Spore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SMenu {
    public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(Registries.MENU, Spore.MODID);

    public static void register(IEventBus eventBus) {
        MENU.register(eventBus);
    }
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerMenu>> CONTAINER = registerMenuType("container", ContainerMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<SurgeryMenu>> SURGERY_MENU = registerMenuType("surgery_menu", SurgeryMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<InjectionMenu>> INJECTION_MENU = registerMenuType("injection_menu", InjectionMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<InjectionRecipeMenu>> INJECTION_RECIPE_MENU = registerMenuType("injection_recipe_menu", InjectionRecipeMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<AssimilationMenu>> ASSIMILATION_MENU = registerMenuType("assimilation_menu", AssimilationMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<IncubatorMenu>> INCUBATOR_MENU = registerMenuType("incubator_menu", IncubatorMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ZoaholicMenu>> ZOAHOLIC_MENU = registerMenuType("zoaholic_menu", ZoaholicMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<SurgeryRecipeMenu>> SURGERY_RECIPE_MENU = registerMenuType("surgery_recipe_menu", SurgeryRecipeMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<CDUMenu>> CDU_MENU = registerMenuType("cdu_menu", CDUMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<CabinetMenu>> CABINET_MENU = registerMenuType("cabinet_menu", CabinetMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<GraftingMenu>> GRAFTING_MENU = registerMenuType("grafting_menu", GraftingMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<GraftingRecipeMenu>> GRAFTING_RECIPE_MENU = registerMenuType("grafting_recipe_menu", GraftingRecipeMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENU.register(name, () -> IMenuTypeExtension.create(factory));
    }
}