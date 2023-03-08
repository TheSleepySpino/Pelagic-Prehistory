package pelagic_prehistory.client.entity;

import net.minecraft.world.entity.LivingEntity;
import pelagic_prehistory.PelagicPrehistory;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class SimplePitchGeoModel<T extends LivingEntity & GeoAnimatable> extends DefaultedEntityGeoModel<T> {

    public SimplePitchGeoModel(final String name) {
        super(new ResourceLocation(PelagicPrehistory.MODID, name));
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        rotateBody(animatable, instanceId, animationState);
        rotateHead(animatable, instanceId, animationState);
    }

    protected Optional<GeoBone> getBodyBone() {
        return this.getBone("body");
    }

    protected Optional<GeoBone> getHeadBone() {
        return this.getBone("head");
    }

    protected float getPitchMultiplier() {
        return -1;
    }

    protected void rotateBody(T animatable, long instanceId, AnimationState<T> animationState) {
        Optional<GeoBone> bone = getBodyBone();
        if(bone.isPresent()) {
            float xRot = animatable.getViewXRot(animationState.getPartialTick()) * getPitchMultiplier();
            float angle = (float) Math.toRadians(xRot);
            bone.get().setRotX(angle);
        }
    }

    protected void rotateHead(T animatable, long instanceId, AnimationState<T> animationState) {
        Optional<GeoBone> bone = getHeadBone();
        if(bone.isPresent()) {
            float yRot = animationState.getData(DataTickets.ENTITY_MODEL_DATA).netHeadYaw();
            float angle = (float) Math.toRadians(yRot);
            bone.get().setRotY(angle);
        }
    }
}
