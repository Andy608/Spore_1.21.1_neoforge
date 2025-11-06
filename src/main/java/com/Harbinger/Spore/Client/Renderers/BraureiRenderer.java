package com.Harbinger.Spore.Client.Renderers;


import com.Harbinger.Spore.Client.Layers.BreweryBottle;
import com.Harbinger.Spore.Client.Layers.BreweryLiquid;
import com.Harbinger.Spore.Client.Models.BraureiModel;
import com.Harbinger.Spore.Client.Special.OrganoidMobRenderer;
import com.Harbinger.Spore.Sentities.Organoids.Brauerei;
import com.Harbinger.Spore.Sentities.Variants.BraureiVariants;
import com.Harbinger.Spore.Spore;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class BraureiRenderer<Type extends Brauerei> extends OrganoidMobRenderer<Type , BraureiModel<Type>> {
    public static final Map<BraureiVariants, ResourceLocation> TEXTURE =
            Util.make(Maps.newEnumMap(BraureiVariants.class), (p_114874_) -> {
                p_114874_.put(BraureiVariants.DEFAULT,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/brewery.png"));
                p_114874_.put(BraureiVariants.HAZARD,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/hazard_brewery.png"));
            });


    public BraureiRenderer(EntityRendererProvider.Context context) {
        super(context, new BraureiModel<>(context.bakeLayer(BraureiModel.LAYER_LOCATION)), 3f);
        this.addLayer(new BreweryLiquid<>(this));
        this.addLayer(new BreweryBottle(this));
    }



    @Override
    public ResourceLocation getTextureLocation(Type entity) {
        return TEXTURE.get(entity.getVariant());
    }


}