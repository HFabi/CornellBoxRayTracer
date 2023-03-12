package de.hfabi.scene.material;

import de.hfabi.raytracer.Intersection;
import de.hfabi.scene.light.Light;
import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;

public class Lambert extends Material {
    /**
     * Ambient coefficient
     */
    RgbColor ambientCoefficient;
    /**
     * diffius coefficient.
     */
    RgbColor diffusCoefficient;
    /**
     * ambient light of the scene.
     */
    RgbColor ambientIntensity;

    /**
     * Constructor
     *
     * @param ambientCoefficient ambient coefficient
     * @param diffusCoefficient  diffuser coefficient
     * @param ambientIntensity   base intensity
     */

    public Lambert(RgbColor ambientCoefficient, RgbColor diffusCoefficient, RgbColor ambientIntensity) {
        this.ambientCoefficient = ambientCoefficient;
        this.diffusCoefficient = diffusCoefficient;
        this.ambientIntensity = ambientIntensity;
    }

    /**
     * Calculates ambient part of color
     *
     * @return ambienter Farbanteil
     */
    @Override
    public RgbColor getAmbientColor() {
        return ambientIntensity.multRGB(ambientCoefficient);
    }

    /**
     * Calcualtes diffuse part (Lambert) based on light intensity
     * Intensität *Komponentenweise Multiplikation* diffuserKoeffizient *Komponentenweise Multp*(Normale *skalar* Lichtvektor)
     *
     * @param light          light
     * @param lightDirection light direction
     * @param intersection   intersection point
     * @param cameraPosition Position der Kamer
     * @return diffuser color part
     */
    @Override
    public RgbColor getColor(Light light, Vec3 lightDirection, Intersection intersection, Vec3 cameraPosition) {
        // Normale aus der Intersection holen
        Vec3 normale = intersection.getNormal().normalize();//sollte bereits normalisiert sein
        float cosinusAlpha = normale.scalar(lightDirection);
        if (cosinusAlpha > 0) return light.getLight().multRGB((diffusCoefficient.multScalar(cosinusAlpha)));
        else return new RgbColor(0f, 0f, 0f);
    }


    /**
     * Lambert has no reflection, always false
     *
     * @return false
     */
    public boolean isReflectiv() {
        return false;
    }

    /**
     * Lambert has no reflection, always null
     *
     * @param reflectionColor
     * @return null
     */
    public RgbColor getReflectionColor(RgbColor reflectionColor) {
        return null;
    }

    @Override
    public RgbColor getRefractionColor(RgbColor refractionColor) {
        return null;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }

    @Override
    public float getRefractiveIndex() {
        return 1;
    }
}
