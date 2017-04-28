package ru.spbu.arts.game.unknown;

import java.awt.image.BufferedImage;

public class Animator {

    private Animation currentAnimation;
    private long animationStart;

    public void start(Animation a) {
        currentAnimation = a;
        animationStart = System.nanoTime();
    }

    public BufferedImage getCurrentFrame() {

        int frame = Timer.getFrameFrom(animationStart);

        if (frame >= currentAnimation.getImages().size()){
            currentAnimation = currentAnimation.getNext();
            frame = frame % currentAnimation.getImages().size();
        }

        return currentAnimation.getImages().get(frame);
    }
}
