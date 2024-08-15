package pelagic_prehistory.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.server.ServerLifecycleHooks;
import pelagic_prehistory.PelagicPrehistory;

public class GinkgoTreeGrower extends AbstractTreeGrower {

    private static final ResourceLocation FEATURE_ID = new ResourceLocation(PelagicPrehistory.MODID, "ginkgo_tree");

    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        ConfiguredFeature<?, ?> cf = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).get(FEATURE_ID);
        if (null == cf) {
            throw new IllegalArgumentException("Failed to create holder for unknown configured feature '" + FEATURE_ID + "'");
        }
        return Holder.direct(cf);
    }
}
