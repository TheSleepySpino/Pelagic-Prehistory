package pelagic_prehistory.client.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import pelagic_prehistory.entity.Irritator;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class IrritatorRenderer<T extends Irritator> extends GeoEntityRenderer<T> {

    public IrritatorRenderer(EntityRendererProvider.Context context) {
        super(context, new IrritatorModel<>("irritator"));
    }


}
