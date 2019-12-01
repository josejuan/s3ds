package com.s3ds.graycode;

import java.awt.*;

class GrayCodeNumbers extends GrayCodeBase {

    @Override
    public String name() {
        return "graycodenumbers";
    }

    @Override
    public String description() {
        return "draw the gray code sequence using numbers";
    }

    @Override
    protected void render(Graphics g, int bit, Rectangle bounds, int virtualWidth) {
        g.setColor(Color.black);
        g.fillRect(0, 0, bounds.width, bounds.height);
        g.setColor(Color.white);
        g.drawString("Frame: " + bit, 200, 200);
    }
}
