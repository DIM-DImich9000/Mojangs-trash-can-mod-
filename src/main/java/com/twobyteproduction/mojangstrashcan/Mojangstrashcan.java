package com.twobyteproduction.mojangstrashcan;

import com.twobyteproduction.mojangstrashcan.block.ModBlockEntities;
import com.twobyteproduction.mojangstrashcan.block.ModBlocks;
import com.twobyteproduction.mojangstrashcan.item.ModItems;
import com.twobyteproduction.mojangstrashcan.menu.ModMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Mojangstrashcan.MOD_ID)
public class Mojangstrashcan {
    public static final String MOD_ID = "mojangstrashcan";

    public Mojangstrashcan(IEventBus modEventBus) {
        // Регистрация блоков и их предметов
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.ITEMS.register(modEventBus);

        // Регистрация сущностей блоков (BlockEntity)
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        // Регистрация меню (GUI)
        ModMenus.MENUS.register(modEventBus);

        // Регистрация предметов
        ModItems.ITEMS.register(modEventBus);

        // Регистрация нашей кастомной вкладки в креативе
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}