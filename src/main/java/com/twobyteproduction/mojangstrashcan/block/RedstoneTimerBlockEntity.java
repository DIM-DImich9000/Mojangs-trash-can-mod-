package com.twobyteproduction.mojangstrashcan.block;

import com.twobyteproduction.mojangstrashcan.menu.RedstoneTimerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RedstoneTimerBlockEntity extends BlockEntity implements MenuProvider {
    private int timeLeft = 0;
    private boolean isActive = false;
    private int pulseTicks = 0;
    private int animationTick = 0;

    public RedstoneTimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REDSTONE_TIMER_BE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RedstoneTimerBlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        BlockState currentState = level.getBlockState(pos);

        if (blockEntity.isActive) {
            if (blockEntity.timeLeft > 0) {
                blockEntity.timeLeft--;

                blockEntity.animationTick++;
                if (blockEntity.animationTick >= 10) {
                    blockEntity.animationTick = 0;
                    boolean currentLit = currentState.getValue(RedstoneTimerBlock.LIT);
                    serverLevel.setBlock(pos, currentState.setValue(RedstoneTimerBlock.LIT, !currentLit), 2);
                }

                if (blockEntity.timeLeft % 20 == 0) {
                    blockEntity.setChanged();
                }
            } else {
                // Время вышло! Выдаем сигнал и ЗВУК
                blockEntity.isActive = false;
                blockEntity.pulseTicks = 2;
                serverLevel.setBlock(pos, currentState.setValue(RedstoneTimerBlock.POWERED, true).setValue(RedstoneTimerBlock.LIT, true), 3);
                serverLevel.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockEntity.setChanged();
            }
        } else if (blockEntity.pulseTicks > 0) {
            blockEntity.pulseTicks--;
            if (blockEntity.pulseTicks == 0) {
                serverLevel.setBlock(pos, currentState.setValue(RedstoneTimerBlock.POWERED, false).setValue(RedstoneTimerBlock.LIT, false), 3);
            }
        } else {
            if (currentState.getValue(RedstoneTimerBlock.LIT)) {
                serverLevel.setBlock(pos, currentState.setValue(RedstoneTimerBlock.LIT, false), 2);
            }
            blockEntity.animationTick = 0;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("TimeLeft", this.timeLeft);
        tag.putBoolean("IsActive", this.isActive);
        tag.putInt("PulseTicks", this.pulseTicks);
        tag.putInt("AnimationTick", this.animationTick);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.timeLeft = tag.getInt("TimeLeft");
        this.isActive = tag.getBoolean("IsActive");
        this.pulseTicks = tag.getInt("PulseTicks");
        this.animationTick = tag.getInt("AnimationTick");
    }

    public int getTimeLeft() { return timeLeft; }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = Math.max(0, timeLeft);
        this.setChanged();
    }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) {
        this.isActive = active;
        if (!active) this.animationTick = 0;
        this.setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mojangstrashcan.redstone_timer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RedstoneTimerMenu(containerId, playerInventory, this);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.getBlockPos());
    }
}