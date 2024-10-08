package pelagic_prehistory.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import pelagic_prehistory.PelagicPrehistory;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

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
        return 1;
    }

    protected void rotateBody(T animatable, int instanceId, AnimationEvent animationState) {
        if(animatable.isOnGround()) {
            return;
        }
        Optional<IBone> bone = getBodyBone();
        if(bone.isPresent()) {
            float xRot = (-1.0F) * animatable.getViewXRot(animationState.getPartialTick()) * getPitchMultiplier();
            float angle = (float) Math.toRadians(xRot);
            bone.get().setRotationX(angle);
        }
    }

    protected void rotateHead(T animatable, int instanceId, AnimationEvent animationState) {
        Optional<IBone> oBone = getHeadBone();
        if(oBone.isPresent()) {
            final IBone bone = oBone.get();
            final Vec2 rotations = getHeadRotations(animatable, instanceId, animationState);
            bone.setRotationX(bone.getRotationX() + rotations.x * getPitchMultiplier());
            bone.setRotationY(bone.getRotationY() + rotations.y);
        }
    }

    protected Vec2 getHeadRotations(T animatable, int instanceId, AnimationEvent animationState) {
        EntityModelData extraData = (EntityModelData) animationState.getExtraDataOfType(EntityModelData.class).get(0);
        AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
        int unpausedMultiplier = !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;
        return new Vec2(extraData.headPitch, extraData.netHeadYaw).scale(Mth.DEG_TO_RAD * unpausedMultiplier);
    }
}
