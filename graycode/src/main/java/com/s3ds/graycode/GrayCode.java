package com.s3ds.graycode;

import java.awt.*;

class GrayCode extends GrayCodeBase {

    @Override
    public String name() {
        return "graycode";
    }

    @Override
    public String description() {
        return "draw the gray code sequence";
    }

    @Override
    protected void render(Graphics g, int bit, Rectangle bounds, int virtualWidth) {
        g.setColor(Color.black);
        g.fillRect(0, 0, bounds.width, bounds.height);
        g.setColor(Color.white);
        final int mask = 1 << (bit - 1);
        for (int x = 0; x < virtualWidth; x += 1) {
            if ((x & mask) != 0)
                g.fillRect(x, 0, 1, bounds.height);
        }
    }
}
