package de.hfabi.utils;

import de.hfabi.utils.algebra.Vec3;

/**
 *
 * @author HSD
 */
public class RgbColor {
    public Vec3 colors;

    /**
     * Set each component separately
     **/
    public float red() {
        return colors.x;
    }

    public float green() {
        return colors.y;
    }

    public float blue() {
        return colors.z;
    }

    /**
     * Generate some standard colors
     **/
    public static RgbColor DARK_CUSTOM = new RgbColor(0.02f, 0.01f, 0.01f);
    public static RgbColor RED = new RgbColor(1, 0, 0);
    public static RgbColor DARK_RED = new RgbColor(.01f, 0, 0);
    public static RgbColor GREEN = new RgbColor(0, 1, 0);
    public static RgbColor DARK_GREEN = new RgbColor(0, .01f, 0);
    public static RgbColor BLUE = new RgbColor(0, 0, 1);
    public static RgbColor DARK_BLUE = new RgbColor(0, 0, .01f);
    public static RgbColor WHITE = new RgbColor(1, 1, 1);
    public static RgbColor BLACK = new RgbColor(0, 0, 0);
    public static RgbColor CYAN = new RgbColor(0, 1, 1);
    public static RgbColor MAGENTA = new RgbColor(1, 0, 1);
    public static RgbColor YELLOW = new RgbColor(1, 1, 0);
    public static RgbColor GRAY = new RgbColor(0.5f, 0.5f, 0.5f);
    public static RgbColor SOFT_GRAY = new RgbColor(0.20f, 0.20f, 0.20f);
    public static RgbColor LIGHT_GRAY = new RgbColor(0.25f, 0.25f, 0.25f);
    public static RgbColor DARK_GRAY = new RgbColor(0.1f, 0.1f, 0.1f);

    /**
     * Standard constructor given 3 single values
     **/
    public RgbColor(float r, float g, float b) {
        colors = new Vec3(r, g, b);

        clamp();
    }

    /**
     * Standard constructor given a 3D vector
     **/
    public RgbColor(Vec3 color) {
        colors = color;
        clamp();
    }

    /**
     * Add another color
     **/
    public void add(float r, float g, float b) {
        colors.x += r;
        colors.y += g;
        colors.z += b;

        clamp();
    }

    /**
     * Get another color as the sum of the vector and the given vector
     **/
    public RgbColor add(RgbColor color) {
        return new RgbColor(colors.add(color.colors));
    }

    /**
     * Get another color as the sum of the vector and the given vector
     **/
    public RgbColor multRGB(RgbColor color) {
        return new RgbColor(colors.x * color.red(),
                colors.y * color.green(),
                colors.z * color.blue());
    }

    /**
     * Get another color as the product of the vector and the given value
     **/
    public RgbColor multScalar(float value) {

        return new RgbColor(colors.multScalar(value));
    }

    /**
     * Get single byte value of color
     **/
    public int getRGB() {
        return ((int) (this.red() * 255f) << 16) + ((int) (this.green() * 255f) << 8) + ((int) (this.blue() * 255f));
    }

    /**
     * Get a new color by the power of all values
     **/
    public RgbColor power() {
        return new RgbColor(this.red() * this.red(), this.green() * this.green(), this.blue() * this.blue());
    }

    /**
     * There are no values allowed below 0 and over 1, so clamp them
     **/
    private void clamp() {
        if (this.red() > 1) colors.x = 1f;
        if (this.green() > 1) colors.y = 1f;
        if (this.blue() > 1) colors.z = 1f;

        if (this.red() < 0) colors.x = 0f;
        if (this.green() < 0) colors.y = 0f;
        if (this.blue() < 0) colors.z = 0f;
    }

    /**
     * Print values
     **/
    @Override
    public String toString() {
        return "( " + this.red() + ", " + this.green() + ", " + this.blue() + " )";
    }

    /**
     * Compare to values and return if they are equal or not
     **/
    public boolean equals(RgbColor inColor) {
        return inColor.red() == this.red() && inColor.green() == this.green() && inColor.blue() == this.blue();
    }

    /**
     * Vergleicht zwei Farben. Ist die Abweichung in allen Teilwerten (rgb) kleiner als 0.005, dann
     * werden die Farben als gleich angesehen.
     *
     * @param inColor Farbe
     * @return true: gleich, false: verschieden
     */
    public boolean isSimilar(RgbColor inColor) {
        return Math.abs(inColor.red() - this.red()) < 0.005f && Math.abs(inColor.green() - this.green()) < 0.005f && Math.abs(inColor.blue() - this.blue()) < 0.005f;
    }
}
