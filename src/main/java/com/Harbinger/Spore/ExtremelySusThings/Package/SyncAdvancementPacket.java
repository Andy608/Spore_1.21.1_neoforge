package com.Harbinger.Spore.ExtremelySusThings.Package;

import com.Harbinger.Spore.ExtremelySusThings.ClientAdvancementTracker;
import com.Harbinger.Spore.Spore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncAdvancementPacket(String advancementId, boolean hasAdvancement) implements CustomPacketPayload {

    public static final Type<SyncAdvancementPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Spore.MODID, "sync_advancement"));

    // StreamCodec for encoding/decoding
    public static final StreamCodec<FriendlyByteBuf, SyncAdvancementPacket> STREAM_CODEC = StreamCodec.of(
            SyncAdvancementPacket::encode,
            SyncAdvancementPacket::new
    );

    public SyncAdvancementPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(), buffer.readBoolean());
    }

    public static void encode(FriendlyByteBuf buffer, SyncAdvancementPacket packet) {
        buffer.writeUtf(packet.advancementId);
        buffer.writeBoolean(packet.hasAdvancement);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Handler method
    public static void handle(SyncAdvancementPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            // In NeoForge, the context automatically handles client/server distinction
            ClientAdvancementTracker.setAdvancement(message.advancementId, message.hasAdvancement);
        });
    }
}