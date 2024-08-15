package pelagic_prehistory.client.entity;

import net.minecraft.world.phys.Vec2;
import pelagic_prehistory.entity.Prognathodon;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Optional;

public class PrognathodonModel<T extends Prognathodon> extends SimplePitchGeoModel<T> {

    public PrognathodonModel(final String name) {
        super(name);
    }

    @Override
    protected void rotateHead(T animatable, int instanceId, AnimationEvent animationState) {
        Optional<IBone> oHead = getHeadBone();
        Optional<IBone> oNeck = Optional.ofNullable(getBone("neck"));
        if(oHead.isPresent() && oNeck.isPresent()) {
            final IBone head = oHead.get();
            final IBone neck = oNeck.get();
            final Vec2 rotations = getHeadRotations(animatable, instanceId, animationState).scale(0.5F);
            head.setRotationX(head.getRotationX() + rotations.x);
            head.setRotationY(head.getRotationY() + rotations.y);
            neck.setRotationX(neck.getRotationX() + rotations.x);
            neck.setRotationY(neck.getRotationY() + rotations.y);
        }
    }
}
