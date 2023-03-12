package de.hfabi.scene.material;

import de.hfabi.raytracer.Intersection;
import de.hfabi.scene.light.Light;
import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;

public class Blinn extends Material {
    /**
     * ambient coefficient
     */
    RgbColor ambientCoefficient;
    /**
     * diffus coefficient
     */
    RgbColor diffusCoefficient;
    /**
     * specular coefficient
     */
    RgbColor specularCoefficient;
    /**
     * ambient light of the scene.
     */
    RgbColor ambientIntensity;
    /**
     * specular exponent
     */
    double specularExponent;


    public Blinn(RgbColor ambientCoefficient, RgbColor diffusCoefficient, RgbColor specularCoefficient, RgbColor ambientIntensity, double specularExponent) {
        this.ambientCoefficient = ambientCoefficient;
        this.diffusCoefficient = diffusCoefficient;
        this.specularCoefficient = specularCoefficient;
        this.ambientIntensity = ambientIntensity;
        this.specularExponent = specularExponent;
    }

    /**
     * Calculates ambient part of color
     *
     * @return ambient color
     */
    @Override
    public RgbColor getAmbientColor() {
        return ambientIntensity.multRGB(ambientCoefficient);
    }

    /**
     * Calculates blinn part of color
     *
     * @param light          light
     * @param lightDirection light direction
     * @param intersection   intersection point
     * @param cameraPosition camera position
     * @return blinn
     */
    @Override
    public RgbColor getColor(Light light, Vec3 lightDirection, Intersection intersection, Vec3 cameraPosition) {
        Vec3 normale = intersection.getNormal().normalize();
        float cosinusAlpha = normale.scalar(lightDirection);
        Vec3 viewvektor = (cameraPosition.sub(intersection.getInterSectionPoint())).normalize();
        Vec3 halbvektor = ((lightDirection.add(viewvektor)).normalize());
        float cosinusBeta = normale.scalar(halbvektor);
        RgbColor notAmbientPart = new RgbColor(0f, 0f, 0f);
        if (cosinusAlpha > 0) {
            RgbColor diffusPart = diffusCoefficient.multScalar(cosinusAlpha);
            notAmbientPart = diffusPart;
        }
        if (cosinusBeta > 0) {
            RgbColor specularPart = specularCoefficient.multScalar((float) Math.pow((cosinusBeta), specularExponent));
            notAmbientPart = notAmbientPart.add(specularPart);

        }
        return light.getLight().multRGB(notAmbientPart);
    }

    /**
     * As in our case blinn has no reflection, returns always false
     *
     * @return false also nicht reflektiv
     */
    public boolean isReflectiv() {
        return false;
    }

    /**
     * As in our case blinn has no reflection, return null
     *
     * @param reflectionColor
     * @return
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
