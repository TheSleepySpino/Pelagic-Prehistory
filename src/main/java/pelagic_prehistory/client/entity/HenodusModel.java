package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Henodus;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class HenodusModel<T extends Henodus> extends SimplePitchGeoModel<T> {

    public HenodusModel(final String name) {
        super(name);
    }

    @Override
    protected float getPitchMultiplier() {
        return -1.0F;
    }

    @Override
    protected void rotateBody(T animatable, int instanceId, AnimationEvent animationState) {
        // do nothing
    }
}
