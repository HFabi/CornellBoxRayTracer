package de.hfabi.scene;

import de.hfabi.scene.camera.Camera;
import de.hfabi.scene.camera.PerspCamera;
import de.hfabi.scene.light.AreaLight;
import de.hfabi.scene.light.Light;
import de.hfabi.scene.light.Pointlight;
import de.hfabi.scene.material.Lambert;
import de.hfabi.scene.material.Material;
import de.hfabi.scene.shape.Plane;
import de.hfabi.scene.shape.Shape;
import de.hfabi.scene.shape.Sphere;
import de.hfabi.scene.shape.Square;
import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;
import de.hfabi.utils.io.Log;

import java.util.LinkedList;
import java.util.List;

public class Scene {

    Camera camera;

    List<Shape> shapeList;

    List<Light> lightList;

    RgbColor ambientIntensity;

    float refractiveIndex;

    public Scene() {
        Log.print(this, "Init");
        shapeList = new LinkedList<Shape>();
        lightList = new LinkedList<Light>();
    }

    /**
     * Creates a new perspective camera
     *
     * @param imageHeight   image height
     * @param imageWidth    image width
     * @param pos           camera position
     * @param lookAt        center of interest
     * @param worldUpVector Up-Vektor of wolrd coordinate system
     * @param viewAngle     view angle
     * @param focalLength   focal length
     */
    public void createPerspCamera(float imageHeight, float imageWidth, Vec3 pos, Vec3 lookAt, Vec3 worldUpVector, float viewAngle, float focalLength) {
        camera = new PerspCamera(imageHeight, imageWidth, pos, lookAt, worldUpVector, viewAngle, focalLength);
    }

    public Camera getCamera() {
        return camera;
    }

    public void createSphere(Vec3 position, float radius, Material material) {
        Shape shape = new Sphere(position, radius, material);
        shapeList.add(shape);
    }

    public void createPointLight(Vec3 position, RgbColor color) {
        Pointlight pointlight = new Pointlight(position, color);
        lightList.add(pointlight);
    }

    public List<Shape> getShapeList() {
        Log.print(this, "Länge der Shapelist " + shapeList.size());
        return shapeList;
    }

    public List<Light> getLightList() {
        Log.print(this, "Länge der Lightlist " + lightList.size());
        return lightList;
    }

    public void setAmbientIntensity(RgbColor ambientIntensity) {
        this.ambientIntensity = ambientIntensity;
    }

    public void createPlane(Vec3 position, Vec3 normal, Material material) {
        Shape shape = new Plane(position, normal, material);
        shapeList.add(shape);
    }

    public void createSquare(Vec3 position, Vec3 normal, float sideLength, Material material) {
        Shape shape = new Square(position, normal, sideLength, material);
        shapeList.add(shape);
    }

