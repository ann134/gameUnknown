package pac2Graphics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import java.awt.*;

public class InvisibleObject extends GameObject {

    private double w;
    private double h;

    public InvisibleObject(double w, double h){
        this.w = w;
        this.h = h;

        body = new Body();
        Rectangle r = new Rectangle(w, h);
        BodyFixture bf = new BodyFixture(r);
        bf.setSensor(true);
        body.addFixture(bf);

        body.setMass(MassType.INFINITE);
    }

    public void draw(Canvas canvas, int frame) {

    }

    public void drawDebug(Canvas canvas){
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-w / 2, h / 2, w / 2, h / 2);
        canvas.drawLine(-w / 2, -h / 2, w / 2, -h / 2);

        canvas.drawLine(-w / 2, h / 2, -w / 2, -h / 2);
        canvas.drawLine(w / 2, h / 2, w / 2, -h / 2);
    }
}
