package com.twobyteproduction.mojangstrashcan.event;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import com.twobyteproduction.mojangstrashcan.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Mojangstrashcan.MOD_ID)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Задержка в 1 тик, чтобы игра успела загрузить инвентарь
            player.server.execute(() -> {
                CompoundTag data = player.getPersistentData();
                if (!data.getBoolean("ReceivedGuideBook")) {
                    ItemStack book = new ItemStack(ModItems.GUIDE_BOOK.get());
                    if (!player.getInventory().add(book)) {
                        player.drop(book, false);
                    }
                    data.putBoolean("ReceivedGuideBook", true);
                }
            });
        }
    }
}