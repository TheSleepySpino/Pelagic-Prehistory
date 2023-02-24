package pelagic_prehistory;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@SuppressWarnings("unused")
public final class PPRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PelagicPrehistory.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PelagicPrehistory.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PelagicPrehistory.MODID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PelagicPrehistory.MODID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PelagicPrehistory.MODID);

    public static void register() {
        BlockReg.register();
        ItemReg.register();
        BlockEntityReg.register();
        EntityReg.register();
        MenuReg.register();
    }

    public static final class ItemReg {

        public static void register() {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemReg::onTabBuildContents);
        }

        public static void onTabBuildContents(final CreativeModeTabEvent.BuildContents event) {

        }

        /*public static final RegistryObject<Item> SAMURAI_SPAWN_PLATFORM = ITEMS.register("samurai_spawn_platform", () ->
                new BlockItem(BlockReg.SAMURAI_SPAWN_PLATFORM.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

        public static final RegistryObject<Item> SAMURAI_SPAWN_EGG = ITEMS.register("samurai_spawn_egg", () ->
                new ForgeSpawnEggItem(EntityReg.SAMURAI, 0xFFFCCC, 0xA54700, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    */
    }

    public static final class BlockReg {

        public static void register() {
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final RegistryObject<Block> SAMURAI_SPAWN_PLATFORM = BLOCKS.register("samurai_spawn_platform", () ->
                new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));

    }

    public static final class BlockEntityReg {

        public static void register() {
            BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

       /* public static final RegistryObject<BlockEntityType<CashRegisterBlockEntity>> CASH_REGISTER = BLOCK_ENTITY_TYPES.register("cash_register", () ->
                BlockEntityType.Builder.of((pos, state) -> new CashRegisterBlockEntity(BlockEntityReg.CASH_REGISTER.get(), pos, state),
                                BlockReg.CASH_REGISTER.get(), BlockReg.CREATIVE_CASH_REGISTER.get())
                        .build(null));*/
    }

    public static final class EntityReg {

        public static void register() {
            ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
            FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityReg::onEntityAttributeCreation);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityReg::onRegisterSpawnPlacement);
        }

        public static void onEntityAttributeCreation(final EntityAttributeCreationEvent event) {
            //event.put(BIG_SHARK.get(), BigShark.createAttributes().build());
        }

        public static void onRegisterSpawnPlacement(final SpawnPlacementRegisterEvent event) {
            //event.register(BIG_SHARK.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BigShark::checkSharkSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        }

       /* public static final RegistryObject<EntityType<? extends Samurai>> SAMURAI = ENTITY_TYPES.register("samurai", () ->
                EntityType.Builder.<Samurai>of(Samurai::new, MobCategory.MISC)
                        .sized(1.46F, 3.74F)
                        .clientTrackingRange(8)
                        .fireImmune()
                        .build("samurai"));

        public static final RegistryObject<EntityType<? extends NinjaStar>> STAR = ENTITY_TYPES.register("star", () ->
                EntityType.Builder.<NinjaStar>of(NinjaStar::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F).noSummon().clientTrackingRange(4).updateInterval(10)
                        .build("star"));*/
    }

    public static final class MenuReg {

        public static void register() {
            MENU_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        /*public static final RegistryObject<MenuType<PocketMenu>> POCKET = MENU_TYPES.register("pocket", () ->
                IForgeMenuType.create((windowId, inv, data) -> {
                    final int entityId = data.readInt();
                    CompoundTag pocketTag = data.readNbt();
                    IPocket iPocket = inv.player.getCapability(PoliceModule.POCKET_CAPABILITY).orElse(PocketCapability.EMPTY);
                    if (iPocket != PocketCapability.EMPTY && pocketTag != null) {
                        iPocket.deserializeNBT(pocketTag);
                    }
                    return new PocketMenu(windowId, inv, iPocket.getInventory());
                })
        );*/
    }
}
