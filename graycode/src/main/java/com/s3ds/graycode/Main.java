package com.s3ds.graycode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import static com.s3ds.graycode.Tuple.tuple;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;

public class Main {
    static String arg0 = "s3ds-graycode";
    static Map<String, Command> commands = of(
            new Help(),
            new Box(),
            new GrayCode(),
            new GrayCodeNumbers(),
            new ScanLine()
    ).collect(toMap(Command::name, x -> x));

    static void fatal(String format, Object... args) {
        System.err.printf(format, args);
        System.exit(1);
    }

    private static void gfx(Gfx gfx) {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // closest resolution
        final Tuple<GraphicsDevice, DisplayMode> dm = Arrays.stream(env.getScreenDevices())
                .filter(d -> d.getIDstring().equals(gfx.getDeviceID()))
                .flatMap(d -> Arrays.stream(d.getDisplayModes()).map(m -> tuple(d, m)))
                .min(Comparator.comparingDouble(a -> Math.sqrt(Math.pow(gfx.getScreenWidth() - a.snd.getWidth(), 2) + Math.pow(gfx.getScreenHeight() - a.snd.getHeight(), 2))))
                .orElseGet(() -> {
                    fatal("no display available!");
                    return tuple(null, null);
                });
        if (gfx.getScreenWidth() != dm.snd.getWidth() || gfx.getScreenHeight() != dm.snd.getHeight())
            fatal("screen resolution do not math, the closest one is %dx%d", dm.snd.getWidth(), dm.snd.getHeight());

        try {
            final Frame mainFrame = new Frame(dm.fst.getDefaultConfiguration());
            mainFrame.setUndecorated(true);
            mainFrame.setIgnoreRepaint(false);
            dm.fst.setFullScreenWindow(mainFrame);
            if (mainFrame.getBounds().getWidth() != gfx.getScreenWidth() || mainFrame.getBounds().getHeight() != gfx.getScreenHeight()) {
                if (!dm.fst.isDisplayChangeSupported())
                    fatal("current display do not support change diplay resolution");
                dm.fst.setDisplayMode(dm.snd);
            }
            gfx.run(mainFrame);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            dm.fst.setFullScreenWindow(null);
        }
        System.exit(0);
    }

    private static void runCommand(final Command c, final String... args) {
        c.run(args);
        ofNullable(c.getGfx()).ifPresent(Main::gfx);
    }

    public static void main(final String... args) {
        if (args.length < 1)
            runCommand(commands.get("help"), "help");
        else if (!commands.containsKey(args[0]))
            fatal("ERROR: unknown '%s' command, try 'help'.%n", args[0]);
        else
            runCommand(commands.get(args[0]), args);
    }

}
