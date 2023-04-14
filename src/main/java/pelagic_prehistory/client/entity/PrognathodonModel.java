package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Prognathodon;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Optional;

public class PrognathodonModel<T extends Prognathodon> extends SimplePitchGeoModel<T> {

    public PrognathodonModel(final String name) {
        super(name);
    }

    @Override
    protected float getPitchMultiplier() {
        return -1.0F;
    }

    @Override
    protected void rotateHead(T animatable, int instanceId, AnimationEvent animationState) {
        Optional<IBone> head = getHeadBone();
        Optional<IBone> neck = Optional.ofNullable(getBone("neck"));
        if(head.isPresent() && neck.isPresent()) {
            float yRot = getNetHeadYaw(animatable, animationState.getPartialTick());
            float angle = (float) Math.toRadians(-yRot);
            head.get().setRotationY(angle * 0.5F);
            neck.get().setRotationY(angle * 0.5F);
        }
    }
}
