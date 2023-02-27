package pelagic_prehistory.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import pelagic_prehistory.PPRegistry;
import pelagic_prehistory.menu.AnalyzerMenu;
import pelagic_prehistory.recipe.AnalyzerRecipe;

import java.util.Optional;

public class AnalyzerBlockEntity extends BlockEntity implements Container, MenuProvider {

    private static final int CONTAINER_SIZE = 6;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> createUnSidedHandler());

    private final ContainerData data;
    private int progress;
    private int maxProgress = 90;

    public AnalyzerBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                switch (pIndex) {
                    case 0: return AnalyzerBlockEntity.this.progress;
                    case 1: return AnalyzerBlockEntity.this.maxProgress;
                    default: return -1;
                }
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0: AnalyzerBlockEntity.this.progress = pValue; break;
                    case 1: AnalyzerBlockEntity.this.maxProgress = pValue; break;
                    default: break;
                }
            }

            @Override
            public int getCount() {
                return AnalyzerMenu.CONTAINER_DATA_SIZE;
            }
        };
    }

    public void inventoryChanged() {
        if (getLevel() != null && !getLevel().isClientSide()) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.inventoryChanged();
    }

    // TICKING //

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AnalyzerBlockEntity blockEntity) {
        if(hasRecipe(level, blockEntity)) {
            // update progress
            blockEntity.progress = Math.min(blockEntity.progress + 1, blockEntity.maxProgress);
            if(!blockEntity.isFull() && blockEntity.progress >= blockEntity.maxProgress) {
                craftItem(level, blockEntity);
            }
        } else {
            blockEntity.resetProgress();
        }
    }

    // RECIPE //

    private static boolean hasRecipe(Level level, AnalyzerBlockEntity entity) {
        // create container with input items only
        final Container input = new SimpleContainer(entity.getItem(0));
        // locate matching recipe
        Optional<AnalyzerRecipe> oRecipe = level.getRecipeManager()
                .getRecipeFor(PPRegistry.RecipeReg.ANALYZING_TYPE.get(), input, level);
        return oRecipe.isPresent();
    }

    private static void craftItem(Level level, AnalyzerBlockEntity entity) {
        // create container with input items only
        final Container input = new SimpleContainer(entity.getItem(0));
        // locate matching recipe
        Optional<AnalyzerRecipe> oRecipe = level.getRecipeManager()
                .getRecipeFor(PPRegistry.RecipeReg.ANALYZING_TYPE.get(), input, level);
        if(oRecipe.isEmpty()) {
            return;
        }
        // assemble recipe
        final ItemStack output = oRecipe.get().assemble(input, entity.getLevel().getRandom());
        if(output.isEmpty()) {
            return;
        }
        // add recipe output to container
        final IItemHandler itemHandler = entity.itemHandler.orElse(EmptyHandler.INSTANCE);
        for(int i = 1, n = itemHandler.getSlots(); i < n; i++) {
            // insert item
            if(itemHandler.insertItem(i, output, false).isEmpty()) {
                // remove input
                entity.removeItem(0, 1);
                entity.resetProgress();
                break;
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    // CAPABILITY //

    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(this);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(() -> createUnSidedHandler());
    }

    // CONTAINER //

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    public void dropAllItems() {
        if (this.level != null && !this.level.isClientSide()) {
            Containers.dropContents(this.level, this.getBlockPos(), this.getInventory());
        }
        this.inventoryChanged();
    }

    public boolean isFull() {
        for(ItemStack itemStack : this.inventory) {
            if(itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0, n = getContainerSize(); i < n; i++) {
            if(!getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return pSlot >= 0 && pSlot < this.inventory.size() ? this.inventory.get(pSlot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(this.inventory, pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.inventory, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if (pSlot >= 0 && pSlot < this.inventory.size()) {
            this.inventory.set(pSlot, pStack);
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return pPlayer.position().closerThan(Vec3.atCenterOf(worldPosition), 8.0D);
        }
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
        this.setChanged();
    }

    // MENU PROVIDER //

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AnalyzerMenu(PPRegistry.MenuReg.ANALYZER.get(), pContainerId, pPlayerInventory, this, this.data, this);
    }

    // NBT //

    private static final String KEY_PROGRESS = "Progress";
    private static final String KEY_MAX_PROGRESS = "MaxProgress";

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt(KEY_PROGRESS);
        maxProgress = tag.getInt(KEY_MAX_PROGRESS);
        ContainerHelper.loadAllItems(tag, getInventory());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(KEY_PROGRESS, progress);
        tag.putInt(KEY_MAX_PROGRESS, maxProgress);
        ContainerHelper.saveAllItems(tag, getInventory());
    }
}
