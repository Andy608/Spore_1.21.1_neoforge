package com.Harbinger.Spore.Sevents;

import com.Harbinger.Spore.Sentities.BaseEntities.EvolvedInfected;
import com.Harbinger.Spore.Sentities.BaseEntities.Hyper;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.Organoid;
import com.Harbinger.Spore.Sentities.Projectile.*;
import com.Harbinger.Spore.Sentities.Utility.ScentEntity;
import com.Harbinger.Spore.core.SConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DespawnSystem {
    private static int tickCounter = 0;
    private static final int CHECK_INTERVAL = 1200; // 60 seconds

    public static void tickMobCleaner(MinecraftServer server){
        tickCounter++;
        if (tickCounter >= CHECK_INTERVAL) {
            if (server != null) {
                for (ServerLevel level : server.getAllLevels()) {
                    cleanUpMobs(level);
                }
            }
            tickCounter = 0;
        }
    }
    private static void cleanUpMobs(ServerLevel level) {
        List<Infected> infected = new ArrayList<>();
        List<Projectile> projectileExcess = new ArrayList<>();
        List<EvolvedInfected> evolved = new ArrayList<>();
        List<Hyper> hyper = new ArrayList<>();
        List<Organoid> organoid = new ArrayList<>();
        List<ScentEntity> scent = new ArrayList<>();

        for (Entity entity : level.getAllEntities()) {
            if (!SConfig.SERVER.despawn_blacklist.get().contains(entity.getEncodeId()) &&
                    !entity.hasCustomName()) {

                switch (entity) {
                    case Organoid o -> organoid.add(o);
                    case EvolvedInfected e -> evolved.add(e);
                    case Hyper h -> hyper.add(h);
                    case ScentEntity s -> scent.add(s);
                    case Infected i -> infected.add(i);
                    case AcidBall i -> projectileExcess.add(i);
                    case BileProjectile i -> projectileExcess.add(i);
                    case StingerProjectile i -> projectileExcess.add(i);
                    case Vomit i -> projectileExcess.add(i);
                    case FleshBomb i -> projectileExcess.add(i);
                    default -> {
                    }
                }
            }
        }

        despawnExcess(level, infected, SConfig.SERVER.max_infected_cap.get());
        despawnExcess(level, evolved, SConfig.SERVER.max_evolved_cap.get());
        despawnExcess(level, hyper, SConfig.SERVER.max_hyper_cap.get());
        despawnExcess(level, organoid, SConfig.SERVER.max_organoid_cap.get());
        despawnExcess(level, scent, SConfig.SERVER.max_scent_cap.get());
        despawnExcess(level, projectileExcess, 100);
    }

    private static <T extends Entity> void despawnExcess(ServerLevel level, List<T> entities, int cap) {
        if (entities.size() <= cap) return;
        int toRemove = entities.size() - cap;
        int despawns = 0;
        List<ServerPlayer> players = level.getPlayers(p -> true);

        if (players.isEmpty()) {
            for (int i = 0; i < toRemove; i++) {
                T entity = entities.get(i);
                entity.discard();
                despawns++;
            }
        } else {
            entities.sort(Comparator.comparingDouble((Entity e) ->
                    level.getNearestPlayer(e, -1) != null ? e.distanceToSqr(Objects.requireNonNull(level.getNearestPlayer(e, -1))) : Double.MAX_VALUE).reversed());
            for (int i = 0; i < toRemove; i++) {
                T entity = entities.get(i);
                entity.discard();
                despawns++;
            }
        }
        System.out.println("Despawned " + despawns + " mobs in level: " + level.dimension().location());
    }
}
