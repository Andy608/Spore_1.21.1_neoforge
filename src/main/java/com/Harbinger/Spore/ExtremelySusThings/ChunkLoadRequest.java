package com.Harbinger.Spore.ExtremelySusThings;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Objects;
import java.util.UUID;

public class ChunkLoadRequest {

    private ChunkPos[] chunkPositionsToLoad;
    private int priority;
    private final String requestID;
    private final long tickAmount;
    private long ticksUntilExpiration;
    private final UUID ownerUUID;
    private final ResourceKey<Level> dimension;

    public ChunkLoadRequest(
            ResourceKey<Level> dimension,
            ChunkPos[] chunkPositionsToLoad,
            int priority,
            String requestID,
            long ticksUntilExpiration,
            UUID ownerUUID)
    {
        this.chunkPositionsToLoad = chunkPositionsToLoad;
        this.priority = priority;
        this.requestID = requestID;
        this.tickAmount = ticksUntilExpiration;
        this.ticksUntilExpiration = ticksUntilExpiration;
        this.ownerUUID = ownerUUID;
        this.dimension = dimension;
    }

    public ChunkPos[] getChunkPositionsToLoad() {
        return chunkPositionsToLoad;
    }

    public int getPriority() {
        return priority;
    }
    public long getTickAmount(){
        return this.tickAmount;
    }

    public ServerLevel getDimension() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null ? server.getLevel(dimension) : null;
    }

    public String getRequestID() {
        return requestID;
    }

    public boolean isRequestID(String requestID) {
        return Objects.equals(this.requestID, requestID);
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public boolean isExpired() {
        return ticksUntilExpiration <= 0;
    }

    public void decrementTicksUntilExpiration(int amountToDecrement) {
        ticksUntilExpiration -= amountToDecrement;
    }

    public long getTicksUntilExpiration() {
        return ticksUntilExpiration;
    }
    private void setTicksUntilExpiration(long value){
        ticksUntilExpiration = value;
    }

    public boolean isHigherPriorityThan(ChunkLoadRequest other) {
        return priority < other.priority;
    }

    public boolean doesContainChunk(ChunkPos chunkPos) {
        for (ChunkPos pos : chunkPositionsToLoad) {
            if (pos.equals(chunkPos)) {
                return true;
            }
        }
        return false;
    }
    public boolean isOwnerStillPresentInChunk() {
        if (ownerUUID == null) return false;
        ServerLevel level = getDimension();
        if (level == null) return false;

        Entity entity = level.getEntity(ownerUUID);
        if (entity == null || !entity.isAlive()) {
            return false;
        }

        ChunkPos entityPos = new ChunkPos(entity.blockPosition());
        for (ChunkPos pos : chunkPositionsToLoad) {
            if (pos.equals(entityPos)) {
                return true;
            }
        }
        return false;
    }

    public boolean refreshIfOwnerStillPresent(long newDurationTicks) {
        if (isOwnerStillPresentInChunk()) {
            this.ticksUntilExpiration = newDurationTicks;
            return true;
        }
        return false;
    }

    public void removeChunk(ChunkPos chunkPos) {
        if (!doesContainChunk(chunkPos)) return;

        ChunkPos[] newChunkPositionsToLoad = new ChunkPos[chunkPositionsToLoad.length - 1];
        int index = 0;
        for (ChunkPos pos : chunkPositionsToLoad) {
            if (!pos.equals(chunkPos)) {
                newChunkPositionsToLoad[index++] = pos;
            }
        }
        chunkPositionsToLoad = newChunkPositionsToLoad;
    }

    public void addChunk(ChunkPos chunkPos) {
        if (doesContainChunk(chunkPos)) return;

        ChunkPos[] newChunkPositionsToLoad = new ChunkPos[chunkPositionsToLoad.length + 1];
        System.arraycopy(chunkPositionsToLoad, 0, newChunkPositionsToLoad, 0, chunkPositionsToLoad.length);
        newChunkPositionsToLoad[chunkPositionsToLoad.length] = chunkPos;
        chunkPositionsToLoad = newChunkPositionsToLoad;
    }

    public void setChunkPositionsToLoad(ChunkPos[] chunkPositionsToLoad) {
        this.chunkPositionsToLoad = chunkPositionsToLoad;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("RequestID", requestID);
        tag.putInt("Priority", priority);
        tag.putLong("TicksUntilExpiration", ticksUntilExpiration);
        tag.putLong("StartTickValue", tickAmount);
        if (ownerUUID != null) tag.putUUID("OwnerUUID", ownerUUID);
        tag.putString("Dimension", dimension.location().toString());

        ListTag chunks = new ListTag();
        for (ChunkPos pos : chunkPositionsToLoad) {
            CompoundTag c = new CompoundTag();
            c.putInt("X", pos.x);
            c.putInt("Z", pos.z);
            chunks.add(c);
        }
        tag.put("Chunks", chunks);
        return tag;
    }

    public static ChunkLoadRequest deserializeNBT(CompoundTag tag) {
        String requestID = tag.getString("RequestID");
        int priority = tag.getInt("Priority");
        long ticksUntilExpiration = tag.getLong("TicksUntilExpiration");
        long startTicks = tag.getLong("StartTickValue");
        UUID ownerUUID = tag.hasUUID("OwnerUUID") ? tag.getUUID("OwnerUUID") : null;

        ResourceKey<Level> dimKey = ResourceKey.create(
                Registries.DIMENSION,
                ResourceLocation.parse(tag.getString("Dimension"))
        );

        ListTag chunksList = tag.getList("Chunks", 10);
        ChunkPos[] positions = new ChunkPos[chunksList.size()];
        for (int i = 0; i < chunksList.size(); i++) {
            CompoundTag c = chunksList.getCompound(i);
            positions[i] = new ChunkPos(c.getInt("X"), c.getInt("Z"));
        }
        ChunkLoadRequest request = new ChunkLoadRequest(dimKey, positions, priority, requestID, startTicks, ownerUUID);
        request.setTicksUntilExpiration(ticksUntilExpiration);
        return request;
    }


    public static void loadChunkData(ServerLevel level){
        SporeSavedData data = SporeSavedData.get(level);
        for (ChunkLoadRequest request : data.getRequests()) {
            ChunkLoaderHelper.ACTIVE_REQUESTS.put(request.getRequestID(), request);
            for (ChunkPos pos : request.getChunkPositionsToLoad()) {
                ChunkLoaderHelper.forceChunk(level, pos);
            }
        }
    }
}
