package com.twobyteproduction.mojangstrashcan.client;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import com.twobyteproduction.mojangstrashcan.entity.ExplosiveArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ExplosiveArrowRenderer extends ArrowRenderer<ExplosiveArrow> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Mojangstrashcan.MOD_ID, "textures/entity/explosive_arrow_entity.png");

    public ExplosiveArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ExplosiveArrow entity) {
        return TEXTURE;
    }
}