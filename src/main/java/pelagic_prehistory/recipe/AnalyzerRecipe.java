package pelagic_prehistory.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import pelagic_prehistory.PPRegistry;
import pelagic_prehistory.PelagicPrehistory;

import java.util.List;
import java.util.Optional;

public class AnalyzerRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Ingredient input;
    private final List<WeightedItem> results;

    public AnalyzerRecipe(final ResourceLocation id, Ingredient input, List<WeightedItem> results) {
        this.id = id;
        this.input = input;
        this.results = ImmutableList.copyOf(results);
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        // validate recipe has input
        if(input.isEmpty()) {
            return false;
        }
        // validate container has item
        if(pContainer.getContainerSize() < 1) {
            return false;
        }
        // check if items are the same
        return input.test(pContainer.getItem(0));
    }

    /**
     * @param pContainer the input container
     * @return the output item
     * @deprecated use {@link #assemble(Container, RandomSource)}
     */
    @Deprecated
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
        return PPRegistry.ItemReg.UNKNOWN_VIAL.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PPRegistry.RecipeReg.ANALYZING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PPRegistry.RecipeReg.ANALYZING_TYPE.get();
    }

    /**
     * @param container the container
     * @param random a random source
     * @return a randomly sampled item stack from the results list, may be empty
     */
    public ItemStack assemble(final Container container, final RandomSource random) {
        return Optional.ofNullable(WeightedUtil.sample(results, random)).orElse(WeightedItem.EMPTY).getItemStack();
    }

    public static class Serializer implements RecipeSerializer<AnalyzerRecipe> {

        private static final String INPUT = "input";
        private static final String RESULTS = "results";

        @Override
        public AnalyzerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            // parse input item
            final Ingredient input = Ingredient.fromJson(pSerializedRecipe.get(INPUT));
            // parse result items
            final List<WeightedItem> results = WeightedItem.CODEC.listOf().parse(JsonOps.INSTANCE, pSerializedRecipe.get(RESULTS))
                    .resultOrPartial(s -> PelagicPrehistory.LOGGER.error("[AnalyzerRecipe] Failed to parse recipe results \"" + pRecipeId + "\":\n" + s))
                    .orElse(List.of());
            // create recipe
            return new AnalyzerRecipe(pRecipeId, input, results);
        }

        @Override
        public @Nullable AnalyzerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            final Ingredient input = Ingredient.fromNetwork(pBuffer);
            final List<WeightedItem> results = pBuffer.readWithCodec(WeightedItem.CODEC.listOf());
            return new AnalyzerRecipe(pRecipeId, input, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AnalyzerRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeWithCodec(WeightedItem.CODEC.listOf(), pRecipe.results);
        }
    }
}
