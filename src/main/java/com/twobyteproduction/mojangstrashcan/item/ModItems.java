package com.twobyteproduction.mojangstrashcan.item;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Mojangstrashcan.MOD_ID);

    public static final DeferredItem<GuideBookItem> GUIDE_BOOK = ITEMS.register("guide_book",
            () -> new GuideBookItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> RUBY = ITEMS.registerSimpleItem("ruby");
    public static final DeferredItem<QuiverItem> QUIVER = ITEMS.register("quiver",
            () -> new QuiverItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> TAB_ICON = ITEMS.registerSimpleItem("tab_icon");
    public static final DeferredItem<LightHelmetItem> LIGHT_HELMET = ITEMS.register("light_helmet",
            () -> new LightHelmetItem(new Item.Properties()));

    public static final DeferredItem<ExplosiveArrowItem> EXPLOSIVE_ARROW = ITEMS.register("explosive_arrow",
            () -> new ExplosiveArrowItem(new Item.Properties()));
}