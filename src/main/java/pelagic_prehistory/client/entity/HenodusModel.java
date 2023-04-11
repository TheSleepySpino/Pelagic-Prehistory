package pelagic_prehistory.client.entity;

import pelagic_prehistory.entity.Henodus;

public class HenodusModel<T extends Henodus> extends SimplePitchGeoModel<T> {

    public HenodusModel(final String name) {
        super(name);
    }

    @Override
    protected float getPitchMultiplier() {
        return 1.0F;
    }
}
