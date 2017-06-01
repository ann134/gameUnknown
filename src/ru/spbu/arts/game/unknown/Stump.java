package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Stump extends GameObject{

    private BufferedImage stump;
    private double w;
    private double h;

    public Stump(double w, double h) throws IOException {

        this.w = w;
        this.h = h;

        stump = ImageIO.read(new File("images/stump.png"));

        body = new Body();
        org.dyn4j.geometry.Rectangle r = new org.dyn4j.geometry.Rectangle(w, h);

//        body.addFixture(heroShape, BodyFixture.DEFAULT_DENSITY, 10, BodyFixture.DEFAULT_RESTITUTION);
        body.addFixture(r, 2);


        body.setMass(MassType.INFINITE);

    }

    public void draw(ru.spbu.arts.game.unknown.Canvas canvas, int frame) {
        canvas.drawImage(stump, -w / 2.0, h / 2.0, w, h);
    }

    public void drawDebug(ru.spbu.arts.game.unknown.Canvas canvas){
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-w / 2, h / 2, w / 2, h / 2);
        canvas.drawLine(-w / 2, -h / 2, w / 2, -h / 2);

        canvas.drawLine(-w / 2, h / 2, -w / 2, -h / 2);
        canvas.drawLine(w / 2, h / 2, w / 2, -h / 2);
    }
}
