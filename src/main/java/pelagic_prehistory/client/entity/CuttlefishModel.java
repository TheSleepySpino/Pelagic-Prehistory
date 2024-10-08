package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Cuttlefish;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Optional;

public class CuttlefishModel<T extends Cuttlefish> extends SimplePitchGeoModel<T> {

    public CuttlefishModel(final String name) {
        super(name);
    }

    @Override
    protected Optional<IBone> getBodyBone() {
        return Optional.ofNullable(getBone("root"));
    }

    @Override
    protected Optional<IBone> getHeadBone() {
        return Optional.empty();
    }

    @Override
    protected void rotateBody(T animatable, int instanceId, AnimationEvent animationState) {
        super.rotateBody(animatable, instanceId, animationState);
        Optional<IBone> bone = getBodyBone();
        bone.get().setRotationY(bone.get().getRotationY() + Mth.PI);

        if(bone.isPresent()) {
            float xRot = animatable.getViewXRot(animationState.getPartialTick()) * getPitchMultiplier();
            float yRot = animatable.getViewYRot(animationState.getPartialTick());
            bone.get().setRotationX((float) Math.toRadians(xRot));
            bone.get().setRotationY((float) Math.toRadians(yRot));
        }

    }
}
