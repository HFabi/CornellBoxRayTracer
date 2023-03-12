package de.hfabi.scene.light;

import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;

import java.util.List;

public class Pointlight extends Light {
    /**
     * intensity in rgb
     */
    private RgbColor intensity;

    /**
     * Constructor.
     *
     * @param pos       position
     * @param intensity intensity
     */
    public Pointlight(Vec3 pos, RgbColor intensity) {
        super(pos);
        this.intensity = intensity;
    }

    @Override
    public RgbColor getLight() {
        return this.intensity;
    }

    @Override
    public boolean isAreaLight() {
        return false;
    }

    @Override
    public List<Pointlight> getPointLightList() {
        return null;
    }

    public String toString() {
        return this.getPosition().toString();
    }
}
