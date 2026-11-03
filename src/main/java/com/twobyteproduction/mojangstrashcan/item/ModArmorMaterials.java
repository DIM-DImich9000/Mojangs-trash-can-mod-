package com.twobyteproduction.mojangstrashcan.item;

import com.twobyteproduction.mojangstrashcan.Mojangstrashcan;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, Mojangstrashcan.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LIGHT_HELMET_MATERIAL =
            ARMOR_MATERIALS.register("light_helmet", () -> new ArmorMaterial(
                    Map.of(ArmorItem.Type.HELMET, 2),
                    15,
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(Items.IRON_INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Mojangstrashcan.MOD_ID, "light_helmet"))),
                    0.0F,
                    0.0F
            ));
}