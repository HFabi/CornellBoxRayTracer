package de.hfabi.scene.shape;

import de.hfabi.ray.Ray;
import de.hfabi.raytracer.Intersection;
import de.hfabi.scene.SceneObject;
import de.hfabi.scene.material.Material;
import de.hfabi.utils.algebra.Vec3;

public abstract class Shape extends SceneObject {

    public Shape(Vec3 position) {
        super(position);
    }

    public abstract Intersection intersect(Ray ray);

    public abstract Material getMaterial();

    public abstract String toString();
}
