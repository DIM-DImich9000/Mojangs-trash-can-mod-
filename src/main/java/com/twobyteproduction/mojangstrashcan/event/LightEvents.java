package com.twobyteproduction.mojangstrashcan.event;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import com.twobyteproduction.mojangstrashcan.item.LightHelmetItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Mojangstrashcan.MOD_ID)
public class LightEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        CompoundTag playerData = player.getPersistentData();

        if (!(helmet.getItem() instanceof LightHelmetItem)) {
            if (playerData.contains("HelmetLightX")) {
                BlockPos lastLightPos = new BlockPos(
                        playerData.getInt("HelmetLightX"),
                        playerData.getInt("HelmetLightY"),
                        playerData.getInt("HelmetLightZ")
                );
                if (player.level().getBlockState(lastLightPos).is(Blocks.LIGHT)) {
                    player.level().setBlockAndUpdate(lastLightPos, Blocks.AIR.defaultBlockState());
                }
                playerData.remove("HelmetLightX");
                playerData.remove("HelmetLightY");
                playerData.remove("HelmetLightZ");
            }
            return;
        }

        // Инициализация заряда в данных игрока
        if (!playerData.contains("HelmetCharge")) {
            CompoundTag tag = helmet.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            playerData.putInt("HelmetCharge", tag.getInt("Charge"));
        }

        int charge = playerData.getInt("HelmetCharge");

        if (charge > 0) {
            if (player.level().getGameTime() % 20 == 0) {
                int rts = player.level().getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
                if (rts > 0) {
                    int acc = playerData.getInt("HelmetDrainAcc");
                    int total = acc + (rts * 20);
                    int drain = total / 3;
                    playerData.putInt("HelmetDrainAcc", total % 3);

                    if (drain > 0) {
                        charge = Math.max(0, charge - drain);
                        playerData.putInt("HelmetCharge", charge);

                        if (charge == 0) {
                            CompoundTag tag = helmet.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                            tag.putInt("Charge", 0);
                            helmet.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        }
                    }
                }
            }

            BlockPos currentPos = player.blockPosition();
            BlockPos lastLightPos = null;
            if (playerData.contains("HelmetLightX")) {
                lastLightPos = new BlockPos(
                        playerData.getInt("HelmetLightX"),
                        playerData.getInt("HelmetLightY"),
                        playerData.getInt("HelmetLightZ")
                );
            }
            if (lastLightPos != null && !lastLightPos.equals(currentPos)) {
                if (player.level().getBlockState(lastLightPos).is(Blocks.LIGHT)) {
                    player.level().setBlockAndUpdate(lastLightPos, Blocks.AIR.defaultBlockState());
                }
            }
            if (player.level().getBlockState(currentPos).isAir()) {
                player.level().setBlockAndUpdate(currentPos, Blocks.LIGHT.defaultBlockState().setValue(BlockStateProperties.LEVEL, 10));
            }
            playerData.putInt("HelmetLightX", currentPos.getX());
            playerData.putInt("HelmetLightY", currentPos.getY());
            playerData.putInt("HelmetLightZ", currentPos.getZ());
        } else {
            if (playerData.contains("HelmetLightX")) {
                BlockPos lastLightPos = new BlockPos(
                        playerData.getInt("HelmetLightX"),
                        playerData.getInt("HelmetLightY"),
                        playerData.getInt("HelmetLightZ")
                );
                if (player.level().getBlockState(lastLightPos).is(Blocks.LIGHT)) {
                    player.level().setBlockAndUpdate(lastLightPos, Blocks.AIR.defaultBlockState());
                }
                playerData.remove("HelmetLightX");
                playerData.remove("HelmetLightY");
                playerData.remove("HelmetLightZ");
            }
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getSlot() != EquipmentSlot.HEAD) return;
        if (player.level().isClientSide()) return;

        ItemStack from = event.getFrom();
        CompoundTag playerData = player.getPersistentData();

        if (from.getItem() instanceof LightHelmetItem && playerData.contains("HelmetCharge")) {
            int charge = playerData.getInt("HelmetCharge");
            CompoundTag tag = from.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            tag.putInt("Charge", charge);
            from.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

            playerData.remove("HelmetCharge");
            playerData.remove("HelmetDrainAcc");
        }
    }
}