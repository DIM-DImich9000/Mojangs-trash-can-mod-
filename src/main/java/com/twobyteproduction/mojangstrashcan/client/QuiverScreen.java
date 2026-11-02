package com.twobyteproduction.mojangstrashcan.client;

import com.twobyteproduction.mojangstrashcan.menu.QuiverMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class QuiverScreen extends AbstractContainerScreen<QuiverMenu> {
    public QuiverScreen(QuiverMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = 72;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // Фон (Коричневый)
        guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF8B5A2B);

        // Рамка окна (Черная)
        guiGraphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos, 0xFF000000);
        guiGraphics.fill(leftPos - 1, topPos - 1, leftPos, topPos + imageHeight + 1, 0xFF000000);
        guiGraphics.fill(leftPos - 1, topPos + imageHeight, leftPos + imageWidth + 1, topPos + imageHeight + 1, 0xFF000000);
        guiGraphics.fill(leftPos + imageWidth, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, 0xFF000000);

        // Отрисовка фонов для слотов (Темно-коричневые)
        drawSlots(guiGraphics, 61, 19, 3, 1);   // Слоты колчана
        drawSlots(guiGraphics, 7, 83, 9, 3);    // Инвентарь игрока
        drawSlots(guiGraphics, 7, 141, 9, 1);   // Хотбар игрока
    }

    private void drawSlots(GuiGraphics g, int x, int y, int cols, int rows) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int slotX = leftPos + x + col * 18;
                int slotY = topPos + y + row * 18;
                // Темно-коричневый фон
                g.fill(slotX, slotY, slotX + 18, slotY + 18, 0xFF3E2723);
                // Светлая обводка (сверху и слева)
                g.fill(slotX, slotY, slotX + 18, slotY + 1, 0xFF5C3A21);
                g.fill(slotX, slotY, slotX + 1, slotY + 18, 0xFF5C3A21);
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // --- НУМЕРАЦИЯ СЛОТОВ КОЛЧАНА (1, 2, 3) ПОД СЛОТАМИ ---
        for (int i = 0; i < 3; i++) {
            int x = leftPos + 61 + i * 18;
            int y = topPos + 19;
            // Отрисовка цифры по центру под слотом (y + 20)
            guiGraphics.drawString(this.font, String.valueOf(i + 1), x + 6, y + 20, 0xFFFFFF, false);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}