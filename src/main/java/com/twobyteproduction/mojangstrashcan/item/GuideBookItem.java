package com.twobyteproduction.mojangstrashcan.item;

import com.twobyteproduction.mojangstrashcan.menu.GuideBookMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GuideBookItem extends Item {
    public GuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {

            CompoundTag data = player.getPersistentData();
            int startTab = 2;
            int startPage = 0;

            if (!data.getBoolean("GuideBookHasRead")) {
                startTab = 3;
                startPage = 0;
                data.putBoolean("GuideBookHasRead", true);
            } else {
                startTab = data.getInt("GuideBookTab");
                startPage = data.getInt("GuideBookPage");
            }

            final int tab = startTab;
            final int page = startPage;

            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, p) -> new GuideBookMenu(containerId, inventory, stack, tab, page),
                    Component.translatable("item.mojangstrashcan.guide_book")
            ));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}