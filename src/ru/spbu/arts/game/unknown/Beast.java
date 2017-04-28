package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Beast extends GameObject {

    public final static double W = 8;
    public final static double H = 10;

    /*private BufferedImage[] beasts = new BufferedImage[10];
    private BufferedImage[] beastsDeath = new BufferedImage[18];
    private BufferedImage beastAfterDeath;*/

    ArrayList<BufferedImage> beasts = new ArrayList<>();
    ArrayList<BufferedImage> beastsDeath = new ArrayList<>();
    ArrayList<BufferedImage> beastAfterDeath = new ArrayList<>();

    private final Animation wind;
    private final Animation death;
    private final Animation afterDeath;
    private Animator animator = new Animator();


    private boolean dead = false;
    //private long movementStart = 0;

    public Beast () throws IOException {
        /*for (int i = 1; i < beasts.length + 1; i++) {
            String s = "images/beast/beast" + i +".png";
            BufferedImage b = ImageIO.read(new File(s));
            beasts[i - 1] = b;
            beastsL.add(b);
        }*/

        for (int i = 1; i < 10; i++){
            String s = "images/beast/beast" + i +".png";
            BufferedImage b = ImageIO.read(new File(s));
            beasts.add(b);
        }
        wind =  new Animation(beasts);

        for (int i = 1; i < 18; i++) {
            String s = "images/beast/beastDead" + i +".png";
            BufferedImage b = ImageIO.read(new File(s));
            beastsDeath.add(b);
        }
        death = new Animation(beastsDeath);

        beastAfterDeath.add(ImageIO.read(new File("images/beast/beastAftD.png")));
        afterDeath = new Animation(beastAfterDeath);

        death.setNext(afterDeath);
        animator.start(wind);


        body = new Body();
        Rectangle beastShape = new Rectangle(W, H);
        BodyFixture bf = new BodyFixture(beastShape);
        bf.setSensor(true);
        body.addFixture(bf);

        body.setMass(MassType.INFINITE);
    }

    public void draw(ru.spbu.arts.game.unknown.Canvas canvas, int frameWhy) {

        //int frame;

        /*if (movementStart == 0){
            if (dead){
                beast = beastAfterDeath;
            } else {
                frame = Timer.getFrameFrom(frameWhy); // вычисляем какой кадр с начала движения (глобальный кадр?)
                beast = beasts[frame % beasts.length]; // делим на длинну массива, т е на число отрисованных повторяющихся кадров (локальный кадр?)
            }
        } else {
            frame = Timer.getFrameFrom(movementStart);
            beast = beastsDeath[frame % beastsDeath.length];
            if(frame % beastsDeath.length == beastsDeath.length - 1){
                movementStart = 0;
                dead = true;
            }

        }*/

        BufferedImage beast = animator.getCurrentFrame();
        canvas.drawImage(beast, -W / 2, H / 2, W, H);
    }



    public void drawDebug(ru.spbu.arts.game.unknown.Canvas canvas) {
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-W / 2, H / 2, W / 2, H / 2);
        canvas.drawLine(-W / 2, -H / 2, W / 2, -H / 2);

        canvas.drawLine(-W / 2, H / 2, -W / 2, -H / 2);
        canvas.drawLine(W / 2, H / 2, W / 2, -H / 2);
    }

    /*public long getMovementStart() {
        return movementStart;
    }
    public void setMovementStart(long movementStart) {
        this.movementStart = movementStart;
    }*/

    public void startDeath(){
        animator.start(death);
    }


}
