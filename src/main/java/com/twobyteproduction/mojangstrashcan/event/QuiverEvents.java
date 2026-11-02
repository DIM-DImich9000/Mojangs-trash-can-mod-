package com.twobyteproduction.mojangstrashcan.event;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import com.twobyteproduction.mojangstrashcan.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

@EventBusSubscriber(modid = Mojangstrashcan.MOD_ID)
public class QuiverEvents {

    // 1. Когда лук/арбалет ищет стрелу, мы даем ему стрелу из колчана и ставим метку
    @SubscribeEvent
    public static void onGetProjectile(LivingGetProjectileEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack quiver = player.getInventory().getItem(i);
            if (quiver.is(ModItems.QUIVER.get())) {

                CompoundTag customData = quiver.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                ListTag itemsTag = customData.getList("QuiverItems", Tag.TAG_COMPOUND);

                for (int slot = 0; slot < 3; slot++) {
                    if (slot < itemsTag.size()) {
                        CompoundTag itemTag = itemsTag.getCompound(slot);
                        if (!itemTag.isEmpty()) {
                            ItemStack arrow = ItemStack.parseOptional(player.registryAccess(), itemTag);
                            if (arrow.is(ItemTags.ARROWS)) {

                                ItemStack shot = arrow.copyWithCount(1);

                                // Ставим метку
                                CompoundTag arrowTag = new CompoundTag();
                                arrowTag.putBoolean("FromQuiver", true);
                                shot.set(DataComponents.CUSTOM_DATA, CustomData.of(arrowTag));

                                event.setProjectileItemStack(shot);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    // 2. Когда помеченная стрела вылетает в мир, удаляем её из колчана
    @SubscribeEvent
    public static void onArrowSpawn(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        Entity entity = event.getEntity();
        if (entity instanceof AbstractArrow arrow) {
            // ИСПРАВЛЕНИЕ: Используем getPickupItemStackOrigin() вместо getItemStack()
            ItemStack arrowStack = arrow.getPickupItemStackOrigin();
            if (arrowStack.isEmpty()) return;

            CompoundTag tag = arrowStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

            if (tag.getBoolean("FromQuiver")) {
                if (arrow.getOwner() instanceof Player player) {

                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack quiver = player.getInventory().getItem(i);
                        if (quiver.is(ModItems.QUIVER.get())) {
                            CompoundTag customData = quiver.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                            ListTag itemsTag = customData.getList("QuiverItems", Tag.TAG_COMPOUND);

                            for (int slot = 0; slot < 3; slot++) {
                                if (slot < itemsTag.size()) {
                                    CompoundTag itemTag = itemsTag.getCompound(slot);
                                    if (!itemTag.isEmpty()) {
                                        ItemStack stack = ItemStack.parseOptional(player.registryAccess(), itemTag);
                                        if (stack.is(ItemTags.ARROWS)) {

                                            stack.shrink(1);
                                            if (stack.isEmpty()) {
                                                itemsTag.set(slot, new CompoundTag());
                                            } else {
                                                itemsTag.set(slot, stack.save(player.registryAccess()));
                                            }

                                            customData.put("QuiverItems", itemsTag);
                                            quiver.set(DataComponents.CUSTOM_DATA, CustomData.of(customData));

                                            CompoundTag arrowTag = arrowStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                                            arrowTag.remove("FromQuiver");
                                            arrowStack.set(DataComponents.CUSTOM_DATA, CustomData.of(arrowTag));

                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}