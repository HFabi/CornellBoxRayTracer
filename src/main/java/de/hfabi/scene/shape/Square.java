package de.hfabi.scene.shape;

import de.hfabi.ray.Ray;
import de.hfabi.raytracer.Intersection;
import de.hfabi.scene.material.Material;
import de.hfabi.utils.algebra.Vec3;

public class Square extends Plane {
    /**
     * material.
     */
    Material material;
    /**
     * position (center).
     */
    Vec3 position;
    float halfSideLength;

    /**
     * Constructor.
     *
     * @param position   position
     * @param normal     normal
     * @param sideLength side length
     * @param material   material
     */
    public Square(Vec3 position, Vec3 normal, float sideLength, Material material) {
        super(position, normal, material);
        //Berechnung der Eckpunkte
        this.material = material;
        this.position = position;
        this.halfSideLength = sideLength / 2.0f;
    }

    /**
     * Calculates intersection.
     *
     * @param ray ray
     * @return Intersection
     */
    @Override
    public Intersection intersect(Ray ray) {
        Intersection intersection = super.intersect(ray);
        if (intersection != null) {
            Vec3 intersectionPoint = intersection.getInterSectionPoint();
            if (Math.abs(intersectionPoint.x - this.position.x) > halfSideLength)
                return null;
            if (Math.abs(intersectionPoint.z - this.position.z) > halfSideLength)
                return null;
            //ist in Ebene
            intersection.setShape(this);
            return intersection;
        }
        return null;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public String toString() {
        return "Square";
    }
}
