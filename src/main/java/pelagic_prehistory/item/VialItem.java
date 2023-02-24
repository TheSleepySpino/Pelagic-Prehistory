package pelagic_prehistory.item;

import net.minecraft.world.item.Item;

public class VialItem extends Item {

    private final int color;

    public VialItem(final int color, Properties pProperties) {
        super(pProperties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
