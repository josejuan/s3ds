package com.s3ds.recon;

import com.jogamp.opengl.GL2;

import java.awt.event.KeyEvent;

public class Decorator implements GLObject {

    public static float[] COLOR_RED = {1, 0, 0};
    public static float[] COLOR_GREEN = {0, 1, 0};
    public static float[] COLOR_BLUE = {0, 0, 1};
    public static float[] COLOR_WHITE = {1, 1, 1};

    private final GLObject object;
    private final float[] color;

    public Decorator(GLObject object, float[] color) {
        this.color = color;
        this.object = object;
    }

    @Override
    public void draw(GL2 gl) {
        gl.glColor3fv(color, 0);
        object.draw(gl);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        object.keyPressed(e);
    }

    public static Decorator decorate(GLObject object, float [] color) {
        return new Decorator(object, color);
    }
}
