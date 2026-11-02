package com.twobyteproduction.mojangstrashcan.block;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Mojangstrashcan.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneTimerBlockEntity>> REDSTONE_TIMER_BE =
            BLOCK_ENTITIES.register("redstone_timer",
                    () -> BlockEntityType.Builder.of(RedstoneTimerBlockEntity::new, ModBlocks.REDSTONE_TIMER.get()).build(null));
}