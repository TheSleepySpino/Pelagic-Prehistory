package pelagic_prehistory.client.entity;

import pelagic_prehistory.PelagicPrehistory;
import pelagic_prehistory.entity.Dugong;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class DugongModel<T extends Dugong> extends DefaultedEntityGeoModel<T> {

    public DugongModel() {
        super(new ResourceLocation(PelagicPrehistory.MODID, "dugong"));
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        Optional<GeoBone> bone = this.getBone("body");
        if(bone.isPresent()) {
            float angle = -animatable.getViewXRot(animationState.getPartialTick());
            bone.get().setRotX((float) Math.toRadians(angle));
        }
    }
}
