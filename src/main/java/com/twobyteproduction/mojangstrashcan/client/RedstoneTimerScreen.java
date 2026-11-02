package com.twobyteproduction.mojangstrashcan.client;

import com.twobyteproduction.mojangstrashcan.menu.RedstoneTimerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class RedstoneTimerScreen extends AbstractContainerScreen<RedstoneTimerMenu> {
    private EditBox minInput, secInput, tickInput;

    public RedstoneTimerScreen(RedstoneTimerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 200;
    }

    @Override
    protected void init() {
        super.init();

        this.minInput = new EditBox(this.font, leftPos + 10, topPos + 50, 35, 16, Component.literal("Min"));
        this.minInput.setMaxLength(2);
        this.minInput.setValue("0");
        this.addRenderableWidget(this.minInput);

        this.secInput = new EditBox(this.font, leftPos + 50, topPos + 50, 35, 16, Component.literal("Sec"));
        this.secInput.setMaxLength(2);
        this.secInput.setValue("10");
        this.addRenderableWidget(this.secInput);

        this.tickInput = new EditBox(this.font, leftPos + 90, topPos + 50, 35, 16, Component.literal("Tick"));
        this.tickInput.setMaxLength(2);
        this.tickInput.setValue("0");
        this.addRenderableWidget(this.tickInput);

        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.set"), b -> {
            int m = parseSafe(minInput.getValue());
            int s = parseSafe(secInput.getValue());
            int t = parseSafe(tickInput.getValue());
            int totalTicks = m * 1200 + s * 20 + t;

            if (this.minecraft != null && this.minecraft.gameMode != null) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, totalTicks + 1000);
            }
        }).bounds(leftPos + 130, topPos + 50, 36, 16).build());

        int startX = this.leftPos + 10;
        int startY = this.topPos + 100;
        int w = 40;
        int h = 20;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.min_plus"), b -> sendButton(0)).bounds(startX, startY, w, h).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.min_minus"), b -> sendButton(1)).bounds(startX + w + 5, startY, w, h).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.sec_plus"), b -> sendButton(2)).bounds(startX, startY + 25, w, h).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.sec_minus"), b -> sendButton(3)).bounds(startX + w + 5, startY + 25, w, h).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.tick_plus"), b -> sendButton(4)).bounds(startX, startY + 50, w, h).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.tick_minus"), b -> sendButton(5)).bounds(startX + w + 5, startY + 50, w, h).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.toggle"), b -> sendButton(6)).bounds(startX + 100, startY + 15, 60, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.mojangstrashcan.redstone_timer.button.reset"), b -> sendButton(7)).bounds(startX + 100, startY + 45, 60, 20).build());

        this.setFocused(null);
    }

    // ИСПРАВЛЕНИЕ: Отключаем стандартную надпись "Инвентарь"
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Пусто, чтобы ничего не рисовалось поверх
    }

    private int parseSafe(String val) {
        try { return Integer.parseInt(val); } catch (NumberFormatException e) { return 0; }
    }

    private void sendButton(int id) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFFC6C6C6);
        guiGraphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos, 0xFF000000);
        guiGraphics.fill(leftPos - 1, topPos - 1, leftPos, topPos + imageHeight + 1, 0xFF000000);
        guiGraphics.fill(leftPos - 1, topPos + imageHeight, leftPos + imageWidth + 1, topPos + imageHeight + 1, 0xFFFFFFFF);
        guiGraphics.fill(leftPos + imageWidth, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, 0xFFFFFFFF);

        int boxX = leftPos + 8;
        int boxY = topPos + 20;
        int boxW = imageWidth - 16;
        int boxH = 24;

        guiGraphics.fill(boxX, boxY, boxX + boxW, boxY + boxH, 0xFF373737);
        guiGraphics.fill(boxX, boxY, boxX + boxW - 1, boxY + 1, 0xFF8B8B8B);
        guiGraphics.fill(boxX, boxY, boxX + 1, boxY + boxH - 1, 0xFF8B8B8B);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        // Заголовок
        guiGraphics.drawString(this.font, this.title, leftPos + 8, topPos + 6, 0x404040, false);

        // Время
        int minutes = menu.getMinutes();
        int seconds = menu.getSeconds();
        int ticks = menu.getTicks();
        String timeString = String.format("%02d:%02d.%02d", minutes, seconds, ticks);
        Component timeText = Component.translatable("gui.mojangstrashcan.redstone_timer.label.time", timeString);
        guiGraphics.drawString(this.font, timeText, leftPos + 12, topPos + 27, 0xFFFFFF, false);

        // Подписи под полями
        guiGraphics.drawString(this.font, Component.translatable("gui.mojangstrashcan.redstone_timer.label.min"), leftPos + 18, topPos + 70, 0x404040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.mojangstrashcan.redstone_timer.label.sec"), leftPos + 58, topPos + 70, 0x404040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.mojangstrashcan.redstone_timer.label.tick"), leftPos + 98, topPos + 70, 0x404040, false);
    }
}