package com.Harbinger.Spore.ExtremelySusThings.Package;
import com.Harbinger.Spore.Screens.SurgeryMenu;
import com.Harbinger.Spore.Spore;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenSurgeryScreenPacket(BlockPos pos, int id) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenSurgeryScreenPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Spore.MODID, "open_surgery_screen"));


    public static final StreamCodec<FriendlyByteBuf, OpenSurgeryScreenPacket> STREAM_CODEC = StreamCodec.of(
            OpenSurgeryScreenPacket::encode,
            OpenSurgeryScreenPacket::new
    );

    private static void encode(FriendlyByteBuf friendlyByteBuf, OpenSurgeryScreenPacket openGraftingScreenPacket) {
        friendlyByteBuf.writeBlockPos(openGraftingScreenPacket.pos);
        friendlyByteBuf.writeInt(openGraftingScreenPacket.id);
    }

    public OpenSurgeryScreenPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }
    public static void handle(OpenSurgeryScreenPacket msg, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            if (level.isClientSide){
                return;
            }
            Entity truePlayer = level.getEntity(msg.id);
            BlockEntity be = level.getBlockEntity(msg.pos);
            if (be instanceof com.Harbinger.Spore.SBlockEntities.SurgeryTableBlockEntity table && truePlayer instanceof ServerPlayer trueP) {
                trueP.openMenu(new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("block.spore.surgery_table");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inv, Player ply) {
                        return new SurgeryMenu(id, inv, table, table.data);
                    }
                });
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
