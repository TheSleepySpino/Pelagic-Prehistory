package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.BigShark;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BigSharkRenderer<T extends BigShark> extends GeoEntityRenderer<T> {

    public BigSharkRenderer(EntityRendererProvider.Context context) {
        super(context, new BigSharkModel<>());
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return 0;
    }
}
