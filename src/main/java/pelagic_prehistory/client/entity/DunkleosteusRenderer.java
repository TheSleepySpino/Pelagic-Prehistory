package pelagic_prehistory.client.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import pelagic_prehistory.entity.Dunkleosteus;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DunkleosteusRenderer<T extends Dunkleosteus> extends GeoEntityRenderer<T> {

    public DunkleosteusRenderer(EntityRendererProvider.Context context) {
        super(context, new DunkleosteusModel<T>("dunkleosteus"));
    }
}
