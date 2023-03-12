package de.hfabi.scene.camera;

import de.hfabi.ray.Ray;
import de.hfabi.utils.algebra.Vec3;

public class PerspCamera extends Camera {
    /**
     * Constructor.
     *
     * @param imageHeight   image height
     * @param imageWidth    image width
     * @param pos           camera position
     * @param lookAt        look at direction
     * @param worldUpVector Up-Vector of world coordinate system
     * @param viewAngle     view angle
     * @param focalLength   focal length
     */
    public PerspCamera(float imageHeight, float imageWidth, Vec3 pos, Vec3 lookAt, Vec3 worldUpVector, float viewAngle, float focalLength) {
        super(imageHeight, imageWidth, pos, lookAt, worldUpVector, viewAngle, focalLength);
    }

    /**
     * Calcualtes direction of primary ray.
     *
     * @param x           pixel position x
     * @param y           pixel position y
     * @param imageHeight image height
     * @param imageWidth  image width
     * @return point of ray
     */
    public Vec3 calculateDestinationPoint(int x, int y, int imageWidth, int imageHeight) {
        // normalisieren des Fensters
        float normalizeX = (float) (2f * ((x + 0.5f) / imageWidth) - 1f);
        float normalizeY = (float) (2f * ((y + 0.5f) / imageHeight) - 1f);
        //Der normierte SideVector und CameraUpVector wird in Abhängigkeit von den
        //Bildschirmkoordinaten (siehe Berechnung normalizeX, nomralizeY) so
        //skaliert, dass die Summe von MidPoint, SideVector und CameraUpVector
        //den Ortsvektor zum DestinationPoint ergibt.
        Vec3 vectorX = sideVector.multScalar((width / 2f) * normalizeX);
        Vec3 vectorY = cameraUpVector.multScalar((height / 2f) * normalizeY);
        return (this.midpoint.add(vectorX).add(vectorY));
    }

    /**
     * Calculates primary ray.
     *
     * @param x           pixel position x
     * @param y           pixel position y
     * @param imageWidth  final image width
     * @param imageHeight final image height
     * @return ray
     */
    public Ray calculateRay(int x, int y, int imageWidth, int imageHeight) {
        Vec3 destinationPoint = calculateDestinationPoint(x, y, imageWidth, imageHeight);
        Vec3 direction = (destinationPoint.sub(getPosition())).normalize();
        return new Ray(getPosition(), direction);
    }

    /**
     * Calculates distance between two pixels, related to 3d room.
     * This is the basis for the step width of the anti aliasing
     *
     * @param imageWidth  image width
     * @param imageHeigth image height
     * @return step
     */
    public float calculateSingleStep(int imageWidth, int imageHeigth) {
        Ray rayA = calculateRay(0, 0, imageWidth, imageHeigth);
        Ray rayB = calculateRay(1, 0, imageWidth, imageHeigth);
        float rayDifference = (rayA.direction.x - (rayB.direction.x));
        float step = rayDifference / 2.0f;
        return step;
    }
}
