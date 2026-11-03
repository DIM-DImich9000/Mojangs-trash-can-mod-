package com.twobyteproduction.mojangstrashcan.client;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import com.twobyteproduction.mojangstrashcan.entity.ModEntities;
import com.twobyteproduction.mojangstrashcan.item.ModItems;
import com.twobyteproduction.mojangstrashcan.menu.ModMenus;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Mojangstrashcan.MOD_ID, value = Dist.CLIENT)
public class MojangstrashcanClient {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.REDSTONE_TIMER_MENU.get(), RedstoneTimerScreen::new);
        event.register(ModMenus.GUIDE_BOOK_MENU.get(), GuideBookScreen::new);
        event.register(ModMenus.QUIVER_MENU.get(), QuiverScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.EXPLOSIVE_ARROW.get(), ExplosiveArrowRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.QUIVER.get(),
                    ResourceLocation.fromNamespaceAndPath(Mojangstrashcan.MOD_ID, "has_arrows"),
                    (stack, level, entity, seed) -> {
                        CompoundTag customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                        ListTag itemsTag = customData.getList("QuiverItems", Tag.TAG_COMPOUND);
                        for (int i = 0; i < itemsTag.size(); i++) {
                            if (!itemsTag.getCompound(i).isEmpty()) return 1.0F;
                        }
                        return 0.0F;
                    });
            ItemProperties.register(ModItems.LIGHT_HELMET.get(),
                    ResourceLocation.fromNamespaceAndPath(Mojangstrashcan.MOD_ID, "charged"),
                    (stack, level, entity, seed) -> {
                        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                        return tag.getInt("Charge") > 0 ? 1.0F : 0.0F;
                    });
        });
    }
}