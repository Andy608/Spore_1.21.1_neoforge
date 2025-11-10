package com.Harbinger.Spore.ExtremelySusThings.Package;
import com.Harbinger.Spore.Screens.GraftingMenu;
import com.Harbinger.Spore.Spore;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

public record OpenGraftingScreenPacket(BlockPos pos, int id) implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<OpenGraftingScreenPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Spore.MODID, "open_grafting_screen"));


    public static final StreamCodec<FriendlyByteBuf, OpenGraftingScreenPacket> STREAM_CODEC = StreamCodec.of(
            OpenGraftingScreenPacket::encode,
            OpenGraftingScreenPacket::new
    );

    private static void encode(FriendlyByteBuf friendlyByteBuf, OpenGraftingScreenPacket openGraftingScreenPacket) {
        friendlyByteBuf.writeBlockPos(openGraftingScreenPacket.pos);
        friendlyByteBuf.writeInt(openGraftingScreenPacket.id);
    }

    public OpenGraftingScreenPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }
    public static void handle(OpenGraftingScreenPacket msg, IPayloadContext context) {
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
                        return new GraftingMenu(id, inv, table);
                    }

                    @Override
                    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
                        MenuProvider.super.writeClientSideData(menu, buffer);
                        buffer.writeBlockPos(table.getBlockPos());
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
