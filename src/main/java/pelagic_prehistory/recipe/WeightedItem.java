package pelagic_prehistory.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class WeightedItem extends WeightedEntry.IntrusiveBase {

    public static final WeightedItem EMPTY = new WeightedItem(ItemStack.EMPTY);

    public static final Codec<ItemStack> ITEM_OR_STACK_CODEC = Codec.either(ForgeRegistries.ITEMS.getCodec(), ItemStack.CODEC)
            .xmap(either -> either.map(ItemStack::new, Function.identity()),
                    stack -> stack.getCount() == 1 && !stack.hasTag() ? Either.left(stack.getItem()) : Either.right(stack));

    private static final Codec<WeightedItem> WEIGHTED_ITEM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ITEM_OR_STACK_CODEC.fieldOf("item").forGetter(WeightedItem::getItemStack),
            Weight.CODEC.optionalFieldOf("weight", Weight.of(1)).forGetter(WeightedItem::getWeight)
    ).apply(instance, WeightedItem::new));

    public static final Codec<WeightedItem> CODEC = Codec.either(ITEM_OR_STACK_CODEC, WEIGHTED_ITEM_CODEC)
            .xmap(either -> either.map(WeightedItem::new, Function.identity()),
                    weightedItem -> weightedItem.getWeight().asInt() == 1 ? Either.left(weightedItem.getItemStack()) : Either.right(weightedItem));

    private final ItemStack itemStack;

    public WeightedItem(ItemStack itemStack) {
        this(itemStack, Weight.of(1));
    }

    public WeightedItem(ItemStack itemStack, Weight pWeight) {
        super(pWeight);
        this.itemStack = itemStack;
        this.itemStack.setCount(1);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
