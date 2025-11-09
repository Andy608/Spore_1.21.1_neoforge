package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Sitems.*;
import com.Harbinger.Spore.Sitems.Agents.*;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsMutations;
import com.Harbinger.Spore.Spore;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class Sitems {
    public  static  final List<Item> BIOLOGICAL_ITEMS = new ArrayList<>();
    public  static  final List<Item> TECHNOLOGICAL_ITEMS = new ArrayList<>();
    public  static  final List<Item> TINTABLE_ITEMS = new ArrayList<>();
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Spore.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
    public  static final DeferredItem<Item> ACID = ITEMS.register("acid",
            () -> new Item( new Item.Properties()));
    public  static final DeferredItem<Item> BILE = ITEMS.register("bile",
            () -> new Item( new Item.Properties()));
    public  static final DeferredItem<Item> ACID_BALL = ITEMS.register("acid_ball",
            () -> new Item( new Item.Properties()));

    public  static final DeferredItem<Item> CLAW_FRAGMENT = ITEMS.register("claw_fragment",
            () -> new OrganItem("spore.scanner.organ.claw_fragment","spore:anatomy_act_1"));
    public  static final DeferredItem<Item> CLAW = ITEMS.register("claw",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> ARMOR_FRAGMENT = ITEMS.register("armor_fragment",
            () -> new OrganItem("spore.scanner.organ.armor_fragment","spore:anatomy_act_2"));
    public  static final DeferredItem<Item> MUTATED_HEART = ITEMS.register("mutated_heart",
            () -> new OrganItem("spore.scanner.organ.mutated_heart","spore:anatomy_act_3"));
    public  static final DeferredItem<Item> MUTATED_FIBER = ITEMS.register("mutated_fiber",
            () -> new OrganItem("spore.scanner.organ.mutated_fiber","spore:anatomy_act_4"));
    public  static final DeferredItem<Item> WING_MEMBRANE = ITEMS.register("wing_membrane",
            () -> new OrganItem("spore.scanner.organ.wing_membrane","spore:anatomy_act_5"));
    public  static final DeferredItem<Item> FLESHY_BONE = ITEMS.register("fleshy_bone",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> HARDENED_BIND = ITEMS.register("hardened_bind",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> FLESHY_CLAW = ITEMS.register("fleshy_claw",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> LIVING_CORE = ITEMS.register("living_core",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> SPINE_FRAGMENT = ITEMS.register("spine_fragment",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> NERVES = ITEMS.register("nerves",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> CEREBRUM = ITEMS.register("cerebrum",
            () -> new OrganItem("spore.scanner.organ.cerebrum","spore:anatomy_act_6"));
    public  static final DeferredItem<Item> SPINE = ITEMS.register("spine",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> ARMOR_PLATE = ITEMS.register("armor_plate",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> PLATED_MUSCLE = ITEMS.register("plated_muscle",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> ALVEOLIC_SACK = ITEMS.register("alveolic_sack",
            () -> new OrganItem("spore.scanner.organ.alveolic_sack","spore:anatomy_act_7"));
    public  static final DeferredItem<Item> ALTERED_SPLEEN = ITEMS.register("altered_spleen",
            () -> new OrganItem("spore.scanner.organ.altered_spleen","spore:anatomy_act_8"));
    public  static final DeferredItem<Item> CORROSIVE_SACK = ITEMS.register("corrosive_sack",
            () -> new OrganItem( "spore.scanner.organ.corrosive_sack","spore:anatomy_act_9"));
    public  static final DeferredItem<Item> ORGANOID_MEMBRANE = ITEMS.register("organoid_membrane",
            () -> new OrganItem("spore.scanner.organ.organoid_membrane","spore:anatomy_act_10"));
    public  static final DeferredItem<Item> TENDONS = ITEMS.register("tendons",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> INNARDS = ITEMS.register("innards",
            () -> new Innards( new Item.Properties()));
    public  static final DeferredItem<Item> SICKLE_FRAGMENT = ITEMS.register("sickle_fragment",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> FANG = ITEMS.register("fang",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> SPIKE = ITEMS.register("spike",
            () -> new BaseItem( new Item.Properties()));
    public  static final DeferredItem<Item> SHIELD_FRAGMENT = ITEMS.register("shield_fragment",
            () -> new BaseItem(new Item.Properties()));
    public  static final DeferredItem<Item> R_WING = ITEMS.register("r_wing",
            () -> new BaseItem( new Item.Properties()));

    public  static final DeferredItem<Item> TUMOR = ITEMS.register("tumor",
            () -> new Tumor(Tumor.TumorType.REGULAR));
    public  static final DeferredItem<Item> SICKEN_TUMOR = ITEMS.register("sicken_tumor",
            () -> new Tumor(Tumor.TumorType.SICKEN));
    public  static final DeferredItem<Item> CALCIFIED_TUMOR = ITEMS.register("calcified_tumor",
            () -> new Tumor(Tumor.TumorType.CALCIFIED));
    public  static final DeferredItem<Item> BILE_TUMOR = ITEMS.register("bile_tumor",
            () -> new Tumor(Tumor.TumorType.BILE));
    public  static final DeferredItem<Item> FROZEN_TUMOR = ITEMS.register("frozen_tumor",
            () -> new Tumor(Tumor.TumorType.FROZEN));

    public  static final DeferredItem<Item> REFORGED_BIOMASS_T = ITEMS.register("reforged_biomass_t",
            () -> new BaseItem(new Item.Properties()));
    public  static final DeferredItem<Item> REFORGED_BIOMASS_W = ITEMS.register("reforged_biomass_w",
            () -> new BaseItem(new Item.Properties()));
    public  static final DeferredItem<Item> REFORGED_BIOMASS_A = ITEMS.register("reforged_biomass_a",
            () -> new BaseItem(new Item.Properties()));
    public  static final DeferredItem<Item> RESPIRATOR = ITEMS.register("respirator",
            () -> new BaseItem(new Item.Properties()));
    public  static final DeferredItem<Item> SAUSAGE = ITEMS.register("sausage",
            () -> new BaseItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,300,1),1f).build())));
    public  static final DeferredItem<Item> FIBER_STEW = ITEMS.register("fiber_stew",
            () -> new BowlItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(6).saturationModifier(1.5F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,300,0),1f).build())));
    public  static final DeferredItem<Item> HEART_KEBAB = ITEMS.register("heart_kebab",
            () -> new BaseItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.4F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.HUNGER,100,0),1f).build())));
    public  static final DeferredItem<Item> ROASTED_HEART_KEBAB = ITEMS.register("roasted_heart_kebab",
            () -> new BaseItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(8).saturationModifier(1.2F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,300,1),1f).build())));
    public  static final DeferredItem<Item> ROASTED_TUMOR = ITEMS.register("roasted_tumor",
            () -> new BaseItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(10).saturationModifier(1.8F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,300,1),1f).build())));
    public  static final DeferredItem<Item> VIGIL_EYE_SOUP = ITEMS.register("vigil_eye_soup",
            () -> new BowlItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(12).saturationModifier(1.2F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.NIGHT_VISION,1200,0),1f).build())));
    public  static final DeferredItem<Item> MILKY_SACK = ITEMS.register("milky_sack",
            () -> new BaseItem(new Item.Properties().stacksTo(8).food(new FoodProperties.Builder().nutrition(4).saturationModifier(1.2F).alwaysEdible().build())));
    public  static final DeferredItem<Item> BRAIN_NOODLES = ITEMS.register("brain_noodles",
            () -> new BowlItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(10).saturationModifier(2F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.DIG_SPEED,300,1),1f).build())));
    public  static final DeferredItem<Item> FRIED_WING_MEMBRANE = ITEMS.register("fried_wing_membrane",
            () -> new BaseItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(10).saturationModifier(3F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.SLOW_FALLING,300,1),1f).build())));
    public  static final DeferredItem<Item> BIOMASS_BACON = ITEMS.register("biomass_bacon",
            () -> new BaseItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.5F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.SATURATION,300,1),1f).build())));
    public  static final DeferredItem<Item> TENDON_GUM = ITEMS.register("tendon_gum",
            () -> new BaseItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.JUMP,300,1),1f).build())));
    public  static final DeferredItem<Item> ORGANOID_SOUP = ITEMS.register("organoid_soup",
            () -> new BowlItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(4F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,400,2),1f).build()).stacksTo(16)));
    public  static final DeferredItem<Item> FUNGAL_SAUCE = ITEMS.register("fungal_sauce",
            () -> new BowlItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(6F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,400,0),1f).build()).stacksTo(16)));
    public  static final DeferredItem<Item> SLICE_OF_HEARTPIE = ITEMS.register("slice_of_heartpie",
            () -> new BaseItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(3F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,100,1),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,200,1),1f).build())));
    public  static final DeferredItem<Item> FUNGAL_BURGER = ITEMS.register("fungal_burger",
            () -> new BaseItem(new Item.Properties().stacksTo(8).food(new FoodProperties.Builder().nutrition(30).saturationModifier(12F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.SLOW_FALLING,300,1),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,600,1),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.REGENERATION,600,0),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,600,1),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,500,1),1f).build())));
    public  static final DeferredItem<Item> FLESHY_RIBS = ITEMS.register("fleshy_ribs",
            () -> new BaseItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(2F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,400,1),1f).build())));
    public  static final DeferredItem<Item> MEATY_ICECREAM = ITEMS.register("meaty_icecream",
            () -> new BaseItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(2F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,400,0),1f).build())));
    public  static final DeferredItem<Item> AMALGAMATED_ROAST = ITEMS.register("amalgamated_roast",
            () -> new BaseItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(16).saturationModifier(8F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST,600,1),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,600,0),1f).alwaysEdible().build())));
    public  static final DeferredItem<Item> ELDRITCH_SUSHI = ITEMS.register("eldritch_sushi",
            () -> new BaseItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(20).saturationModifier(6F).alwaysEdible().effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.DOLPHINS_GRACE,1200,0),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.CONDUIT_POWER,1200,0),1f).build())));
    public  static final DeferredItem<Item> STUFFED_ABOMINATION = ITEMS.register("stuffed_abomination",
            () -> new BaseItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(18).saturationModifier(7F).alwaysEdible().effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1000,1),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.SLOW_FALLING,1000,0),1f).alwaysEdible().build())));
    public  static final DeferredItem<Item> FROZEN_DECAYED_BIOMASS = ITEMS.register("frozen_decayed_biomass",
            () -> new BaseItem(new Item.Properties()));
    public  static final DeferredItem<Item> DECAYED_TORSO = ITEMS.register("decayed_torso",
            () -> new BaseItem(new Item.Properties()));
    public  static final DeferredItem<Item> STUFFED_TORSO = ITEMS.register("stuffed_torso",
            () -> new BaseItem(new Item.Properties()));
    public  static final DeferredItem<Item> DECAYED_LIMBS = ITEMS.register("decayed_limbs",
            () -> new DecayedLimbs(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1F).effect(()-> new MobEffectInstance(Seffects.MYCELIUM,200,0),0.4f)
                    .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION,300,1),1f).build())));

    public  static final DeferredItem<Item> INFECTED_HUMAN_SPAWNEGG = ITEMS.register("infected_human_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_HUMAN,-9357608,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INFECTED_HUSK_SPAWNEGG = ITEMS.register("inf_husk_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_HUSK,-875608,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INF_VILLAGER_SPAWNEGG = ITEMS.register("inf_villager_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_VILLAGER,-6639718,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INF_DISEASED_VILLAGER_SPAWNEGG = ITEMS.register("inf_diseased_villager_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_DISEASED_VILLAGER,-6633211,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INF_WITCH_SPAWNEGG = ITEMS.register("inf_witch_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_WITCH,-8512718,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INF_PILLAGER_SPAWNEGG = ITEMS.register("inf_pillager_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_PILLAGER,-2312718,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INF_VIND_SPAWNEGG = ITEMS.register("inf_vind_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_VINDICATOR,-984718,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> INF_EVO_SPAWNEGG = ITEMS.register("inf_evo_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_EVOKER,-254718,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> INF_WANDERER_SPAWNEGG = ITEMS.register("inf_wanderer_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_WANDERER,-6639718,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INF_DROWNED_SPAWNEGG = ITEMS.register("inf_drowned_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_DROWNED,-16751002,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INF_PLAYER_SPAWNEGG = ITEMS.register("inf_player_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_PLAYER,-86751002,SpawnEggType.INFECTED));
    public  static final DeferredItem<Item> INF_HAZMAT_SPAWNEGG = ITEMS.register("inf_hazmat_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_HAZMAT,-6345002,SpawnEggType.INFECTED));

    public  static final DeferredItem<Item> PLAGUED_SPAWNEGG = ITEMS.register("plagued_spawnegg",
            () -> new SporeSpawnEgg(Sentities.PLAGUED,78294644,SpawnEggType.EXPERIMENT));
    public  static final DeferredItem<Item> LACERATOR_SPAWNEGG = ITEMS.register("lacerator_spawnegg",
            () -> new SporeSpawnEgg(Sentities.LACERATOR,2412344,SpawnEggType.EXPERIMENT));
    public  static final DeferredItem<Item> BIOBLOOB_SPAWNEGG = ITEMS.register("biobloob_spawnegg",
            () -> new SporeSpawnEgg(Sentities.BIOBLOOB,7412344,SpawnEggType.EXPERIMENT));
    public  static final DeferredItem<Item> SAUGLING_SPAWNEGG = ITEMS.register("saugling_spawnegg",
            () -> new SporeSpawnEgg(Sentities.SAUGLING,8901238,SpawnEggType.EXPERIMENT));


    public  static final DeferredItem<Item> KNIGHT_SPAWNEGG = ITEMS.register("knight_spawnegg",
            () -> new SporeSpawnEgg(Sentities.KNIGHT,-7681208,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> GRIEFER_SPAWNEGG = ITEMS.register("griefer_spawnegg",
            () -> new SporeSpawnEgg(Sentities.GRIEFER,-5750208,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> BRAIO_SPAWNEGG = ITEMS.register("braio_spawnegg",
            () -> new SporeSpawnEgg(Sentities.BRAIOMIL,-6124508,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> BUSSER_SPAWNEGG = ITEMS.register("busser_spawnegg",
            () -> new SporeSpawnEgg(Sentities.BUSSER,-3724508,SpawnEggType.EVOLVED));

    public  static final DeferredItem<Item> LEAPER_SPAWNEGG = ITEMS.register("leaper_spawnegg",
            () -> new SporeSpawnEgg(Sentities.LEAPER,-9762718,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> SLASHER_SPAWNEGG = ITEMS.register("slasher_spawnegg",
            () -> new SporeSpawnEgg(Sentities.SLASHER,-8564118,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> SPITTER_SPAWNEGG = ITEMS.register("spitter_spawnegg",
            () -> new SporeSpawnEgg(Sentities.SPITTER,-8164818,SpawnEggType.EVOLVED));

    public  static final DeferredItem<Item> HOWLER_SPAWNEGG = ITEMS.register("howler_spawnegg",
            () -> new SporeSpawnEgg(Sentities.HOWLER,-32464818,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> STALKER_SPAWNEGG = ITEMS.register("stalker_spawnegg",
            () -> new SporeSpawnEgg(Sentities.STALKER,-42364818,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> BRUTE_SPAWNEGG = ITEMS.register("brute_spawnegg",
            () -> new SporeSpawnEgg(Sentities.BRUTE,-1235818,SpawnEggType.EVOLVED));

    public  static final DeferredItem<Item> VOLATILE_SPAWNEGG = ITEMS.register("volatile_spawnegg",
            () -> new SporeSpawnEgg(Sentities.VOLATILE,-976435818,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> MEPH_SPAWNEGG = ITEMS.register("meph_spawnegg",
            () -> new SporeSpawnEgg(Sentities.MEPHETIC,-412343453,SpawnEggType.EVOLVED));

    public  static final DeferredItem<Item> INEBRIATER_SPAWNEGG = ITEMS.register("inebriater_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INEBRIATER,-412435818,SpawnEggType.EVOLVED));

    public  static final DeferredItem<Item> THORN_SPAWNEGG = ITEMS.register("thorn_spawnegg",
            () -> new SporeSpawnEgg(Sentities.THORN,-1243545,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> JAGD_SPAWNEGG = ITEMS.register("jagd_spawnegg",
            () -> new SporeSpawnEgg(Sentities.JAGD,-95469235,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> SCAVENGER_SPAWNEGG = ITEMS.register("scavenger_spawnegg",
            () -> new SporeSpawnEgg(Sentities.SCAVENGER,-54353454,SpawnEggType.EVOLVED));

    public  static final DeferredItem<Item> BLOATER_SPAWNEGG = ITEMS.register("bloater_spawnegg",
            () -> new SporeSpawnEgg(Sentities.BLOATER,-6834952,SpawnEggType.EVOLVED));

    public  static final DeferredItem<Item> NUCLEA_SPAWNEGG = ITEMS.register("nuclea_spawnegg",
            () -> new SporeSpawnEgg(Sentities.NUCLEA,-265262544,SpawnEggType.EVOLVED));
    public  static final DeferredItem<Item> PROT_SPAWNEGG = ITEMS.register("prot_spawnegg",
            () -> new SporeSpawnEgg(Sentities.PROTECTOR,-965262544,SpawnEggType.EVOLVED));

    public  static final DeferredItem<Item> SCENT_SPAWNEGG = ITEMS.register("scent_spawnegg",
            () -> new SporeSpawnEgg(Sentities.SCENT,-1,SpawnEggType.UNKNOWN));

    public  static final DeferredItem<Item> ILLUSION_SPAWNEGG = ITEMS.register("illusion_spawnegg",
            () -> new SporeSpawnEgg(Sentities.ILLUSION,-1,SpawnEggType.UNKNOWN));

    public  static final DeferredItem<Item> SCAMPER_SPAWNEGG = ITEMS.register("scamper_spawnegg",
            () -> new SporeSpawnEgg(Sentities.SCAMPER,-33777216,SpawnEggType.UNKNOWN));

    public  static final DeferredItem<Item> GASTGABER_SPAWNEGG = ITEMS.register("gastgaber_spawnegg",
            () -> new SporeSpawnEgg(Sentities.GASTGABER,-241247216,SpawnEggType.UNKNOWN));

    public  static final DeferredItem<Item> SPECTER_SPAWNEGG = ITEMS.register("specter_spawnegg",
            () -> new SporeSpawnEgg(Sentities.SPECTER,-876534333,SpawnEggType.UNKNOWN));

    public  static final DeferredItem<Item> CONSTRUCT_SPAWNEGG = ITEMS.register("construct_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INF_CONSTRUCT,-65242341,SpawnEggType.UNKNOWN));

    public  static final DeferredItem<Item> MOUND_SPAWNEGG = ITEMS.register("mound_spawnegg",
            () -> new SporeSpawnEgg(Sentities.MOUND,-5750208,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> VIGIL_SPAWNEGG = ITEMS.register("vigil_spawnegg",
            () -> new SporeSpawnEgg(Sentities.VIGIL,-64160208,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> UMARMED_SPAWNEGG = ITEMS.register("umarmed_spawnegg",
            () -> new SporeSpawnEgg(Sentities.UMARMED,-8650208,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> USURPER_SPAWNEGG = ITEMS.register("usurper_spawnegg",
            () -> new SporeSpawnEgg(Sentities.USURPER,-432208,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> BRAUREI_SPAWNEGG = ITEMS.register("braurei_spawnegg",
            () -> new SporeSpawnEgg(Sentities.BRAUREI,-745723,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> VERVA_SPAWNEGG = ITEMS.register("verva_spawnegg",
            () -> new SporeSpawnEgg(Sentities.VERVA,-412323,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> DELUSIONER_SPAWNEGG = ITEMS.register("delusioner_spawnegg",
            () -> new SporeSpawnEgg(Sentities.DELUSIONARE,-93652400,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> RECONSTRUCTOR_SPAWNEGG = ITEMS.register("reconstructor_spawnegg",
            () -> new SporeSpawnEgg(Sentities.RECONSTRUCTOR,-2353208,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> PROTO_SPAWNEGG = ITEMS.register("proto_spawnegg",
            () -> new SporeSpawnEgg(Sentities.PROTO,244208,SpawnEggType.ORGANOID));

    public  static final DeferredItem<Item> WENDIGO_SPAWNEGG = ITEMS.register("wendigo_spawnegg",
            () -> new SporeSpawnEgg(Sentities.WENDIGO,-354345818,SpawnEggType.HYPER));

    public  static final DeferredItem<Item> INQUISITOR_SPAWNEGG = ITEMS.register("inquisitor_spawnegg",
            () -> new SporeSpawnEgg(Sentities.INQUISITOR,-6435818,SpawnEggType.HYPER));

    public  static final DeferredItem<Item> BROTKATZE_SPAWNEGG = ITEMS.register("brotkatze_spawnegg",
            () -> new SporeSpawnEgg(Sentities.BROTKATZE,-7352318,SpawnEggType.HYPER));

    public  static final DeferredItem<Item> HEVOKER_SPAWNEGG = ITEMS.register("hevoker_spawnegg",
            () -> new SporeSpawnEgg(Sentities.HEVOKER,-867785,SpawnEggType.HYPER));

    public  static final DeferredItem<Item> OGRE_SPAWNEGG = ITEMS.register("ogre_spawnegg",
            () -> new SporeSpawnEgg(Sentities.OGRE,-241434523,SpawnEggType.HYPER));

    public  static final DeferredItem<Item> HVINDICATOR_SPAWNEGG = ITEMS.register("hvindicator_spawnegg",
            () -> new SporeSpawnEgg(Sentities.HVINDICATOR,-347898989,SpawnEggType.HYPER));

    public  static final DeferredItem<Item> SIEGER_SPAWNEGG = ITEMS.register("sieger_spawnegg",
            () -> new SporeSpawnEgg(Sentities.SIEGER,244208,SpawnEggType.CALAMITY));

    public  static final DeferredItem<Item> GAZEN_SPAWNEGG = ITEMS.register("gazen_spawnegg",
            () -> new SporeSpawnEgg(Sentities.GAZENBREACHER,865020865,SpawnEggType.CALAMITY));

    public  static final DeferredItem<Item> HINDEN_SPAWNEGG = ITEMS.register("hinden_spawnegg",
            () -> new SporeSpawnEgg(Sentities.HINDENBURG,346320865,SpawnEggType.CALAMITY));

    public  static final DeferredItem<Item> HOWITZER_SPAWNEGG = ITEMS.register("howitzer_spawnegg",
            () -> new SporeSpawnEgg(Sentities.HOWITZER,18414394,SpawnEggType.CALAMITY));

    public  static final DeferredItem<Item> HOHLFRESSER_SPAWNEGG = ITEMS.register("hohlfresser_spawnegg",
            () -> new SporeSpawnEgg(Sentities.HOHLFRESSER,76414394,SpawnEggType.CALAMITY));

    public  static final DeferredItem<Item> SABER = ITEMS.register("saber",
            InfectedSaber::new);
    public  static final DeferredItem<Item> GREATSWORD = ITEMS.register("greatsword",
            InfectedGreatSword::new);
    public  static final DeferredItem<Item> CLEAVER = ITEMS.register("cleaver",
            InfectedCleaver::new);
    public  static final DeferredItem<Item> ARMADS = ITEMS.register("armads",
            InfectedArmads::new);
    public  static final DeferredItem<Item> INFECTED_BOW = ITEMS.register("infected_bow",
            InfectedGreatBow::new);
    public  static final DeferredItem<Item> MAUL = ITEMS.register("maul",
            InfectedMaul::new);
    public  static final DeferredItem<Item> COMBAT_PICKAXE = ITEMS.register("combat_pickaxe",
            InfectedPickaxe::new);
    public  static final DeferredItem<Item> SCYTHE = ITEMS.register("scythe",
            InfectedScythe::new);
    public  static final DeferredItem<Item> COMBAT_SHOVEL = ITEMS.register("combat_shovel",
            InfectedCombatShovel::new);
    public  static final DeferredItem<Item> INFECTED_SPEAR = ITEMS.register("infected_spear",
            InfectedSpearItem::new);
    public  static final DeferredItem<Item> INFECTED_CROSSBOW = ITEMS.register("infected_crossbow",
            InfectedCrossbow::new);
    public  static final DeferredItem<Item> MACE = ITEMS.register("mace",
            InfectedMace::new);
    public  static final DeferredItem<Item> SICKLE = ITEMS.register("sickle",
           InfectedSickle::new);
    public  static final DeferredItem<Item> HALBERD = ITEMS.register("halberd",
            InfectedHalbert::new);
    public  static final DeferredItem<Item> KNIFE = ITEMS.register("knife",
            InfectedKnife::new);
    public  static final DeferredItem<Item> BOOMERANG = ITEMS.register("boomerang",
            InfectedBoomerang::new);
    public  static final DeferredItem<Item> RAPIER = ITEMS.register("rapier",
            InfectedRapier::new);
    public  static final DeferredItem<Item> SHIELD = ITEMS.register("shield",
            InfectedShield::new);
    public  static final DeferredItem<Item> VIGIL_EYE = ITEMS.register("vigil_eye",
            VigilEye::new);
    public static final DeferredItem<Item> SYMBIOTIC_REAGENT = ITEMS.register("symbiotic_reagent",
            () -> new BiologicalReagent(BiologicalReagent.AcceptedTypes.ALL_TYPES,Senchantments.SYMBIOTIC_RECONSTITUTION));
    public  static final DeferredItem<Item> CRYOGENIC_REAGENT = ITEMS.register("cryogenic_reagent",
            () -> new BiologicalReagent(BiologicalReagent.AcceptedTypes.WEAPON_TYPES,Senchantments.CRYOGENIC_ASPECT));
    public  static final DeferredItem<Item> GASTRIC_REAGENT = ITEMS.register("gastric_reagent",
            () -> new BiologicalReagent(BiologicalReagent.AcceptedTypes.WEAPON_TYPES,Senchantments.GASTRIC_SPEWAGE));
    public  static final DeferredItem<Item> CORROSIVE_REAGENT = ITEMS.register("corrosive_reagent",
            () -> new BiologicalReagent(BiologicalReagent.AcceptedTypes.WEAPON_TYPES,Senchantments.CORROSIVE_POTENCY));
    public  static final DeferredItem<Item> SERRATED_REAGENT = ITEMS.register("serrated_reagent",
            () -> new BiologicalReagent(BiologicalReagent.AcceptedTypes.ARMOR_TYPES,Senchantments.SERRATED_THORNS));
    public  static final DeferredItem<Item> VORACIOUS_REAGENT = ITEMS.register("voracious_reagent",
            () -> new BiologicalReagent(BiologicalReagent.AcceptedTypes.ALL_TYPES,Senchantments.VORACIOUS_MAW));
    public  static final DeferredItem<Item> INF_HELMET = ITEMS.register("inf_helmet",
            InfectedHelmet::new);
    public  static final DeferredItem<Item> INF_CHEST = ITEMS.register("inf_chest",
            InfectedChestplate::new);
    public  static final DeferredItem<Item> INF_PANTS = ITEMS.register("inf_pants",
            InfectedLeggings::new);
    public  static final DeferredItem<Item> INF_BOOTS = ITEMS.register("inf_boots",
            InfectedBoots::new);
    public  static final DeferredItem<Item> PLATED_HELMET = ITEMS.register("plated_helmet",
            PlatedHelmet::new);
    public  static final DeferredItem<Item> PLATED_CHEST = ITEMS.register("plated_chest",
            PlatedChestplate::new);
    public  static final DeferredItem<Item> PLATED_PANTS = ITEMS.register("plated_pants",
            PlatedLeggings::new);
    public  static final DeferredItem<Item> PLATED_BOOTS = ITEMS.register("plated_boots",
            PlatedBoots::new);
    public  static final DeferredItem<Item> LIVING_HELMET = ITEMS.register("living_helmet",
            LivingHelmet::new);
    public  static final DeferredItem<Item> LIVING_CHEST = ITEMS.register("living_chest",
            LivingChestplate::new);
    public  static final DeferredItem<Item> LIVING_PANTS = ITEMS.register("living_pants",
            LivingLeggings::new);
    public  static final DeferredItem<Item> LIVING_BOOTS = ITEMS.register("living_boots",
            LivingBoots::new);
    public  static final DeferredItem<Item> R_ELYTRON = ITEMS.register("r_elytron", Elytron.InfectedElytron::new);
    public  static final DeferredItem<Item> INF_UP_HELMET = ITEMS.register("inf_up_helmet",
            UpgradedInfectedExoskeleton.InfectedUpHelmet::new);
    public  static final DeferredItem<Item> INF_UP_CHESTPLATE = ITEMS.register("inf_up_chest",
            UpgradedInfectedExoskeleton.InfectedUpChestplate::new);
    public  static final DeferredItem<Item> INF_UP_PANTS = ITEMS.register("inf_up_pants",
            UpgradedInfectedExoskeleton.InfectedUpPants::new);
    public  static final DeferredItem<Item> INF_UP_BOOTS = ITEMS.register("inf_up_boots",
            UpgradedInfectedExoskeleton.InfectedUpBoots::new);

    public  static final DeferredItem<Item> CORRUPTED_RECORD = ITEMS.register("corrupted_record",
            () -> new BaseItem2(new Item.Properties().stacksTo(1).jukeboxPlayable(Ssounds.CORRUPTED_RECORD_SONG_KEY)));
    public  static final DeferredItem<Item> FORGOTTEN_RECORD = ITEMS.register("forgotten_record",
            () -> new BaseItem2(new Item.Properties().stacksTo(1).jukeboxPlayable(Ssounds.FORGOTTEN_PATIENT_SONG_KEY)));
    public  static final DeferredItem<Item> FORSAKEN_RECORD = ITEMS.register("forsaken_record",
            () -> new BaseItem2(new Item.Properties().stacksTo(1).jukeboxPlayable(Ssounds.FORSAKEN_FUTURE_SONG_KEY)));
    public  static final DeferredItem<Item> BUCKET_OF_BILE = ITEMS.register("bucket_of_bile",
            () -> new SporeBucket(Sfluids.BILE_FLUID_FLOWING.get(),new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public  static final DeferredItem<Item> GAS_MASK = ITEMS.register("gas_mask",
            GasMaskItem::new);
    public  static final DeferredItem<Item> SCANNER = ITEMS.register("scanner",
            () -> new ScannerItem( new Item.Properties().stacksTo(1)));
    public  static final DeferredItem<Item> BIOMASS = ITEMS.register("biomass",
            Biomass::new);
    public  static final DeferredItem<Item> CIRCUIT_BOARD = ITEMS.register("circuit_board",
            () -> new BaseItem2( new Item.Properties()));
    public  static final DeferredItem<Item> ICE_CANISTER = ITEMS.register("ice_canister",
            () -> new BaseItem2( new Item.Properties()));
    public  static final DeferredItem<Item> DOCUMENTS = ITEMS.register("documents",
            () -> new BaseItem2(new Item.Properties()));
    public  static final DeferredItem<Item> AMETHYST_DUST = ITEMS.register("amethyst_dust",
            () -> new BaseItem2( new Item.Properties()));
    public  static final DeferredItem<Item> COMPOUND = ITEMS.register("compound",
            () -> new BaseItem2( new Item.Properties()));
    public  static final DeferredItem<Item> COMPOUND_PLATE = ITEMS.register("compound_plate",
            () -> new BaseItem2( new Item.Properties()));
    public  static final DeferredItem<Item> HARDENING_AGENT = ITEMS.register("hardening_agent",
            HardeningAgent::new);
    public  static final DeferredItem<Item> SHARPENING_AGENT = ITEMS.register("sharpening_agent",
            SharpeningAgent::new);
    public  static final DeferredItem<Item> INTEGRATING_AGENT = ITEMS.register("integrating_agent",
            ConnectingAgent::new);
    public  static final DeferredItem<Item> MUTATION_SYRINGE = ITEMS.register("mutation_syringe",
            MutationSyringe::new);
    public  static final DeferredItem<Item> SYRINGE = ITEMS.register("syringe",
            Syringe::new);
    public  static final DeferredItem<Item> VAMPIRIC_SYRINGE = ITEMS.register("vampiric_syringe",
            () -> new WeaponSyringe(SporeToolsMutations.VAMPIRIC));
    public  static final DeferredItem<Item> CALCIFIED_SYRINGE = ITEMS.register("calcified_syringe",
            () -> new WeaponSyringe(SporeToolsMutations.CALCIFIED));
    public  static final DeferredItem<Item> BEZERK_SYRINGE = ITEMS.register("bezerk_syringe",
            () -> new WeaponSyringe(SporeToolsMutations.BEZERK));
    public  static final DeferredItem<Item> TOXIC_SYRINGE = ITEMS.register("toxic_syringe",
            () -> new WeaponSyringe(SporeToolsMutations.TOXIC));
    public  static final DeferredItem<Item> ROTTEN_SYRINGE = ITEMS.register("rotten_syringe",
            () -> new WeaponSyringe(SporeToolsMutations.ROTTEN));
    public  static final DeferredItem<Item> REINFORCED_SYRINGE = ITEMS.register("reinforced_syringe",
            () -> new ArmorSyringe(SporeArmorMutations.REINFORCED));
    public  static final DeferredItem<Item> SKELETAL_SYRINGE = ITEMS.register("skeletal_syringe",
            () -> new ArmorSyringe(SporeArmorMutations.SKELETAL));
    public  static final DeferredItem<Item> DROWNED_SYRINGE = ITEMS.register("drowned_syringe",
            () -> new ArmorSyringe(SporeArmorMutations.DROWNED));
    public  static final DeferredItem<Item> CHARRED_SYRINGE = ITEMS.register("charred_syringe",
            () -> new ArmorSyringe(SporeArmorMutations.CHARRED));
    public  static final DeferredItem<Item> REAVER = ITEMS.register("reaver",
            Reaver::new);
    public  static final DeferredItem<Item> PCI = ITEMS.register("pci",
            PCI::new);
    public  static final DeferredItem<Item> SYRINGE_GUN = ITEMS.register("syringe_gun",
            SyringeGun::new);



    private static DeferredItem<Item> soup(DeferredHolder<Block, Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new SkullSoupItem(block.get()));
    }

    private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItemBase2(block.get(), new Item.Properties()));
    }

    private static DeferredItem<Item> Techblock(DeferredHolder<Block, Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItemBase(block.get(), new Item.Properties()));
    }

    private static DeferredItem<Item> Exceptions(DeferredHolder<Block, Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItemCBU(block.get()));
    }
    public static final DeferredItem<Item> HEART_PIE = block(Sblocks.HEART_PIE);
    public static final DeferredItem<Item> COOKED_TORSO = block(Sblocks.COOKED_TORSO);
    public static final DeferredItem<Item> SKULL_SOUP = soup(Sblocks.SKULL_SOUP);
    public static final DeferredItem<Item> CONTAINER = Techblock(Sblocks.CONTAINER);
    public static final DeferredItem<Item> CDU = Exceptions(Sblocks.CDU);
    public static final DeferredItem<Item> ZOAHOLIC = Exceptions(Sblocks.ZOAHOLIC);
    public static final DeferredItem<Item> INCUBATOR = Exceptions(Sblocks.INCUBATOR);
    public static final DeferredItem<Item> SURGERY_TABLE = Exceptions(Sblocks.SURGERY_TABLE);
    public static final DeferredItem<Item> CABINET = Techblock(Sblocks.CABINET);
    public static final DeferredItem<Item> LAB_BLOCK = Techblock(Sblocks.LAB_BLOCK);
    public static final DeferredItem<Item> LAB_BLOCK1 = Techblock(Sblocks.LAB_BLOCK1);
    public static final DeferredItem<Item> LAB_BLOCK2 = Techblock(Sblocks.LAB_BLOCK2);
    public static final DeferredItem<Item> LAB_BLOCK3 = Techblock(Sblocks.LAB_BLOCK3);
    public static final DeferredItem<Item> LAB_SLAB = Techblock(Sblocks.LAB_SLAB);
    public static final DeferredItem<Item> LAB_SLAB1 = Techblock(Sblocks.LAB_SLAB1);
    public static final DeferredItem<Item> LAB_SLAB2 = Techblock(Sblocks.LAB_SLAB2);
    public static final DeferredItem<Item> LAB_SLAB3 = Techblock(Sblocks.LAB_SLAB3);
    public static final DeferredItem<Item> LAB_STAIR = Techblock(Sblocks.LAB_STAIR);
    public static final DeferredItem<Item> IRON_LADDER = Techblock(Sblocks.IRON_LADDER);
    public static final DeferredItem<Item> LABORATORY_BED = Techblock(Sblocks.LABORATORY_BED);
    public static final DeferredItem<Item> VENT_PLATE = Techblock(Sblocks.VENT_PLATE);
    public static final DeferredItem<Item> RUSTED_VENT_PLATE = Techblock(Sblocks.RUSTED_VENT_PLATE);
    public static final DeferredItem<Item> VENT_DOOR = Techblock(Sblocks.VENT_DOOR);
    public static final DeferredItem<Item> REINFORCED_DOOR = Techblock(Sblocks.REINFORCED_DOOR);
    public static final DeferredItem<Item> FROZEN_REINFORCED_DOOR = Techblock(Sblocks.FROZEN_REINFORCED_DOOR);
    public static final DeferredItem<Item> RUSTED_REINFORCED_DOOR = Techblock(Sblocks.RUSTED_REINFORCED_DOOR);

    public static final DeferredItem<Item> GROWTHS_BIG = block(Sblocks.GROWTHS_BIG);
    public static final DeferredItem<Item> GROWTHS_SMALL = block(Sblocks.GROWTHS_SMALL);
    public static final DeferredItem<Item> BLOOM_G = block(Sblocks.BLOOM_G);
    public static final DeferredItem<Item> BLOOM_GG = block(Sblocks.BLOOM_GG);
    public static final DeferredItem<Item> FUNGAL_ROOTS = block(Sblocks.FUNGAL_ROOTS);
    public static final DeferredItem<Item> GROWTH_MYCELIUM = block(Sblocks.GROWTH_MYCELIUM);
    public static final DeferredItem<Item> FUNGAL_STEP_SAPLING = block(Sblocks.FUNGAL_STEM_SAPLING);
    public static final DeferredItem<Item> MYCELIUM_VEINS = block(Sblocks.MYCELIUM_VEINS);
    public static final DeferredItem<Item> BIOMASS_BULB = block(Sblocks.BIOMASS_BULB);

    public static final DeferredItem<Item> ROTTEN_LOG = block(Sblocks.ROTTEN_LOG);
    public static final DeferredItem<Item> ROTTEN_PLANKS = block(Sblocks.ROTTEN_PLANKS);
    public static final DeferredItem<Item> ROTTEN_STAIR = block(Sblocks.ROTTEN_STAIR);
    public static final DeferredItem<Item> ROTTEN_SLAB = block(Sblocks.ROTTEN_SLAB);
    public static final DeferredItem<Item> ROTTEN_SCRAPS = block(Sblocks.ROTTEN_SCRAPS);
    public static final DeferredItem<Item> ROTTEN_BRANCH = block(Sblocks.ROTTEN_BRANCH);
    public static final DeferredItem<Item> ROTTEN_BUSH = block(Sblocks.ROTTEN_BUSH);
    public static final DeferredItem<Item> ROTTEN_CROPS = block(Sblocks.ROTTEN_CROPS);
    public static final DeferredItem<Item> ROOTED_BIOMASS = block(Sblocks.ROOTED_BIOMASS);
    public static final DeferredItem<Item> BIOMASS_BLOCK = block(Sblocks.BIOMASS_BLOCK);
    public static final DeferredItem<Item> CALCIFIED_BIOMASS_BLOCK = block(Sblocks.CALCIFIED_BIOMASS_BLOCK);
    public static final DeferredItem<Item> SICKEN_BIOMASS_BLOCK = block(Sblocks.SICKEN_BIOMASS_BLOCK);
    public static final DeferredItem<Item> GASTRIC_BIOMASS = block(Sblocks.GASTRIC_BIOMASS);
    public static final DeferredItem<Item> MEMBRANE_BLOCK = block(Sblocks.MEMBRANE_BLOCK);
    public static final DeferredItem<Item> ROOTED_MYCELIUM = block(Sblocks.ROOTED_MYCELIUM);
    public static final DeferredItem<Item> CRUSTED_BILE = block(Sblocks.CRUSTED_BILE);
    public static final DeferredItem<Item> MYCELIUM_BLOCK = block(Sblocks.MYCELIUM_BLOCK);
    public static final DeferredItem<Item> MYCELIUM_SLAB = block(Sblocks.MYCELIUM_SLAB);
    public static final DeferredItem<Item> FUNGAL_SHELL = block(Sblocks.FUNGAL_SHELL);
    public static final DeferredItem<Item> ORGANITE = block(Sblocks.ORGANITE);
    public static final DeferredItem<Item> FROST_BURNED_BIOMASS = block(Sblocks.FROST_BURNED_BIOMASS);
    public static final DeferredItem<Item> FROZEN_REMAINS = block(Sblocks.FROZEN_REMAINS);

    public static final DeferredItem<Item> INFESTED_DEEPSLATE = block(Sblocks.INFESTED_DEEPSLATE);
    public static final DeferredItem<Item> INFESTED_DIRT = block(Sblocks.INFESTED_DIRT);
    public static final DeferredItem<Item> INFESTED_END_STONE = block(Sblocks.INFESTED_END_STONE);
    public static final DeferredItem<Item> INFESTED_GRAVEL = block(Sblocks.INFESTED_GRAVEL);
    public static final DeferredItem<Item> INFESTED_NETHERRACK = block(Sblocks.INFESTED_NETHERRACK);
    public static final DeferredItem<Item> INFESTED_SAND = block(Sblocks.INFESTED_SAND);
    public static final DeferredItem<Item> INFESTED_SOUL_SAND = block(Sblocks.INFESTED_SOUL_SAND);
    public static final DeferredItem<Item> INFESTED_STONE = block(Sblocks.INFESTED_STONE);
    public static final DeferredItem<Item> INFESTED_RED_SAND = block(Sblocks.INFESTED_RED_SAND);
    public static final DeferredItem<Item> INFESTED_CLAY = block(Sblocks.INFESTED_CLAY);
    public static final DeferredItem<Item> INFESTED_COBBLESTONE = block(Sblocks.INFESTED_COBBLESTONE);
    public static final DeferredItem<Item> INFESTED_COBBLED_DEEPSLATE = block(Sblocks.INFESTED_COBBLED_DEEPSLATE);
    public static final DeferredItem<Item> INFESTED_LABORATORY_BLOCK = block(Sblocks.INFESTED_LABORATORY_BLOCK);
    public static final DeferredItem<Item> INFESTED_LABORATORY_BLOCK1 = block(Sblocks.INFESTED_LABORATORY_BLOCK1);
    public static final DeferredItem<Item> INFESTED_LABORATORY_BLOCK2 = block(Sblocks.INFESTED_LABORATORY_BLOCK2);
    public static final DeferredItem<Item> INFESTED_LABORATORY_BLOCK3 = block(Sblocks.INFESTED_LABORATORY_BLOCK3);
    public static final DeferredItem<Item> OVERGROWN_SPAWNER = block(Sblocks.OVERGROWN_SPAWNER);
    public static final DeferredItem<Item> BRAIN_REMNANTS = block(Sblocks.BRAIN_REMNANTS);
    public static final DeferredItem<Item> OUTPOST_WATCHER = block(Sblocks.OUTPOST_WATCHER);

    public static final DeferredItem<Item> REMAINS = block(Sblocks.REMAINS);
    public static final DeferredItem<Item> BIOMASS_LUMP = block(Sblocks.BIOMASS_LUMP);
    public static final DeferredItem<Item> HIVE_SPAWN = block(Sblocks.HIVE_SPAWN);
}
