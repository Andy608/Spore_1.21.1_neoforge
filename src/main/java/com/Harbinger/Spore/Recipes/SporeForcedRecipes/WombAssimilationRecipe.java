package com.Harbinger.Spore.Recipes.SporeForcedRecipes;

import com.Harbinger.Spore.Sentities.VariantKeeper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.*;

public class WombAssimilationRecipe {
    public record Pair(String id,int variant){}
    public record Recipe(List<Pair> ids, ResourceLocation icon,String attribute){
        private record InnerPair(LivingEntity living,int variant){}
        public boolean canAssemble(Level level, LivingEntity entity){
            List<InnerPair> entities = new ArrayList<>();
            for (Pair pair : ids){
                Optional<EntityType<?>> type = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.parse(pair.id));
                if (type.isPresent()){
                    LivingEntity idEntity = (LivingEntity) type.get().create(level);
                    entities.add(new InnerPair(idEntity,pair.variant));
                }
            }
            for (InnerPair innerPair : entities){
                if (innerPair.living instanceof VariantKeeper && entity instanceof VariantKeeper testKeeper){
                    if (Objects.equals(innerPair.living.getEncodeId(), entity.getEncodeId()) && innerPair.variant == testKeeper.getTypeVariant()){
                        return true;
                    }
                }else {
                    if (Objects.equals(innerPair.living.getEncodeId(), entity.getEncodeId())){
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static final Recipe BALLISTIC_CALAMITY = new Recipe(List.of(new Pair("spore:spitter",1),new Pair("spore:busser",2),new Pair("spore:griefer",0),new Pair("spore:griefer",4))
            ,ResourceLocation.parse("spore:textures/gui/icons/ballistic.png"),"spore:ballistic");
    public static final Recipe CORROSIVE_CALAMITY = new Recipe(List.of(new Pair("spore:spitter",0),new Pair("spore:spitter",2),new Pair("spore:spitter",3),new Pair("spore:spitter",4))
            ,ResourceLocation.parse("spore:textures/mob_effect/corrosion.png"),"spore:corrosives");
    public static final Recipe GRINDING_CALAMITY = new Recipe(List.of(new Pair("spore:inf_vindicator",0),new Pair("spore:hvindicator",0),new Pair("spore:wendigo",0))
            ,ResourceLocation.parse("spore:textures/gui/icons/grinding.png"),"spore:grinding");
    public static final Recipe LOCAL_CALAMITY = new Recipe(List.of(new Pair("spore:braiomil",0),new Pair("spore:brot",0))
            ,ResourceLocation.parse("spore:textures/mob_effect/marker.png"),"spore:localization");
    public static final Recipe REJUVENATION_CALAMITY = new Recipe(List.of(new Pair("spore:volatile",0),new Pair("spore:mephitic",0),new Pair("spore:hevoker",0),new Pair("spore:inf_witch",0))
            ,ResourceLocation.parse("minecraft:textures/mob_effect/regeneration.png"),"spore:rejuvenation");
    public static final Recipe TOXIC_CALAMITY = new Recipe(List.of(new Pair("spore:busser",3),new Pair("spore:griefer",1),new Pair("spore:thorn",1))
            ,ResourceLocation.parse("minecraft:textures/mob_effect/poison.png"),"spore:toxicity");
    public static final Recipe LACERATION_CALAMITY = new Recipe(List.of(new Pair("spore:slasher",1),new Pair("spore:lacerator",0),new Pair("spore:knight",0))
            ,ResourceLocation.parse("spore:textures/gui/icons/laceration.png"),"spore:laceration");
    public static final List<Recipe> getWombAssimilationRecipes = List.of(
            BALLISTIC_CALAMITY,
            CORROSIVE_CALAMITY,
            GRINDING_CALAMITY,
            LOCAL_CALAMITY,
            REJUVENATION_CALAMITY,
            TOXIC_CALAMITY,
            LACERATION_CALAMITY
    );

    public static Recipe getUsableRecipe(Level level , LivingEntity living){
        for (Recipe recipe : getWombAssimilationRecipes){
            if (recipe.canAssemble(level,living)){
                return recipe;
            }
        }
        return null;
    }
}
