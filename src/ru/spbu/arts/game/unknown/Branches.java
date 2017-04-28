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

public class Branches extends GameObject {

    public final static double W = 3;
    public final static double H = 3;

    /*private BufferedImage[] branchesArray = new BufferedImage[3];
    private BufferedImage[] branchesDeath = new BufferedImage[6];
    private BufferedImage afterDeath;*/

    private ArrayList<BufferedImage> branchesArray = new ArrayList<>();
    private ArrayList<BufferedImage> branchesDeath = new ArrayList<>();
    private ArrayList<BufferedImage> branchesAfterDeath = new ArrayList<>();

    private final Animation wind;
    private final Animation death;
    private final Animation afterDeath;
    private Animator animator = new Animator();



    private boolean dead = false;

    private long movementStart;

    public Branches () throws IOException {

        for (int i = 1; i < 3; i++) {
            String s = "images/branches/branches" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            branchesArray.add(b);
        }
        wind =  new Animation(branchesArray);

        for (int i = 1; i < 6; i++) {
            String s = "images/branches/branches1" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            branchesDeath.add(b);
        }
        death = new Animation(branchesDeath);

        branchesAfterDeath.add(ImageIO.read(new File("images/branches/branchesAftD.png")));
        afterDeath = new Animation(branchesAfterDeath);

        death.setNext(afterDeath);
        animator.start(wind);

        body = new Body();
        Rectangle branchesShape = new Rectangle(W, H);
        BodyFixture bf = new BodyFixture(branchesShape);
        bf.setSensor(true);
        body.addFixture(bf);

        body.setMass(MassType.INFINITE);
    }

    public void draw(ru.spbu.arts.game.unknown.Canvas canvas, int frameWhy) {

        BufferedImage branches = animator.getCurrentFrame();

        canvas.drawImage(branches, -W / 2, H / 2, W, H);
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
