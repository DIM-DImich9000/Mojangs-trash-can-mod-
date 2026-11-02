package com.twobyteproduction.mojangstrashcan.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class GuideBookMenu extends AbstractContainerMenu {
    private final ContainerData data;
    private final Player player;

    // Конструктор для сервера
    public GuideBookMenu(int containerId, Inventory playerInventory, ItemStack bookStack, int startTab, int startPage) {
        super(ModMenus.GUIDE_BOOK_MENU.get(), containerId);
        this.player = playerInventory.player;
        this.data = new SimpleContainerData(2);
        this.data.set(0, startTab);
        this.data.set(1, startPage);
        this.addDataSlots(data);
    }

    // Конструктор для клиента (сеть)
    public GuideBookMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ItemStack.EMPTY, 2, 0);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        // ID 20 - кнопка "Окей" на титульном листе
        if (id == 20) {
            this.data.set(0, 2); // Перекидываем на "Прочее"
            this.data.set(1, 0);
            saveState();
            return true;
        }

        // Переключение вкладок (0, 1, 2, 4). Вкладка 3 - скрытая (титул)
        if (id >= 0 && id <= 4 && id != 3) {
            this.data.set(0, id);
            this.data.set(1, 0);
            saveState();
            return true;
        }
        if (id == 10) { // Стрелка влево
            int page = data.get(1);
            if (page > 0) {
                data.set(1, page - 1);
                saveState();
            }
            return true;
        }
        if (id == 11) { // Стрелка вправо
            int page = data.get(1);
            if (page < 1) {
                data.set(1, page + 1);
                saveState();
            }
            return true;
        }
        return false;
    }

    private void saveState() {
        player.getPersistentData().putInt("GuideBookTab", data.get(0));
        player.getPersistentData().putInt("GuideBookPage", data.get(1));
    }

    public int getCurrentCategory() { return data.get(0); }
    public int getCurrentPage() { return data.get(1); }

    @Override
    public boolean stillValid(Player player) { return true; }

    @Override
    public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }
}