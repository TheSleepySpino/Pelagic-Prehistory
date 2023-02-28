package pelagic_prehistory;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pelagic_prehistory.block.AnalyzerBlock;
import pelagic_prehistory.block.AnalyzerBlockEntity;
import pelagic_prehistory.block.InfuserBlock;
import pelagic_prehistory.block.InfuserBlockEntity;
import pelagic_prehistory.item.VialItem;
import pelagic_prehistory.menu.AnalyzerMenu;
import pelagic_prehistory.menu.InfuserMenu;
import pelagic_prehistory.recipe.AnalyzerRecipe;
import pelagic_prehistory.recipe.InfuserRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


@SuppressWarnings("unused")
public final class PPRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PelagicPrehistory.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PelagicPrehistory.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PelagicPrehistory.MODID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PelagicPrehistory.MODID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PelagicPrehistory.MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PelagicPrehistory.MODID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, PelagicPrehistory.MODID);

    public static void register() {
        BlockReg.register();
        ItemReg.register();
        BlockEntityReg.register();
        EntityReg.register();
        MenuReg.register();
        RecipeReg.register();
    }

    public static final class ItemReg {

        private static final List<RegistryObject<Item>> VIAL_ITEMS = new ArrayList<>();
        private static final List<RegistryObject<Item>> SPAWN_EGGS = new ArrayList<>();

        public static void register() {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
            VIAL_ITEMS.add(UNKNOWN_VIAL);
        }

        // CRAFTING MATERIALS //
        private static final FoodProperties CUTTLEFISH_FOOD = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build();
        public static final RegistryObject<Item> RAW_CUTTLEFISH = registerWithTab("raw_cuttlefish", () -> new Item(new Item.Properties().food(CUTTLEFISH_FOOD)));
        private static final FoodProperties CUTTLEFISH_STEW_FOOD = new FoodProperties.Builder().nutrition(8).saturationMod(0.3F).build();
        public static final RegistryObject<Item> CUTTLEFISH_STEW = registerWithTab("cuttlefish_stew", () -> new Item(new Item.Properties().food(CUTTLEFISH_STEW_FOOD)));
        public static final RegistryObject<Item> FOSSIL = registerWithTab("fossil", () -> new Item(new Item.Properties()));

        // VIALS //
        public static final RegistryObject<Item> GINGKO_TREE_VIAL = registerVial("gingko_tree", 0x0); // TODO color
        public static final RegistryObject<Item> BAWITIUS_VIAL = registerVialAndEgg("bawitius",0xb75194);
        public static final RegistryObject<Item> CLADOSELACHE_VIAL = registerVialAndEgg("cladoselache",0xa254a9);
        public static final RegistryObject<Item> CYMBOSPONDYLUS_VIAL = registerVialAndEgg("cymbospondylus",0x80872c);
        public static final RegistryObject<Item> DUNKLEOSTEUS_VIAL = registerVialAndEgg("dunkleosteus",0x3a9db3);
        public static final RegistryObject<Item> HENODUS_VIAL = registerVialAndEgg("henodus",0x977343);
        public static final RegistryObject<Item> LEPIDOTES_VIAL = registerVialAndEgg("lepidotes",0xb7bb65);
        public static final RegistryObject<Item> PLESIOSAURUS_VIAL = registerVialAndEgg("plesiosaurus", 0x429389);
        public static final RegistryObject<Item> PLIOSAURUS_VIAL = registerVialAndEgg("pliosaurus", 0x4e402c);
        public static final RegistryObject<Item> PROGNATHODON_VIAL = registerVialAndEgg("prognathodon", 0xa1ae75);
        public static final RegistryObject<Item> SHONISAURUS_VIAL = registerVialAndEgg("shonisaurus", 0x3a746b);
        public static final RegistryObject<Item> UNKNOWN_VIAL = ITEMS.register("unknown_vial", () -> new VialItem(0xc0c0c0, new Item.Properties()));

        /**
         * Creates a registry object for a block item and adds it to the mod creative tab
         * @param block the block
         * @return the registry object
         */
        private static RegistryObject<Item> registerBlockItem(final RegistryObject<Block> block) {
            return registerWithTab(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        }

        /**
         * Creates a registry object for the given vial item and adds it to the mod creative tab
         * @param name the registry name
         * @param color the vial color
         * @return the item registry object
         */
        private static RegistryObject<Item> registerVial(final String name, final int color) {
            final RegistryObject<Item> vial = registerWithTab(name + "_vial", () -> new VialItem(color, new Item.Properties().stacksTo(16)));
            VIAL_ITEMS.add(vial);
            return vial;
        }

        /**
         * Creates a registry object for the given vial item, egg item, spawn egg item,
         * and adds them to the correct creative tabs
         * @param name the registry name
         * @param color the vial color
         * @return the item registry object
         */
        private static RegistryObject<Item> registerVialAndEgg(final String name, final int color) {
            final RegistryObject<Item> vial = registerVial(name, color);
            /* TODO use entity type once all entities are added
            final RegistryObject<EntityType<?>> entityType = RegistryObject.create(new ResourceLocation(PelagicPrehistory.MODID, name), ForgeRegistries.ENTITY_TYPES);
            final RegistryObject<Item> egg = registerWithTab(name + "_egg", () -> new ForgeSpawnEggItem(entityType, color, 0xC0C0C0, new Item.Properties()));
            final RegistryObject<Item> spawnEgg = ITEMS.register(name + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, color, 0xC0C0C0, new Item.Properties()));
            SPAWN_EGGS.add(spawnEgg);*/
            final RegistryObject<Item> egg = registerWithTab(name + "_egg", () -> new Item(new Item.Properties()));
            final RegistryObject<Item> spawnEgg = ITEMS.register(name + "_spawn_egg", () -> new Item(new Item.Properties()));
            SPAWN_EGGS.add(spawnEgg);
            return vial;
        }

        /**
         * Creates a registry object for the given item and adds it to the mod creative tab
         * @param name the registry name
         * @param supplier the item supplier
         * @return the item registry object
         */
        private static RegistryObject<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
            final RegistryObject<Item> item = ITEMS.register(name, supplier);
            PPTab.add(item);
            return item;
        }

        public static List<RegistryObject<Item>> getVialItems() {
            return ImmutableList.copyOf(VIAL_ITEMS);
        }

        public static List<RegistryObject<Item>> getSpawnEggs() {
            return ImmutableList.copyOf(SPAWN_EGGS);
        }

    }

    public static final class BlockReg {

        public static void register() {
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final RegistryObject<Block> ANALYZER = registerWithItem("analyzer", () ->
                new AnalyzerBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.METAL)));
        public static final RegistryObject<Block> INFUSER = registerWithItem("infuser", () ->
                new InfuserBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.METAL)));
        public static final RegistryObject<Block> ANCIENT_SEDIMENT = registerBlockSlabStairsWallPlateButton("ancient_sediment", BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE));
        public static final RegistryObject<Block> ANCIENT_SEDIMENT_BRICKS = registerWithItem("ancient_sediment_bricks", () ->
                new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));
        public static final RegistryObject<Block> ANCIENT_SEDIMENT_FOSSIL = registerWithItem("ancient_sediment_fossil", () ->
                new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(4.0F, 8.0F).sound(SoundType.DEEPSLATE)));
        public static final RegistryObject<Block> ANCIENT_SEDIMENT_TABLETS = registerWithItem("ancient_sediment_tablets", () ->
                new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));
        public static final RegistryObject<Block> GINGKO_SAPLING = registerWithItem("gingko_sapling", () ->
                new Block(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));


        private static RegistryObject<Block> registerWithItem(final String name, final Supplier<Block> supplier) {
            final RegistryObject<Block> block = BLOCKS.register(name, supplier);
            ItemReg.registerBlockItem(block);
            return block;
        }

        private static RegistryObject<Block> registerBlockSlabStairsWallPlateButton(final String name, final BlockBehaviour.Properties properties) {
            final RegistryObject<Block> block = registerWithItem(name, () -> new Block(properties));
            final RegistryObject<Block> slab = registerWithItem(name + "_slab", () -> new SlabBlock(properties));
            final RegistryObject<Block> stairs = registerWithItem(name + "_stairs", () -> new StairBlock(() -> block.get().defaultBlockState(), properties));
            final RegistryObject<Block> walls = registerWithItem(name + "_wall", () -> new WallBlock(properties));
            final RegistryObject<Block> pressurePlate = registerWithItem(name + "_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, properties, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON));
            final RegistryObject<Block> button = registerWithItem(name + "_button", () -> new ButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.STONE), 20, false, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
            return block;
        }
    }

    public static final class BlockEntityReg {

        public static void register() {
            BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

       public static final RegistryObject<BlockEntityType<AnalyzerBlockEntity>> ANALYZER = BLOCK_ENTITY_TYPES.register("analyzer", () ->
                BlockEntityType.Builder.of((pos, state) -> new AnalyzerBlockEntity(BlockEntityReg.ANALYZER.get(), pos, state),
                                BlockReg.ANALYZER.get())
                        .build(null));

        public static final RegistryObject<BlockEntityType<InfuserBlockEntity>> INFUSER = BLOCK_ENTITY_TYPES.register("infuser", () ->
                BlockEntityType.Builder.of((pos, state) -> new InfuserBlockEntity(BlockEntityReg.INFUSER.get(), pos, state),
                                BlockReg.INFUSER.get())
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

        public static final RegistryObject<MenuType<AnalyzerMenu>> ANALYZER = MENU_TYPES.register("analyzer", () ->
                IForgeMenuType.create(((windowId, inv, data) -> {
                    final BlockPos pos = data.readBlockPos();
                    return new AnalyzerMenu(MenuReg.ANALYZER.get(), windowId, inv, (AnalyzerBlockEntity) inv.player.level.getBlockEntity(pos));
                })
            )
        );

        public static final RegistryObject<MenuType<InfuserMenu>> INFUSER = MENU_TYPES.register("infuser", () ->
                IForgeMenuType.create(((windowId, inv, data) -> {
                            final BlockPos pos = data.readBlockPos();
                            return new InfuserMenu(MenuReg.INFUSER.get(), windowId, inv, (InfuserBlockEntity) inv.player.level.getBlockEntity(pos));
                        })
                )
        );
    }

    public static final class RecipeReg {

        public static void register() {
            RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
            RECIPE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final RegistryObject<RecipeType<AnalyzerRecipe>> ANALYZING_TYPE = RECIPE_TYPES.register("analyzing", () -> RecipeType.simple(new ResourceLocation(PelagicPrehistory.MODID, "analyzing")));
        public static final RegistryObject<RecipeType<InfuserRecipe>> INFUSING_TYPE = RECIPE_TYPES.register("infusing", () -> RecipeType.simple(new ResourceLocation(PelagicPrehistory.MODID, "infusing")));

        public static final RegistryObject<RecipeSerializer<AnalyzerRecipe>> ANALYZING_SERIALIZER = RECIPE_SERIALIZERS.register("analyzing", () -> new AnalyzerRecipe.Serializer());
        public static final RegistryObject<RecipeSerializer<InfuserRecipe>> INFUSING_SERIALIZER = RECIPE_SERIALIZERS.register("infusing", () -> new InfuserRecipe.Serializer());
    }
}
