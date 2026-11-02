package com.twobyteproduction.mojangstrashcan.menu;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Mojangstrashcan.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<RedstoneTimerMenu>> REDSTONE_TIMER_MENU =
            MENUS.register("redstone_timer_menu", () -> IMenuTypeExtension.create(RedstoneTimerMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<GuideBookMenu>> GUIDE_BOOK_MENU =
            MENUS.register("guide_book_menu", () -> IMenuTypeExtension.create((containerId, inv, buf) -> new GuideBookMenu(containerId, inv)));

    public static final DeferredHolder<MenuType<?>, MenuType<QuiverMenu>> QUIVER_MENU =
            MENUS.register("quiver_menu", () -> IMenuTypeExtension.create(QuiverMenu::new));
}