package com.twobyteproduction.mojangstrashcan;

import com.twobyteproduction.mojangstrashcan.block.ModBlocks;
import com.twobyteproduction.mojangstrashcan.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Mojangstrashcan.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOJANGS_TRASH_CAN_TAB =
            CREATIVE_MODE_TABS.register("mojangstrashcan_tab", () -> CreativeModeTab.builder()
                    // Название вкладки (из файла переводов)
                    .title(Component.translatable("itemGroup.mojangstrashcan"))
                    // Иконка вкладки (наша красная урна)
                    .icon(() -> new ItemStack(ModItems.TAB_ICON.get()))
                    // Список предметов во вкладке
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GUIDE_BOOK.get());
                        output.accept(ModItems.QUIVER.get());
                        output.accept(ModBlocks.REDSTONE_TIMER.get());

                        output.accept(ModItems.RUBY.get());
                        output.accept(ModBlocks.RUBY_BLOCK.get());
                        output.accept(ModBlocks.RUBY_ORE.get());

                        // Сам TAB_ICON мы сюда НЕ добавляем, чтобы он не лежал в инвентаре
                    }).build());
}