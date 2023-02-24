package pelagic_prehistory;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pelagic_prehistory.block.AnalyzerBlock;
import pelagic_prehistory.block.AnalyzerBlockEntity;
import pelagic_prehistory.block.InfuserBlock;
import pelagic_prehistory.item.VialItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

        private static final List<RegistryObject<Item>> VIAL_ITEMS = new ArrayList<>();
        private static CreativeModeTab tab;

        public static void register() {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemReg::onTabRegister);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemReg::onTabBuildContents);
        }

        public static void onTabRegister(final CreativeModeTabEvent.Register event) {
            tab = event.registerCreativeModeTab(new ResourceLocation(PelagicPrehistory.MODID, "tab"), b -> b
                    .title(Component.translatable("itemGroup." + PelagicPrehistory.MODID))
                    .icon(Suppliers.memoize(() -> new ItemStack(PLESIOSAURUS_VIAL.get()))));
        }

        public static void onTabBuildContents(final CreativeModeTabEvent.BuildContents event) {
            if(event.getTab() == CreativeModeTabs.NATURAL_BLOCKS) {
                event.accept(ANCIENT_SEDIMENT_BLOCK);
                event.accept(ANCIENT_SEDIMENT_BRICKS_BLOCK);
                event.accept(ANCIENT_SEDIMENT_FOSSIL_BLOCK);
                event.accept(ANCIENT_SEDIMENT_TABLETS_BLOCK);
            }
            if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) {
                event.accept(RAW_CUTTLEFISH);
                event.accept(CUTTLEFISH_STEW);
            }
            if(event.getTab() == tab) {
                // block items
                event.accept(ANCIENT_SEDIMENT_BLOCK);
                event.accept(ANCIENT_SEDIMENT_BRICKS_BLOCK);
                event.accept(ANCIENT_SEDIMENT_FOSSIL_BLOCK);
                event.accept(ANCIENT_SEDIMENT_TABLETS_BLOCK);
                event.accept(ANALYZER_BLOCK);
                event.accept(INFUSER_BLOCK);
                // crafting materials
                event.accept(FOSSIL);
                event.accept(RAW_CUTTLEFISH);
                event.accept(CUTTLEFISH_STEW);
                // vials
                event.accept(BAWITIUS_VIAL);
                event.accept(CLADOSELACHE_VIAL);
                event.accept(CYMBOSPONDYLUS_VIAL);
                event.accept(DUNKLEOSTEUS_VIAL);
                event.accept(HENODUS_VIAL);
                event.accept(LEPIDOTES_VIAL);
                event.accept(PLESIOSAURUS_VIAL);
                event.accept(PLIOSAURUS_VIAL);
                event.accept(PROGNATHODON_VIAL);
                event.accept(SHONISAURUS_VIAL);
            }
        }

        // CRAFTING MATERIALS //
        private static final FoodProperties CUTTLEFISH_FOOD = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build();
        public static final RegistryObject<Item> RAW_CUTTLEFISH = ITEMS.register("raw_cuttlefish", () -> new Item(new Item.Properties().food(CUTTLEFISH_FOOD)));
        private static final FoodProperties CUTTLEFISH_STEW_FOOD = new FoodProperties.Builder().nutrition(8).saturationMod(0.3F).build();
        public static final RegistryObject<Item> CUTTLEFISH_STEW = ITEMS.register("cuttlefish_stew", () -> new Item(new Item.Properties().food(CUTTLEFISH_STEW_FOOD)));
        public static final RegistryObject<Item> FOSSIL = ITEMS.register("fossil", () -> new Item(new Item.Properties()));

        // VIALS //
        public static final RegistryObject<Item> BAWITIUS_VIAL = registerVial("bawitius",0xb75194);
        public static final RegistryObject<Item> CLADOSELACHE_VIAL = registerVial("cladoselache",0xa254a9);
        public static final RegistryObject<Item> CYMBOSPONDYLUS_VIAL = registerVial("cymbospondylus",0x80872c);
        public static final RegistryObject<Item> DUNKLEOSTEUS_VIAL = registerVial("dunkleosteus",0x3a9db3);
        public static final RegistryObject<Item> HENODUS_VIAL = registerVial("henodus",0x977343);
        public static final RegistryObject<Item> LEPIDOTES_VIAL = registerVial("lepidotes",0xb7bb65);
        public static final RegistryObject<Item> PLESIOSAURUS_VIAL = registerVial("plesiosaurus", 0x429389);
        public static final RegistryObject<Item> PLIOSAURUS_VIAL = registerVial("pliosaurus", 0x4e402c);
        public static final RegistryObject<Item> PROGNATHODON_VIAL = registerVial("prognathodon", 0xa1ae75);
        public static final RegistryObject<Item> SHONISAURUS_VIAL = registerVial("shonisaurus", 0x3a746b);

        // BLOCK ITEMS //
        public static final RegistryObject<Item> ANALYZER_BLOCK = registerBlockItem(BlockReg.ANALYZER);
        public static final RegistryObject<Item> INFUSER_BLOCK = registerBlockItem(BlockReg.INFUSER);
        public static final RegistryObject<Item> ANCIENT_SEDIMENT_BLOCK = registerBlockItem(BlockReg.ANCIENT_SEDIMENT);
        public static final RegistryObject<Item> ANCIENT_SEDIMENT_BRICKS_BLOCK = registerBlockItem(BlockReg.ANCIENT_SEDIMENT_BRICKS);
        public static final RegistryObject<Item> ANCIENT_SEDIMENT_FOSSIL_BLOCK = registerBlockItem(BlockReg.ANCIENT_SEDIMENT_FOSSIL);
        public static final RegistryObject<Item> ANCIENT_SEDIMENT_TABLETS_BLOCK = registerBlockItem(BlockReg.ANCIENT_SEDIMENT_TABLETS);

        private static RegistryObject<Item> registerBlockItem(final RegistryObject<Block> block) {
            return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        }

        private static RegistryObject<Item> registerVial(final String name, final int color) {
            final RegistryObject<Item> item = ITEMS.register(name + "_vial", () -> new VialItem(color, new Item.Properties().stacksTo(16)));
            VIAL_ITEMS.add(item);
            return item;
        }

        public static List<RegistryObject<Item>> getVialItems() {
            return ImmutableList.copyOf(VIAL_ITEMS);
        }
    }

    public static final class BlockReg {

        public static void register() {
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final RegistryObject<Block> ANALYZER = BLOCKS.register("analyzer", () ->
                new AnalyzerBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.METAL)));
        public static final RegistryObject<Block> ANCIENT_SEDIMENT = BLOCKS.register("ancient_sediment", () ->
                new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));
        public static final RegistryObject<Block> ANCIENT_SEDIMENT_BRICKS = BLOCKS.register("ancient_sediment_bricks", () ->
                new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));
        public static final RegistryObject<Block> ANCIENT_SEDIMENT_FOSSIL = BLOCKS.register("ancient_sediment_fossil", () ->
                new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(4.0F, 8.0F).sound(SoundType.DEEPSLATE)));
        public static final RegistryObject<Block> ANCIENT_SEDIMENT_TABLETS = BLOCKS.register("ancient_sediment_tablets", () ->
                new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));
        public static final RegistryObject<Block> INFUSER = BLOCKS.register("infuser", () ->
                new InfuserBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.METAL)));

    }

    public static final class BlockEntityReg {

        public static void register() {
            BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

       public static final RegistryObject<BlockEntityType<AnalyzerBlockEntity>> ANALYZER = BLOCK_ENTITY_TYPES.register("analyzer", () ->
                BlockEntityType.Builder.of((pos, state) -> new AnalyzerBlockEntity(BlockEntityReg.ANALYZER.get(), pos, state),
                                BlockReg.ANALYZER.get())
                        .build(null));
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
                        .build("samurai"));*/
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