    public void createCornellBox(RgbColor ambientIntensity) {
        // Lambert
        //Ebene hinten
        createPlane(new Vec3(0, 0, -2), new Vec3(0, 0, 1), new Lambert(new RgbColor(0.5f, 0.5f, 0.5f), new RgbColor(0.9f, 0.9f, 0.9f), ambientIntensity));
        //createPlane(new Vec3(0,0,3),new Vec3(0,0,1) , new Phong(new RgbColor(0.5f,0.5f,0.5f), new RgbColor(0.3f,0.3f,0.3f) ,new RgbColor(0.3f,0.3f,0.3f), ambientIntensity,50,new RgbColor(0f,0f,0f),new RgbColor(0.5f,0.5f,0.5f)));
        //Ebene links
        createPlane(new Vec3(-4, 0, 0), new Vec3(1, 0, 0), new Lambert(new RgbColor(0.5f, 0f, 0f), new RgbColor(0.7f, 0f, 0f), ambientIntensity));
        //Ebene rechts
        createPlane(new Vec3(4, 0, 0), new Vec3(-1, 0, 0), new Lambert(new RgbColor(0f, 0f, 0.5f), new RgbColor(0f, 0f, 0.7f), ambientIntensity));
        //createPlane(new Vec3(5,0,0),new Vec3(-1,0,0) , new Phong(new RgbColor(0.5f,0.5f,0.5f), new RgbColor(0.3f,0.3f,0.3f) ,new RgbColor(0.3f,0.3f,0.3f), ambientIntensity,50,new RgbColor(0f,0f,0f),new RgbColor(0.5f,0.5f,0.5f)));
        //Ebene unten
        createPlane(new Vec3(0, -3.33f, 0), new Vec3(0, 1, 0), new Lambert(new RgbColor(0.9f, 0.9f, 0.9f), new RgbColor(0.9f, 0.9f, 0.9f), ambientIntensity));
        //Ebene oben
        createPlane(new Vec3(0, 3.33f, 0), new Vec3(0, -1, 0), new Lambert(new RgbColor(0.9f, 0.9f, 0.9f), new RgbColor(0.9f, 0.9f, 0.9f), ambientIntensity));
        // Ebene vorne (hinter der Kamera)
        createPlane(new Vec3(0, 0, 8), new Vec3(0, 0, -1), new Lambert(new RgbColor(0.4f, 0.4f, 0.4f), new RgbColor(0.8f, 0.8f, 0.8f), ambientIntensity));

        /* Spiegelnd Phong
        //Ebene hinten
        createPlane(new Vec3(0,0,3),new Vec3(0,0,1) ,new Phong(new RgbColor(0.5f,0.5f,0.5f), new RgbColor(0.3f,0.3f,0.3f) ,new RgbColor(0.3f,0.3f,0.3f), ambientIntensity,50,new RgbColor(0.5f,0.5f,0.5f),new RgbColor(0.5f,0.5f,0.5f)));
        //Ebene links
        createPlane(new Vec3(-2,0,0),new Vec3(1,0,0) ,  new Phong(new RgbColor(0.1f,0.1f,0.9f), new RgbColor(0.1f,0.1f,0.9f) ,new RgbColor(0.1f,0.1f,0.9f), ambientIntensity,50,new RgbColor(0.5f,0.5f,0.5f),new RgbColor(0.5f,0.5f,0.5f)));
        //Ebene recht
        createPlane(new Vec3(2,0,0),new Vec3(-1,0,0) , new Phong(new RgbColor(0.9f,0.1f,0.1f), new RgbColor(0.9f,0.1f,0.1f) ,new RgbColor(0.9f,0.1f,0.1f), ambientIntensity,50,new RgbColor(0.5f,0.5f,0.5f),new RgbColor(0.5f,0.5f,0.5f)));
        //Ebene unten
        createPlane(new Vec3(0,-3.5f,0),new Vec3(0,1,0) ,  new Phong(new RgbColor(0.5f,0.5f,0.5f), new RgbColor(0.3f,0.3f,0.3f) ,new RgbColor(0.3f,0.3f,0.3f), ambientIntensity,50,new RgbColor(0.5f,0.5f,0.5f),new RgbColor(0.5f,0.5f,0.5f)));
        //Ebene oben
        createPlane(new Vec3(0,3.5f,0),new Vec3(0,-1,0) ,  new Phong(new RgbColor(0.5f,0.5f,0.5f), new RgbColor(0.3f,0.3f,0.3f) ,new RgbColor(0.3f,0.3f,0.3f), ambientIntensity,50,new RgbColor(0.5f,0.5f,0.5f),new RgbColor(0.5f,0.5f,0.5f)));
        */

        //Quadrat Licht
        //createSquare(new Vec3(0,3.4f,9f),new Vec3(0,-1,0), 1f, new Lambert(new RgbColor(1f,1f,1f), new RgbColor(0f,0f,0f), new RgbColor(0.8f,0.8f,0.8f)));
    }


    public void createAreaLight(Vec3 position, Vec3 normal, float sideLength, RgbColor color, int amountSample) {
        createSquare(position, normal, sideLength, new Lambert(new RgbColor(1f, 1f, 1f), new RgbColor(0f, 0f, 0f), new RgbColor(0.8f, 0.8f, 0.8f)));

        Light light = new AreaLight(new Vec3(position.x, position.y - 0.2f, position.z), color, amountSample, sideLength);
        lightList.add(light);

    }

    public void setRefractiveIndex(float medium) {
        this.refractiveIndex = medium;
    }

    public float getRefractiveIndex() {
        return refractiveIndex;
    }
}
