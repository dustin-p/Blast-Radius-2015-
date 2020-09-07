package com.porterdustin.zombienuke;
import com.porterdustin.zombienuke.R;

public abstract class Weapon {
    public enum Type {RANGED, MELEE};
    private Type type;
    private int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
