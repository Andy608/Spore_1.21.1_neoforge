package com.Harbinger.Spore.Client.Renderers;

import com.Harbinger.Spore.Client.Layers.CustomArmorLayer;
import com.Harbinger.Spore.Client.Models.InfectedPlayerModel;
import com.Harbinger.Spore.Client.Models.InfectedTechnoModel;
import com.Harbinger.Spore.Client.Special.BaseInfectedRenderer;
import com.Harbinger.Spore.Sentities.BasicInfected.InfectedPlayer;
import com.Harbinger.Spore.Sentities.Variants.InfPlayerSkins;
import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.Seffects;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class InfectedPlayerRenderer extends BaseInfectedRenderer<InfectedPlayer , HumanoidModel<InfectedPlayer>> {
    private static final ResourceLocation EYES_TEXTURE = ResourceLocation.fromNamespaceAndPath(Spore.MODID,
            "textures/entity/eyes/inf_player.png");
    private final HumanoidModel<InfectedPlayer> mainModel = this.getModel();
    private final HumanoidModel<InfectedPlayer> technoSkin;
    private final HumanoidModel<InfectedPlayer> madnessModel;
    public static final Map<InfPlayerSkins, ResourceLocation> MAIN_TEXTURES =
            Util.make(Maps.newEnumMap(InfPlayerSkins.class), (p_114874_) -> {
                p_114874_.put(InfPlayerSkins.STEVE,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_steve.png"));
                p_114874_.put(InfPlayerSkins.ALEX,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_alex.png"));
                p_114874_.put(InfPlayerSkins.EFE,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_efe.png"));
                p_114874_.put(InfPlayerSkins.MAKENA,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_makena.png"));
                p_114874_.put(InfPlayerSkins.SUNNY,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_sunny.png"));
                p_114874_.put(InfPlayerSkins.ZURI,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_zuri.png"));
                p_114874_.put(InfPlayerSkins.ARI,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_ari.png"));
                p_114874_.put(InfPlayerSkins.KAI,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_kai.png"));
                p_114874_.put(InfPlayerSkins.NO0R,
                         ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/entity/player/inf_player_noor.png"));
            });
    public static final Map<InfPlayerSkins, ResourceLocation> MADNESS_TEXTURES =
            Util.make(Maps.newEnumMap(InfPlayerSkins.class), (p_114874_) -> {
                p_114874_.put(InfPlayerSkins.STEVE,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/steve.png"));
                p_114874_.put(InfPlayerSkins.ALEX,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/alex.png"));
                p_114874_.put(InfPlayerSkins.EFE,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/efe.png"));
                p_114874_.put(InfPlayerSkins.MAKENA,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/makena.png"));
                p_114874_.put(InfPlayerSkins.SUNNY,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/sunny.png"));
                p_114874_.put(InfPlayerSkins.ZURI,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/zuri.png"));
                p_114874_.put(InfPlayerSkins.ARI,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/ari.png"));
                p_114874_.put(InfPlayerSkins.KAI,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/kai.png"));
                p_114874_.put(InfPlayerSkins.NO0R,
                         ResourceLocation.parse("minecraft:textures/entity/player/wide/noor.png"));
            });

    public static final Map<Component,ResourceLocation> SPECIAL_SKINS =new HashMap<>(){{
        put(Component.literal("Technoblade"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/techno_skin.png"));
        put(Component.literal("CODATOWER"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_coda_skin.png"));
        put(Component.literal("Flash62724"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_slasher.png"));
        put(Component.literal("TVGuy"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_blura.png"));
        put(Component.literal("mrlambert6"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_lambert.png"));
        put(Component.literal("NexouuZ"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_nexouuz.png"));
        put(Component.literal("SyrCrypt"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_syrcrypt.png"));
        put(Component.literal("KaratFeng"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_karat_skin.png"));
        put(Component.literal("BigXplosion"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_explosion_skin.png"));
        put(Component.literal("Toasteroni"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_toast.png"));
        put(Component.literal("Dr_Pilot_MOO"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/dr_pilot_moo.png"));
        put(Component.literal("UnmeiHa"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_nunny.png"));
        put(Component.literal("AllToAshes"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_alltoashes.png"));
        put(Component.literal("0dna"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_0dna.png"));
        put(Component.literal("PedroHenrry"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_pedro.png"));
        put(Component.literal("minisketchy0919"), ResourceLocation.fromNamespaceAndPath(Spore.MODID,
                "textures/entity/player/inf_player_minisketchy0919.png"));
    }};

    public InfectedPlayerRenderer(EntityRendererProvider.Context context) {
        super(context, new InfectedPlayerModel<>(context.bakeLayer(InfectedPlayerModel.LAYER_LOCATION)), 0.5f);
        this.madnessModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER));
        this.technoSkin = new InfectedTechnoModel<>(context.bakeLayer(InfectedTechnoModel.LAYER_LOCATION));
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel
                (context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CustomArmorLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(InfectedPlayer infectedPlayer) {
        if (isTheViewerMad(infectedPlayer)){
            return MADNESS_TEXTURES.get(infectedPlayer.getVariant());
        }
        Component component = infectedPlayer.getName();
        ResourceLocation location = SPECIAL_SKINS.get(component);
        if (location != null){
            return location;
        }
        return MAIN_TEXTURES.get(infectedPlayer.getVariant());
    }

    public boolean isTheViewerMad(InfectedPlayer infectedPlayer){
        if (Minecraft.getInstance().cameraEntity instanceof Player player){
            MobEffectInstance instance = player.getEffect(Seffects.MADNESS);
            return instance != null && instance.getAmplifier() > 0 && player.distanceTo(infectedPlayer) > 30;
        }
        return false;
    }

    @Override
    public ResourceLocation eyeLayerTexture() {
        return EYES_TEXTURE;
    }

    @Override
    public void render(InfectedPlayer type, float value1, float value2, PoseStack stack, MultiBufferSource bufferSource, int light) {
        this.model = isTheViewerMad(type) ? madnessModel : Objects.equals(type.getCustomName(), Component.literal("Technoblade")) ? technoSkin : mainModel;
        super.render(type, value1, value2, stack, bufferSource, light);
    }
}
