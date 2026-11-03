package com.twobyteproduction.mojangstrashcan.client;

import com.twobyteproduction.mojangstrashcan.block.ModBlocks;
import com.twobyteproduction.mojangstrashcan.item.ModItems;
import com.twobyteproduction.mojangstrashcan.menu.GuideBookMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuideBookScreen extends AbstractContainerScreen<GuideBookMenu> {
    public GuideBookScreen(GuideBookMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 260;
        this.imageHeight = 210;
        this.inventoryLabelX = -1000;
        this.inventoryLabelY = -1000;
    }

    @Override
    protected void init() {
        super.init();
        this.setFocused(null);
    }

    // Отключаем стандартную отрисовку названий
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {}

    private void sendButton(int id) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int category = menu.getCurrentCategory();

            // Кнопка "Окей" на титульном листе
            if (category == 3) {
                int btnX = leftPos + (imageWidth / 2) - 30;
                int btnY = topPos + 120;
                if (mouseX >= btnX && mouseX <= btnX + 60 && mouseY >= btnY && mouseY <= btnY + 20) {
                    sendButton(20);
                    return true;
                }
            }

            // Клики по закладкам
            if (category != 3 && mouseX >= leftPos - 30 && mouseX <= leftPos + 5) {
                if (mouseY >= topPos + 15 && mouseY <= topPos + 55) { sendButton(0); return true; }   // Минералы
                if (mouseY >= topPos + 65 && mouseY <= topPos + 105) { sendButton(1); return true; }  // Механизмы
                if (mouseY >= topPos + 115 && mouseY <= topPos + 155) { sendButton(2); return true; } // Прочее
                if (mouseY >= topPos + 165 && mouseY <= topPos + 205) { sendButton(4); return true; } // Предметы
            }

            // Клики по стрелкам (влево/вправо)
            if (category != 3) {
                if (mouseX >= leftPos + 40 && mouseX <= leftPos + 70 && mouseY >= topPos + imageHeight - 30 && mouseY <= topPos + imageHeight - 10) {
                    sendButton(10); return true;
                }
                if (mouseX >= leftPos + imageWidth - 70 && mouseX <= leftPos + imageWidth - 40 && mouseY >= topPos + imageHeight - 30 && mouseY <= topPos + imageHeight - 10) {
                    sendButton(11); return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int category = menu.getCurrentCategory();

        // 1. Отрисовка закладок (скрываем на титульном листе)
        if (category != 3) {
            drawTab(guiGraphics, leftPos - 30, topPos + 15, category == 0, 0xFF8B0000, 0xFF550000, new ItemStack(ModItems.RUBY.get()));
            drawTab(guiGraphics, leftPos - 30, topPos + 65, category == 1, 0xFF555555, 0xFF222222, new ItemStack(Items.REDSTONE));
            drawTab(guiGraphics, leftPos - 30, topPos + 115, category == 2, 0xFF8B4513, 0xFF5C2E0B, new ItemStack(Items.DEAD_BUSH));
            drawTab(guiGraphics, leftPos - 30, topPos + 165, category == 4, 0xFF8A2BE2, 0xFF4B0082, new ItemStack(Items.AMETHYST_SHARD));
        }

        // 2. Обложка книги
        guiGraphics.fill(leftPos + 5, topPos + 5, leftPos + imageWidth + 5, topPos + imageHeight + 5, 0x55000000); // Тень
        guiGraphics.fill(leftPos - 5, topPos - 5, leftPos + imageWidth + 5, topPos + imageHeight + 5, 0xFF4F351D); // Кожа

        // 3. Корешок (только если не титульный лист)
        if (category != 3) {
            guiGraphics.fill(leftPos + (imageWidth / 2) - 3, topPos - 5, leftPos + (imageWidth / 2) + 3, topPos + imageHeight + 5, 0xFF3A2410);
        }

        // 4. Золотые уголки
        int gold = 0xFFD4AF37;
        int cornerSize = 10;
        guiGraphics.fill(leftPos - 5, topPos - 5, leftPos + cornerSize, topPos - 2, gold);
        guiGraphics.fill(leftPos - 5, topPos - 5, leftPos - 2, topPos + cornerSize, gold);
        guiGraphics.fill(leftPos + imageWidth - cornerSize, topPos - 5, leftPos + imageWidth + 5, topPos - 2, gold);
        guiGraphics.fill(leftPos + imageWidth + 2, topPos - 5, leftPos + imageWidth + 5, topPos + cornerSize, gold);
        guiGraphics.fill(leftPos - 5, topPos + imageHeight + 2, leftPos + cornerSize, topPos + imageHeight + 5, gold);
        guiGraphics.fill(leftPos - 5, topPos + imageHeight - cornerSize, leftPos - 2, topPos + imageHeight + 5, gold);
        guiGraphics.fill(leftPos + imageWidth - cornerSize, topPos + imageHeight + 2, leftPos + imageWidth + 5, topPos + imageHeight + 5, gold);
        guiGraphics.fill(leftPos + imageWidth + 2, topPos + imageHeight - cornerSize, leftPos + imageWidth + 5, topPos + imageHeight + 5, gold);

        // 5. Страницы (пергамент)
        int pageColor = 0xFFF6E8D5;
        if (category == 3) {
            guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, pageColor);
        } else {
            guiGraphics.fill(leftPos, topPos, leftPos + (imageWidth / 2) - 5, topPos + imageHeight, pageColor);
            guiGraphics.fill(leftPos + (imageWidth / 2) + 5, topPos, leftPos + imageWidth, topPos + imageHeight, pageColor);
        }
    }

    private void drawTab(GuiGraphics g, int x, int y, boolean isActive, int activeColor, int inactiveColor, ItemStack icon) {
        int color = isActive ? activeColor : inactiveColor;
        int width = 35;
        int height = 40;

        g.fill(x, y, x + width, y + height, color);
        g.fill(x, y + height, x + width, y + height + 1, 0xFF000000);
        g.fill(x + width, y, x + width + 1, y + height, 0xFF000000);
        g.fill(x + 12, y + height, x + 23, y + height + 3, 0x00000000); // Вырез закладки
        g.renderItem(icon, x + 9, y + 12);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        int category = menu.getCurrentCategory();
        int page = menu.getCurrentPage();
        int textColor = 0x3F2E1E;
        int yOffset = 30;

        // --- Отрисовка контента ---
        if (category == 0) { // Минералы
            if (page == 0) renderRubyOre(guiGraphics, textColor, yOffset);
            else renderRubyBlock(guiGraphics, textColor, yOffset);
        } else if (category == 1) { // Механизмы
            if (page == 0) renderTimerRecipe(guiGraphics, textColor, yOffset);
        } else if (category == 2) { // Прочее
            if (page == 0) renderBookRecipe(guiGraphics, textColor, yOffset);
            else if (page == 1) renderHelmetPage(guiGraphics, textColor, yOffset);
        } else if (category == 3) { // Титульный лист
            renderBigTitlePage(guiGraphics);
        } else if (category == 4) { // Предметы
            if (page == 0) renderQuiverPage(guiGraphics, textColor, yOffset);
            else if (page == 1) renderExplosiveArrowPage(guiGraphics, textColor, yOffset);
        }

        // --- Номера страниц и стрелки (только для обычных вкладок) ---
        if (category != 3) {
            int leftPageNum = (page * 2) + 1;
            int rightPageNum = (page * 2) + 2;

            guiGraphics.drawString(this.font, String.valueOf(leftPageNum), leftPos + 20, topPos + imageHeight - 20, 0x8B8B8B, false);
            guiGraphics.drawString(this.font, String.valueOf(rightPageNum), leftPos + imageWidth - 25, topPos + imageHeight - 20, 0x8B8B8B, false);

            // Проверяем, есть ли следующая/предыдущая страница
            boolean hasNextPage = (category == 0 && page < 1) || (category == 2 && page < 1) || (category == 4 && page < 1);
            boolean hasPrevPage = page > 0;

            if (hasPrevPage) {
                guiGraphics.drawString(this.font, "<---", leftPos + 46, topPos + imageHeight - 21, 0xFF000000, false);
                guiGraphics.drawString(this.font, "<---", leftPos + 45, topPos + imageHeight - 22, 0xFF3A2410, false);
            }
            if (hasNextPage) {
                guiGraphics.drawString(this.font, "--->", leftPos + imageWidth - 76, topPos + imageHeight - 21, 0xFF000000, false);
                guiGraphics.drawString(this.font, "--->", leftPos + imageWidth - 75, topPos + imageHeight - 22, 0xFF3A2410, false);
            }
        }

        // --- Подсказки закладок ---
        if (category != 3 && mouseX >= leftPos - 30 && mouseX <= leftPos + 5) {
            if (mouseY >= topPos + 15 && mouseY <= topPos + 55) guiGraphics.renderTooltip(this.font, Component.translatable("gui.mojangstrashcan.guide_book.tab.minerals"), mouseX, mouseY);
            else if (mouseY >= topPos + 65 && mouseY <= topPos + 105) guiGraphics.renderTooltip(this.font, Component.translatable("gui.mojangstrashcan.guide_book.tab.mechanisms"), mouseX, mouseY);
            else if (mouseY >= topPos + 115 && mouseY <= topPos + 155) guiGraphics.renderTooltip(this.font, Component.translatable("gui.mojangstrashcan.guide_book.tab.misc"), mouseX, mouseY);
            else if (mouseY >= topPos + 165 && mouseY <= topPos + 205) guiGraphics.renderTooltip(this.font, Component.translatable("gui.mojangstrashcan.guide_book.tab.items"), mouseX, mouseY);
        }
    }

    // ==========================================
    // --- МЕТОДЫ ОТРИСОВКИ СТРАНИЦ ---
    // ==========================================

    private void renderBigTitlePage(GuiGraphics g) {
        int centerX = leftPos + (imageWidth / 2);

        Component text1 = Component.translatable("gui.mojangstrashcan.guide_book.title_page.left");
        g.drawString(this.font, text1, centerX - (font.width(text1) / 2), topPos + 60, 0x8B0000, false);

        Component text2 = Component.translatable("gui.mojangstrashcan.guide_book.title_page.right");
        g.drawString(this.font, text2, centerX - (font.width(text2) / 2), topPos + 80, 0x8B0000, false);

        int btnX = centerX - 30;
        int btnY = topPos + 120;

        // Отрисовка кнопки "Окей"
        g.fill(btnX, btnY, btnX + 60, btnY + 20, 0xFF8B8B8B);
        g.fill(btnX, btnY, btnX + 60, btnY + 1, 0xFF373737);
        g.fill(btnX, btnY, btnX + 1, btnY + 20, 0xFF373737);
        g.fill(btnX, btnY + 19, btnX + 60, btnY + 20, 0xFFFFFFFF);
        g.fill(btnX + 59, btnY, btnX + 60, btnY + 20, 0xFFFFFFFF);

        Component btnText = Component.literal("Окей");
        g.drawString(this.font, btnText, btnX + 30 - (font.width(btnText) / 2), btnY + 6, 0xFFFFFF, false);
    }

    private void renderQuiverPage(GuiGraphics g, int textColor, int yOffset) {
        int leftCenter = leftPos + 62;
        int rightCenter = leftPos + 192;
        int y = topPos + yOffset;

        ItemStack[] items = new ItemStack[]{
                new ItemStack(Items.LEATHER), new ItemStack(Items.STRING), new ItemStack(Items.LEATHER),
                new ItemStack(Items.LEATHER), null, new ItemStack(Items.LEATHER),
                new ItemStack(Items.LEATHER), new ItemStack(Items.LEATHER), new ItemStack(Items.LEATHER)
        };
        drawCraftingGrid(g, leftCenter - 35, y + 15, items, new ItemStack(ModItems.QUIVER.get()));

        Component title = Component.translatable("gui.mojangstrashcan.guide_book.recipe_title.quiver");
        g.drawString(this.font, title, rightCenter - (font.width(title) / 2), y, 0x8B0000, false);
        drawCenteredWrappedText(g, Component.translatable("gui.mojangstrashcan.guide_book.recipe_desc.quiver_usage"), rightCenter, y + 20, 95, textColor);
    }

    private void renderExplosiveArrowPage(GuiGraphics g, int textColor, int yOffset) {
        int leftCenter = leftPos + 62;
        int rightCenter = leftPos + 192;
        int y = topPos + yOffset;

        // Бесформенный крафт (ингредиенты просто лежат в сетке)
        ItemStack[] items = new ItemStack[]{
                new ItemStack(Items.FLINT), new ItemStack(Items.STICK), new ItemStack(Items.FEATHER),
                new ItemStack(Items.PAPER), new ItemStack(Items.GUNPOWDER), null,
                null, null, null
        };
        drawCraftingGrid(g, leftCenter - 35, y + 15, items, new ItemStack(ModItems.EXPLOSIVE_ARROW.get()));

        // Надпись "Бесформенный"
        Component shapeless = Component.translatable("gui.mojangstrashcan.guide_book.shapeless").withStyle(ChatFormatting.ITALIC);
        g.drawString(this.font, shapeless, leftCenter - (font.width(shapeless) / 2), y + 80, 0x8B8B8B, false);

        Component title = Component.translatable("gui.mojangstrashcan.guide_book.recipe_title.arrow");
        g.drawString(this.font, title, rightCenter - (font.width(title) / 2), y, 0x8B0000, false);
        g.renderItem(new ItemStack(ModItems.EXPLOSIVE_ARROW.get()), rightCenter - 8, y + 15);
        drawCenteredWrappedText(g, Component.translatable("gui.mojangstrashcan.guide_book.recipe_desc.arrow"), rightCenter, y + 45, 95, textColor);
    }

    private void renderHelmetPage(GuiGraphics g, int textColor, int yOffset) {
        int leftCenter = leftPos + 62;
        int rightCenter = leftPos + 192;
        int y = topPos + yOffset;

        ItemStack[] items = new ItemStack[]{
                null, new ItemStack(Items.GLASS), null,
                new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_HELMET), new ItemStack(Items.IRON_INGOT),
                null, new ItemStack(Items.LEATHER), null
        };
        drawCraftingGrid(g, leftCenter - 35, y + 15, items, new ItemStack(ModItems.LIGHT_HELMET.get()));

        Component title = Component.translatable("gui.mojangstrashcan.guide_book.recipe_title.helmet");
        g.drawString(this.font, title, rightCenter - (font.width(title) / 2), y, 0x8B0000, false);
        g.renderItem(new ItemStack(ModItems.LIGHT_HELMET.get()), rightCenter - 8, y + 15);
        drawCenteredWrappedText(g, Component.translatable("gui.mojangstrashcan.guide_book.recipe_desc.helmet"), rightCenter, y + 45, 95, textColor);
    }

    private void renderBookRecipe(GuiGraphics g, int textColor, int yOffset) {
        int leftCenter = leftPos + 62;
        int rightCenter = leftPos + 192;
        int y = topPos + yOffset;

        ItemStack[] items = new ItemStack[]{
                null, null, null,
                null, new ItemStack(Items.BOOK), null,
                null, new ItemStack(Items.COPPER_INGOT), null
        };
        drawCraftingGrid(g, leftCenter - 35, y + 15, items, new ItemStack(ModItems.GUIDE_BOOK.get()));

        // Надпись "Бесформенный"
        Component shapeless = Component.translatable("gui.mojangstrashcan.guide_book.shapeless").withStyle(ChatFormatting.ITALIC);
        g.drawString(this.font, shapeless, leftCenter - (font.width(shapeless) / 2), y + 80, 0x8B8B8B, false);

        Component title = Component.translatable("gui.mojangstrashcan.guide_book.recipe_title.book");
        g.drawString(this.font, title, rightCenter - (font.width(title) / 2), y, 0x8B0000, false);
        drawCenteredWrappedText(g, Component.translatable("gui.mojangstrashcan.guide_book.recipe_desc.book"), rightCenter, y + 20, 95, textColor);
    }

    private void renderRubyOre(GuiGraphics g, int textColor, int yOffset) {
        int leftCenter = leftPos + 62;
        int rightCenter = leftPos + 192;
        int y = topPos + yOffset;

        Component titleOre = Component.translatable("gui.mojangstrashcan.guide_book.recipe_title.ruby_ore");
        g.drawString(this.font, titleOre, leftCenter - (font.width(titleOre) / 2), y, 0x8B0000, false);
        g.renderItem(new ItemStack(ModBlocks.RUBY_ORE.get()), leftCenter - 8, y + 15);
        drawCenteredWrappedText(g, Component.translatable("gui.mojangstrashcan.guide_book.recipe_desc.ruby_ore"), leftCenter, y + 45, 95, textColor);

        Component titleRuby = Component.translatable("gui.mojangstrashcan.guide_book.recipe_title.ruby");
        g.drawString(this.font, titleRuby, rightCenter - (font.width(titleRuby) / 2), y, 0x8B0000, false);
        g.renderItem(new ItemStack(ModItems.RUBY.get()), rightCenter - 8, y + 15);
        drawCenteredWrappedText(g, Component.translatable("gui.mojangstrashcan.guide_book.recipe_desc.ruby"), rightCenter, y + 45, 95, textColor);
    }

    private void renderRubyBlock(GuiGraphics g, int textColor, int yOffset) {
        int leftCenter = leftPos + 62;
        int rightCenter = leftPos + 192;
        int y = topPos + yOffset;

        ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < 9; i++) items[i] = new ItemStack(ModItems.RUBY.get());
        drawCraftingGrid(g, leftCenter - 35, y + 15, items, new ItemStack(ModBlocks.RUBY_BLOCK.get()));

        Component title = Component.translatable("gui.mojangstrashcan.guide_book.recipe_title.ruby_block");
        g.drawString(this.font, title, rightCenter - (font.width(title) / 2), y, 0x8B0000, false);
        drawCenteredWrappedText(g, Component.translatable("gui.mojangstrashcan.guide_book.recipe_desc.ruby_block"), rightCenter, y + 20, 95, textColor);
    }

    private void renderTimerRecipe(GuiGraphics g, int textColor, int yOffset) {
        int leftCenter = leftPos + 62;
        int rightCenter = leftPos + 192;
        int y = topPos + yOffset;

        ItemStack[] items = new ItemStack[]{
                new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.CLOCK), new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.REDSTONE_TORCH), new ItemStack(Items.GOLD_INGOT)
        };
        drawCraftingGrid(g, leftCenter - 35, y + 15, items, new ItemStack(ModBlocks.REDSTONE_TIMER.get()));

        Component title = Component.translatable("gui.mojangstrashcan.guide_book.recipe_title.timer");
        g.drawString(this.font, title, rightCenter - (font.width(title) / 2), y, 0x8B0000, false);
        drawCenteredWrappedText(g, Component.translatable("gui.mojangstrashcan.guide_book.recipe_desc.timer"), rightCenter, y + 20, 95, textColor);
    }

    // ==========================================
    // --- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ---
    // ==========================================

    private void drawCenteredWrappedText(GuiGraphics g, Component text, int centerX, int y, int width, int color) {
        List<FormattedCharSequence> lines = this.font.split(text, width);
        for (int i = 0; i < lines.size(); i++) {
            int lineWidth = this.font.width(lines.get(i));
            g.drawString(this.font, lines.get(i), centerX - (lineWidth / 2), y + (i * 12), color, false);
        }
    }

    private void drawCraftingGrid(GuiGraphics g, int x, int y, ItemStack[] items, ItemStack result) {
        int slotSize = 18;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slotX = x + (col * slotSize);
                int slotY = y + (row * slotSize);
                // Фон слота
                g.fill(slotX, slotY, slotX + 16, slotY + 16, 0xFF8B8B8B);
                // Тень
                g.fill(slotX, slotY, slotX + 16, slotY + 1, 0xFF373737);
                g.fill(slotX, slotY, slotX + 1, slotY + 16, 0xFF373737);
                // Блик
                g.fill(slotX, slotY + 15, slotX + 16, slotY + 16, 0xFFFFFFFF);
                g.fill(slotX + 15, slotY, slotX + 16, slotY + 16, 0xFFFFFFFF);

                int index = row * 3 + col;
                if (items[index] != null) g.renderItem(items[index], slotX, slotY);
            }
        }

        // Стрелочка
        int arrowX = x + (slotSize * 3) + 8;
        int arrowY = y + (slotSize * 3) / 2 - 4;
        g.fill(arrowX, arrowY + 2, arrowX + 10, arrowY + 6, 0xFF373737);
        g.fill(arrowX + 6, arrowY, arrowX + 10, arrowY + 8, 0xFF373737);

        // Слот результата
        int resultX = arrowX + 14;
        int resultY = y + (slotSize * 3) / 2 - 8;
        g.fill(resultX, resultY, resultX + 16, resultY + 16, 0xFF8B8B8B);
        g.fill(resultX, resultY, resultX + 16, resultY + 1, 0xFF373737);
        g.fill(resultX, resultY, resultX + 1, resultY + 16, 0xFF373737);
        g.fill(resultX, resultY + 15, resultX + 16, resultY + 16, 0xFFFFFFFF);
        g.fill(resultX + 15, resultY, resultX + 16, resultY + 16, 0xFFFFFFFF);
        if (result != null && !result.isEmpty()) g.renderItem(result, resultX, resultY);
    }
}