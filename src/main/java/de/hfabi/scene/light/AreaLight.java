package de.hfabi.scene.light;

import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AreaLight extends Light {
    /**
     * intensity in rgb
     */
    private RgbColor intensity;
    /**
     * point lights
     */
    private List<Pointlight> pointLightList;


    /**
     * Constructor.
     *
     * @param pos       position
     * @param intensity intensity
     */
    public AreaLight(Vec3 pos, RgbColor intensity, int amountSample, float sideLength) {
        super(pos);
        this.intensity = intensity;
        pointLightList = new ArrayList<>();
        //zufällige Verteilung der Punktlichter auf der zur Verfügugn stehenden Fläche.
        for (int i = 0; i < amountSample; i++) {
            float rx = (float) Math.random();
            float rz = (float) Math.random();
            float posX = rx * sideLength - (sideLength / 2.0f);
            float posZ = rz * sideLength - (sideLength / 2.0f);
            pointLightList.add(new Pointlight(new Vec3(pos.x + posX, pos.y, pos.z + posZ), intensity.multScalar((1f / amountSample))));
        }
    }

    @Override
    public RgbColor getLight() {
        return this.intensity;
    }

    @Override
    public boolean isAreaLight() {
        return true;
    }

    public List getPointLightList() {
        return pointLightList;
    }
}
