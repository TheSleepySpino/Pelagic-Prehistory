package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Cuttlefish;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

import java.util.Optional;

public class CuttlefishModel<T extends Cuttlefish> extends SimplePitchGeoModel<T> {

    public CuttlefishModel(final String name) {
        super(name);
    }

    @Override
    protected float getPitchMultiplier() {
        return -1.0F;
    }

    @Override
    protected Optional<GeoBone> getBodyBone() {
        return getBone("root");
    }

    @Override
    protected Optional<GeoBone> getHeadBone() {
        return Optional.empty();
    }

    @Override
    protected void rotateBody(T animatable, long instanceId, AnimationState<T> animationState) {
        Optional<GeoBone> bone = getBodyBone();
        if(bone.isPresent()) {
            float xRot = animatable.getViewXRot(animationState.getPartialTick()) * getPitchMultiplier();
            float yRot = animatable.getViewYRot(animationState.getPartialTick());
            bone.get().setRotX((float) Math.toRadians(xRot));
            bone.get().setRotY((float) Math.toRadians(yRot));
        }

    }
}
