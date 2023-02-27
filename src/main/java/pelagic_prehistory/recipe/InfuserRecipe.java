package pelagic_prehistory.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;
import pelagic_prehistory.PPRegistry;

public class InfuserRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Ingredient input;
    private final Ingredient egg;
    private final ItemStack result;

    public InfuserRecipe(final ResourceLocation id, Ingredient input, Ingredient egg, ItemStack result) {
        this.id = id;
        this.input = input;
        this.egg = egg;
        this.result = result;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        // validate recipe has input
        if(input.isEmpty()) {
            return false;
        }
        // validate container has item
        if(pContainer.getContainerSize() < 2) {
            return false;
        }
        // check if items are the same
        return input.test(pContainer.getItem(0)) && egg.test(pContainer.getItem(1));
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PPRegistry.RecipeReg.INFUSING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PPRegistry.RecipeReg.INFUSING_TYPE.get();
    }


    public static class Serializer implements RecipeSerializer<InfuserRecipe> {

        private static final String INPUT = "input";
        private static final String EGG = "egg";
        private static final String RESULT = "result";

        @Override
        public InfuserRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            // parse input item
            final Ingredient input = Ingredient.fromJson(pSerializedRecipe.get(INPUT));
            final Ingredient egg = pSerializedRecipe.has(EGG) ? Ingredient.fromJson(pSerializedRecipe.get(EGG)) : Ingredient.of(Items.EGG);
            final ItemStack result = CraftingHelper.getItemStack(pSerializedRecipe.getAsJsonObject(RESULT), true, true);
            // create recipe
            return new InfuserRecipe(pRecipeId, input, egg, result);
        }

        @Override
        public @Nullable InfuserRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            final Ingredient input = Ingredient.fromNetwork(pBuffer);
            final Ingredient egg = Ingredient.fromNetwork(pBuffer);
            final ItemStack result = pBuffer.readItem();
            return new InfuserRecipe(pRecipeId, input, egg, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, InfuserRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pRecipe.egg.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
        }
    }
}
