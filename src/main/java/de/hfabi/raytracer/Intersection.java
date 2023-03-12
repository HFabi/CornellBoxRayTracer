package de.hfabi.raytracer;

import de.hfabi.ray.Ray;
import de.hfabi.scene.shape.Shape;
import de.hfabi.utils.algebra.Vec3;

/**
 * Intersection
 */
public class Intersection {
    /**
     * intersection point.
     */
    private Vec3 interSectionPoint;
    /**
     * normal at intersection of area.
     */
    private Vec3 normal;
    /**
     * incoming ray into the intersection point.
     */
    private Ray inRay;
    /**
     * outgoing ray, goes out from intersection point, e.g. for reflection/ refraction.
     */
    private Ray outRay;
    /**
     * object, cut out by ray.
     */
    private Shape shape;
    /**
     * distance of intersection point from starting point of the ray.
     */
    private float distance;
    /**
     * true if incoming ray.
     */
    private boolean incoming;
    /**
     * true, if object is hit by ray.
     */
    private boolean hit;

    /**
     * Constructor.
     *
     * @param interSectionPoint intersection point
     * @param normal            normal
     * @param inRay             incoming ray
     * @param outRay            outgoing ray
     * @param shape             shape for intersection calculation
     * @param distance          distance
     * @param incoming          true: is incoming ray, false: is outgoing ray
     * @param hit               true: intersection exists, false: no intersection point
     */
    public Intersection(Vec3 interSectionPoint, Vec3 normal, Ray inRay, Ray outRay, Shape shape, float distance, boolean incoming, boolean hit) {
        this.interSectionPoint = interSectionPoint;
        this.normal = normal;
        this.inRay = inRay;
        this.outRay = outRay;
        this.shape = shape;
        this.distance = distance;
        this.incoming = incoming;
        this.hit = hit;
    }

    /**
     * Calculation of reflection ray.
     *
     * @return reflection ray
     */
    public Ray calculateReflectionRay() {
        // Richtung des eingehenden Strahls umdrehen
        Vec3 incomingRay = inRay.direction.multScalar(-1f);
        float cosinusAlpha = normal.scalar(incomingRay);
        Vec3 reflexionDirection = ((normal.multScalar(2 * cosinusAlpha)).sub(incomingRay)).normalize();
        return new Ray(this.getInterSectionPoint(), reflexionDirection);
    }

    /**
     * Calculation of refraction ray.
     *
     * @return refraction ray
     */
    public Ray calculateRefractionRay(float snelliusScene, float snelliusShape) {
        float snelliusEingang = snelliusScene;
        float snelliusAusgang = snelliusShape;

        // swap refraction index and normal depending on if ray is incoming or outgoing
        Vec3 normale = normal.normalize();
        if (normale.scalar(inRay.direction.normalize()) > 0) {
            snelliusEingang = snelliusShape;
            snelliusAusgang = snelliusScene;
            normale = normale.multScalar(-1).normalize();
        }

        Vec3 incomingRay = inRay.direction.multScalar(-1).normalize();
        // Calcualte angle between normal and incoming ray  >> Skalarproduk
        float cosinusAlpha = normale.scalar(incomingRay);
        // calculation based on Snellius, because da trigonometrical is too slow
        float cosinusBeta = (float) Math.sqrt((1 - (Math.pow((snelliusEingang / snelliusAusgang), 2f) * (1 - Math.pow((cosinusAlpha), 2f)))));
        // calculate direction
        Vec3 refractionDirection = (((normale.multScalar(cosinusAlpha)).sub(incomingRay)).multScalar((snelliusEingang / snelliusAusgang))).sub(normale.multScalar(cosinusBeta));

        return new Ray(this.getInterSectionPoint(), refractionDirection.normalize());
    }

    /**
     * Calculate if out of distance
     */
    public boolean isOutOfDistance() {
        return false;
    }

    public float getDistance() {
        return this.distance;
    }

    public boolean isHit() {
        return this.hit;
    }

    public Vec3 getInterSectionPoint() {
        return this.interSectionPoint;
    }

    public Vec3 getNormal() {
        return this.normal;
    }

    public Shape getShape() {
        return this.shape;
    }

    public Ray getInRay() {
        return this.inRay;
    }

    public String toString() {
        return "INTERSECTION::  intersectPoint:" + this.interSectionPoint;
    }

    public void setOutRay(Ray outRay) {
        this.outRay = outRay;
    }

    public Ray getOutRay() {
        return this.outRay;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
