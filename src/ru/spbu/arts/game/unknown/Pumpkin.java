package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pumpkin extends GameObject {

    private BufferedImage pumpkins;
    private double radius;


    public Pumpkin(double radius) throws IOException {
        this.radius = radius;
        takeable = true;

        pumpkins = ImageIO.read(new File("images/pumpkin.png"));

        Circle cirShape = new Circle(radius);
        body = new Body();
        body.addFixture(cirShape);
        body.setMass(MassType.NORMAL);
        body.setLinearDamping(0.05);
    }

    public void draw(Canvas canvas, int frame) {
        double r = radius;

        canvas.drawImage(pumpkins, -r, r, r * 2, r * 2);
    }



    public Vector2 getCarriedRightPoint() {
        return new Vector2(radius, 0);
    }

    public Vector2 getCarriedLeftPoint() {
        return new Vector2(-radius, 0);
    }
}
