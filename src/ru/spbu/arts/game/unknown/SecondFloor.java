package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SecondFloor extends GameObject {

    private double w;
    private double h;

    public SecondFloor (double w, double h) {
        this.w = w;
        this.h = h;

        body = new Body();
        org.dyn4j.geometry.Rectangle r = new org.dyn4j.geometry.Rectangle(w, h);
//        body.addFixture(heroShape, BodyFixture.DEFAULT_DENSITY, 10, BodyFixture.DEFAULT_RESTITUTION);
        body.addFixture(r, 2);
        body.setMass(MassType.INFINITE);

    }

    public void draw(ru.spbu.arts.game.unknown.Canvas canvas, int frame) {
    }

    public void drawDebug(ru.spbu.arts.game.unknown.Canvas canvas){
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-w / 2, h / 2, w / 2, h / 2);
        canvas.drawLine(-w / 2, -h / 2, w / 2, -h / 2);

        canvas.drawLine(-w / 2, h / 2, -w / 2, -h / 2);
        canvas.drawLine(w / 2, h / 2, w / 2, -h / 2);
    }
}
