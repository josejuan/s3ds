package com.s3ds.graycode;

class Help implements Command {

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "show the help";
    }

    @Override
    public String usage() {
        return "";
    }

    @Override
    public void run(String... args) {
        if (args.length != 1)
            Main.fatal("help command has no arguments");
        Main.commands.forEach((i, c) -> {
            System.out.printf("- %s, %s%n", c.name(), c.description());
            System.out.printf("    USAGE: %s %s %s%n", Main.arg0, c.name(), c.usage());
        });
    }

    @Override
    public Gfx getGfx() {
        return null;
    }
}
