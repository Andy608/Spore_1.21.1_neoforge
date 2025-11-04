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

    private static Supplier<SoundEvent> soundRegistry(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Spore.MODID, id)));
    }
    ///The sound events
    public static final Supplier<SoundEvent> CORRUPTED_RECORD = soundRegistry("corrupted_record");

    public static final Supplier<SoundEvent> FORGOTTEN_PATIENT = soundRegistry("forgotten_patient");

    public static final Supplier<SoundEvent> FORSAKEN_FUTURE = soundRegistry("forsaken_future");
    ///SongRegistry
    public static final Supplier<JukeboxSong> CORRUPTED_RECORD_SONG = () -> new JukeboxSong(Holder.direct(CORRUPTED_RECORD.get()),
            Component.translatable("item.spore.corrupted_record.desc"),1680,1);

    public static final Supplier<JukeboxSong> FORGOTTEN_PATIENT_SONG = () -> new JukeboxSong(Holder.direct(FORGOTTEN_PATIENT.get()),
            Component.translatable("item.spore.forgotten_record.desc"),1100,1);

    public static final Supplier<JukeboxSong> FORSAKEN_FUTURE_SONG = () -> new JukeboxSong(Holder.direct(FORSAKEN_FUTURE.get()),
            Component.translatable("item.spore.forsaken_record.desc"),1200,1);
    ///SongKeys
    public static final ResourceKey<JukeboxSong> CORRUPTED_RECORD_SONG_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "corrupted_record"));

    public static final ResourceKey<JukeboxSong> FORGOTTEN_PATIENT_SONG_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "forgotten_patient"));

    public static final ResourceKey<JukeboxSong> FORSAKEN_FUTURE_SONG_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Spore.MODID, "forsaken_future"));
    ///Hate this fuck you mojang
    public static final Supplier<SoundEvent> NUKE = soundRegistry("nuke");

    public static final Supplier<SoundEvent> AREA_AMBIENT = soundRegistry("spore_area_ambient");

    public static final Supplier<SoundEvent> REBIRTH = soundRegistry("rebirth");

    public static final Supplier<SoundEvent> INF_DAMAGE = soundRegistry("inf_damage");

    public static final Supplier<SoundEvent> INF_GROWL = soundRegistry("inf_growl");

    public static final Supplier<SoundEvent> HOWLER_GROWL = soundRegistry("howler_growl");

    public static final Supplier<SoundEvent> INF_VILLAGER_DAMAGE = soundRegistry("inf_villager_damage");

    public static final Supplier<SoundEvent> INF_VILLAGER_GROWL = soundRegistry("inf_villager_growl");

    public static final Supplier<SoundEvent> INF_VILLAGER_DEATH = soundRegistry("inf_villager_death");

    public static final Supplier<SoundEvent> INF_EVOKER_DAMAGE = soundRegistry("inf_evoker_damage");

    public static final Supplier<SoundEvent> INF_EVOKER_GROWL = soundRegistry("inf_evoker_growl");

    public static final Supplier<SoundEvent> INF_EVOKER_DEATH = soundRegistry("inf_evoker_death");

    public static final Supplier<SoundEvent> BRAIOMIL_ATTACK = soundRegistry("braiomil_attack");

    public static final Supplier<SoundEvent> SIEGER_AMBIENT = soundRegistry("sieger_ambient");

    public static final Supplier<SoundEvent> SIEGER_BITE = soundRegistry("sieger_bite");

    public static final Supplier<SoundEvent> GAZEN_AMBIENT = soundRegistry("gazen_ambient");

    public static final Supplier<SoundEvent> HINDEN_AMBIENT = soundRegistry("hinden_ambient");

    public static final Supplier<SoundEvent> BRAUREI_AMBIENT = soundRegistry("braurei_ambient");

    public static final Supplier<SoundEvent> HINDEN_NUKE = soundRegistry("hinden_nuke");

    public static final Supplier<SoundEvent> SONAR = soundRegistry("sonar");

    public static final Supplier<SoundEvent> LANDING = soundRegistry("landing");

    public static final Supplier<SoundEvent> HOWITZER_AMBIENT = soundRegistry("howitzer_ambient");

    public static final Supplier<SoundEvent> FALLING_BOMB = soundRegistry("falling_bomb");

    public static final Supplier<SoundEvent> SIGNAL = soundRegistry("signal");

    public static final Supplier<SoundEvent> UMARMER_AMBIENT = soundRegistry("umarmer_ambient");

    public static final Supplier<SoundEvent> VIGIL_AMBIENT = soundRegistry("vigil_ambient");

    public static final Supplier<SoundEvent> WENDIGO_AMBIENT = soundRegistry("wendigo_ambient");

    public static final Supplier<SoundEvent> WENDIGO_SCREECH = soundRegistry("wendigo_screech");

    public static final Supplier<SoundEvent> WOMB_AMBIENT = soundRegistry("womb_ambient");

    public static final Supplier<SoundEvent> USURPER_AMBIENT = soundRegistry("usurper_ambient");

    public static final Supplier<SoundEvent> INQUISITOR_AMBIENT = soundRegistry("inquisitor_ambient");

    public static final Supplier<SoundEvent> BROT_AMBIENT = soundRegistry("brot_ambient");

    public static final Supplier<SoundEvent> DELUSIONER_AMBIENT = soundRegistry("delusioner_ambient");

    public static final Supplier<SoundEvent> DELUSIONER_CASTING = soundRegistry("delusioner_casting");

    public static final Supplier<SoundEvent> BIOBLOB = soundRegistry("bioblob");

    public static final Supplier<SoundEvent> SPIT = soundRegistry("spit");

    public static final Supplier<SoundEvent> MADNESS = soundRegistry("madness");

    public static final Supplier<SoundEvent> LIMB_SLASH = soundRegistry("limb_slash");

    public static final Supplier<SoundEvent> PROTO_AMBIENT = soundRegistry("proto_ambient");

    public static final Supplier<SoundEvent> FUNGAL_BURST = soundRegistry("fungal_burst");

    public static final Supplier<SoundEvent> HEART_BEAT = soundRegistry("heart_beat");

    public static final Supplier<SoundEvent> PUFF = soundRegistry("puff");

    public static final Supplier<SoundEvent> PRINTING = soundRegistry("printing");

    public static final Supplier<SoundEvent> GAST_AMBIENT = soundRegistry("gast_ambient");

    public static final Supplier<SoundEvent> SAW_SOUND = soundRegistry("saw_sound");

    public static final Supplier<SoundEvent> ENGINE = soundRegistry("engine");

    public static final Supplier<SoundEvent> SPECTER_AMBIENT = soundRegistry("specter_ambient");

    public static final Supplier<SoundEvent> CONSTRUCT_AMBIENT = soundRegistry("construct_ambient");

    public static final Supplier<SoundEvent> SCAVENGER_SCREECH = soundRegistry("scavenger_screech");

    public static final Supplier<SoundEvent> BROKEN_SCREAMS = soundRegistry("broken_screams");

    public static final Supplier<SoundEvent> HYPER_EVOLVE = soundRegistry("hyper_evolve");

    public static final Supplier<SoundEvent> OGRE_AMBIENT = soundRegistry("ogre_ambient");

    public static final Supplier<SoundEvent> CALAMITY_SPAWN = soundRegistry("calamity_spawn");

    public static final Supplier<SoundEvent> CALAMITY_INCOMING = soundRegistry("calamity_incoming");

    public static final Supplier<SoundEvent> SURGERY = soundRegistry("surgery");

    public static final Supplier<SoundEvent> EVOLVE_HURT = soundRegistry("evolve_hurt");

    public static final Supplier<SoundEvent> HEVOKER_AMBIENT = soundRegistry("hevoker_ambient");

    public static final Supplier<SoundEvent> HINDICATOR_AMBIENT = soundRegistry("hindicator_ambient");

    public static final Supplier<SoundEvent> INFECTED_WEAPON_THROW = soundRegistry("infected_weapon_throw");

    public static final Supplier<SoundEvent> INFECTED_WEAPON_HIT_ENTITY = soundRegistry("infected_weapon_hit_entity");

    public static final Supplier<SoundEvent> INFECTED_WEAPON_HIT_BLOCK = soundRegistry("infected_weapon_hit_block");

    public static final Supplier<SoundEvent> CDU_INSERT = soundRegistry("cdu_insert");

    public static final Supplier<SoundEvent> CDU_AMBIENT = soundRegistry("cdu_ambient");

    public static final Supplier<SoundEvent> CLEAVER_SPIN = soundRegistry("cleaver_spin");

    public static final Supplier<SoundEvent> INFECTED_PICKAXE = soundRegistry("infected_pickaxe");

    public static final Supplier<SoundEvent> REAVER_REAVE = soundRegistry("reaver_reave");

    public static final Supplier<SoundEvent> SABER_LEAP = soundRegistry("saber_leap");

    public static final Supplier<SoundEvent> SCANNER_ITEM = soundRegistry("scanner_item");

    public static final Supplier<SoundEvent> SCANNER_EMPTY = soundRegistry("scanner_empty");

    public static final Supplier<SoundEvent> SCANNER_MOB = soundRegistry("scanner_mob");

    public static final Supplier<SoundEvent> VIGIL_EYE_USE = soundRegistry("vigil_eye_use");

    public static final Supplier<SoundEvent> SYRINGE_SUCK = soundRegistry("syringe_suck");

    public static final Supplier<SoundEvent> SYRINGE_INJECT = soundRegistry("syringe_inject");

    public static final Supplier<SoundEvent> PCI_INJECT = soundRegistry("pci_inject");

    public static final Supplier<SoundEvent> REAGENT = soundRegistry("reagent");

    public static final Supplier<SoundEvent> INFECTED_GEAR_BREAK = soundRegistry("infected_gear_break");

    public static final Supplier<SoundEvent> INFECTED_GEAR_EQUIP = soundRegistry("infected_gear_equip");

    public static final Supplier<SoundEvent> SHIELD_BASH = soundRegistry("shield_bash");

    public static final Supplier<SoundEvent> TUMOROID_EXPLOSION = soundRegistry("tumoroid_explosion");

    public static final Supplier<SoundEvent> INF_VILLAGER_AMBIENT = soundRegistry("villager_ambient");

    public static final Supplier<SoundEvent> INF_PILLAGER_AMBIENT = soundRegistry("pillager_ambient");

    public static final Supplier<SoundEvent> ADVENTURER_AMBIENT = soundRegistry("adventurer_ambient");

    public static final Supplier<SoundEvent> TRADER_AMBIENT = soundRegistry("trader_ambient");

    public static final Supplier<SoundEvent> WITCH_AMBIENT = soundRegistry("witch_ambient");

    public static final Supplier<SoundEvent> DROWNED_AMBIENT = soundRegistry("drowned_ambient");

    public static final Supplier<SoundEvent> HUSK_AMBIENT = soundRegistry("husk_ambient");

    public static final Supplier<SoundEvent> VINDICATOR_AMBIENT = soundRegistry("vindicator_ambient");

    public static final Supplier<SoundEvent> SCAMPER_AMBIENT = soundRegistry("scamper_ambient");

    public static final Supplier<SoundEvent> ORGANOID_DAMAGE = soundRegistry("organoid_damage");

    public static final Supplier<SoundEvent> HYPER_DAMAGE = soundRegistry("hyper_damage");

    public static final Supplier<SoundEvent> CALAMITY_DAMAGE = soundRegistry("calamity_damage");

    public static final Supplier<SoundEvent> INEBRIATER_INJECT = soundRegistry("inebriater_inject");

    public static final Supplier<SoundEvent> LACERATOR_AMBIENT = soundRegistry("lacerator_ambient");

    public static final Supplier<SoundEvent> SAUGLING_AMBIENT = soundRegistry("saugling_ambient");

    public static final Supplier<SoundEvent> PLAGUED_AMBIENT = soundRegistry("plagued_ambient");

    public static final Supplier<SoundEvent> SAUGLING_CHEST_AMBIENT = soundRegistry("saugling_chest_ambient");

    public static final Supplier<SoundEvent> SAUGLING_JUMPSCARE = soundRegistry("saugling_jumpscare");

    public static final Supplier<SoundEvent> SCIENTIST_AMBIENT = soundRegistry("scientist_ambient");

    public static final Supplier<SoundEvent> SPORE_BURST = soundRegistry("spore_burst");

    public static final Supplier<SoundEvent> WORM_DIGGING = soundRegistry("worm_digging");

    public static final Supplier<SoundEvent> HOHL_AMBIENT = soundRegistry("hohl_ambient");

    public static final Supplier<SoundEvent> CALAMITY_DEATH = soundRegistry("calamity_death");

    public static final Supplier<SoundEvent> SYRINGE_RELOAD = soundRegistry("syringe_reload");

    public static final Supplier<SoundEvent> SYRINGE_SPIN = soundRegistry("syringe_spin");

    public static final Supplier<SoundEvent> SYRINGE_SHOOT = soundRegistry("syringe_shoot");

    public static final Supplier<SoundEvent> SYRINGE_GUN_INJECT = soundRegistry("syringe_gun_inject");

    private Ssounds() {
    }

}
