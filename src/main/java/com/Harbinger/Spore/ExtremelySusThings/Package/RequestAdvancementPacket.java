package com.Harbinger.Spore.ExtremelySusThings.Package;

import com.Harbinger.Spore.ExtremelySusThings.SporePacketHandler;
import com.Harbinger.Spore.Spore;
import net.minecraft.advancements.AdvancementHolder;
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

public record RequestAdvancementPacket(String advancementId, int id) implements CustomPacketPayload {

    public static final Type<RequestAdvancementPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Spore.MODID, "request_advancement"));
    public static final StreamCodec<FriendlyByteBuf, RequestAdvancementPacket> STREAM_CODEC = StreamCodec.of(
            RequestAdvancementPacket::encode,
            RequestAdvancementPacket::new
    );

    public RequestAdvancementPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(), buffer.readInt());
    }

    // Static encode method for StreamCodec
    public static void encode(FriendlyByteBuf buffer, RequestAdvancementPacket packet) {
        buffer.writeUtf(packet.advancementId);
        buffer.writeInt(packet.id);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Handler method
    public static void handle(RequestAdvancementPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            if (level.isClientSide){
                return;
            }
            Entity truePlayer = level.getEntity(message.id);
            if (truePlayer instanceof ServerPlayer playerValue) {
                MinecraftServer server = playerValue.server;
                AdvancementHolder holder = server.getAdvancements().get(ResourceLocation.parse(message.advancementId));
                if (holder == null){
                    return;
                }
                // Check if the player has the advancement
                boolean hasAdvancement = playerValue.getAdvancements()
                        .getOrStartProgress(holder)
                        .isDone();

                SporePacketHandler.sendToClient(new SyncAdvancementPacket(message.advancementId, hasAdvancement), playerValue);
            }
        });
    }
}