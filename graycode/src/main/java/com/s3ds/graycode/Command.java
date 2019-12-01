package com.s3ds.graycode;

interface Command {
    String name();

    String description();

    String usage();

    void run(String... args);

    /**
     *
     * @return null if not gfx phase
     */
    Gfx getGfx();
}
