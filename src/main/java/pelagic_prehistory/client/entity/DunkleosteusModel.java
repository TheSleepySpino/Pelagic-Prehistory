package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Dunkleosteus;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Optional;

public class DunkleosteusModel<T extends Dunkleosteus> extends SimplePitchGeoModel<T> {

    public DunkleosteusModel(final String name) {
        super(name);
    }

    @Override
    protected Optional<IBone> getHeadBone() {
        return Optional.ofNullable(this.getBone("bone2"));
    }

    @Override
    protected Optional<IBone> getBodyBone() {
        return Optional.ofNullable(this.getBone("bone"));
    }
}
