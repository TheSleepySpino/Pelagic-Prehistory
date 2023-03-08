package pelagic_prehistory;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pelagic_prehistory.block.AnalyzerBlock;
import pelagic_prehistory.block.AnalyzerBlockEntity;
import pelagic_prehistory.block.CharniaBlock;
import pelagic_prehistory.entity.Henodus;
import pelagic_prehistory.entity.Lepidotes;
import pelagic_prehistory.worldgen.GinkgoTreeFeature;
import pelagic_prehistory.worldgen.GinkgoTreeGrower;
import pelagic_prehistory.block.InfuserBlock;
import pelagic_prehistory.block.InfuserBlockEntity;
import pelagic_prehistory.entity.Dugong;
import pelagic_prehistory.entity.Plesiosaurus;
import pelagic_prehistory.entity.Pliosaurus;
import pelagic_prehistory.item.VialItem;
import pelagic_prehistory.menu.AnalyzerMenu;
import pelagic_prehistory.menu.InfuserMenu;
import pelagic_prehistory.recipe.AnalyzerRecipe;
import pelagic_prehistory.recipe.InfuserRecipe;
import pelagic_prehistory.worldgen.LocStructureProcessor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


@SuppressWarnings("unused")
public final class PPRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PelagicPrehistory.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PelagicPrehistory.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PelagicPrehistory.MODID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PelagicPrehistory.MODID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PelagicPrehistory.MODID);
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PelagicPrehistory.MODID);
    private static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PROCESSOR.key(), PelagicPrehistory.MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PelagicPrehistory.MODID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, PelagicPrehistory.MODID);

    public static void register() {
        BlockReg.register();
        ItemReg.register();
        BlockEntityReg.register();
        EntityReg.register();
        FeatureReg.register();
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

        // SPAWN EGGS //
        public static final RegistryObject<Item> DUGONG_SPAWN_EGG = registerSpawnEgg("dugong", EntityReg.DUGONG, 0x0, 0x0); // TODO color

        // VIALS, EGGS, AND SPAWN EGGS //
        public static final RegistryObject<Item> CHARNIA_VIAL = registerVial("charnia", 0xada74c);
        public static final RegistryObject<Item> GINKGO_TREE_VIAL = registerVial("ginkgo_tree", 0x9bd367);
        public static final RegistryObject<Item> BAWITIUS_VIAL = registerVialAndEggs(null, "bawitius", "eggs",0xb75194);
        public static final RegistryObject<Item> CLADOSELACHE_VIAL = registerVialAndEggs(null, "cladoselache", "eggs",0xa254a9);
        public static final RegistryObject<Item> CYMBOSPONDYLUS_VIAL = registerVialAndEggs(null, "cymbospondylus", "egg",0x80872c);
        public static final RegistryObject<Item> DUNKLEOSTEUS_VIAL = registerVialAndEggs(null, "dunkleosteus", "egg",0x3a9db3);
        public static final RegistryObject<Item> HENODUS_VIAL = registerVialAndEggs(EntityReg.HENODUS, "henodus", "egg",0x977343);
        public static final RegistryObject<Item> LEPIDOTES_VIAL = registerVialAndEggs(EntityReg.LEPIDOTES, "lepidotes", "eggs",0xb7bb65);
        public static final RegistryObject<Item> PLESIOSAURUS_VIAL = registerVialAndEggs(EntityReg.PLESIOSAURUS, "plesiosaurus", "egg", 0x429389);
        public static final RegistryObject<Item> PLIOSAURUS_VIAL = registerVialAndEggs(EntityReg.PLIOSAURUS, "pliosaurus", "pup", 0x4e402c);
        public static final RegistryObject<Item> PROGNATHODON_VIAL = registerVialAndEggs(null, "prognathodon", "egg", 0xa1ae75);
        public static final RegistryObject<Item> SHONISAURUS_VIAL = registerVialAndEggs(null, "shonisaurus", "egg", 0x3a746b);
        public static final RegistryObject<Item> UNKNOWN_VIAL = ITEMS.register("unknown_vial", () -> new VialItem(0x4c4c4c, new Item.Properties()));

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
         * Creates a registry object for the given vial item and adds it to the mod creative tab
         * @param name the registry name
         * @param entityType the entity type supplier
         * @param bgColor the background color
         * @param fgColor the foreground color
         * @return the item registry object
         */
        private static <T extends Mob> RegistryObject<Item> registerSpawnEgg(final String name, final RegistryObject<EntityType<T>> entityType, final int bgColor, final int fgColor) {
            final RegistryObject<Item> spawnEgg = ITEMS.register(name + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, bgColor, fgColor, new Item.Properties()));
            SPAWN_EGGS.add(spawnEgg);
            return spawnEgg;
        }

        /**
         * Creates a registry object for the given vial item, egg item, spawn egg item,
         * and adds them to the correct creative tabs
         * @param entityType the entity type for the egg items
         * @param name the registry name
         * @param eggSuffix the suffix for the egg item
         * @param color the vial color
         * @return the item registry object
         */
        private static <T extends Mob> RegistryObject<Item> registerVialAndEggs(final RegistryObject<EntityType<T>> entityType, final String name, final String eggSuffix, final int color) {
            final RegistryObject<Item> vial = registerVial(name, color);
            if(null == entityType) {
                // TODO remove this when entity types are no longer null
                final RegistryObject<Item> egg = registerWithTab(name + "_" + eggSuffix, () -> new Item(new Item.Properties()));
                final RegistryObject<Item> spawnEgg = registerWithTab(name + "_spawn_egg", () -> new Item(new Item.Properties()));
            } else {
                final RegistryObject<Item> egg = registerWithTab(name + "_" + eggSuffix, () -> new ForgeSpawnEggItem(entityType, -1, -1, new Item.Properties()));
                final RegistryObject<Item> spawnEgg = registerSpawnEgg(name, entityType, color, 0xC0C0C0);
            }

            return vial;
        }

        /**
         * Creates a registry object for the given item and adds it to the mod creative tab
         * @param name the registry name
         * @param supplier the item supplier
         * @return the item registry object
         */
        private static RegistryObject<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
            return PPTab.add(ITEMS.register(name, supplier));
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
        public static final RegistryObject<Block> CHARNIA = registerWithItem("charnia",
                () -> new CharniaBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_WATER_PLANT).noCollission().instabreak().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XZ)),
                b -> ItemReg.registerWithTab("charnia", () -> new DoubleHighBlockItem(b.get(), new Item.Properties())));
        public static final RegistryObject<Block> GINKGO_SAPLING = registerWithItem("ginkgo_sapling", () ->
                new SaplingBlock(new GinkgoTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
        public static final RegistryObject<Block> GINKGO_LOG = registerWoodBlocks("ginkgo", 2.0F, 3.0F, MaterialColor.WOOD, MaterialColor.SAND, 5, 5, 20);
        public static final RegistryObject<Block> GINKGO_LEAVES = registerLeaves("ginkgo", 30, 60);

        private static RegistryObject<Block> registerWithItem(final String name, final Supplier<Block> supplier) {
            return registerWithItem(name, supplier, ItemReg::registerBlockItem);
        }

        private static RegistryObject<Block> registerWithItem(final String name, final Supplier<Block> blockSupplier, final Function<RegistryObject<Block>, RegistryObject<Item>> itemSupplier) {
            final RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
            final RegistryObject<Item> item = itemSupplier.apply(block);
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

        /**
         * Registers all of the following: log, stripped log, wood, stripped wood, planks, stairs, slab,
         * door, trapdoor, button
         *
         * @param name               the base registry name
         * @param strength           the destroy time
         * @param hardness           the explosion resistance
         * @param side               the material color of the side
         * @param top                the material color of the top
         * @param fireSpread         the fire spread chance. The higher the number returned, the faster fire will spread around this block.
         * @param logFlammability    Chance that fire will spread and consume the log. 300 being a 100% chance, 0, being a 0% chance.
         * @param planksFlammability Chance that fire will spread and consume the plank. 300 being a 100% chance, 0, being a 0% chance.
         * @return the log block
         */
        private static RegistryObject<Block> registerWoodBlocks(final String name, final float strength, final float hardness,
                                                                final MaterialColor side, final MaterialColor top,
                                                                final int fireSpread, final int logFlammability, final int planksFlammability) {
            // create properties
            final BlockBehaviour.Properties woodProperties = BlockBehaviour.Properties.of(Material.WOOD, side).strength(strength, hardness).sound(SoundType.WOOD);
            final BlockBehaviour.Properties logProperties = BlockBehaviour.Properties.of(Material.WOOD, (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side).strength(strength, hardness).sound(SoundType.WOOD);
            final Block.Properties doorProperties = BlockBehaviour.Properties.of(Material.WOOD, side).strength(strength, hardness).sound(SoundType.WOOD).noOcclusion().isValidSpawn((b, i, p, a) -> false);

            // register blocks
            final RegistryObject<Block> strippedLog = BLOCKS.register("stripped_" + name + "_log", () -> new FlammableRotatedPillarBlock(woodProperties, fireSpread, logFlammability));
            final RegistryObject<Block> strippedWood = BLOCKS.register("stripped_" + name + "_wood", () -> new FlammableRotatedPillarBlock(woodProperties, fireSpread, logFlammability));
            final RegistryObject<Block> log = registerWithItem(name + "_log", () -> new FlammableRotatedPillarBlock(strippedLog, logProperties, fireSpread, logFlammability));
            final RegistryObject<Block> wood = registerWithItem(name + "_wood", () -> new FlammableRotatedPillarBlock(strippedWood, woodProperties, fireSpread, logFlammability));
            ItemReg.registerBlockItem(strippedLog);
            ItemReg.registerBlockItem(strippedWood);
            final RegistryObject<Block> planks = registerWithItem(name + "_planks", () -> new FlammableBlock(woodProperties, fireSpread, planksFlammability));
            final RegistryObject<Block> slab = registerWithItem(name + "_slab", () -> new FlammableSlabBlock(woodProperties, fireSpread, planksFlammability));
            final RegistryObject<Block> stairs = registerWithItem(name + "_stairs", () -> new FlammableStairBlock(() -> planks.get().defaultBlockState(), woodProperties, fireSpread, planksFlammability));
            final RegistryObject<Block> door = registerWithItem(name + "_door", () -> new DoorBlock(doorProperties, SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN));
            final RegistryObject<Block> trapdoor = registerWithItem(name + "_trapdoor", () -> new TrapDoorBlock(doorProperties, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN));
            final RegistryObject<Block> pressurePlate = registerWithItem(name + "_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, woodProperties, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON));
            final RegistryObject<Block> button = registerWithItem(name + "_button", () -> new ButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD), 30, true, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON));
            final RegistryObject<Block> fence = registerWithItem(name + "_fence", () -> new FenceBlock(woodProperties));
            final RegistryObject<Block> fenceGate = registerWithItem(name + "_fence_gate", () -> new FenceGateBlock(woodProperties, SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN));
            return log;
        }

        /**
         * @param name the base registry name
         * @param fireSpread   the fire spread chance. The higher the number returned, the faster fire will spread around this block.
         * @param flammability Chance that fire will spread and consume the block. 300 being a 100% chance, 0, being a 0% chance.
         * @return the leaves block registry object
         */
        private static RegistryObject<Block> registerLeaves(final String name, final int fireSpread, final int flammability) {
            final BlockBehaviour.Properties properties = Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS)
                    .noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).isSuffocating((s, r, p) -> false).isViewBlocking((s, r, p) -> false);
            return registerWithItem(name + "_leaves", () -> new FlammableLeavesBlock(properties, fireSpread, flammability));
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
            event.put(DUGONG.get(), Dugong.createAttributes().build());
            event.put(HENODUS.get(), Henodus.createAttributes().build());
            event.put(LEPIDOTES.get(), Lepidotes.createAttributes().build());
            event.put(PLESIOSAURUS.get(), Plesiosaurus.createAttributes().build());
            event.put(PLIOSAURUS.get(), Pliosaurus.createAttributes().build());
        }

        public static void onRegisterSpawnPlacement(final SpawnPlacementRegisterEvent event) {
            event.register(DUGONG.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(HENODUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(LEPIDOTES.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(PLESIOSAURUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(PLIOSAURUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        }

        public static final RegistryObject<EntityType<Dugong>> DUGONG = ENTITY_TYPES.register("dugong", () ->
                EntityType.Builder.of(Dugong::new, MobCategory.WATER_CREATURE)
                        .sized(0.98F, 0.746F)
                        .build("dugong"));

        public static final RegistryObject<EntityType<Henodus>> HENODUS = ENTITY_TYPES.register("henodus", () ->
                EntityType.Builder.of(Henodus::new, MobCategory.WATER_CREATURE)
                        .sized(0.746F, 0.188F)
                        .build("henodus"));

        public static final RegistryObject<EntityType<Lepidotes>> LEPIDOTES = ENTITY_TYPES.register("lepidotes", () ->
                EntityType.Builder.of(Lepidotes::new, MobCategory.WATER_CREATURE)
                        .sized(0.875F, 0.625F)
                        .build("lepidotes"));

        public static final RegistryObject<EntityType<Plesiosaurus>> PLESIOSAURUS = ENTITY_TYPES.register("plesiosaurus", () ->
                EntityType.Builder.of(Plesiosaurus::new, MobCategory.WATER_CREATURE)
                        .sized(0.98F, 0.48F)
                        .build("plesiosaurus"));

        public static final RegistryObject<EntityType<Pliosaurus>> PLIOSAURUS = ENTITY_TYPES.register("pliosaurus", () ->
                EntityType.Builder.of(Pliosaurus::new, MobCategory.WATER_CREATURE)
                        .sized(0.98F, 0.48F)
                        .build("pliosaurus"));
    }

    public static final class FeatureReg {

        public static void register() {
            FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
            STRUCTURE_PROCESSORS.register(FMLJavaModLoadingContext.get().getModEventBus());
            FMLJavaModLoadingContext.get().getModEventBus().addListener(FeatureReg::registerStructureProcessors);
        }

        public static StructureProcessorType<LocStructureProcessor> LOC_PROCESSOR;
        public static RegistryObject<GinkgoTreeFeature> GINKGO_TREE_FEATURE = FEATURES.register("ginkgo_tree", () -> new GinkgoTreeFeature(TreeConfiguration.CODEC));

        private static void registerStructureProcessors(final FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                ResourceLocation locProcessorId = new ResourceLocation(PelagicPrehistory.MODID, "loc");
                LOC_PROCESSOR = StructureProcessorType.register(locProcessorId.toString(), LocStructureProcessor.CODEC);
            });
        }
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

    //// FLAMMABLE BLOCKS ////

    private static class FlammableBlock extends Block {

        private final int fireSpread;
        private final int flammability;

        public FlammableBlock(Properties properties, int fireSpread, int flammability) {
            super(properties);
            this.fireSpread = fireSpread;
            this.flammability = flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return fireSpread;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return flammability;
        }
    }

    private static class FlammableRotatedPillarBlock extends RotatedPillarBlock {

        @Nullable
        private final Supplier<Block> strippedResult;
        private final int fireSpread;
        private final int flammability;

        public FlammableRotatedPillarBlock(@Nullable final Supplier<Block> strippedResult, Properties properties, int fireSpread, int flammability) {
            super(properties);
            this.strippedResult = strippedResult;
            this.fireSpread = fireSpread;
            this.flammability = flammability;
        }

        public FlammableRotatedPillarBlock(Properties properties, int fireSpread, int flammability) {
            this(null, properties, fireSpread, flammability);
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return fireSpread;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return flammability;
        }

        @Override
        public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
            if (toolAction == ToolActions.AXE_STRIP && strippedResult != null) {
                return strippedResult.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
            }
            return super.getToolModifiedState(state, context, toolAction, simulate);
        }
    }

    private static class FlammableSlabBlock extends SlabBlock {

        private final int fireSpread;
        private final int flammability;

        public FlammableSlabBlock(Properties properties, int fireSpread, int flammability) {
            super(properties);
            this.fireSpread = fireSpread;
            this.flammability = flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return fireSpread;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return flammability;
        }
    }

    private static class FlammableStairBlock extends StairBlock {

        private final int fireSpread;
        private final int flammability;

        public FlammableStairBlock(Supplier<BlockState> state, Properties properties, int fireSpread, int flammability) {
            super(state, properties);
            this.fireSpread = fireSpread;
            this.flammability = flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return fireSpread;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return flammability;
        }
    }

    private static class FlammableLeavesBlock extends LeavesBlock {

        private final int fireSpread;
        private final int flammability;

        public FlammableLeavesBlock(Properties properties, int fireSpread, int flammability) {
            super(properties);
            this.fireSpread = fireSpread;
            this.flammability = flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return fireSpread;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return flammability;
        }
    }
}
