package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Spinosaurus;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class SpinosaurusModel<T extends Spinosaurus> extends SimplePitchGeoModel<T> {

    public SpinosaurusModel(final String name) {
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
