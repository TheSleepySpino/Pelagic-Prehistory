package pelagic_prehistory.client.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import pelagic_prehistory.entity.Shonisaurus;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ShonisaurusRenderer<T extends Shonisaurus> extends GeoEntityRenderer<T> {

    public ShonisaurusRenderer(EntityRendererProvider.Context context) {
        super(context, new ShonisaurusModel<>("shonisaurus"));
    }

    @Override
    public float getWidthScale(T entity) {
        return 2.0F;
    }

    @Override
    public float getHeightScale(T entity) {
        return 2.0F;
    }
}
