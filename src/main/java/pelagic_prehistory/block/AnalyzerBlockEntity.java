package pelagic_prehistory.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pelagic_prehistory.PPRegistry;
import pelagic_prehistory.menu.AnalyzerMenu;
import pelagic_prehistory.recipe.AnalyzerRecipe;

import java.util.Optional;

public class AnalyzerBlockEntity extends PPBlockEntityBase<AnalyzerRecipe> {

    public AnalyzerBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.maxProgress = 200;
    }

    // TICKING //

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AnalyzerBlockEntity blockEntity) {
        if(blockEntity.hasRecipe(level)) {
            // update progress
            blockEntity.progress = Math.min(blockEntity.progress + 1, blockEntity.maxProgress);
            if(!blockEntity.isFull() && blockEntity.progress >= blockEntity.maxProgress) {
                blockEntity.assembleRecipe(level);
            }
        } else {
            blockEntity.resetProgress();
        }
        // sound
        blockEntity.tickSound(level, blockPos);
    }

    @Override
    protected SoundEvent getSound() {
        // TODO analyzer sound
        return SoundEvents.CAMPFIRE_CRACKLE;
    }

    // BLOCK ENTITY BASE //

    @Override
    protected Container createInputContainer() {
        return new SimpleContainer(getItem(0));
    }

    @Override
    protected Optional<AnalyzerRecipe> getRecipeFor(Level level, Container input) {
        return level.getRecipeManager().getRecipeFor(PPRegistry.RecipeReg.ANALYZING_TYPE.get(), input, level);
    }

    @Override
    protected void assembleRecipe(Level level, Container input, AnalyzerRecipe recipe) {
        final ItemStack output = recipe.assemble(input, level.getRandom());
        if(output.isEmpty()) {
            return;
        }
        final IItemHandler itemHandler = this.itemHandler.orElse(EmptyHandler.INSTANCE);
        for(int i = 1, n = itemHandler.getSlots(); i < n; i++) {
            // insert item
            if(itemHandler.insertItem(i, output, false).isEmpty()) {
                // remove input
                this.removeItem(0, 1);
                this.resetProgress();
                break;
            }
        }
    }

    @Override
    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(this) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot == 0 ? stack.is(PPRegistry.ItemReg.FOSSIL.get()) : super.isItemValid(slot, stack);
            }

            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return slot == 0 ? ItemStack.EMPTY : super.extractItem(slot, amount, simulate);
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return /*slot > 0 || !stack.is(PPRegistry.ItemReg.FOSSIL.get()) ? stack : */super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    // MENU PROVIDER //

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AnalyzerMenu(PPRegistry.MenuReg.ANALYZER.get(), pContainerId, pPlayerInventory, this, this.data, this);
    }
}
