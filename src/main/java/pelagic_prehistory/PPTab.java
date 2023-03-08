package pelagic_prehistory;

import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pelagic_prehistory.PPRegistry.ItemReg;

import java.util.ArrayList;
import java.util.List;

public final class PPTab {
    private static CreativeModeTab tab;
    private static final List<RegistryObject<Item>> SORTED_ITEMS = new ArrayList<>();

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PPTab::onTabRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PPTab::onTabBuildContents);
    }

    public static RegistryObject<Item> add(final RegistryObject<Item> item) {
        SORTED_ITEMS.add(item);
        return item;
    }

    public static void onTabRegister(final CreativeModeTabEvent.Register event) {
        tab = event.registerCreativeModeTab(new ResourceLocation(PelagicPrehistory.MODID, "tab"), b -> b
                .title(Component.translatable("itemGroup." + PelagicPrehistory.MODID))
                .icon(Suppliers.memoize(() -> new ItemStack(ItemReg.PLESIOSAURUS_VIAL.get()))));
    }

    public static void onTabBuildContents(final CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(RegistryObject.create(new ResourceLocation(PelagicPrehistory.MODID, "analyzer"), ForgeRegistries.ITEMS));
            event.accept(RegistryObject.create(new ResourceLocation(PelagicPrehistory.MODID, "infuser"), ForgeRegistries.ITEMS));
        }
        if(event.getTab() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(RegistryObject.create(new ResourceLocation(PelagicPrehistory.MODID, "ginkgo_sapling"), ForgeRegistries.ITEMS));
            event.accept(RegistryObject.create(new ResourceLocation(PelagicPrehistory.MODID, "ancient_sediment"), ForgeRegistries.ITEMS));
            event.accept(RegistryObject.create(new ResourceLocation(PelagicPrehistory.MODID, "ancient_sediment_fossil"), ForgeRegistries.ITEMS));
        }
        if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ItemReg.RAW_CUTTLEFISH);
            event.accept(ItemReg.CUTTLEFISH_STEW);
        }
        if(event.getTab() == CreativeModeTabs.SPAWN_EGGS) {
            ItemReg.getSpawnEggs().forEach(i -> event.accept(i));
        }
        if(event.getTab() == tab) {
            // all items
            SORTED_ITEMS.forEach(i -> event.accept(i));
        }
    }
}
