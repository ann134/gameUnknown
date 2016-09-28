package pac1;

import org.dyn4j.dynamics.Body;

import java.awt.*;

public class MyGameObject extends Body {

    protected Color color;

    public MyGameObject() {
        System.out.println("game obj");

        /*this.color = new Color(
                (float)Math.random() * 0.5f + 0.5f,
                (float)Math.random() * 0.5f + 0.5f,
                (float)Math.random() * 0.5f + 0.5f);*/

    }

    /*public void render(Graphics2D g) {
        AffineTransform ot = g.getTransform();
        System.out.println("game obj");

        AffineTransform lt = new AffineTransform();
        lt.translate(this.transform.getTranslationX() * TryDemo.SCALE, this.transform.getTranslationY() * TryDemo.SCALE);
        lt.rotate(this.transform.getRotation());

        g.transform(lt);

        for (BodyFixture fixture : this.fixtures) {
            Convex convex = fixture.getShape();
            Graphics2DRenderer.render(g, convex, TryDemo.SCALE, color);
        }
        g.setTransform(ot);
    }*/
}