package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Cladoselache;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Optional;

public class CladoselacheModel<T extends Cladoselache> extends SimplePitchGeoModel<T> {

    public CladoselacheModel(final String name) {
        super(name);
    }

    @Override
    protected Optional<IBone> getHeadBone() {
        return Optional.empty();
    }

    @Override
    protected Optional<IBone> getBodyBone() {
        return Optional.ofNullable(getBone("bone"));
    }
}
