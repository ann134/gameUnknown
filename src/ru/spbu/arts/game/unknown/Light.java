package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Light extends GameObject {

    //для физического тела
    public final static double W = 1.25 / 2;
    public final static double H = 1.25 / 2 + 0.25;

    //для рисовашек
    public final static double w = W * 2;
    public final static double h = H * 2 - 0.5;


    /*private BufferedImage[] lights = new BufferedImage[10];
    private BufferedImage[] lightsDeath = new BufferedImage[10];
    private BufferedImage lightsAfterDeath;*/

    private ArrayList<BufferedImage> lights = new ArrayList<>();
    private ArrayList<BufferedImage> lightsDeath = new ArrayList<>();
    private ArrayList<BufferedImage> lightsAfterDeath = new ArrayList<>();

    private final Animation wind;
    private final Animation death;
    private final Animation afterDeath;
    private Animator animator = new Animator();

    private boolean dead = false;

    private long movementStart;

    public Light() throws IOException {
        takeable = true;

        for (int i = 1; i < 10; i++) {
            String s = "images/light/light" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            lights.add(b);
        }
        wind =  new Animation(lights);

        for (int i = 1; i < 10; i++) {
            String s = "images/light/light" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            lightsDeath.add(b);
        }
        death = new Animation(lightsDeath);

        lightsAfterDeath.add(ImageIO.read(new File("images/light/lightAftD.png")));
        afterDeath = new Animation(lightsAfterDeath);

        death.setNext(afterDeath);
        death.setNextAction(() -> {
            kill();
            ((Beast)allWorldGameObjects.getByName("beast")).startDeath();
        });
        animator.start(wind);

        body = new Body();
        Rectangle lightShape = new Rectangle(W, H);
        body.addFixture(lightShape);

        body.setMass(MassType.NORMAL);
    }


    public void draw(ru.spbu.arts.game.unknown.Canvas canvas, int frameWhy) {
        BufferedImage light = animator.getCurrentFrame();
        canvas.drawImage(light, -w / 2, h / 2, w, h);
    }


    public void drawDebug(ru.spbu.arts.game.unknown.Canvas canvas) {
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-W / 2, H / 2, W / 2, H / 2);
        canvas.drawLine(-W / 2, -H / 2, W / 2, -H / 2);

        canvas.drawLine(-W / 2, H / 2, -W / 2, -H / 2);
        canvas.drawLine(W / 2, H / 2, W / 2, -H / 2);
    }

    public Vector2 getCarriedRightPoint() {
        return new Vector2(W/2, 0.5);
    }

    public Vector2 getCarriedLeftPoint() {
        return new Vector2(-W/2, 0.5);
    }

    public boolean alive() {
        return !dead;
    }

    public void kill(){
        dead = true;
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
