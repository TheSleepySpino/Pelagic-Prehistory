package pelagic_prehistory.client.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import pelagic_prehistory.PelagicPrehistory;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.Optional;

public class SimplePitchGeoModel<T extends LivingEntity & IAnimatable> extends AnimatedGeoModel<T> {

    private final ResourceLocation modelLocation;
    private final ResourceLocation textureLocation;
    private final ResourceLocation animationLocation;

    public SimplePitchGeoModel(final String name) {
        super();
        this.textureLocation = new ResourceLocation(PelagicPrehistory.MODID, "textures/entity/" + name + ".png");
        this.modelLocation = new ResourceLocation(PelagicPrehistory.MODID, "geo/entity/" + name + ".geo.json");
        this.animationLocation = new ResourceLocation(PelagicPrehistory.MODID, "animations/entity/" + name + ".animation.json");
    }

    @Override
    public ResourceLocation getModelResource(T object) {
        return modelLocation;
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return textureLocation;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animationLocation;
    }

    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        rotateBody(animatable, instanceId, animationEvent);
        rotateHead(animatable, instanceId, animationEvent);
    }

    protected Optional<IBone> getBodyBone() {
        return Optional.ofNullable(this.getBone("body"));
    }

    protected Optional<IBone> getHeadBone() {
        return Optional.ofNullable(this.getBone("head"));
    }

    protected float getPitchMultiplier() {
        return -1;
    }

    protected void rotateBody(T animatable, int instanceId, AnimationEvent animationState) {
        Optional<IBone> bone = getBodyBone();
        if(bone.isPresent()) {
            float xRot = animatable.getViewXRot(animationState.getPartialTick()) * getPitchMultiplier();
            float angle = (float) Math.toRadians(xRot);
            bone.get().setRotationX(angle);
        }
    }

    protected void rotateHead(T animatable, int instanceId, AnimationEvent animationState) {
        Optional<IBone> bone = getHeadBone();
        if(bone.isPresent()) {
            float yRot = getNetHeadYaw(animatable, animationState.getPartialTick());
            float angle = (float) Math.toRadians(yRot);
            bone.get().setRotationY(angle);
        }
    }

    protected float getNetHeadYaw(final T entity, final float partialTick) {
        float yBodyRot = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        float yHeadRot = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
        return yHeadRot - yBodyRot;
    }
}
