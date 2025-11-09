package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Screens.*;
import com.Harbinger.Spore.Spore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SMenu {
    public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(Registries.MENU, Spore.MODID);
    public static void register(IEventBus eventBus) {
        MENU.register(eventBus);
    }

    public static final DeferredHolder<MenuType<?>,MenuType<ContainerMenu>> CONTAINER = MENU.register("container",   () -> IMenuTypeExtension.create(ContainerMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<SurgeryMenu>> SURGERY_MENU = MENU.register("surgery_menu", () -> IMenuTypeExtension.create(SurgeryMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<InjectionMenu>> INJECTION_MENU = MENU.register("injection_menu", () -> IMenuTypeExtension.create(InjectionMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<InjectionRecipeMenu>> INJECTION_RECIPE_MENU = MENU.register("injection_recipe_menu", () -> IMenuTypeExtension.create(InjectionRecipeMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<AssimilationMenu>> ASSIMILATION_MENU = MENU.register("assimilation_menu", () -> IMenuTypeExtension.create(AssimilationMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<IncubatorMenu>> INCUBATOR_MENU = MENU.register("incubator_menu", () -> IMenuTypeExtension.create(IncubatorMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<ZoaholicMenu>> ZOAHOLIC_MENU = MENU.register("zoaholic_menu", () -> IMenuTypeExtension.create(ZoaholicMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<SurgeryRecipeMenu>> SURGERY_RECIPE_MENU = MENU.register("surgery_recipe_menu", () -> IMenuTypeExtension.create(SurgeryRecipeMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<CDUMenu>> CDU_MENU = MENU.register("cdu_menu", () -> IMenuTypeExtension.create(CDUMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<CabinetMenu>> CABINET_MENU = MENU.register("cabinet_menu", () -> IMenuTypeExtension.create(CabinetMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<GraftingMenu>> GRAFTING_MENU = MENU.register("grafting_menu", () -> IMenuTypeExtension.create(GraftingMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<GraftingRecipeMenu>> GRAFTING_RECIPE_MENU = MENU.register("grafting_recipe_menu", () -> IMenuTypeExtension.create(GraftingRecipeMenu::new));

}
