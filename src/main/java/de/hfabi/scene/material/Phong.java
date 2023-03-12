package de.hfabi.scene.material;

import de.hfabi.raytracer.Intersection;
import de.hfabi.scene.light.Light;
import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;

public class Phong extends Material {
    /**
     * ambienter coefficient
     */
    RgbColor ambientCoefficient;
    /**
     * diffuser coefficient
     */
    RgbColor diffusCoefficient;
    /**
     * sepcular coefficient
     */
    RgbColor specularCoefficient;
    /**
     * ambient intensity
     */
    RgbColor ambientIntensity;
    /**
     * specular Exponent
     */
    double specularExponent;

    /**
     * reflexion coefficient
     */
    RgbColor reflexionCoefficient;

    /**
     * refraction coefficient.
     */
    RgbColor refractionCoefficient;

    /**
     * refraction index.
     */
    float refractiveIndex;

    /**
     * Constructor
     *
     * @param ambientCoefficient    ambient coefficient
     * @param diffusCoefficient     diffuser coefficient
     * @param specularCoefficient   specular coefficient
     * @param ambientIntensity      base intensity
     * @param specularExponent      specular Exponent
     * @param reflexionCoefficient  reflexion coefficient
     * @param refractionCoefficient refraction coefficient
     */
    public Phong(RgbColor ambientCoefficient, RgbColor diffusCoefficient, RgbColor specularCoefficient, RgbColor ambientIntensity, double specularExponent, RgbColor reflexionCoefficient, RgbColor refractionCoefficient, float refractiveIndex) {
        this.ambientCoefficient = ambientCoefficient;
        this.diffusCoefficient = diffusCoefficient;
        this.specularCoefficient = specularCoefficient;
        this.ambientIntensity = ambientIntensity;
        this.specularExponent = specularExponent;
        this.reflexionCoefficient = reflexionCoefficient;
        this.refractionCoefficient = refractionCoefficient;
        this.refractiveIndex = refractiveIndex;
    }

    /**
     * Calculates ambient part of color
     *
     * @return ambient color part
     */
    @Override
    public RgbColor getAmbientColor() {
        return ambientIntensity.multRGB(ambientCoefficient);
    }

    /**
     * Calculates color based on phong
     *
     * @param light          light
     * @param lightDirection light direction
     * @param intersection   intersection point
     * @param cameraPosition camera position
     * @return Phong-color
     */
    @Override
    public RgbColor getColor(Light light, Vec3 lightDirection, Intersection intersection, Vec3 cameraPosition) {
        Vec3 normale = intersection.getNormal().normalize();
        //diffuse Anteil:  N scalar L ( LightDirection wird im Raytracer normalisiert)
        float cosinusAlpha = normale.scalar(lightDirection);
        Vec3 viewvektor = (cameraPosition.sub(intersection.getInterSectionPoint())).normalize();
        // Reflexionsvektorberechnung: 2*(N scalar L)*N-L
        Vec3 reflexionvektor = ((normale.multScalar(2 * cosinusAlpha)).sub(lightDirection)).normalize();
        //spekularer Anteil: V scalar R
        float cosinusBeta = viewvektor.scalar(reflexionvektor);
        RgbColor notAmbientPart = new RgbColor(0f, 0f, 0f);
        // nur wenn cosinusAlpha oder cosinusBeta kleiner 90°, dann wird spekularer und diffuser Anteil berechnet
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

    public RgbColor getReflectionColor(RgbColor reflectionColor) {
        return reflectionColor.multRGB(reflexionCoefficient);
    }

    public boolean isReflectiv() {
        return (!this.reflexionCoefficient.equals(new RgbColor(0, 0, 0)));
    }


    public boolean isTransparent() {
        return (!this.refractionCoefficient.equals(new RgbColor(0, 0, 0)));
    }

    public RgbColor getRefractionColor(RgbColor refractionColor) {
        return refractionColor.multRGB(refractionCoefficient);
    }

    public float getRefractiveIndex() {
        return refractiveIndex;
    }
}
