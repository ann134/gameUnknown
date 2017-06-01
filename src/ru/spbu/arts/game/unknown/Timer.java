package ru.spbu.arts.game.unknown;

public class Timer {
    public static final int FRAME_RATE = 7;
    public static final double FRAME_TIME = GamePanel.NANO_TO_BASE / FRAME_RATE;

    private static long start;

    public static int getFrameFrom(long startPoint){
        long now = System.nanoTime();
        return ((int) ((now - startPoint) / GamePanel.NANO_TO_BASE * FRAME_RATE));
    }

    public static void start() {
        start = System.nanoTime();
    }

    public static long getStart() {
        return start;
    }
}
