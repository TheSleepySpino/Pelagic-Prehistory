package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Irritator;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class IrritatorModel<T extends Irritator> extends SimplePitchGeoModel<T> {

    public IrritatorModel(final String name) {
        super(name);
    }

    @Override
    protected float getPitchMultiplier() {
        return 1.0F;
    }

    @Override
    protected void rotateBody(T animatable, int instanceId, AnimationEvent animationState) {
        // do nothing
    }
}
