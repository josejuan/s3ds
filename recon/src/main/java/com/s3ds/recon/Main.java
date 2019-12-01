package com.s3ds.recon;

import java.io.IOException;

import static com.s3ds.recon.Decorator.*;
import static java.util.Arrays.asList;

public class Main {
    public static void main(final String... args) throws IOException {

        final Frustum camera = new Frustum('1', new float[]{0.8f, 0.0f, -2.0f}, new float[]{0, 0, 0.5f}, new float[]{0, 1, 0}, 3.57f, 1.97f);
        final Frustum projector = new Frustum('2', new float[]{-0.2f, 0, -1.0f}, new float[]{0, 0, 0.5f}, new float[]{0, 1, 0}, 1.6f, 0.9f);

        final ScanLine sequence = new ScanLine(args[0]);
        final ScanLineRecon recon = new ScanLineRecon(sequence, projector, camera);

        final Viewer viewer = new Viewer(asList(
                decorate(projector, COLOR_RED),
                decorate(camera, COLOR_GREEN),
                decorate(recon, COLOR_WHITE)
        ));

        viewer.run();
    }
}
