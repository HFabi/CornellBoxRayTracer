package de.hfabi.scene.light;

import de.hfabi.scene.SceneObject;
import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;

import java.util.List;

public abstract class Light extends SceneObject {
    /**
     * Color, color intensity
     */
    private RgbColor color;

    /**
     * Constructor.
     *
     * @param pos Position
     */
    Light(Vec3 pos) {
        super(pos);
    }

    public abstract RgbColor getLight();
    public abstract boolean isAreaLight();

    public abstract List<Pointlight> getPointLightList();

}
