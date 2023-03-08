package pelagic_prehistory.client.entity;

import net.minecraft.world.entity.LivingEntity;
import pelagic_prehistory.PelagicPrehistory;
import pelagic_prehistory.entity.Plesiosaurus;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class PlesiosaurusModel<T extends Plesiosaurus> extends SimplePitchGeoModel<T> {

    public PlesiosaurusModel(final String name) {
        super(name);
    }

    @Override
    protected float getPitchMultiplier() {
        return 1.0F;
    }

    @Override
    protected void rotateHead(T animatable, long instanceId, AnimationState<T> animationState) {
        Optional<GeoBone> head = getHeadBone();
        Optional<GeoBone> neck = getBone("neck");
        if(head.isPresent() && neck.isPresent()) {
            float yRot = animationState.getData(DataTickets.ENTITY_MODEL_DATA).netHeadYaw();
            float angle = (float) Math.toRadians(yRot);
            head.get().setRotY(angle * 0.5F);
            neck.get().setRotY(angle * 0.5F);
        }
    }
}
