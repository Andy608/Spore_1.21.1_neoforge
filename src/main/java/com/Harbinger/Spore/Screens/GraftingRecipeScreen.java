package com.Harbinger.Spore.Screens;

import com.Harbinger.Spore.Recipes.GraftingRecipe;
import com.Harbinger.Spore.SBlockEntities.SurgeryTableBlockEntity;
import com.Harbinger.Spore.Spore;
import com.Harbinger.Spore.core.Srecipes;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class GraftingRecipeScreen extends AbstractContainerScreen<GraftingRecipeMenu> implements TutorialMenuMethods{
    private static final ResourceLocation TEXTURE =ResourceLocation.fromNamespaceAndPath(Spore.MODID, "textures/gui/grafting_recipe_gui.png");
    private int currentItemIndex = 0;
    private Button leftButton;
    private Button rightButton;
    private final List<RecipeHolder<GraftingRecipe>> recipes;

    public GraftingRecipeScreen(GraftingRecipeMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 84;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null){
            this.recipes = new ArrayList<>();
        }else {
            this.recipes = level.getRecipeManager().getAllRecipesFor(Srecipes.GRAFTING_TYPE.get());
        }
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        int buttonY = topPos-20;
        int buttonX = leftPos+88;
        this.leftButton = addRenderableWidget(Button.builder(Component.literal("<"), button -> changeRecipe(-1))
                .bounds(buttonX-10, buttonY, 20, 20)
                .build()
        );

        this.rightButton = addRenderableWidget(Button.builder(Component.literal(">"), button -> changeRecipe(1))
                .bounds(buttonX+10, buttonY, 20, 20)
                .build()
        );
    }
    private void changeRecipe(int direction) {
        if (!recipes.isEmpty()) {
            currentItemIndex = (currentItemIndex + direction) % recipes.size();
            if (currentItemIndex < 0) {
                currentItemIndex += recipes.size();
            }
        }
    }
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics,mouseX,mouseY,delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        GraftingRecipe recipe = recipes.get(currentItemIndex).value();
        renderFakeItem(font,guiGraphics,getItemStackFromIngredient(recipe.getIngredients().get(SurgeryTableBlockEntity.GRATING_ITEM_ONE)), leftPos +25, topPos +8);
        renderFakeItem(font,guiGraphics,getItemStackFromIngredient(recipe.getIngredients().get(SurgeryTableBlockEntity.GRATING_INGREDIENT)), leftPos +25, topPos +35);
        renderFakeItem(font,guiGraphics,getItemStackFromIngredient(recipe.getIngredients().get(SurgeryTableBlockEntity.GRATING_ITEM_TWO)),leftPos +25, topPos +62);
        ItemStack stack = recipe.getResultItem(null);
        renderFakeItem(font,guiGraphics,stack, leftPos +88, topPos +35);
    }

    private ItemStack getItemStackFromIngredient(Ingredient ingredient){
        ItemStack[] itemStacks = ingredient.getItems();
        return itemStacks.length > 0 ? itemStacks[0] : ItemStack.EMPTY;
    }
}
