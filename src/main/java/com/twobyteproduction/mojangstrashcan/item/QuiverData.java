package com.twobyteproduction.mojangstrashcan.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class QuiverData {
    public static ListTag getItems(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains("QuiverItems")) {
            ListTag list = new ListTag();
            list.add(new CompoundTag());
            list.add(new CompoundTag());
            list.add(new CompoundTag());
            tag.put("QuiverItems", list);
        }
        return tag.getList("QuiverItems", Tag.TAG_COMPOUND);
    }

    public static void setItems(ItemStack stack, ListTag items) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.put("QuiverItems", items);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}