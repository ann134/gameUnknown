package pac2Graphics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Light extends GameObject {

    //для физического тела
    public final static double W = 1.25 / 2;
    public final static double H = 1.25 / 2 + 0.25;

    //для рисовашек
    public final static double w = W * 2;
    public final static double h = H * 2 - 0.5;

    private BufferedImage[] lights = new BufferedImage[10];

    private long movementStart;

    public Light () throws IOException {
        takeable = true;

        for (int i = 1; i < lights.length + 1; i++) {
            String s = "images/light/light" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            lights[i - 1] = b;
        }

        body = new Body();
        Rectangle lightShape = new Rectangle(W, H);
        body.addFixture(lightShape);

        body.setMass(MassType.NORMAL);
    }


    public void draw(Canvas canvas, int frameWhy) {

        int frame = Timer.getFrameFrom(movementStart); // вычисляем какой кадр с начала движения (глобальный кадр?)

        //BufferedImage[] wirtsArray = getWirtsArray(); //вибираем нужный массив

        BufferedImage greg = lights[frame % lights.length]; // делим на длинну массива, т е на число отрисованных повторяющихся кадров (локальный кадр?)

        canvas.drawImage(greg, -w / 2, h / 2, w, h);
    }


    public void drawDebug(Canvas canvas) {
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-W / 2, H / 2, W / 2, H / 2);
        canvas.drawLine(-W / 2, -H / 2, W / 2, -H / 2);

        canvas.drawLine(-W / 2, H / 2, -W / 2, -H / 2);
        canvas.drawLine(W / 2, H / 2, W / 2, -H / 2);
    }

    public Vector2 getCarriedRightPoint() {
        return new Vector2(W/2, 0.5);
    }

    public Vector2 getCarriedLeftPoint() {
        return new Vector2(-W/2, 0.5);
    }

    public long getMovementStart() {
        return movementStart;
    }
    public void setMovementStart(long movementStart) {
        this.movementStart = movementStart;
    }
}
