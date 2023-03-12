package de.hfabi.scene;

import de.hfabi.utils.algebra.Vec3;

public class SceneObject {
    /**
     * position of scene object
     */
    private Vec3 mPosition = new Vec3();

    /**
     * SceneObject is the base of every component
     **/
    public SceneObject(Vec3 pos) {
        mPosition = pos;
    }

    public Vec3 getPosition() {
        return mPosition;
    }
}
