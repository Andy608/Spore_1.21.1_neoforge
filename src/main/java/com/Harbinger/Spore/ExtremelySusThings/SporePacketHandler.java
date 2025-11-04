package com.Harbinger.Spore.ExtremelySusThings;

import com.Harbinger.Spore.ExtremelySusThings.Package.*;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class SporePacketHandler {

    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("spore");

        // Register server-bound packets (client -> server)
        registrar.playToServer(RequestAdvancementPacket.TYPE, RequestAdvancementPacket.STREAM_CODEC, RequestAdvancementPacket::handle);
        registrar.playToServer(AdvancementGivingPackage.TYPE, AdvancementGivingPackage.STREAM_CODEC, AdvancementGivingPackage::handle);
        registrar.playToServer(OpenSurgeryScreenPacket.TYPE, OpenSurgeryScreenPacket.STREAM_CODEC, OpenSurgeryScreenPacket::handle);
        registrar.playToServer(OpenGraftingScreenPacket.TYPE, OpenGraftingScreenPacket.STREAM_CODEC, OpenGraftingScreenPacket::handle);

        // Register client-bound packets (server -> client)
        registrar.playToClient(SyncAdvancementPacket.TYPE, SyncAdvancementPacket.STREAM_CODEC, SyncAdvancementPacket::handle);
    }

    public static void sendToServer(net.minecraft.network.protocol.common.custom.CustomPacketPayload packet) {
        net.minecraft.client.Minecraft.getInstance().getConnection().send(packet);
    }

    public static void sendToClient(net.minecraft.network.protocol.common.custom.CustomPacketPayload packet, ServerPlayer player) {
        player.connection.send(packet);
    }
}