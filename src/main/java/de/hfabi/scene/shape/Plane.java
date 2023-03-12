package de.hfabi.scene.shape;

import de.hfabi.ray.Ray;
import de.hfabi.raytracer.Intersection;
import de.hfabi.scene.material.Material;
import de.hfabi.utils.algebra.Vec3;

public class Plane extends Shape {
    Vec3 normal;
    Material material;

    /**
     * Constructor.
     *
     * @param position position
     */
    public Plane(Vec3 position, Vec3 normal, Material material) {
        super(position);
        this.normal = normal;
        this.material = material;
    }

    /**
     * Calculates intersection.
     *
     * @param ray ray
     * @return Intersection
     */
    @Override
    public Intersection intersect(Ray ray) {
        //Test ob überhaupt ein Schnittpunkt vorliegt
        float tNenner = normal.scalar(ray.direction);
        // nur genauen Schnittpunkt berechnen wenn tNenner kleiner 0
        // Planes werden nur von vorne betrachtet
        if (tNenner < 0) {
            Vec3 transformedStartPoint = ray.startPoint.sub(getPosition());
            float t = -1 * ((normal.scalar(transformedStartPoint)) / tNenner);
            Vec3 intersectionPoint = transformedStartPoint.add(ray.direction.multScalar(t));
            intersectionPoint = intersectionPoint.add(getPosition());

            // Distanz zum Anfangspunkt des Rays berechnen
            float distance = (intersectionPoint.sub(ray.startPoint)).length();
            // neues Intersection-objekt erstellen
            Intersection intersection = new Intersection(intersectionPoint, normal, ray, null, this, distance, true, true);

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
        return "Ebene::" + getPosition();
    }
}
