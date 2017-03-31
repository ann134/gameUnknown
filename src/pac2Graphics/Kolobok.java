package pac2Graphics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Kolobok extends GameObject {

    private BufferedImage smile;
    private double radius;


    public Kolobok(double radius) throws IOException {
        this.radius = radius;
        takeable = true;

        smile = ImageIO.read(new File("smile.png"));

        Circle cirShape = new Circle(radius);
        body = new Body();
        body.addFixture(cirShape);
        body.setMass(MassType.NORMAL);
        body.setLinearDamping(0.05);
    }

    public void draw(Canvas canvas, int frame) {
        double r = radius;

        canvas.drawImage(smile, -r, r, r * 2, r * 2);
    }


    public Vector2 getCarriedPoint(){
        return new Vector2(-radius, 0);
    }
}
