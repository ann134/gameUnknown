package ru.spbu.arts.game.unknown;

import com.sun.org.apache.xerces.internal.impl.dv.xs.AnyURIDV;

import java.awt.image.BufferedImage;
import java.util.List;

public class Animator {

    private Animation currentAnimation;
    private long animationStart;

    public void start(Animation a) {
        currentAnimation = a;
        animationStart = System.nanoTime();
    }

    public BufferedImage getCurrentFrame() {
        int frame = Timer.getFrameFrom(animationStart);
        List<BufferedImage> images = currentAnimation.getImages();

        while (frame >= images.size()) {
            frame = frame - images.size();
            animationStart = (long) (animationStart + images.size() * Timer.FRAME_TIME);

            Runnable nextAction = currentAnimation.getNextAction();
            if (nextAction != null)
                nextAction.run();

            currentAnimation = currentAnimation.getNext();
            images = currentAnimation.getImages();
        }

        return images.get(frame);
    }
}
