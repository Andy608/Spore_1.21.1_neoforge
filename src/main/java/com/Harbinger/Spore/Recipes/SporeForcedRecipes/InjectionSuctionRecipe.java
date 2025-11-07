package com.Harbinger.Spore.Recipes.SporeForcedRecipes;

import com.Harbinger.Spore.Sentities.VariantKeeper;
import com.Harbinger.Spore.core.Sitems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InjectionSuctionRecipe {
    public record Pair(String id,int variant){}
    public record Recipe(List<Pair> ids, Item output){
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

    public static final Recipe VAMPIRIC_SYRINGE = new Recipe(List.of(new Pair("spore:hevoker",1),new Pair("spore:volatile",2))
            , Sitems.VAMPIRIC_SYRINGE.get());

    public static final Recipe CALCIFIED_SYRINGE = new Recipe(List.of(new Pair("spore:knight",0),new Pair("spore:stalker",0))
            , Sitems.CALCIFIED_SYRINGE.get());

    public static final Recipe BEZERK_SYRINGE = new Recipe(List.of(new Pair("spore:inf_vindicator",0),new Pair("spore:wendigo",0))
            ,Sitems.BEZERK_SYRINGE.get());

    public static final Recipe TOXIC_SYRINGE = new Recipe(List.of(new Pair("spore:busser",3),new Pair("spore:griefer",1),new Pair("spore:thorn",1))
            ,Sitems.TOXIC_SYRINGE.get());

    public static final Recipe ROTTEN_SYRINGE = new Recipe(List.of(new Pair("spore:braiomil",0),new Pair("spore:brot",0),new Pair("spore:plagued",0))
            ,Sitems.ROTTEN_SYRINGE.get());

    public static final Recipe REINFORCED_SYRINGE = new Recipe(List.of(new Pair("spore:leaper",0),new Pair("spore:ogre",0))
            , Sitems.REINFORCED_SYRINGE.get());

    public static final Recipe SKELETAL_SYRINGE = new Recipe(List.of(new Pair("spore:inquisitor",0),new Pair("spore:nuclea",0))
            , Sitems.SKELETAL_SYRINGE.get());

    public static final Recipe DROWNED = new Recipe(List.of(new Pair("spore:inquisitor",0),new Pair("spore:nuclea",0))
            ,Sitems.DROWNED_SYRINGE.get());

    public static final Recipe CHARRED = new Recipe(List.of(new Pair("spore:griefer",4),new Pair("spore:umarmed",1))
            ,Sitems.CHARRED_SYRINGE.get());

    public static final List<Recipe> getInjectionList = List.of(
            VAMPIRIC_SYRINGE,
            CALCIFIED_SYRINGE,
            BEZERK_SYRINGE,
            TOXIC_SYRINGE,
            ROTTEN_SYRINGE,
            REINFORCED_SYRINGE,
            SKELETAL_SYRINGE,
            DROWNED,
            CHARRED
    );

    public static Recipe getUsableRecipe(Level level , LivingEntity living){
        for (Recipe recipe : getInjectionList){
            if (recipe.canAssemble(level,living)){
                return recipe;
            }
        }
        return null;
    }
}
