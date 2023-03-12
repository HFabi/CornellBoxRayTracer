package de.hfabi.scene.shape;

import de.hfabi.ray.Ray;
import de.hfabi.raytracer.Intersection;
import de.hfabi.scene.material.Material;
import de.hfabi.utils.algebra.Matrix4x4;
import de.hfabi.utils.algebra.Vec3;

public class Sphere extends Shape {
    /**
     * radius.
     */
    float radius;
    /**
     * transformationsmatrix.
     */
    Matrix4x4 transformMatrix;
    /**
     * Inverse Matrix.
     */
    Matrix4x4 inverseMatrix;
    /**
     * Material.
     */
    Material material;

    /**
     * Constructor.
     *
     * @param position position
     * @param radius   radius
     * @param material material
     */
    public Sphere(Vec3 position, float radius, Material material) {
        super(position);
        this.radius = radius;
        this.transformMatrix = calculateTransformMatrix(position);
        this.inverseMatrix = this.transformMatrix.invert();
        this.material = material;
    }


    /**
     * Check intersection with sphere.
     *
     * @param ray ray
     * @return intersection
     */
    public Intersection intersect(Ray ray) {

        Intersection intersection;
        // Strahl mit der TransformMatrix der Kugel transformieren
        Vec3 transformedDirection = ray.direction;
        Vec3 transformedStartPoint = this.inverseMatrix.multVec3(ray.startPoint, true);
        Ray transformedRay = new Ray(transformedStartPoint, transformedDirection);
        // Schnitttest mit Objekt selber berechnen
        // hit-Test: Diskriminante berechnen
        float b = 2f * (transformedStartPoint.scalar(transformedDirection));
        float c = (float) Math.pow(transformedStartPoint.x, 2f) + (float) Math.pow(transformedStartPoint.y, 2f) + (float) Math.pow(transformedStartPoint.z, 2f) - 1;

        float determinant = (float) Math.pow(b, 2f) - (4f * c);
        boolean hit = (determinant >= 0);

        if (hit) {
            // Wenn hit-Test positiv ist, genaue Schnittpunkte berechnen
            float t1 = (-b - (float) Math.sqrt(determinant)) / 2f;
            float t2 = (-b + (float) Math.sqrt(determinant)) / 2f;

            // temporärer Speicher für das kleinste positive t
            float t = -1;
            // Test, welcher Schnittpunk näher an der kamera ist, im Positiven!
            //
            if (t1 >= 0) { //WICHTIG >= 0 muss fuer einen fall hinzugenommen werden
                t = t1;
            }
            //
            if (t1 < 0 && t2 > 0) {
                t = t2;
            }

            // Für den näheren Schnittpunkt weitere Werte für das Intersection-Objekt berechnen
            if (t != -1) {
                Vec3 transformedIntersectionPoint = transformedStartPoint.add(transformedDirection.multScalar(t));
                //Rücktransformation berechnen
                Vec3 intersectionPoint = this.transformMatrix.multVec3(transformedIntersectionPoint, true);
                // Distanz zum Anfangspunkt des Rays berechnen
                float distance = intersectionPoint.sub(ray.startPoint).length();

                //Bei Transparenz kann es passieren, das durch self-shadowing der Punkt t1 ausgewählt wurde.
                //Deshalb wird hier die Distance überprüft und der zweit-nächste Punkt berechnet und zurückgegeben.
                //Das passiert an dieser STelle, weil dadurch der zweite Punkt auch wirklich nur für diesen
                //Fall ausgerechnet wird (und nicht immer 2 Punkte von der Intersection berechnet und an den Raytracer
                //übergeben werden müssen)
                if (Math.abs(distance) < 0.001f && t == t1) {
                    t = t2;
                    transformedIntersectionPoint = transformedStartPoint.add(transformedDirection.multScalar(t));
                    //Rücktransformation berechnen
                    intersectionPoint = this.transformMatrix.multVec3(transformedIntersectionPoint, true);
                    // Distanz zum Anfangspunkt des Rays berechnen
                    distance = intersectionPoint.sub(ray.startPoint).length();
                }
                // Normale berechnen
                Vec3 normal = intersectionPoint.sub(getPosition()).normalize();
                // neues Intersection-objekt erstellen
                intersection = new Intersection(intersectionPoint, normal, ray, null, this, distance, true, true);

                return intersection;
            }
        }

        return null;
    }

    /**
     * Calculates die transformed matrix of sphere.
     *
     * @param pos position
     * @return transformed matrix
     */
    public Matrix4x4 calculateTransformMatrix(Vec3 pos) {
        Matrix4x4 transformMatrix = new Matrix4x4();
        transformMatrix.scale(radius);
        transformMatrix.translateXYZ(pos);
        return transformMatrix;
    }

    /**
     * Material of sphere.
     *
     * @return Material
     */
    public Material getMaterial() {
        return this.material;
    }

    public String toString() {
        return "Kugel  ::  Position:" + this.getPosition() + "  Radius:" + radius;
    }
}
