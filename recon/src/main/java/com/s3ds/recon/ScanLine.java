package com.s3ds.recon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static com.s3ds.recon.Util.safe;

public class ScanLine {
    private final byte[/* frame */][/* width */][/* height */] pxs;

    public ScanLine(final String imageFolder) throws IOException {
        pxs = Files
                .walk(Paths.get(imageFolder))
                .filter(Files::isRegularFile)
                .sorted(Comparator.comparing(f -> f.getFileName().toString()))
                .map(Path::toFile)
                .map(safe(ImageIO::read))
                .map(ScanLine::convertToByte)
                .toArray(byte[][][]::new);
    }

    private static byte[][] convertToByte(BufferedImage image) {
        final byte[] db = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int w = image.getWidth();
        final int h = image.getHeight();
        byte[][] rs = new byte[w][h];
        final int pixelLength = image.getAlphaRaster() != null ? 4 : 3;
        for (int pixel = 0, row = 0, col = 0; pixel + pixelLength - 1 < db.length; pixel += pixelLength) {
            rs[col][row] = (byte) (db[pixel + 1] & 0xff);
            col++;
            if (col == w) {
                col = 0;
                row++;
            }
        }
        return rs;
    }

    public byte[][][] getPixelsBuffer() {
        return pxs;
    }
}
