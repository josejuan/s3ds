package com.s3ds.graycode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

class ScanLine implements Command, Gfx {

    private String deviceId;
    private int screenWidth;
    private int screenHeight;
    private int fromX;
    private int toX;
    private int delay;

    @Override
    public String name() {
        return "scanline";
    }

    @Override
    public String description() {
        return "Do a horizontal scan line";
    }

    @Override
    public String usage() {
        return "#device-id #screen-width #screen-height #from-x #to-x #delay-milliseconds";
    }

    @Override
    public void run(String... args) {
        deviceId = args[1];
        screenWidth = parseInt(args[2]);
        screenHeight = parseInt(args[3]);
        fromX = parseInt(args[4]);
        toX = parseInt(args[5]);
        delay = parseInt(args[6]);
    }

    @Override
    public Gfx getGfx() {
        return this;
    }

    @Override
    public String getDeviceID() {
        return deviceId;
    }

    @Override
    public int getScreenWidth() {
        return screenWidth;
    }

    @Override
    public int getScreenHeight() {
        return screenHeight;
    }

    @Override
    public void run(final Frame f) {
        final Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new IllegalStateException(e);
        }
        ActionListener taskPerformer = evt -> robot.keyPress(62);
        new Timer(delay / 2, taskPerformer).start();

        Rectangle bounds = f.getBounds();
        Graphics g = f.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, bounds.width, bounds.height);
        int x = fromX;
        while (true) {
            g.setColor(Color.black);
            g.fillRect(x, 0, 1, bounds.height);
            if (++x > toX)
                x = fromX;
            g.setColor(Color.white);
            g.fillRect(x, 0, 1, bounds.height);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }
}
