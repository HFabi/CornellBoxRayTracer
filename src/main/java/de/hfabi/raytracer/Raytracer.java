package de.hfabi.raytracer;

import de.hfabi.ray.Ray;
import de.hfabi.scene.Scene;
import de.hfabi.scene.camera.Camera;
import de.hfabi.scene.light.Light;
import de.hfabi.scene.material.Material;
import de.hfabi.scene.shape.Shape;
import de.hfabi.ui.Window;
import de.hfabi.utils.RgbColor;
import de.hfabi.utils.algebra.Vec2;
import de.hfabi.utils.algebra.Vec3;
import de.hfabi.utils.io.Log;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Raytracer {
    /** image. */
    private BufferedImage mBufferedImage;
    /** created scene */
    private Scene mScene;
    /** render window. */
    private Window mRenderWindow;
    /** scene camera.*/
    private Camera camera;
    /** all scene objects. */
    private List<Shape> shapeList;
    /** all scene lights. */
    private List<Light> lightList;
    /** depth of recursion. */
    private int mMaxRecursions;
    /** Anti-Aliasing*/
    private int anti_aliasing;
    /** list of intersection tests of a ray. */
    private List <Intersection> intersectionList;
    /** background color, when no object hit. */
    RgbColor backgroundColor = RgbColor.BLACK;


    /**
     * Constructor.
     * @param scene         scene
     * @param renderWindow  render window
     * @param recursions    depth of recursion, for reflection and refraction
     */
    public Raytracer(Scene scene, Window renderWindow, int recursions, int anti_aliasing){
        Log.print(this, "Init");
        mMaxRecursions = recursions;
        mBufferedImage = renderWindow.getBufferedImage();
        mScene = scene;
        mRenderWindow = renderWindow;
        camera=scene.getCamera();
        shapeList=scene.getShapeList();
        lightList=scene.getLightList();
        this.anti_aliasing = anti_aliasing;
    }

    /**
     * Render a scene.
     */
    public void renderScene(){
        Log.print(this, "Start rendering");
        //normales Rendern
        if(anti_aliasing==0){
            for (int y = 0; y < mBufferedImage.getHeight(); y++) {
                // Columns
                for (int x = 0; x < mBufferedImage.getWidth(); x++) {
                    // Erstellen eines Primärstrahls
                    Ray ray = camera.calculateRay(x,y, mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
                    // Methode die Farbe für den jeweiligen Pixel berechnet
                    RgbColor color = sendRay(ray,0, null);
                    mRenderWindow.setPixel(mBufferedImage, color, new Vec2(x,y));
                }
            }
        }
        //Rendern mit Anti-Aliasing
        //Es wurde mit ABSICHT jeweils EIGENE METHODEN erstellt, damit möglichst wenige Rechnungen und oder logische
        //Abfragen bei jedem einzelnen Strahl ausgewertet werdenmüssen, was Zeit kostet.
        if(anti_aliasing==2)
            renderSceneX2();
        if(anti_aliasing==4) {
            //BITTE HIER EINE DER BEIDEN METHODEN adaptiveRenderSceneX4 oder renderSceneX4 wählen.
            //ES IST NORMAL, DAS BEI adaptiveRenderSceneX4 DEER BILDSCHIRM ZUNÄCHST SCHWARZ BLEIBT !!
            //renderSceneX4();
            adaptiveRenderSceneX4();
        }
        if(anti_aliasing==8)
            renderSceneX8();

    }// Ende renderScene()

    /**
     * Adaptives Aussenden von 1 bzw. 4 Strahlen pro Pixel.
     * Zunächst wird ein Strahl durch die Mitte jedes Pixel gesendet, die Richtung sowie Farbe in einem Array abgespeichert.
     * Unterscheiden sich die Farben benachbarter Pixel zu stark, werden 4 Strahlen durch diesen Pixel gesendet.
     */
    public void adaptiveRenderSceneX4(){
        //Speicher initialisieren
        Pixel[][] arrayPixel = new Pixel[mRenderWindow.getmWidth()][mRenderWindow.getmHeight()];

        //SCHRITT 1: (Deshalb ist zunächst der Bildschrim schwarz)
        //Datenspeicher anlegen. Für jeden Pixel des Bild wird ein Strahl durch die Mitte gesendet
        //und der Ray sowie die Farbwerte gespeichert. (Siehe innere Klasse Pixel)
        for (int y = 0; y < mBufferedImage.getHeight(); y++) {
            // Columns
            for (int x = 0; x < mBufferedImage.getWidth(); x++) {
                Ray ray = camera.calculateRay(x,y, mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
                arrayPixel[x][y] = new Pixel();
                arrayPixel[x][y].ray = ray;
                arrayPixel[x][y].color = sendRay(ray,0, null);
            }
        }
        //SCHRITT 2: Adaptiv rendern
        //Step-Size berechnen
        //Ray rayA = camera.calculateRay(0,0, mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
        //Ray rayB = camera.calculateRay(1,0, mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
        //float rayDifference = (rayA.direction.x-(rayB.direction.x));
        // float step = rayDifference/2.0f;
        float step = camera.calculateSingleStep(mRenderWindow.getmWidth(), mRenderWindow.getmHeight());

        for (int y = 0; y < mBufferedImage.getHeight(); y++) {
            // Columns
            for (int x = 0; x < mBufferedImage.getWidth(); x++) {
                RgbColor color = new RgbColor(0,0,0);
                //Pixelvergleich
                if(x+1< mBufferedImage.getWidth() && y+1 < mBufferedImage.getHeight() && x-1> 0 && y-1 > 0 &&
                        arrayPixel[x][y].color.isSimilar(arrayPixel[x+1][y].color) &&
                        arrayPixel[x][y].color.isSimilar(arrayPixel[x][y+1].color)&&
                        arrayPixel[x][y].color.isSimilar(arrayPixel[x-1][y].color) &&
                        arrayPixel[x][y].color.isSimilar(arrayPixel[x][y-1].color)){
                    color = arrayPixel[x][y].color; //Farbe wiederherstellen aus Datenspeicher
                    //BITTE HIER EINE Zeile 161 einkommentieren, um die Kantenerkennung sichtbar zu machen.
                    //color = RgbColor.YELLOW;
                }
                else{
                    //Senden von 4 Strahlen
                    Ray ray = arrayPixel[x][y].ray; //Ray wiederherstellen aus Datenspeicher

                    for(int i = -1; i<2;i=i+2){
                        for(int j = -1;j<2; j=j+2) {
                            Ray tempRay = new Ray();
                            tempRay.direction = (ray.direction.add(new Vec3(step * i, step * j, 0))).normalize();
                            tempRay.startPoint = ray.startPoint;

                            RgbColor tempcolor1 = sendRay(tempRay, 0, null);
                            color = color.add(tempcolor1.multScalar(1.0f / 4.0f));
                        }
                    }

                }
                mRenderWindow.setPixel(mBufferedImage, color, new Vec2(x,y));
            }
        }
    }

    /**
     * Aussenden von 4 Strahlen pro Pixel (verteilt als Quadrat).
     */
    public void renderSceneX4(){
        //Abstand bzw. step für Positionierung des Rays
        float step = camera.calculateSingleStep(mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
        for (int y = 0; y < mBufferedImage.getHeight(); y++) {
            for (int x = 0; x < mBufferedImage.getWidth(); x++) {

                RgbColor color = new RgbColor(0,0,0);
                //Erstellen eines Primärstrahls
                Ray ray = camera.calculateRay(x,y, mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
                //vier oder 8 Strahlen auf Quadrat verteilen
                for(int i = -1; i<2;i=i+2){
                    for(int j = -1;j<2; j=j+2) {
                        Ray tempRay = new Ray();
                        tempRay.direction = (ray.direction.add(new Vec3(step * i, step * j, 0))).normalize();
                        tempRay.startPoint = ray.startPoint;
                        RgbColor tempcolor = sendRay(tempRay, 0, null);
                        color = color.add(tempcolor.multScalar(1.0f / (float) 4.0f));
                    }
                }
                mRenderWindow.setPixel(mBufferedImage, color, new Vec2(x,y));
            }
        }
    }

    /**
     * Aussenden von 8 Strahlen pro Pixel (verteilt als Quadrat).
     */
    public void renderSceneX8(){

        //Abstand bzw. step für Positionierung des Rays
        float step = camera.calculateSingleStep(mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
        for (int y = 0; y < mBufferedImage.getHeight(); y++) {
            for (int x = 0; x < mBufferedImage.getWidth(); x++) {

                RgbColor color = new RgbColor(0,0,0);
                //Erstellen eines Primärstrahls
                Ray ray = camera.calculateRay(x,y, mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
                //vier oder 8 Strahlen auf Quadrat verteilen
                for(int i = -1; i<2;i++){
                    for(int j = -1;j<2; j++) {
                        if(!(i==0 && j==0)) {
                            Ray tempRay = new Ray();
                            tempRay.direction = (ray.direction.add(new Vec3(step * i, step * j, 0))).normalize();
                            tempRay.startPoint = ray.startPoint;
                            RgbColor tempcolor = sendRay(tempRay, 0, null);
                            color = color.add(tempcolor.multScalar(1.0f / 8.0f));
                        }
                    }
                }
                mRenderWindow.setPixel(mBufferedImage, color, new Vec2(x,y));
            }
        }
    }

    /**
     * Aussenden von 2 Strahlen pro Pixel (als Linie).
     */
    public void renderSceneX2(){
        //Abstand bzw. step für Positionierung des Rays
        float step = camera.calculateSingleStep(mRenderWindow.getmWidth(), mRenderWindow.getmHeight());

        for (int y = 0; y < mBufferedImage.getHeight(); y++) {
            for (int x = 0; x < mBufferedImage.getWidth(); x++) {
                RgbColor color = new RgbColor(0, 0, 0);
                //Erstellen eines Primärstrahls
                Ray ray = camera.calculateRay(x, y, mRenderWindow.getmWidth(), mRenderWindow.getmHeight());
                //zwei Strahlen auf Linie verteilen
                for (int i = -1; i < 2; i = i + 2) {
                    Ray tempRay = new Ray();
                    tempRay.direction = (ray.direction.add(new Vec3(step * i, 0, 0))).normalize();
                    tempRay.startPoint = ray.startPoint;
                    RgbColor tempcolor = sendRay(tempRay, 0, null);
                    color = color.add(tempcolor.multScalar(1.0f / 2.0f));
                }
                mRenderWindow.setPixel(mBufferedImage, color, new Vec2(x, y));
            }
        }

    }

    /**
     * Umrechnung Farbe für Farbverlauf (Aufgabe 1)
     * Farben werden um 1f erhöht, damit keine negativen Werte auftreten
     * und dann auf den Berech 0 bis 255 verteilt.
     * @param colors Farbe
     * @return RGB Color
     */
    private Vec3 mapColor (Vec3 colors){
        colors.x = (float) ((colors.x+1f)/2.0);
        colors.y = (float) ((colors.y+1f)/2.0);
        colors.z = (float) ((colors.z+1f)/2.0);
        return colors;
    }

    /**
     * Berechnet die Farbe am aktuellen Punkt beim Aussenden eines
     * Primärstrahls.
     * @param ray Strahl
     * @return Farbe in RGB
     */
    private RgbColor sendRay(Ray ray, int recursionsCounter, Shape ownShape) {

        // Neue Intersection Liste (für jede Rekursion neu erstellen)
        intersectionList = new ArrayList<>();
        RgbColor color = backgroundColor;

        // - - - - - - - - - - - - - - - - -
        // SCHNITTTEST ALLER OBJEKTE MIT RAY
        // - - - - - - - - - - - - - - - - -
        if(ownShape!=null) {
            //Schnittpunkte mit eigenem Objekt zunächst ausschliessen
            shapeList.remove(ownShape);
            for (Shape shape : shapeList) {
                // aktuellen Schnittpunkt zur Liste hinzufügen, wenn es kein "leerer" Schnittpunkt ist
                Intersection intersection = shape.intersect(ray);
                if (intersection != null) intersectionList.add(intersection); //naher schnittpunkt
            }
            //Bei Transparenz auch Schnittpunkte mit eigenenm Objekt betrachten
            if(ownShape.getMaterial().isTransparent()) {
                Intersection intersection = ownShape.intersect(ray); //entfernter schnittpunkt
                if (intersection != null && Math.abs(intersection.getDistance())>0.001f) intersectionList.add(intersection);
            }
            //entfernen des eigenen Objekts rückgängig machen
            shapeList.add(ownShape);
        }
        else {
            // Schnitttest aller Objekte mit dem Ray
            for (Shape shape : shapeList) {
                // aktuellen Schnittpunkt zur Liste hinzufügen, wenn es kein "leerer" Schnittpunkt ist
                Intersection intersection = shape.intersect(ray);
                if (intersection != null) intersectionList.add(intersection);
            }
        }
        if (intersectionList.size() != 0) {
            //nearest Intersection bestimmen
            Intersection nearestIntersection = searchNearestIntersection(intersectionList);
            // Material des jeweiligen Shape abspeichern
            Material material = nearestIntersection.getShape().getMaterial();
            // Farbe setzt sich zusammen aus ambienten, diffusen und spekularen anteil
            // ambient wird nur einmal berechnet, während diffus und spekular pro Licht berechnet wird
            color = material.getAmbientColor();

            // - - - - - - -
            // SEKUNDÄRSTRAHL
            // - - - - - - -
            if (ownShape == null || ownShape != nearestIntersection.getShape()) {

                // Durch Lichtliste iterieren
                for (Light light : lightList) {

                    if (light.isAreaLight()){
                        for (Light pointLight : light.getPointLightList()){
                            Vec3 lightDirection = (pointLight.getPosition().sub(nearestIntersection.getInterSectionPoint()));
                            Vec3 normalizedLightDirection = lightDirection.normalize();
                            Ray lightRay = new Ray(nearestIntersection.getInterSectionPoint(), normalizedLightDirection);
                            // Schattentest : Wenn Schatten, nur Ambienten Wert, sonst Farbwert nach Blinn/ Phong/ Lambert addieren
                            if (!isShadow(lightRay, nearestIntersection.getShape(), lightDirection.length())) {
                                // für jedes Licht calculateColor aufrufen, die den Farbwert zurückgibt, Farbwerte addieren
                                color = color.add(material.getColor(pointLight, normalizedLightDirection, nearestIntersection, camera.getPosition()));

                            }
                        }

                    }
                    else {

                        Vec3 lightDirection = (light.getPosition().sub(nearestIntersection.getInterSectionPoint()));
                        Vec3 normalizedLightDirection = lightDirection.normalize();
                        Ray lightRay = new Ray(nearestIntersection.getInterSectionPoint(), normalizedLightDirection);
                        // Schattentest : Wenn Schatten, nur Ambienten Wert, sonst Farbwert nach Blinn/ Phong/ Lambert addieren
                        if (!isShadow(lightRay, nearestIntersection.getShape(), lightDirection.length())) {

                            // für jedes Licht calculateColor aufrufen, die den Farbwert zurückgibt, Farbwerte addieren
                            color = color.add(material.getColor(light, normalizedLightDirection, nearestIntersection, camera.getPosition()));

                        }
                    }
                }

            }
            // - - - - - - - - - - - - -
            // REKURSION UND TRANSPARENZ
            // - - - - - - - - - - - - -
            // Rekursiontiefe, nur wenn maximale Rekursionstiefe noch nicht erreicht ist, dann Reflexion und Refraktion berechnen
            if (recursionsCounter <= mMaxRecursions) {

                // Reflexion
                if (nearestIntersection.getShape().getMaterial().isReflectiv()) {
                    Ray reflectionRay = nearestIntersection.calculateReflectionRay();
                    // rekursiver Aufruf, d.h. reflexionsRay wird in die Szene geschickt und geprüft welche Objekte er schneidet

                    RgbColor reflectionColor = sendRay(reflectionRay, ++recursionsCounter, nearestIntersection.getShape());
                    // Farbe des geschnitten Objektes auf die eigene Farbe dazuaddieren
                    color = color.add(material.getReflectionColor(reflectionColor));

                }
                // Refraktion
                if (nearestIntersection.getShape().getMaterial().isTransparent()) {
                    float snelliusScene = mScene.getRefractiveIndex();
                    float snelliusShape = nearestIntersection.getShape().getMaterial().getRefractiveIndex();
                    Ray refractionRay = nearestIntersection.calculateRefractionRay(snelliusScene, snelliusShape);
                    RgbColor refractionColor = sendRay(refractionRay, ++recursionsCounter, nearestIntersection.getShape());
                    color = color.add(material.getRefractionColor(refractionColor));
                }
            }
        }
        return color;
    }

    /**
     * Hit-Test nur für Schatten-Test des Sekundärstrahls.
     * Schneller, weil sofort abgebrochen wird sobald ein Objekt
     * gefunden wurde, das im Weg zur Lichtquelle steht.
     * @param ray Strahl (sekundärstrahl)
     * @return true: ist im Schatten, false: ist nicht im Schatten.
     */
    private boolean isShadow (Ray ray, Shape _shape, float lightDistance){
        for (Shape shape: shapeList) {
            // nicht nochmal auf das gleiche Shape-Objekt testen
            Intersection intersection=shape.intersect(ray);
            if (intersection!=null && shape != _shape && intersection.getDistance()<lightDistance) {
                //Log.print(this, "Intersection: "+intersection+"Distanz: "+intersection.getDistance()+"Shape: "+ _shape.toString()+"intersection Shape: "+shape.toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Suchen der nächstgelegenen Intersection.
     * @param intersectionList  Intersection Liste
     * @return Intersection
     */
    public Intersection searchNearestIntersection(List<Intersection> intersectionList){
        Intersection nearestIntersection=null;
        for (Intersection intersection : intersectionList) {
            if (nearestIntersection==null) { //noch kein Vergleichsobjekt vorhanden
                nearestIntersection = intersection;
            } else {
                if (nearestIntersection.getDistance() > intersection.getDistance()){
                    nearestIntersection = intersection;
                }
            }
        }
        return nearestIntersection;
    }

    /**
     * Innere Klasse Pixel.
     * Datentyp für Adaptives Anti-Aliasing. Wird nur in der klasse Raytracer benötigt
     */
    class Pixel{
        /**Speicher Ray */
        Ray ray;
        /**Speicher Farbwert */
        RgbColor color;
    }//end Klasse Pixel
}
