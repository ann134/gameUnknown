package pac2Graphics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Beast extends GameObject {

    public final static double W = 8;
    public final static double H = 10;

    private BufferedImage[] beasts = new BufferedImage[10];

    private long movementStart;

    public Beast () throws IOException {

        for (int i = 1; i < beasts.length + 1; i++) {
            String s = "images/beast/beast" + i +".png";
            BufferedImage b = ImageIO.read(new File(s));
            beasts[i - 1] = b;
        }

        body = new Body();
        Rectangle beastShape = new Rectangle(W, H);
        BodyFixture bf = new BodyFixture(beastShape);
        bf.setSensor(true);
        body.addFixture(bf);

        body.setMass(MassType.INFINITE);
    }

    public void draw(Canvas canvas, int frameWhy) {

        int frame = Timer.getFrameFrom(movementStart); // вычисляем какой кадр с начала движения (глобальный кадр?)

        //BufferedImage[] wirtsArray = getWirtsArray(); //вибираем нужный массив

        BufferedImage beast = beasts[frame % beasts.length]; // делим на длинну массива, т е на число отрисованных повторяющихся кадров (локальный кадр?)

        canvas.drawImage(beast, -W / 2, H / 2, W, H);
    }



    public void drawDebug(Canvas canvas) {
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-W / 2, H / 2, W / 2, H / 2);
        canvas.drawLine(-W / 2, -H / 2, W / 2, -H / 2);

        canvas.drawLine(-W / 2, H / 2, -W / 2, -H / 2);
        canvas.drawLine(W / 2, H / 2, W / 2, -H / 2);
    }

    public long getMovementStart() {
        return movementStart;
    }
    public void setMovementStart(long movementStart) {
        this.movementStart = movementStart;
    }
}
