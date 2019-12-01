package com.s3ds.graycode;

import java.awt.*;

import static java.lang.Integer.parseInt;

class Box implements Command, Gfx {

    private String deviceId;
    private int screenWidth;
    private int screenHeight;
    private int lineWidth;

    @Override
    public String name() {
        return "box";
    }

    @Override
    public String description() {
        return "draw a full screen box";
    }

    @Override
    public String usage() {
        return "#device-id #screen-width #screen-height #line-width";
    }

    @Override
    public void run(String... args) {
        deviceId = args[1];
        screenWidth = parseInt(args[2]);
        screenHeight = parseInt(args[3]);
        lineWidth = parseInt(args[4]);
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
    public void run(Frame f) {
        Rectangle bounds = f.getBounds();
        Graphics g = f.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, bounds.width, bounds.height);
        g.setColor(Color.black);
        g.fillRect(lineWidth, lineWidth, bounds.width - 2 * lineWidth, bounds.height - 2 * lineWidth);
        try {
            Thread.sleep((int) 1000000);
        } catch (InterruptedException e) {
        }
    }
}
