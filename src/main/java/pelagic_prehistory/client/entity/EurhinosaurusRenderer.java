package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Eurhinosaurus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class EurhinosaurusRenderer<T extends Eurhinosaurus> extends GeoEntityRenderer<T> {

    public EurhinosaurusRenderer(EntityRendererProvider.Context context) {
        super(context, new EurhinosaurusModel<>("eurhinosaurus"));
    }
}
