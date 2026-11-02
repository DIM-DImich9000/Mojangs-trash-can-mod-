package com.twobyteproduction.mojangstrashcan.item;

import com.twobyteproduction.mojangstrashcan.menu.QuiverMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuiverItem extends Item {
    public QuiverItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, p) -> new QuiverMenu(containerId, inventory, stack),
                    Component.translatable("item.mojangstrashcan.quiver")
            ), buf -> ItemStack.STREAM_CODEC.encode(buf, stack));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        ListTag itemsTag = QuiverData.getItems(stack);
        Map<String, Integer> arrowCounts = new LinkedHashMap<>();

        for (int i = 0; i < itemsTag.size(); i++) {
            CompoundTag itemTag = itemsTag.getCompound(i);
            if (!itemTag.isEmpty()) {
                ItemStack item = ItemStack.parseOptional(context.registries(), itemTag);
                if (!item.isEmpty()) {
                    String name = item.getItem().getDescriptionId();
                    arrowCounts.put(name, arrowCounts.getOrDefault(name, 0) + item.getCount());
                }
            }
        }

        if (arrowCounts.isEmpty()) {
            tooltipComponents.add(Component.translatable("item.mojangstrashcan.quiver.tooltip.empty").withStyle(ChatFormatting.GRAY));
        } else {
            int total = 0;
            for (Map.Entry<String, Integer> entry : arrowCounts.entrySet()) {
                Component typeName = Component.translatable(entry.getKey());
                tooltipComponents.add(Component.translatable("item.mojangstrashcan.quiver.tooltip.entry", entry.getValue(), typeName).withStyle(ChatFormatting.GREEN));
                total += entry.getValue();
            }
            tooltipComponents.add(Component.translatable("item.mojangstrashcan.quiver.tooltip.total", total).withStyle(ChatFormatting.DARK_GREEN));
        }
    }
}