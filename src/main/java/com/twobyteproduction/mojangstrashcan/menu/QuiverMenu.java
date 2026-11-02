package com.twobyteproduction.mojangstrashcan.menu;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

public class QuiverMenu extends AbstractContainerMenu {
    private final ItemStack quiverStack;
    private final SimpleContainer container;
    private final Player player;

    public QuiverMenu(int containerId, Inventory playerInventory, ItemStack quiverStack) {
        super(ModMenus.QUIVER_MENU.get(), containerId);
        this.quiverStack = quiverStack;
        this.player = playerInventory.player;

        this.container = new SimpleContainer(3) {
            @Override
            public boolean canPlaceItem(int slot, ItemStack stack) {
                return stack.is(Items.ARROW) || stack.is(Items.SPECTRAL_ARROW) || stack.is(Items.TIPPED_ARROW);
            }

            @Override
            public void setChanged() {
                super.setChanged();
                saveToStack();
            }
        };

        // Загружаем данные из CustomData
        CompoundTag customData = quiverStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        ListTag itemsTag = customData.getList("QuiverItems", Tag.TAG_COMPOUND);

        for (int i = 0; i < 3; i++) {
            if (i < itemsTag.size()) {
                CompoundTag itemTag = itemsTag.getCompound(i);
                if (!itemTag.isEmpty()) {
                    this.container.setItem(i, ItemStack.parseOptional(playerInventory.player.registryAccess(), itemTag));
                }
            }
        }

        for (int i = 0; i < 3; i++) this.addSlot(new Slot(this.container, i, 62 + i * 18, 20));
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++) this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
    }

    public QuiverMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, ItemStack.STREAM_CODEC.decode(extraData));
    }

    private void saveToStack() {
        ListTag itemsTag = new ListTag();
        for (int i = 0; i < 3; i++) {
            ItemStack stack = this.container.getItem(i);
            if (stack.isEmpty()) {
                itemsTag.add(new CompoundTag()); // Сохраняем пустой слот
            } else {
                itemsTag.add(stack.save(player.registryAccess()));
            }
        }
        CompoundTag customData = quiverStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        customData.put("QuiverItems", itemsTag);
        quiverStack.set(DataComponents.CUSTOM_DATA, CustomData.of(customData));

        // Обновляем инвентарь на сервере
        if (!player.level().isClientSide) {
            player.getInventory().setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) { return true; }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 3) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (itemstack1.is(Items.ARROW) || itemstack1.is(Items.SPECTRAL_ARROW) || itemstack1.is(Items.TIPPED_ARROW)) {
                if (!this.moveItemStackTo(itemstack1, 0, 3, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return itemstack;
    }
}