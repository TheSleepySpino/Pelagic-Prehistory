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
    private static List<RegistryObject<Item>> ITEMS = new ArrayList<>();

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PPTab::onTabRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PPTab::onTabBuildContents);
    }

    public static void add(final RegistryObject<Item> item) {
        ITEMS.add(item);
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
            event.accept(RegistryObject.create(new ResourceLocation(PelagicPrehistory.MODID, "ancient_sediment"), ForgeRegistries.ITEMS));
            event.accept(RegistryObject.create(new ResourceLocation(PelagicPrehistory.MODID, "ancient_sediment_fossil"), ForgeRegistries.ITEMS));
        }
        if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ItemReg.RAW_CUTTLEFISH);
            event.accept(ItemReg.CUTTLEFISH_STEW);
        }
        if(event.getTab() == tab) {
            // all items
            ITEMS.forEach(i -> event.accept(i));
        }
    }
}
