package pelagic_prehistory.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import pelagic_prehistory.PPRegistry;
import pelagic_prehistory.item.VialItem;

public final class ClientEvents {

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(ModHandler.class);
        MinecraftForge.EVENT_BUS.register(ForgeHandler.class);
    }

    public static final class ModHandler {
        @SubscribeEvent
        public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {

        }

        @SubscribeEvent
        public static void onCommonSetup(final FMLCommonSetupEvent event) {

        }

        @SubscribeEvent
        public static void onRegisterItemColors(final RegisterColorHandlersEvent.Item event) {
            event.register((itemStack, tintIndex) -> {
                if(tintIndex == 1 && itemStack.getItem() instanceof VialItem vial) {
                    return vial.getColor();
                }
                return -1;
            }, PPRegistry.ItemReg.getVialItems().stream().map(RegistryObject::get).toList().toArray(new Item[0]));
        }
    }

    public static final class ForgeHandler {

    }
}
