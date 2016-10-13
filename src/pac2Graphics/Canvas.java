package pac2Graphics;


import org.dyn4j.dynamics.Body;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Canvas {
    public final static double SCALE = 45.0;

    private Graphics2D g;
    private Camera camera;
    private AffineTransform saveTransform;

    public Canvas(Graphics2D g, Camera camera) {
        this.g = (Graphics2D) g.create();
        this.camera = camera;

        saveTransform = g.getTransform();
    }

    public void transformBody(Body body) {
        g.setTransform(saveTransform);

        g.translate(body.getTransform().getTranslationX() * SCALE, body.getTransform().getTranslationY() * -SCALE);
        g.rotate(-body.getTransform().getRotation());
    }

    public void kill() {
        g.dispose();
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        g.drawLine((int) (x1 * SCALE), (int) (y1 * -SCALE), (int) (x2 * SCALE), (int) (y2 * -SCALE));
    }

    public void drawImage(BufferedImage image, double x, double y, double w, double h) {
        g.drawImage(
                image,
                (int) (x * SCALE),
                (int) (y * -SCALE),
                (int) (w * SCALE),
                (int) (h * SCALE),
                null
        );
    }

    public void setColor(Color color) {
        g.setColor(color);
    }
}
