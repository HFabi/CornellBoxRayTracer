package de.hfabi.scene.material;

import de.hfabi.raytracer.Intersection;
import de.hfabi.scene.light.Light;
import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;

public abstract class Material {
    /**
     * Calculates ambient part of color
     *
     * @return ambienter Farbanteil
     */
    public abstract RgbColor getAmbientColor();

    public abstract RgbColor getColor(Light light, Vec3 lightDirection, Intersection intersection, Vec3 cameraPosition);

    public abstract boolean isReflectiv();

    public abstract RgbColor getReflectionColor(RgbColor reflectionColor);

    public abstract RgbColor getRefractionColor(RgbColor refractionColor);

    public abstract boolean isTransparent();


    public abstract float getRefractiveIndex();

}
