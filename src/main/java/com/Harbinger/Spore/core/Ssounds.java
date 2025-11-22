package com.Harbinger.Spore.core;

import com.Harbinger.Spore.Spore;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Ssounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Spore.MODID);
    public static void register(IEventBus eventBus) {SOUNDS.register(eventBus);}

    public static final DeferredRegister<JukeboxSong> SONGS =
            DeferredRegister.create(Registries.JUKEBOX_SONG, Spore.MODID);
    public static void registerSong(IEventBus eventBus) {SONGS.register(eventBus);}

    private static Holder<SoundEvent> soundRegistry(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Spore.MODID, id)));
    }
    ///The sound events
    public static final Holder<SoundEvent> CORRUPTED_RECORD = soundRegistry("corrupted_record");

    public static final Holder<SoundEvent> FORGOTTEN_PATIENT = soundRegistry("forgotten_patient");

    public static final Holder<SoundEvent> FORSAKEN_FUTURE = soundRegistry("forsaken_future");
    ///SongRegistry
    public static final Supplier<JukeboxSong> CORRUPTED_RECORD_SONG =
            SONGS.register("corrupted_record", () -> new JukeboxSong(CORRUPTED_RECORD,
                    Component.translatable("item.spore.corrupted_record.desc"),
                    1680, 1));
    public static final Supplier<JukeboxSong> FORGOTTEN_PATIENT_SONG =
            SONGS.register("forgotten_record", () -> new JukeboxSong(
                    FORGOTTEN_PATIENT,
                    Component.translatable("item.spore.forgotten_record.desc"),
                    1100, 1));
    public static final Supplier<JukeboxSong> FORSAKEN_FUTURE_SONG =
            SONGS.register("forsaken_record", () -> new JukeboxSong(
                    FORSAKEN_FUTURE,
                    Component.translatable("item.spore.forsaken_record.desc"),
                    1200, 1));

    ///SongKeys
    public static final ResourceKey<JukeboxSong> CORRUPTED_RECORD_SONG_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "corrupted_record"));

    public static final ResourceKey<JukeboxSong> FORGOTTEN_PATIENT_SONG_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "forgotten_patient"));

    public static final ResourceKey<JukeboxSong> FORSAKEN_FUTURE_SONG_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "forsaken_future"));
    ///Hate this fuck you mojang
    public static final Holder<SoundEvent> NUKE = soundRegistry("nuke");

    public static final Holder<SoundEvent> AREA_AMBIENT = soundRegistry("spore_area_ambient");

    public static final Holder<SoundEvent> REBIRTH = soundRegistry("rebirth");

    public static final Holder<SoundEvent> INF_DAMAGE = soundRegistry("inf_damage");

    public static final Holder<SoundEvent> INF_GROWL = soundRegistry("inf_growl");

    public static final Holder<SoundEvent> HOWLER_GROWL = soundRegistry("howler_growl");

    public static final Holder<SoundEvent> INF_VILLAGER_DAMAGE = soundRegistry("inf_villager_damage");

    public static final Holder<SoundEvent> INF_VILLAGER_GROWL = soundRegistry("inf_villager_growl");

    public static final Holder<SoundEvent> INF_VILLAGER_DEATH = soundRegistry("inf_villager_death");

    public static final Holder<SoundEvent> INF_EVOKER_DAMAGE = soundRegistry("inf_evoker_damage");

    public static final Holder<SoundEvent> INF_EVOKER_GROWL = soundRegistry("inf_evoker_growl");

    public static final Holder<SoundEvent> INF_EVOKER_DEATH = soundRegistry("inf_evoker_death");

    public static final Holder<SoundEvent> BRAIOMIL_ATTACK = soundRegistry("braiomil_attack");

    public static final Holder<SoundEvent> SIEGER_AMBIENT = soundRegistry("sieger_ambient");

    public static final Holder<SoundEvent> SIEGER_BITE = soundRegistry("sieger_bite");

    public static final Holder<SoundEvent> GAZEN_AMBIENT = soundRegistry("gazen_ambient");

    public static final Holder<SoundEvent> HINDEN_AMBIENT = soundRegistry("hinden_ambient");

    public static final Holder<SoundEvent> BRAUREI_AMBIENT = soundRegistry("braurei_ambient");

    public static final Holder<SoundEvent> HINDEN_NUKE = soundRegistry("hinden_nuke");

    public static final Holder<SoundEvent> SONAR = soundRegistry("sonar");

    public static final Holder<SoundEvent> LANDING = soundRegistry("landing");

    public static final Holder<SoundEvent> HOWITZER_AMBIENT = soundRegistry("howitzer_ambient");

    public static final Holder<SoundEvent> FALLING_BOMB = soundRegistry("falling_bomb");

    public static final Holder<SoundEvent> SIGNAL = soundRegistry("signal");

    public static final Holder<SoundEvent> UMARMER_AMBIENT = soundRegistry("umarmer_ambient");

    public static final Holder<SoundEvent> VIGIL_AMBIENT = soundRegistry("vigil_ambient");

    public static final Holder<SoundEvent> WENDIGO_AMBIENT = soundRegistry("wendigo_ambient");

    public static final Holder<SoundEvent> WENDIGO_SCREECH = soundRegistry("wendigo_screech");

    public static final Holder<SoundEvent> WOMB_AMBIENT = soundRegistry("womb_ambient");

    public static final Holder<SoundEvent> USURPER_AMBIENT = soundRegistry("usurper_ambient");

    public static final Holder<SoundEvent> INQUISITOR_AMBIENT = soundRegistry("inquisitor_ambient");

    public static final Holder<SoundEvent> BROT_AMBIENT = soundRegistry("brot_ambient");

    public static final Holder<SoundEvent> DELUSIONER_AMBIENT = soundRegistry("delusioner_ambient");

    public static final Holder<SoundEvent> DELUSIONER_CASTING = soundRegistry("delusioner_casting");

    public static final Holder<SoundEvent> BIOBLOB = soundRegistry("bioblob");

    public static final Holder<SoundEvent> SPIT = soundRegistry("spit");

    public static final Holder<SoundEvent> MADNESS = soundRegistry("madness");

    public static final Holder<SoundEvent> LIMB_SLASH = soundRegistry("limb_slash");

    public static final Holder<SoundEvent> PROTO_AMBIENT = soundRegistry("proto_ambient");

    public static final Holder<SoundEvent> FUNGAL_BURST = soundRegistry("fungal_burst");

    public static final Holder<SoundEvent> HEART_BEAT = soundRegistry("heart_beat");

    public static final Holder<SoundEvent> PUFF = soundRegistry("puff");

    public static final Holder<SoundEvent> PRINTING = soundRegistry("printing");

    public static final Holder<SoundEvent> GAST_AMBIENT = soundRegistry("gast_ambient");

    public static final Holder<SoundEvent> SAW_SOUND = soundRegistry("saw_sound");

    public static final Holder<SoundEvent> ENGINE = soundRegistry("engine");

    public static final Holder<SoundEvent> SPECTER_AMBIENT = soundRegistry("specter_ambient");

    public static final Holder<SoundEvent> CONSTRUCT_AMBIENT = soundRegistry("construct_ambient");

    public static final Holder<SoundEvent> SCAVENGER_SCREECH = soundRegistry("scavenger_screech");

    public static final Holder<SoundEvent> BROKEN_SCREAMS = soundRegistry("broken_screams");

    public static final Holder<SoundEvent> HYPER_EVOLVE = soundRegistry("hyper_evolve");

    public static final Holder<SoundEvent> OGRE_AMBIENT = soundRegistry("ogre_ambient");

    public static final Holder<SoundEvent> CALAMITY_SPAWN = soundRegistry("calamity_spawn");

    public static final Holder<SoundEvent> CALAMITY_INCOMING = soundRegistry("calamity_incoming");

    public static final Holder<SoundEvent> SURGERY = soundRegistry("surgery");

    public static final Holder<SoundEvent> EVOLVE_HURT = soundRegistry("evolve_hurt");

    public static final Holder<SoundEvent> HEVOKER_AMBIENT = soundRegistry("hevoker_ambient");

    public static final Holder<SoundEvent> HINDICATOR_AMBIENT = soundRegistry("hindicator_ambient");

    public static final Holder<SoundEvent> INFECTED_WEAPON_THROW = soundRegistry("infected_weapon_throw");

    public static final Holder<SoundEvent> INFECTED_WEAPON_HIT_ENTITY = soundRegistry("infected_weapon_hit_entity");

    public static final Holder<SoundEvent> INFECTED_WEAPON_HIT_BLOCK = soundRegistry("infected_weapon_hit_block");

    public static final Holder<SoundEvent> CDU_INSERT = soundRegistry("cdu_insert");

    public static final Holder<SoundEvent> CDU_AMBIENT = soundRegistry("cdu_ambient");

    public static final Holder<SoundEvent> CLEAVER_SPIN = soundRegistry("cleaver_spin");

    public static final Holder<SoundEvent> INFECTED_PICKAXE = soundRegistry("infected_pickaxe");

    public static final Holder<SoundEvent> REAVER_REAVE = soundRegistry("reaver_reave");

    public static final Holder<SoundEvent> SABER_LEAP = soundRegistry("saber_leap");

    public static final Holder<SoundEvent> SCANNER_ITEM = soundRegistry("scanner_item");

    public static final Holder<SoundEvent> SCANNER_EMPTY = soundRegistry("scanner_empty");

    public static final Holder<SoundEvent> SCANNER_MOB = soundRegistry("scanner_mob");

    public static final Holder<SoundEvent> VIGIL_EYE_USE = soundRegistry("vigil_eye_use");

    public static final Holder<SoundEvent> SYRINGE_SUCK = soundRegistry("syringe_suck");

    public static final Holder<SoundEvent> SYRINGE_INJECT = soundRegistry("syringe_inject");

    public static final Holder<SoundEvent> PCI_INJECT = soundRegistry("pci_inject");

    public static final Holder<SoundEvent> REAGENT = soundRegistry("reagent");

    public static final Holder<SoundEvent> INFECTED_GEAR_BREAK = soundRegistry("infected_gear_break");

    public static final Holder<SoundEvent> INFECTED_GEAR_EQUIP = soundRegistry("infected_gear_equip");

    public static final Holder<SoundEvent> SHIELD_BASH = soundRegistry("shield_bash");

    public static final Holder<SoundEvent> TUMOROID_EXPLOSION = soundRegistry("tumoroid_explosion");

    public static final Holder<SoundEvent> INF_VILLAGER_AMBIENT = soundRegistry("villager_ambient");

    public static final Holder<SoundEvent> INF_PILLAGER_AMBIENT = soundRegistry("pillager_ambient");

    public static final Holder<SoundEvent> ADVENTURER_AMBIENT = soundRegistry("adventurer_ambient");

    public static final Holder<SoundEvent> TRADER_AMBIENT = soundRegistry("trader_ambient");

    public static final Holder<SoundEvent> WITCH_AMBIENT = soundRegistry("witch_ambient");

    public static final Holder<SoundEvent> DROWNED_AMBIENT = soundRegistry("drowned_ambient");

    public static final Holder<SoundEvent> HUSK_AMBIENT = soundRegistry("husk_ambient");

    public static final Holder<SoundEvent> VINDICATOR_AMBIENT = soundRegistry("vindicator_ambient");

    public static final Holder<SoundEvent> SCAMPER_AMBIENT = soundRegistry("scamper_ambient");

    public static final Holder<SoundEvent> ORGANOID_DAMAGE = soundRegistry("organoid_damage");

    public static final Holder<SoundEvent> HYPER_DAMAGE = soundRegistry("hyper_damage");

    public static final Holder<SoundEvent> CALAMITY_DAMAGE = soundRegistry("calamity_damage");

    public static final Holder<SoundEvent> INEBRIATER_INJECT = soundRegistry("inebriater_inject");

    public static final Holder<SoundEvent> LACERATOR_AMBIENT = soundRegistry("lacerator_ambient");

    public static final Holder<SoundEvent> SAUGLING_AMBIENT = soundRegistry("saugling_ambient");

    public static final Holder<SoundEvent> PLAGUED_AMBIENT = soundRegistry("plagued_ambient");

    public static final Holder<SoundEvent> SAUGLING_CHEST_AMBIENT = soundRegistry("saugling_chest_ambient");

    public static final Holder<SoundEvent> SAUGLING_JUMPSCARE = soundRegistry("saugling_jumpscare");

    public static final Holder<SoundEvent> SCIENTIST_AMBIENT = soundRegistry("scientist_ambient");

    public static final Holder<SoundEvent> SPORE_BURST = soundRegistry("spore_burst");

    public static final Holder<SoundEvent> WORM_DIGGING = soundRegistry("worm_digging");

    public static final Holder<SoundEvent> HOHL_AMBIENT = soundRegistry("hohl_ambient");

    public static final Holder<SoundEvent> CALAMITY_DEATH = soundRegistry("calamity_death");

    public static final Holder<SoundEvent> SYRINGE_RELOAD = soundRegistry("syringe_reload");

    public static final Holder<SoundEvent> SYRINGE_SPIN = soundRegistry("syringe_spin");

    public static final Holder<SoundEvent> SYRINGE_SHOOT = soundRegistry("syringe_shoot");

    public static final Holder<SoundEvent> SYRINGE_GUN_INJECT = soundRegistry("syringe_gun_inject");

    private Ssounds() {
    }

}
