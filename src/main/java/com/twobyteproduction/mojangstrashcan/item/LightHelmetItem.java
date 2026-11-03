package com.twobyteproduction.mojangstrashcan.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightHelmetItem extends ArmorItem {
    // 10 минут (600 сек * 20 тиков)
    public static final int MAX_CHARGE = 12000;
    // 1 пыль = 100% заряда
    public static final int CHARGE_PER_DUST = 12000;

    public LightHelmetItem(Properties properties) {
        super(ModArmorMaterials.LIGHT_HELMET_MATERIAL, Type.HELMET, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack offhand = player.getOffhandItem();

        if (offhand.is(net.minecraft.world.item.Items.GLOWSTONE_DUST)) {
            if (!level.isClientSide) {
                CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                int charge = tag.getInt("Charge");

                if (charge < MAX_CHARGE) {
                    // ИСПРАВЛЕНИЕ: 1 пыль сразу заполняет до 100%
                    tag.putInt("Charge", MAX_CHARGE);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    offhand.shrink(1);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int charge = tag.getInt("Charge");

        // ИСПРАВЛЕНИЕ: Показываем только статус, без процентов
        if (charge > 0) {
            tooltip.add(Component.translatable("item.mojangstrashcan.light_helmet.tooltip.charged").withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add(Component.translatable("item.mojangstrashcan.light_helmet.tooltip.empty").withStyle(ChatFormatting.GRAY));
        }
    }
}