package de.hfabi.ray;

import de.hfabi.utils.algebra.Vec3;

/**
 * Ray
 */
public class Ray {

    /**
     * start value.
     */
    public Vec3 startPoint;
    /**
     * final value.
     */
    public Vec3 endPoint;
    /**
     * direction.
     */
    public Vec3 direction;
    /**
     * length.
     */
    protected float distance;


    /**
     * Constructor.
     *
     * @param startPoint start point
     * @param direction  direction
     */
    public Ray(Vec3 startPoint, Vec3 direction) {
        this.startPoint = startPoint;
        this.direction = direction;
    }

    public Ray() {

    }

    public String toString() {
        return "RAY :: direction:" + this.direction + " start:" + this.startPoint;
    }
}
