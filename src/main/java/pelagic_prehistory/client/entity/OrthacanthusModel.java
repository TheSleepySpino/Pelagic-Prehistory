package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Orthacanthus;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Optional;

public class OrthacanthusModel<T extends Orthacanthus> extends SimplePitchGeoModel<T> {

    public OrthacanthusModel(final String name) {
        super(name);
    }

    @Override
    protected Optional<IBone> getHeadBone() {
        return Optional.empty();
    }
}
