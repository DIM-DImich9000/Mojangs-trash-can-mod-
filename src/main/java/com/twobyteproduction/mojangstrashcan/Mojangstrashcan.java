package com.twobyteproduction.mojangstrashcan;

import com.twobyteproduction.mojangstrashcan.block.ModBlockEntities;
import com.twobyteproduction.mojangstrashcan.block.ModBlocks;
import com.twobyteproduction.mojangstrashcan.entity.ModEntities;
import com.twobyteproduction.mojangstrashcan.item.ModArmorMaterials;
import com.twobyteproduction.mojangstrashcan.item.ModItems;
import com.twobyteproduction.mojangstrashcan.menu.ModMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Mojangstrashcan.MOD_ID)
public class Mojangstrashcan {
    public static final String MOD_ID = "mojangstrashcan";

    public Mojangstrashcan(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}