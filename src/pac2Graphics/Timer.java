package pac2Graphics;

public class Timer {
    public static long start;
    public static final int FRAME_RATE = 10;

    public static int getFrameFrom(long startPoint){
        long now = System.nanoTime();
        return ((int) ((now - startPoint) / GamePanel.NANO_TO_BASE * FRAME_RATE));
    }
}
