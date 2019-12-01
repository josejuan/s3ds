package com.s3ds.recon;

import com.jogamp.opengl.GL2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.math.VectorUtil.*;

public class ScanLineRecon implements GLObject {
    private final ScanLine sequence;
    private final Frustum projector;
    private final Frustum camera;
    private List<float[]> points = null;
    private byte minLight = 25;

    public ScanLineRecon(final ScanLine sequence, final Frustum projector, final Frustum camera) {
        this.sequence = sequence;
        this.projector = projector;
        this.camera = camera;
    }

    private List<float[]> recon() {
        projector.updateScreenBaseVectors();
        camera.updateScreenBaseVectors();

        final List<float[]> pcloud = new ArrayList<>();
        final byte[][][] pxs = sequence.getPixelsBuffer();
        for (int iframe = 0; iframe < pxs.length; iframe++)
            pcloud.addAll(reconFrame(0.5f - (iframe * 1.0f) / pxs.length, pxs[iframe]));
        return pcloud;
    }

    private List<float[]> reconFrame(float iframe, byte[][] px) {
        final float[] projected = {0, 0, 0};
        projector.screen2space(projected, iframe, 0f);
        subVec3(projected, projected, projector.getOrigin());
        final float[] pNormal = {0, 0, 0};
        crossVec3(pNormal, projected, projector.getUp());
        normalizeVec3(pNormal);

        // a x + b y + c z + d = 0
        final float pD = -pNormal[0] * projector.getOrigin()[0] - pNormal[1] * projector.getOrigin()[1] - pNormal[2] * projector.getOrigin()[2];

        final List<float[]> pcloud = new ArrayList<>();
        for (int ix = 0; ix < px.length; ix++)
            pcloud.addAll(reconSliver(0.5f - (ix * 1.0f) / px.length, px[ix], pNormal, pD));
        return pcloud;
    }

    private List<float[]> reconSliver(float ix, byte[] px, float[] pNormal, float pD) {
        final float[] O = camera.getOrigin();
        final List<float[]> pcloud = new ArrayList<>();
        for (int iy = 0; iy < px.length; iy++)
            if (px[iy] >= minLight) {
                // could be optimized
                final float[] ray = {0, 0, 0};
                camera.screen2space(ray, ix, 0.5f - (iy * 1.0f) / px.length);
                subVec3(ray, ray, O);
                normalizeVec3(ray);

                float t = -(pD + pNormal[0] * O[0] + pNormal[1] * O[1] + pNormal[2] * O[2]) / (pNormal[0] * ray[0] + pNormal[1] * ray[1] + pNormal[2] * ray[2]);

                ray[0] = O[0] + ray[0] * t;
                ray[1] = O[1] + ray[1] * t;
                ray[2] = O[2] + ray[2] * t;

                pcloud.add(ray);
            }
        return pcloud;
    }

    @Override
    public void draw(GL2 gl) {
        if (points == null)
            points = recon();
        gl.glBegin(GL_POINTS);
        points.forEach(p -> gl.glVertex3fv(p, 0));
        gl.glEnd();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == '0')
            points = null;
    }
}
