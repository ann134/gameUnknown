package org.dyn4j.examples;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.examples.Graphics2DRenderer;
import org.dyn4j.geometry.Convex;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameObject2 extends Body {

    protected Color color;

    public GameObject2() {
        this.color = new Color(
                (float)Math.random() * 0.5f + 0.5f,
                (float)Math.random() * 0.5f + 0.5f,
                (float)Math.random() * 0.5f + 0.5f);
    }

    public void render(Graphics2D g) {
        AffineTransform ot = g.getTransform();

        AffineTransform lt = new AffineTransform();
        lt.translate(this.transform.getTranslationX() * TryDemo.SCALE, this.transform.getTranslationY() * TryDemo.SCALE);
        lt.rotate(this.transform.getRotation());

        g.transform(lt);

        for (BodyFixture fixture : this.fixtures) {
            Convex convex = fixture.getShape();
            Graphics2DRenderer.render(g, convex, TryDemo.SCALE, color);
        }
        g.setTransform(ot);
    }
}