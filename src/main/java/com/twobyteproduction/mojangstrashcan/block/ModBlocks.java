package com.twobyteproduction.mojangstrashcan.block;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Mojangstrashcan.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Mojangstrashcan.MOD_ID);

    // Таймер
    public static final DeferredBlock<RedstoneTimerBlock> REDSTONE_TIMER = BLOCKS.register("redstone_timer",
            () -> new RedstoneTimerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5F).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final DeferredItem<BlockItem> REDSTONE_TIMER_ITEM = ITEMS.registerSimpleBlockItem("redstone_timer", REDSTONE_TIMER);

    // Руда
    public static final DeferredBlock<Block> RUBY_ORE = BLOCKS.register("ruby_ore",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));
    public static final DeferredItem<BlockItem> RUBY_ORE_ITEM = ITEMS.registerSimpleBlockItem("ruby_ore", RUBY_ORE);

    // НОВОЕ: Рубиновый блок
    public static final DeferredBlock<Block> RUBY_BLOCK = BLOCKS.register("ruby_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(5.0F, 6.0F).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final DeferredItem<BlockItem> RUBY_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("ruby_block", RUBY_BLOCK);
}