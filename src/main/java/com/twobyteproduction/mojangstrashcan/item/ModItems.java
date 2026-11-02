package com.twobyteproduction.mojangstrashcan.item;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Mojangstrashcan.MOD_ID);

    // Основные предметы мода
    public static final DeferredItem<GuideBookItem> GUIDE_BOOK = ITEMS.register("guide_book",
            () -> new GuideBookItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> RUBY = ITEMS.registerSimpleItem("ruby");

    public static final DeferredItem<QuiverItem> QUIVER = ITEMS.register("quiver",
            () -> new QuiverItem(new Item.Properties().stacksTo(1)));

    // НОВОЕ: Скрытый предмет для иконки вкладки
    public static final DeferredItem<Item> TAB_ICON = ITEMS.registerSimpleItem("tab_icon");
}