package pac2Graphics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Branches extends GameObject {

    public final static double W = 3;
    public final static double H = 3;

    private BufferedImage[] branchesArray = new BufferedImage[1];

    private long movementStart;

    public Branches () throws IOException {

        for (int i = 1; i < branchesArray.length + 1; i++) {
            String s = "branches/branches" + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            branchesArray[i - 1] = b;
        }

        body = new Body();
        Rectangle branchesShape = new Rectangle(W, H);
        BodyFixture bf = new BodyFixture(branchesShape);
        bf.setSensor(true);
        body.addFixture(bf);

        body.setMass(MassType.INFINITE);
    }

    public void draw(Canvas canvas, int frameWhy) {

        int frame = Timer.getFrameFrom(movementStart); // вычисляем какой кадр с начала движения (глобальный кадр?)

        //BufferedImage[] wirtsArray = getWirtsArray(); //вибираем нужный массив

        BufferedImage branches = branchesArray[frame % branchesArray.length]; // делим на длинну массива, т е на число отрисованных повторяющихся кадров (локальный кадр?)

        canvas.drawImage(branches, -W / 2, H / 2, W, H);
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
