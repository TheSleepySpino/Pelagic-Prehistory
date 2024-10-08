package pelagic_prehistory.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.MultiItemValue;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.registries.ForgeRegistries;
import pelagic_prehistory.PPRegistry;
import pelagic_prehistory.recipe.InfuserRecipe;

import java.util.List;
import java.util.Optional;

public class VialItem extends Item {

    private final int color;

    public VialItem(final int color, Properties pProperties) {
        super(pProperties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        // determines the recipe base ingredient for this item and adds it to the tooltip
        if(pLevel != null && (pIsAdvanced.isAdvanced() || Screen.hasShiftDown())) {
            // locate the recipe, if any
            final Optional<InfuserRecipe> oRecipe = pLevel.getRecipeManager().getAllRecipesFor(PPRegistry.RecipeReg.INFUSING_TYPE.get()).stream()
                    .filter(p -> p.getIngredient().test(pStack)).findFirst();
            // validate recipe and base ingredient
            if(oRecipe.isPresent() && !oRecipe.get().getBase().isEmpty()) {
                // create a component for the recipe base ingredient
                final Ingredient base = oRecipe.get().getBase();
                Ingredient.Value value = Ingredient.valueFromJson(base.toJson().getAsJsonObject());
                final Component baseName;
                if(value instanceof Ingredient.TagValue tagBase) {
                    baseName = Component.translatable("item.pelagic_prehistory.vial.tooltip.tag", tagBase.serialize().get("tag").getAsString()).withStyle(ChatFormatting.WHITE);
                } else {
                    baseName = Component.empty().append(base.getItems()[0].getHoverName()).withStyle(ChatFormatting.WHITE);
                }
                // create a component for the infuser block
                final Component infuserName = PPRegistry.BlockReg.INFUSER.get().getName();
                // create a tooltip with all information
                final Component tooltip = Component.translatable("item.pelagic_prehistory.vial.tooltip", baseName, infuserName).withStyle(ChatFormatting.GRAY);
                pTooltipComponents.add(tooltip);
            }
        }
    }
}
