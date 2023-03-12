package de.hfabi;

import de.hfabi.raytracer.Raytracer;
import de.hfabi.scene.Scene;
import de.hfabi.scene.material.Phong;
import de.hfabi.ui.Window;
import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec3;

public class Main {
    /**
     * BOX_DIMENSION
     **/
    static int IMAGE_WIDTH = 800;
    static int IMAGE_HEIGHT = 600;

    /**
     * RAYTRACER
     **/
    //anti aliasing
    static final int x8 = 8; //Entspricht 8 Strahlen
    static final int x4 = 4; //Entspricht 4 Strahlen
    static final int x2 = 2; //Entspricht 4 Strahlen
    static final int x0 = 0; //Entspricht 1 Strahl
    //Bitte gerne auch das ADAPTIVE Anti-Aliasing testen.
    //Dazu hier x4 wählen und in der Klasse Raytracer Zeile eine der beiden Zeilen 113 114 wählen, die andere auskommentieren.
    static int ANTI_Aliasing = x4;


    /**
     * depth of recursion
     */
    static int RECURSIONS = 4;

    /**
     * light
     */
    //ambientes Licht der gesamten Scene
    static RgbColor ambientIntensity = new RgbColor(0.1f, 0.1f, 0.1f);
    //schnellzugriff Samplezahl Area Light
    static int AREA_LIGHT_SAMPLES = 40;

    /**
     * camera
     */
    static Vec3 position = new Vec3(0, 0, 17);
    static Vec3 lookAt = new Vec3(0, 0, 0);
    static Vec3 worldUp = new Vec3(0, 1, 0);
    static int viewAngle = 35; // in DEG
    static int focalLength = 1;

    /**
     * materials
     */
    //Glas
    final static float GLAS = 1.51f;
    //Luft
    final static float AIR = 1.000292f;
    //Wasser
    final static float WATER = 1.33f;


    /**
     * Initial method. This is where the show begins.
     **/
    public static void main(String[] args) {
        long tStart = System.currentTimeMillis();

        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);

        draw(renderWindow);

        renderWindow.exportRendering(Double.toString(stopTime(tStart)), RECURSIONS, ANTI_Aliasing);
    }

    /**
     * Draw the scene that was set up
     **/
    private static void draw(Window renderWindow) {
        Scene renderScene = new Scene();
        setupCameras(renderScene);
        setupScene(renderScene);
        setupLights(renderScene);
        raytraceScene(renderWindow, renderScene);
    }

    /**
     * Raytrace through the scene
     **/
    private static void raytraceScene(Window renderWindow, Scene renderScene) {
        Raytracer raytracer = new Raytracer(renderScene, renderWindow, RECURSIONS, ANTI_Aliasing);

        raytracer.renderScene();
    }

    /**
     * Stop the time for debug only
     **/
    private static double stopTime(long tStart) {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }

    /**
     * Creates camera
     *
     * @param scene scene
     */
    private static void setupCameras(Scene scene) {
        scene.createPerspCamera(IMAGE_HEIGHT, IMAGE_WIDTH, position, lookAt, worldUp, viewAngle, focalLength);
    }

    /**
     * Creates scene
     *
     * @param scene scene
     */
    private static void setupScene(Scene scene) {
        //Kugeln in der box
        //kleine Kugel weiter hinten
        scene.createSphere(new Vec3(-1f, -2.23f, 2.66f), 1f, new Phong(new RgbColor(0.5f, 0.5f, 0.5f), new RgbColor(0.3f, 0.3f, 0.3f), new RgbColor(0.3f, 0.3f, 0.3f), ambientIntensity, 50, new RgbColor(1f, 1f, 1f), new RgbColor(0f, 0f, 0f), 0));
        //große Kugel weiter vorne
        scene.createSphere(new Vec3(1, -2.23f, 4.33f), 1f, new Phong(new RgbColor(0.5f, 0.5f, 0.5f), new RgbColor(0.3f, 0.3f, 0.3f), new RgbColor(0.3f, 0.3f, 0.3f), ambientIntensity, 50, new RgbColor(0f, 0f, 0f), new RgbColor(1f, 1f, 1f), 2.417f));

        //cornell-Box
        scene.createCornellBox(ambientIntensity);

        //scene
        scene.setRefractiveIndex(AIR);

    }

    /**
     * Creates all scene lights
     *
     * @param scene scene
     */
    private static void setupLights(Scene scene) {
        // scene.createPointLight(new Vec3(0,3.3f,9), new RgbColor(0.7f,0.7f,0.7f));
        scene.createAreaLight(new Vec3(0, 3.2f, 3.5f), new Vec3(0, -1, 0), 1.2f, new RgbColor(0.8f, 0.8f, 0.8f), AREA_LIGHT_SAMPLES);
    }
}