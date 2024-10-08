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
    protected Optional<IBone> getHeadBone()
    {
        return Optional.ofNullable(getBone("head"));
    }

    @Override
    protected Optional<IBone> getBodyBone() {
        return Optional.ofNullable(getBone("Lepidotes"));

    }
}
