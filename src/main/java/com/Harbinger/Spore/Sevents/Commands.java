package com.Harbinger.Spore.Sevents;

import com.Harbinger.Spore.ExtremelySusThings.ChunkLoadRequest;
import com.Harbinger.Spore.ExtremelySusThings.SporeSavedData;
import com.Harbinger.Spore.SBlockEntities.CDUBlockEntity;
import com.Harbinger.Spore.SBlockEntities.LivingStructureBlocks;
import com.Harbinger.Spore.Sentities.BaseEntities.*;
import com.Harbinger.Spore.Sentities.Calamities.Gazenbrecher;
import com.Harbinger.Spore.Sentities.Calamities.Hinderburg;
import com.Harbinger.Spore.Sentities.Calamities.Hohlfresser;
import com.Harbinger.Spore.Sentities.Calamities.Sieger;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Scamper;
import com.Harbinger.Spore.Sentities.HitboxesForParts;
import com.Harbinger.Spore.Sentities.Organoids.*;
import com.Harbinger.Spore.Sentities.Utility.*;
import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.SConfig;
import com.Harbinger.Spore.core.Sentities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.List;

public class Commands {
    public static void Command(RegisterCommandsEvent event){
        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":set_area")
                .executes(arguments -> {
                    ServerLevel world = arguments.getSource().getLevel();
                    int x = (int) arguments.getSource().getPosition().x();
                    int y = (int) arguments.getSource().getPosition().y();
                    int z = (int) arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null)
                        entity = FakePlayerFactory.getMinecraft(world);
                    if (entity != null){
                        BlockPos pos = new BlockPos(x ,y,z);
                        AABB hitbox = entity.getBoundingBox().inflate(20);
                        List<Entity> entities = entity.level().getEntities(entity, hitbox);
                        for (Entity entity1 : entities) {
                            if(entity1 instanceof Infected infected) {
                                infected.setSearchPos(pos);
                            }else if (entity1 instanceof Calamity calamity){
                                calamity.setSearchArea(pos);
                            }
                        }
                    }
                    return 1;
                }).requires(s -> s.hasPermission(1)));
        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":nuke_the_land")
                .executes(arguments -> {
                    ServerLevel world = arguments.getSource().getLevel();
                    int x = (int) arguments.getSource().getPosition().x();
                    int y = (int) arguments.getSource().getPosition().y();
                    int z = (int) arguments.getSource().getPosition().z();
                    NukeEntity nukeEntity = new NukeEntity(Sentities.NUKE.get(), world);
                    nukeEntity.setInitRange(1f);
                    nukeEntity.setRange((float) (SConfig.SERVER.nuke_range.get()*1f));
                    nukeEntity.setInitDuration(0);
                    nukeEntity.setDuration(SConfig.SERVER.nuke_time.get());
                    nukeEntity.setDamage((float) (SConfig.SERVER.nuke_damage.get()*1f));
                    nukeEntity.setPos(x,y,z);
                    world.addFreshEntity(nukeEntity);
                    return 1;
                }).requires(s -> s.hasPermission(1)));
        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":corpse")
                .executes(arguments -> {
                    ServerLevel world = arguments.getSource().getLevel();
                    RandomSource randomSource = RandomSource.create();
                    int x = (int) arguments.getSource().getPosition().x();
                    int y = (int) arguments.getSource().getPosition().y();
                    int z = (int) arguments.getSource().getPosition().z();
                    CorpseEntity corpseEntity = new CorpseEntity(Sentities.CORPSE_PIECE.get(), world);
                    corpseEntity.setCorpseType(randomSource.nextInt(HitboxesForParts.values().length));
                    corpseEntity.setOwnerAda(true);
                    corpseEntity.setPos(x,y,z);
                    world.addFreshEntity(corpseEntity);
                    return 1;
                }).requires(s -> s.hasPermission(1)));
        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":erase_the_fungus")
                .executes(arguments -> {
                    ServerLevel serverLevel = arguments.getSource().getLevel();
                    for (Entity entity : serverLevel.getAllEntities()){
                        if (entity instanceof LivingEntity living){
                            if (living instanceof UtilityEntity){
                                living.discard();
                            }
                        }
                    }
                    return 1;
                }).requires(s -> s.hasPermission(1)));
        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":feed")
                .executes(arguments -> {
                    ServerLevel world = arguments.getSource().getLevel();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null)
                        entity = FakePlayerFactory.getMinecraft(world);
                    AABB hitbox = entity.getBoundingBox().inflate(20);
                    List<Entity> entities = entity.level().getEntities(entity, hitbox);
                    for (Entity entity1 : entities) {
                        if(entity1 instanceof Infected infected) {
                            infected.setKills(infected.getKills()+1);
                            infected.setEvoPoints(infected.getEvoPoints()+1);
                        }else if (entity1 instanceof Calamity calamity){
                            calamity.setKills(calamity.getKills()+1);
                        }
                    }
                    return 1;
                }).requires(s -> s.hasPermission(1)));
        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":evolve")
                .executes(arguments -> {
                    ServerLevel world = arguments.getSource().getLevel();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null)
                        entity = FakePlayerFactory.getMinecraft(world);
                    if (entity != null){
                        AABB hitbox = entity.getBoundingBox().inflate(20);
                        List<Entity> entities = entity.level().getEntities(entity, hitbox);
                        for (Entity entity1 : entities) {
                            if(entity1 instanceof Infected infected) {
                                infected.setEvolution(SConfig.SERVER.evolution_age_human.get());
                                if (entity1 instanceof Scamper scamper){
                                    scamper.setAge(SConfig.SERVER.scamper_age.get());
                                }else if (infected instanceof EvolvedInfected evolvedInfected){
                                    evolvedInfected.setEvoPoints(SConfig.SERVER.min_kills_hyper.get());
                                }else
                                    infected.setEvoPoints(SConfig.SERVER.min_kills.get());
                            }else if (entity1 instanceof Mound mound){
                                mound.setAge(mound.getAge()+1);
                            }else if (entity1 instanceof Calamity calamity){
                                calamity.ActivateAdaptation();
                            }
                        }
                    }
                    return 1;
                }).requires(s -> s.hasPermission(1)));
        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":get_data")
                .executes(arguments -> {
                    ServerLevel world = arguments.getSource().getLevel();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity instanceof Player player){
                        SporeSavedData data = SporeSavedData.getDataLocation(world);
                        int numberofprotos = data.getAmountOfHiveminds();
                        player.displayClientMessage(Component.literal("........................................"),false);
                        player.displayClientMessage(Component.literal("There are "+numberofprotos + " proto hiveminds in this dimension"),false);
                        for (ChunkLoadRequest request : data.getRequests()){
                            String id = request.getRequestID();
                            long getDefaultTicks = request.getTickAmount();
                            long ticks = request.getTicksUntilExpiration();
                            player.displayClientMessage(Component.literal("Loaded chunk "+id + " "+ticks +"/"+getDefaultTicks),false);
                        }
                    }
                    return 1;
                }).requires(s -> s.hasPermission(1)));
        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":check_entity")
                .executes(arguments -> {
                    ServerLevel world = arguments.getSource().getLevel();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null)
                        entity = FakePlayerFactory.getMinecraft(world);
                    if (entity instanceof Player player && !player.level().isClientSide){
                        AABB hitbox = entity.getBoundingBox().inflate(5);
                        List<Entity> entities = entity.level().getEntities(entity, hitbox);
                        for (Entity entity1 : entities) {
                            if (entity1 instanceof CorpseEntity corpseEntity){
                                player.displayClientMessage(Component.literal("isAdapted ? " + corpseEntity.getOwnerAda()),false);
                                player.displayClientMessage(Component.literal("ID ? " + corpseEntity.getCorpseType()),false);
                                player.displayClientMessage(Component.literal("Timer ? " + corpseEntity.getTimer()),false);
                                for (int i=0;i<corpseEntity.getInventory().getContainerSize();i++){
                                    ItemStack stack = corpseEntity.getInventory().getItem(i);
                                    if (stack != ItemStack.EMPTY){
                                        player.displayClientMessage(Component.literal("ID ? " + stack.getItem().asItem().getDescription()),false);
                                    }
                                }
                            }
                            if(entity1 instanceof Infected infected) {
                                player.displayClientMessage(Component.literal("Entity "+ infected.getEncodeId() + " " + infected.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + infected.getHealth() + "/" + infected.getMaxHealth()),false);
                                player.displayClientMessage(Component.literal("Kills " + infected.getKills()),false);
                                player.displayClientMessage(Component.literal("Evolution Points " + infected.getEvoPoints()),false);
                                player.displayClientMessage(Component.literal("Position to be Searched " + infected.getSearchPos()),false);
                                player.displayClientMessage(Component.literal("Buffs " + infected.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Seconds until evolution: " + infected.getEvolutionCoolDown() + "/" + SConfig.SERVER.evolution_age_human.get()),false);
                                player.displayClientMessage(Component.literal("Seconds until starvation: " + infected.getHunger() + "/" + (SConfig.SERVER.hunger.get())),false);
                                player.displayClientMessage(Component.literal("Is Linked ? " + infected.getLinked()),false);
                                player.displayClientMessage(Component.literal("Target ? " + infected.getTarget()),false);
                                player.displayClientMessage(Component.literal("Partner ? " + infected.getFollowPartner()),false);
                                if (infected instanceof Scamper scamper){
                                    player.displayClientMessage(Component.literal("Time before overtake ? " + scamper.getAge()+"/"+SConfig.SERVER.scamper_age.get()),false);
                                }
                                if (infected instanceof Hyper scamper){
                                    player.displayClientMessage(Component.literal("get nest location ? " + scamper.getNestLocation()),false);
                                }
                                if (infected instanceof GastGeber geber){
                                    player.displayClientMessage(Component.literal("RootTimer ? " + geber.getTimeRooted()),false);
                                    player.displayClientMessage(Component.literal("Aggression ? " + geber.getAggression()),false);
                                    player.displayClientMessage(Component.literal("Spread ? " + geber.getSpreadInterval()),false);
                                }
                                player.displayClientMessage(Component.literal("-------------------------"),false);

                            }else if (entity1 instanceof Calamity calamity){
                                player.displayClientMessage(Component.literal("Entity "+ calamity.getEncodeId() + " " + calamity.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + calamity.getHealth() + "/" + calamity.getMaxHealth()),false);
                                player.displayClientMessage(Component.literal("Kills " + calamity.getKills()),false);
                                player.displayClientMessage(Component.literal("Position to be Searched " + calamity.getSearchArea()),false);
                                player.displayClientMessage(Component.literal("Buffs " + calamity.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Target ? " + calamity.getTarget()),false);
                                player.displayClientMessage(Component.literal("Mutation Color ? " + calamity.getMutationColor()),false);
                                if (calamity instanceof Sieger sieger){
                                    player.displayClientMessage(Component.literal("Tail health "+ sieger.getTailHp()+"/"+sieger.getMaxTailHp()),false);
                                }
                                if (calamity instanceof Gazenbrecher sieger){
                                    player.displayClientMessage(Component.literal("Tongue health "+ sieger.getTongueHp()+"/"+sieger.getMaxTongueHp()),false);
                                    player.displayClientMessage(Component.literal("Is adapted to fire "+ sieger.isAdaptedToFire() + " fire points" + sieger.getAdaptationCount()),false);
                                }
                                if (calamity instanceof Hinderburg sieger){
                                    player.displayClientMessage(Component.literal("Is armed "+ sieger.isArmed()),false);
                                }
                                if (calamity instanceof Hohlfresser sieger){
                                    player.displayClientMessage(Component.literal("Underground "+ sieger.isUnderground()),false);
                                    player.displayClientMessage(Component.literal("Ores ? "+ sieger.getOres()),false);
                                }
                                player.displayClientMessage(Component.literal("-------------------------"),false);
                            }else if (entity1 instanceof Mound mound){
                                player.displayClientMessage(Component.literal("Entity "+ mound.getEncodeId() + " " + mound.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + mound.getHealth() + "/" + mound.getMaxHealth()),false);
                                player.displayClientMessage(Component.literal("Is Linked ? " + mound.getLinked()),false);
                                player.displayClientMessage(Component.literal("Age " + mound.getAge()),false);
                                player.displayClientMessage(Component.literal("Seconds until growth " + mound.getAgeCounter() + "/" + SConfig.SERVER.mound_age.get()),false);
                                player.displayClientMessage(Component.literal("Seconds until puff " + mound.getCounter() + "/" + SConfig.SERVER.mound_cooldown.get()),false);
                                player.displayClientMessage(Component.literal("Buffs " + mound.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("-------------------------"),false);

                            }else if(entity1 instanceof Proto proto) {
                                player.displayClientMessage(Component.literal("Entity "+ proto.getEncodeId() + " " + proto.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + proto.getHealth() + "/" + proto.getMaxHealth()),false);
                                player.displayClientMessage(Component.literal("Current Target " + proto.getTarget()),false);
                                player.displayClientMessage(Component.literal("Buffs " + proto.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Mobs under control " + proto.getHosts()),false);
                                player.displayClientMessage(Component.literal("Biomass " + proto.getBiomass()),false);
                                for (int i = 0;i<proto.getWeights().length;i++){
                                    player.displayClientMessage(Component.literal("Neuron_"+i+" " + proto.getWeightsValue(i)),false);
                                }
                                for (String s : proto.team_1){
                                    player.displayClientMessage(Component.literal("TEAM_1 "+ s),false);
                                }
                                for (String s : proto.team_2){
                                    player.displayClientMessage(Component.literal("TEAM_2 "+ s),false);
                                }
                                for (String s : proto.team_3){
                                    player.displayClientMessage(Component.literal("TEAM_3 "+ s),false);
                                }
                                for (String s : proto.team_4){
                                    player.displayClientMessage(Component.literal("TEAM_4 "+ s),false);
                                }
                                for (String s : proto.team_5){
                                    player.displayClientMessage(Component.literal("Beloved mobs "+ s),false);
                                }
                                player.displayClientMessage(Component.literal("-------------------------"),false);
                            }
                            else if(entity1 instanceof Womb reformator) {
                                player.displayClientMessage(Component.literal("Entity "+ reformator.getEncodeId() + " " + reformator.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + reformator.getHealth()),false);
                                player.displayClientMessage(Component.literal("Stored Location " + reformator.getLocation()),false);
                                player.displayClientMessage(Component.literal("Buffs " + reformator.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Biomass " + reformator.getBiomass()),false);
                                player.displayClientMessage(Component.literal("State " + reformator.getVariant().getValue()),false);
                                for (String s : reformator.getAttributeIDs()){
                                    player.displayClientMessage(Component.translatable(s),false);
                                }
                                player.displayClientMessage(Component.literal("-------------------------"),false);
                            }else if(entity1 instanceof Vigil vigil) {
                                player.displayClientMessage(Component.literal("Entity "+ vigil.getEncodeId() + " " + vigil.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + vigil.getHealth()),false);
                                player.displayClientMessage(Component.literal("Buffs " + vigil.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("State " + vigil.getTrigger()),false);
                                player.displayClientMessage(Component.literal("Horde size " + vigil.getWaveSize()),false);
                                player.displayClientMessage(Component.literal("Time until it leaves " + vigil.getTimer()+"/6000"),false);
                                player.displayClientMessage(Component.literal("-------------------------"),false);

                            }else if(entity1 instanceof Umarmer umarmer) {
                                player.displayClientMessage(Component.literal("Entity "+ umarmer.getEncodeId() + " " + umarmer.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + umarmer.getHealth()),false);
                                player.displayClientMessage(Component.literal("Buffs " + umarmer.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Shielded? " + umarmer.isShielding()),false);
                                player.displayClientMessage(Component.literal("Pins? " + umarmer.isPinned()),false);
                                player.displayClientMessage(Component.literal("Time until it leaves " + umarmer.getTimer()+"/2400"),false);
                                player.displayClientMessage(Component.literal("-------------------------"),false);
                            }else if(entity1 instanceof Brauerei brauerei) {
                                player.displayClientMessage(Component.literal("Entity "+ brauerei.getEncodeId() + " " + brauerei.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + brauerei.getHealth()),false);
                                player.displayClientMessage(Component.literal("Buffs " + brauerei.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Time until it leaves " + brauerei.getTimer()+"/300"),false);
                                player.displayClientMessage(Component.literal("-------------------------"),false);
                            }
                            else if(entity1 instanceof Delusionare delusionare) {
                                player.displayClientMessage(Component.literal("Entity "+ delusionare.getEncodeId() + " " + delusionare.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + delusionare.getHealth()),false);
                                player.displayClientMessage(Component.literal("Buffs " + delusionare.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Target ? " + delusionare.getTarget()),false);
                                player.displayClientMessage(Component.literal("Magic state " + delusionare.getSpellById() + " casting "+delusionare.isCasting()),false);
                                player.displayClientMessage(Component.literal("-------------------------"),false);
                            }else if(entity1 instanceof Specter specter) {
                                player.displayClientMessage(Component.literal("Entity "+ specter.getEncodeId() + " " + specter.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + specter.getHealth()),false);
                                player.displayClientMessage(Component.literal("Buffs " + specter.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Target ? " + specter.getTarget()),false);
                                player.displayClientMessage(Component.literal("Target Pos " + specter.getTargetPos()),false);
                                player.displayClientMessage(Component.literal("Stomach " + specter.getStomach()),false);
                                player.displayClientMessage(Component.literal("Biomass " + specter.getBiomass()),false);
                                player.displayClientMessage(Component.literal("-------------------------"),false);
                            }else if(entity1 instanceof InfestedConstruct construct) {
                                player.displayClientMessage(Component.literal("Entity "+ construct.getEncodeId() + " " + construct.getCustomName()),false);
                                player.displayClientMessage(Component.literal("Current Health " + construct.getHealth()),false);
                                player.displayClientMessage(Component.literal("Buffs " + construct.getActiveEffects()),false);
                                player.displayClientMessage(Component.literal("Target ? " + construct.getTarget()),false);
                                player.displayClientMessage(Component.literal("Machine hp " + construct.getMachineHealth()),false);
                                player.displayClientMessage(Component.literal("Metal " + construct.getMetalReserve()),false);
                                player.displayClientMessage(Component.literal("-------------------------"),false);
                            }
                        }
                    }
                    return 1;
                }).requires(s -> s.hasPermission(1)));

        event.getDispatcher().register(net.minecraft.commands.Commands.literal(Spore.MODID+":check_block_entity")
                .executes(arguments -> {
                    ServerLevel world = arguments.getSource().getLevel();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null)
                        entity = FakePlayerFactory.getMinecraft(world);
                    if (entity != null) {
                        AABB aabb = entity.getBoundingBox().inflate(5);
                        for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
                            BlockEntity blockEntity = entity.level().getBlockEntity(blockpos);
                            if (entity instanceof Player player && !player.level().isClientSide) {
                                if (blockEntity instanceof LivingStructureBlocks structureBlocks){
                                    player.displayClientMessage(Component.literal("Structure block with " + structureBlocks.getKills() + " kills"), false);
                                }else if (blockEntity instanceof CDUBlockEntity block){
                                    player.displayClientMessage(Component.literal("Fuel " + block.fuel), false);
                                }
                            }

                        }
                    }
                    return 1;
                }).requires(s -> s.hasPermission(1)));

    }
}
