package de.hfabi.scene.camera;

import de.hfabi.ray.Ray;
import de.hfabi.scene.SceneObject;
import de.hfabi.utils.algebra.Vec3;

public abstract class Camera extends SceneObject {
    /**
     * View-Vector.
     */
    protected Vec3 lookAt;
    /**
     * Camera-Up-Vector.
     */
    protected Vec3 cameraUpVector;
    /**
     * Image angle.
     */
    protected float viewAngle;
    /**
     * focal length.
     */
    protected float focalLength;
    /**
     * side vector.
     */
    protected Vec3 sideVector;
    /**
     * midpoint of view plant.
     */
    protected Vec3 midpoint;
    /**
     * width.
     */
    protected float width;
    /**
     * hight.
     */
    protected float height;

    /**
     * Constructor.
     *
     * @param imageHeight   image height
     * @param imageWidth    image width
     * @param pos           camera position
     * @param lookAt        view vector
     * @param worldUpVector User-Up vector
     * @param viewAngle     view angle
     * @param focalLength   focal length
     */
    public Camera(float imageHeight, float imageWidth, Vec3 pos, Vec3 lookAt, Vec3 worldUpVector, float viewAngle, float focalLength) {
        super(pos);
        //Kamera Koordinatensystem erzeugen
        this.lookAt = (lookAt.sub(pos)).normalize();
        this.sideVector = this.lookAt.cross(worldUpVector).normalize();
        this.cameraUpVector = this.sideVector.cross(this.lookAt).normalize();
        //Ausgleich der Koordinatensystem Ausrichtung Bildschirm
        this.cameraUpVector = this.cameraUpVector.multScalar(-1);
        //Viewplane Daten berechnen
        this.viewAngle = (float) (viewAngle * Math.PI / 180.0);
        this.midpoint = pos.add(this.lookAt);
        this.height = (float) (2f * Math.tan(this.viewAngle / 2.0f));
        float calculateRatio = imageWidth / (float) imageHeight;
        this.width = (float) (this.height * calculateRatio);
        //Fokus(noch nicht verwendet)
        this.focalLength = focalLength;
    }

    /**
     * Calculates direction of primary ray.
     *
     * @param x           pixel position x
     * @param y           pixel position y
     * @param imageHeight height final image
     * @param imageWidth  width final image
     * @return point of ray
     */
    public abstract Vec3 calculateDestinationPoint(int x, int y, int imageWidth, int imageHeight);

    /**
     * Calculates primary ray.
     *
     * @param x           pixel position x
     * @param y           pixel position y
     * @param imageWidth  width final image
     * @param imageHeight height final image
     * @return Ray (Strahl)
     */
    public abstract Ray calculateRay(int x, int y, int imageWidth, int imageHeight);

    /**
     * Calculates distance between two pixels, related to 3d room.
     * This is the basis for the step width of the anti aliasing
     *
     * @param imageWidth  width final image
     * @param imageHeigth height final image
     * @return step
     */
    public abstract float calculateSingleStep(int imageWidth, int imageHeigth);
}
