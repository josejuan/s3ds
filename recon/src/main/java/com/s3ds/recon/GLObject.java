package com.s3ds.recon;

import com.jogamp.opengl.GL2;

import java.awt.event.KeyEvent;

public interface GLObject {
    void draw(GL2 gl);
    void keyPressed(KeyEvent e);
}
