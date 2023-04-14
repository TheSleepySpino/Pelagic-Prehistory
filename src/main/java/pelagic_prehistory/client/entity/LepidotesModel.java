package pelagic_prehistory.client.entity;

import net.minecraft.util.Mth;
import pelagic_prehistory.entity.Lepidotes;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Optional;

public class LepidotesModel<T extends Lepidotes> extends SimplePitchGeoModel<T> {

    public LepidotesModel(final String name) {
        super(name);
    }

    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        //rotateTail(animatable, instanceId, animationEvent);
    }

    @Override
    protected Optional<IBone> getHeadBone() {
        return super.getHeadBone();// Optional.empty();
    }

    protected void rotateTail(T animatable, int instanceId, AnimationEvent animationState) {
        Optional<IBone> tail = Optional.ofNullable(getBone("Tail"));
        if(tail.isPresent()) {
            final float ageInTicks = (animatable.tickCount + animatable.getId()) + animationState.getPartialTick();
            final float tailAngle = Mth.cos(ageInTicks * 0.28F) * 0.52F;
            tail.get().setRotationY(tailAngle);
        }
    }
}
