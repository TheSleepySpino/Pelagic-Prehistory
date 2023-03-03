package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Dugong;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DugongRenderer<T extends Dugong> extends GeoEntityRenderer<T> {

    public DugongRenderer(EntityRendererProvider.Context context) {
        super(context, new DugongModel<>());
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return 0;
    }
}
