package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Henodus;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Optional;

public class HenodusModel<T extends Henodus> extends SimplePitchGeoModel<T> {

    public HenodusModel(final String name) {
        super(name);
    }
    @Override
    protected float getPitchMultiplier() {
        return -1.0F;
    }

    @Override
    protected Optional<IBone> getHeadBone() {
        return Optional.ofNullable(this.getBone("head"));
    }

    @Override
    protected Optional<IBone> getBodyBone() {
        return Optional.ofNullable(this.getBone("body"));
    }
}
