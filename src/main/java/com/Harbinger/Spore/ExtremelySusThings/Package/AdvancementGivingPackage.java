package com.Harbinger.Spore.ExtremelySusThings.Package;

import com.Harbinger.Spore.Spore;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AdvancementGivingPackage(String advancement, int id) implements CustomPacketPayload {

    public static final Type<AdvancementGivingPackage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Spore.MODID, "give_advancement"));

    // StreamCodec for encoding/decoding
    public static final StreamCodec<FriendlyByteBuf, AdvancementGivingPackage> STREAM_CODEC = StreamCodec.of(
            AdvancementGivingPackage::encode,
            AdvancementGivingPackage::new
    );

    public AdvancementGivingPackage(FriendlyByteBuf buffer) {
        this(buffer.readUtf(), buffer.readInt());
    }

    // Static encode method for StreamCodec
    public static void encode(FriendlyByteBuf buffer, AdvancementGivingPackage packet) {
        buffer.writeUtf(packet.advancement);
        buffer.writeInt(packet.id);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Handler method
    public static void handle(AdvancementGivingPackage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player entity = context.player();
            Level level = entity.level();
            if (level.isClientSide) {
                System.err.println("[Spore] No player context when handling advancement package.");
                return;
            }
            Entity truePlayer = level.getEntity(message.id);
            if (!(truePlayer instanceof ServerPlayer player)) {
                System.err.println("[Spore] Invalid player entity when handling advancement package.");
                return;
            }

            if (!player.isAlive()) {
                System.err.println("[Spore] Player is not alive when handling advancement package.");
                return;
            }
            MinecraftServer server = player.server;
            server.getAdvancements();

            AdvancementHolder advancement = server.getAdvancements().get(ResourceLocation.parse(message.advancement));
            if (advancement == null) {
                System.err.println("[Spore] Advancement not found: " + message.advancement);
                return;
            }

            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            if (progress.isDone()) {
                return;
            }

            for (String criterion : progress.getRemainingCriteria()) {
                player.getAdvancements().award(advancement, criterion);
            }
        });
    }
}