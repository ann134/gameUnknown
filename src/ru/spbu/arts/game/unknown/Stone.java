package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Stone extends GameObject{

    private BufferedImage stone;
    private double radius;

    public Stone(double radius) throws IOException {
        this.radius = radius;

        stone = ImageIO.read(new File("images/stone.png"));

        Circle cirShape = new Circle(radius);
        body = new Body();
        body.addFixture(cirShape);
        body.setMass(MassType.INFINITE);
        body.setLinearDamping(0.05);
    }

    public void draw(Canvas canvas, int frame) {
        double r = radius;

        canvas.drawImage(stone, -r, r, r * 2, r * 2);
    }
}
