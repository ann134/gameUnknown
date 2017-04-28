package ru.spbu.arts.game.unknown;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class AnimatedGameObjectExample extends GameObject {

    private Animator animator;
    private final Animation b;

    public AnimatedGameObjectExample() throws IOException {
        animator = new Animator();

        Animation a = new Animation(Arrays.asList(
                ImageIO.read(new File("sdfsdf.png")),
                ImageIO.read(new File("sdfsdf.png"))
        ));

        b = new Animation(Arrays.asList(
                ImageIO.read(new File("sdfsdf.png")),
                ImageIO.read(new File("sdfsdf.png"))
        ));

        b.setNext(a); //установить следующую анимацию

        animator.start(a); //запустить анимацию a
    }

    @Override
    public void draw(Canvas canvas, int frame) {
        BufferedImage i = animator.getCurrentFrame();
        //draw i
    }

    public void event() {
        animator.start(b);
    }
}
