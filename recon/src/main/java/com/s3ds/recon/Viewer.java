package com.s3ds.recon;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Viewer extends JPanel implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener {

    private GLJPanel display;
    private float rotateX, rotateY;
    private GLUT glut = new GLUT();

    private boolean dragging;
    private int startX, startY;
    private int prevX, prevY;

    private List<GLObject> objects = new ArrayList<>();

    public Viewer(final Collection<GLObject> objects) {
        this.objects.addAll(objects);

        GLCapabilities caps = new GLCapabilities(null);
        display = new GLJPanel(caps);
        display.setPreferredSize(new Dimension(1500, 1000));
        display.addGLEventListener(this);
        setLayout(new BorderLayout());
        add(display, BorderLayout.CENTER);

        rotateX = 15;
        rotateY = 15;

        display.addKeyListener(this);
        display.addMouseListener(this);
        display.addMouseMotionListener(this);
    }


    public void run() {
        JFrame window = new JFrame("S3DS Viewer");
        window.setContentPane(this);
        window.pack();
        window.setLocation(50, 50);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1.5, 1.5, -1, 1, -2, 2);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        gl.glLoadIdentity();
        gl.glRotatef(rotateY, 0, 1, 0);
        gl.glRotatef(rotateX, 1, 0, 0);

        objects.forEach(o -> o.draw(gl));
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0.8F, 0.8F, 0.8F, 1.0F);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    public void keyPressed(KeyEvent e) {
        objects.forEach(o -> o.keyPressed(e));
        repaint();
    }

    /**
     * Called when the user types a character.
     */
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
    }

    public void keyReleased(KeyEvent e) {
    }


    public void actionPerformed(ActionEvent evt) {
        display.repaint();
    }

    public void mousePressed(MouseEvent evt) {
        if (dragging)
            return;
        int x = evt.getX();
        int y = evt.getY();
        // TODO: respond to mouse click at (x,y)
        dragging = true;  // might not always be correct!
        prevX = startX = x;
        prevY = startY = y;
        display.repaint();
    }

    public void mouseReleased(MouseEvent evt) {
        if (!dragging)
            return;
        dragging = false;
    }

    public void mouseDragged(MouseEvent evt) {
        if (!dragging)
            return;

        int x = evt.getX();
        int y = evt.getY();

        rotateY += x - prevX;
        rotateX += y - prevY;

        prevX = x;
        prevY = y;
        display.repaint();
    }

    public void mouseMoved(MouseEvent evt) {
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }


}