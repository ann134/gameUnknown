package pac2Graphics;


import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
        resetTransform();

        g.translate(body.getTransform().getTranslationX() * SCALE, body.getTransform().getTranslationY() * -SCALE);
        g.rotate(-body.getTransform().getRotation());
    }

    public void resetTransform() {
        g.setTransform(saveTransform);

        Vector2 cameraPos = camera.getPosition();
        g.translate((-cameraPos.x + Camera.SCREEN_W / 2) * SCALE, (cameraPos.y + Camera.SCREEN_H - Camera.HERO_POSITION_H) * SCALE);
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
