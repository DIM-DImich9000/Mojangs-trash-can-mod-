package com.twobyteproduction.mojangstrashcan.menu;

import com.twobyteproduction.mojangstrashcan.block.ModBlocks;
import com.twobyteproduction.mojangstrashcan.block.RedstoneTimerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class RedstoneTimerMenu extends AbstractContainerMenu {
    private final RedstoneTimerBlockEntity blockEntity;
    private final ContainerData data;

    public RedstoneTimerMenu(int containerId, Inventory playerInventory, RedstoneTimerBlockEntity blockEntity) {
        super(ModMenus.REDSTONE_TIMER_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = new SimpleContainerData(4);
        this.addDataSlots(data);
    }

    public RedstoneTimerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, (RedstoneTimerBlockEntity) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (!blockEntity.getLevel().isClientSide) {
            int totalTicks = blockEntity.getTimeLeft();
            data.set(0, totalTicks / 1200);
            data.set(1, (totalTicks / 20) % 60);
            data.set(2, totalTicks % 20);
            data.set(3, blockEntity.isActive() ? 1 : 0);
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id >= 1000) {
            blockEntity.setTimeLeft(id - 1000);
            this.broadcastChanges();
            return true;
        }

        int current = blockEntity.getTimeLeft();
        switch (id) {
            case 0 -> blockEntity.setTimeLeft(current + 1200);
            case 1 -> blockEntity.setTimeLeft(current - 1200);
            case 2 -> blockEntity.setTimeLeft(current + 20);
            case 3 -> blockEntity.setTimeLeft(current - 20);
            case 4 -> blockEntity.setTimeLeft(current + 1);
            case 5 -> blockEntity.setTimeLeft(current - 1);
            case 6 -> blockEntity.setActive(!blockEntity.isActive());
            case 7 -> {
                blockEntity.setActive(false);
                blockEntity.setTimeLeft(0);
            }
            default -> { return false; }
        }
        this.broadcastChanges();
        return true;
    }

    public int getMinutes() { return data.get(0); }
    public int getSeconds() { return data.get(1); }
    public int getTicks() { return data.get(2); }
    public boolean isActive() { return data.get(3) == 1; }

    @Override
    public boolean stillValid(Player player) {
        ContainerLevelAccess access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        return stillValid(access, player, ModBlocks.REDSTONE_TIMER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}