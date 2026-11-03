package com.twobyteproduction.mojangstrashcan.entity;

import com.twobyteproduction.mojangstrashcan.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ExplosiveArrow extends AbstractArrow {
    private boolean hasExploded = false;

    public ExplosiveArrow(EntityType<? extends ExplosiveArrow> type, Level level) {
        super(type, level);
    }

    public ExplosiveArrow(Level level, LivingEntity shooter, ItemStack pickupItemStack, @Nullable ItemStack weapon) {
        super(ModEntities.EXPLOSIVE_ARROW.get(), shooter, level, pickupItemStack, weapon);
    }

    @Override
    protected void onHit(HitResult result) {
        if (this.hasExploded) return;

        if (!this.level().isClientSide) {
            this.hasExploded = true;
            Level level = this.level();
            double x = this.getX();
            double y = this.getY(0.0625D);
            double z = this.getZ();

            // Звук взрыва
            level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Одиночная вспышка
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0, 0, 0);
            }

            // Урон и отбрасывание ВСЕХ (включая игроков)
            AABB area = new AABB(x - 4, y - 4, z - 4, x + 4, y + 4, z + 4);
            for (Entity entity : level.getEntities(this, area)) {
                if (entity instanceof LivingEntity living) {
                    double dist = entity.position().distanceTo(new Vec3(x, y, z));
                    if (dist <= 4.0) {
                        float damage = (float)(6.0 * (1.0 - dist / 4.0));
                        if (damage > 0) {
                            living.hurt(level.damageSources().explosion(this, this.getOwner()), damage);
                        }

                        // ИСПРАВЛЕНИЕ: Подкидывание вверх
                        double dx = entity.getX() - x;
                        double dy = entity.getY() - y;
                        double dz = entity.getZ() - z;
                        double len = Math.sqrt(dx*dx + dy*dy + dz*dz);

                        if (len > 0.01) {
                            // Нормализуем вектор и принудительно даем сильный импульс вверх (0.8)
                            double nx = dx / len;
                            double ny = dy / len;
                            double nz = dz / len;

                            double pushY = Math.max(ny * 1.5, 0.8); // Гарантированно подкидывает вверх
                            living.push(nx * 1.5, pushY, nz * 1.5);
                        } else {
                            // Если стоим ровно на стреле, просто подкидываем вверх
                            living.push(0, 1.0, 0);
                        }
                    }
                }
            }

            this.discard();
        }
        super.onHit(result);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.EXPLOSIVE_ARROW.get());
    }
}