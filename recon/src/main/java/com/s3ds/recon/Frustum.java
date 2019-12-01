package com.s3ds.recon;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.awt.event.KeyEvent;

import static com.jogamp.opengl.math.VectorUtil.*;

public class Frustum implements GLObject {
    private static final float STEP = 0.1f;
    private final char id;
    private boolean moving = false;
    private float[] origin;
    private float[] target;
    private float[] up;
    private float height;
    private float width;

    // screen base vectors
    private float[] vx = new float[3];
    private float[] vy = new float[3];

    public Frustum(char id, float[] origin, float[] target, float[] up, float width, float height) {
        this.id = id;
        this.origin = origin;
        this.target = target;
        this.up = up;
        this.width = width;
        this.height = height;
    }

    public void updateScreenBaseVectors() {
        final float[] vz = new float[3];

        subVec3(vz, target, origin);

        crossVec3(vx, vz, up);
        normalizeVec3(vx);

        crossVec3(vy, vx, vz);
        normalizeVec3(vy);
    }

    /**
     * @param result  3D point
     * @param screenX from -1/2 to 1/2
     * @param screenY from -1/2 to 1/2
     */
    public void screen2space(float[] result, float screenX, float screenY) {
        final float w = width * screenX;
        final float h = height * screenY;
        result[0] = target[0] + w * vx[0] + h * vy[0];
        result[1] = target[1] + w * vx[1] + h * vy[1];
        result[2] = target[2] + w * vx[2] + h * vy[2];
    }

    @Override
    public void draw(GL2 gl) {
        updateScreenBaseVectors();

        final float[] u = new float[]{
                origin[0] + vy[0] * height,
                origin[1] + vy[1] * height,
                origin[2] + vy[2] * height
        };
        final float[] r = new float[]{
                origin[0] + vx[0] * height,
                origin[1] + vx[1] * height,
                origin[2] + vx[2] * height
        };

        final float[] a = new float[3];
        final float[] b = new float[3];
        final float[] c = new float[3];
        final float[] d = new float[3];

        screen2space(a, -0.5f, -0.5f);
        screen2space(b, -0.5f, +0.5f);
        screen2space(c, +0.5f, +0.5f);
        screen2space(d, +0.5f, -0.5f);

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3fv(origin, 0);
        gl.glVertex3fv(a, 0);
        gl.glVertex3fv(b, 0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3fv(origin, 0);
        gl.glVertex3fv(d, 0);
        gl.glVertex3fv(c, 0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINES);
        gl.glVertex3fv(b, 0);
        gl.glVertex3fv(c, 0);
        gl.glVertex3fv(a, 0);
        gl.glVertex3fv(d, 0);
        gl.glVertex3fv(origin, 0);
        gl.glVertex3fv(u, 0);
        gl.glVertex3fv(origin, 0);
        gl.glVertex3fv(r, 0);
        gl.glEnd();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == id)
            moving = !moving;
        if(moving) {
            switch (e.getKeyChar()) {
                case 'w':
                    origin[2] += STEP;
                    break;
                case 's':
                    origin[2] -= STEP;
                    break;
                case 'a':
                    origin[0] += STEP;
                    break;
                case 'd':
                    origin[0] -= STEP;
                    break;
                case 't':
                    height *= 1.1f;
                    break;
                case 'g':
                    height *= 0.9f;
                    break;
                case 'h':
                    width *= 1.1f;
                    break;
                case 'f':
                    width *= 0.9f;
                    break;
            }
            System.out.printf("Frustum #%c: (%f, %f, %f) (%f x %f)%n", id, origin[0], origin[1], origin[2], width, height);
        }
    }

    public float[] getOrigin() {
        return origin;
    }

    public float[] getUp() {
        return up;
    }
}
