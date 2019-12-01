package com.s3ds.graycode;

import java.awt.*;

interface Gfx {
    String getDeviceID();

    int getScreenWidth();

    int getScreenHeight();

    void run(Frame mainFrame);
}
