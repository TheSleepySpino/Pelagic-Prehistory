package pelagic_prehistory.client.entity;

import pelagic_prehistory.PelagicPrehistory;
import pelagic_prehistory.entity.BigShark;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import net.minecraft.resources.ResourceLocation;

public class BigSharkModel<T extends BigShark> extends DefaultedEntityGeoModel<T> {

    public BigSharkModel() {
        super(new ResourceLocation(PelagicPrehistory.MODID, "big_shark"));
    }
}
