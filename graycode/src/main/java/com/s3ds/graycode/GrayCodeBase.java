package com.s3ds.graycode;

import sun.misc.Signal;

import java.awt.*;

import static java.lang.Integer.parseInt;

abstract class GrayCodeBase implements Command, Gfx {

    private String deviceId;
    private int screenWidth;
    private int screenHeight;
    private int delay;

    private static int log2(int x) {
        int r = 0;
        for (int n = x; (n >>>= 1) != 0; ++r) ;
        return ((1 << r) == x) ? r : r + 1;
    }

    @Override
    public String usage() {
        return "#device-id #screen-width #screen-height #delay-milliseconds";
    }

    @Override
    public void run(String... args) {
        deviceId = args[1];
        screenWidth = parseInt(args[2]);
        screenHeight = parseInt(args[3]);
        delay = parseInt(args[4]);
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

        final boolean[] wait = new boolean[]{false};
        Signal.handle(new Signal("USR2"), sig -> wait[0] = true);

        while (!wait[0])
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }

        Rectangle bounds = f.getBounds();
        Graphics g = f.getGraphics();
        int bits = log2(bounds.width);
        int virtualWidth = 1 << bits;
        for (int bit = bits; bit > 0; bit -= 1) {
            render(g, bit, bounds, virtualWidth);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }

    protected abstract void render(Graphics g, int bit, Rectangle bounds, int virtualWidth);
}
