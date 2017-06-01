package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import ru.spbu.arts.game.unknown.Canvas;
import ru.spbu.arts.game.unknown.GameObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CartWheel extends GameObject {


    private BufferedImage wheel;
    private double radius;


    public CartWheel(double radius) throws IOException {
        this.radius = radius;
        takeable = true;

        wheel = ImageIO.read(new File("images/cart/cartwheel.png"));

        Circle cirShape = new Circle(radius);
        body = new Body();
        body.addFixture(cirShape);
        body.setMass(MassType.NORMAL);
        body.setLinearDamping(0.05);
    }

    public void draw(Canvas canvas, int frame) {
        double r = radius;

        canvas.drawImage(wheel, -r, r, r * 2, r * 2);
    }

}
