package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Text extends GameObject {

    public final static double W = 15;
    public final static double H = 8;

    private BufferedImage textEnd;


    public Text () throws IOException {

        textEnd  = ImageIO.read(new File("images/end.png"));

        body = new Body();
        Rectangle textShape = new Rectangle(W, H);
        BodyFixture bf = new BodyFixture(textShape);
        bf.setSensor(true);
        body.addFixture(bf);

        body.setMass(MassType.INFINITE);
    }

    public void draw(ru.spbu.arts.game.unknown.Canvas canvas, int frameWhy) {

        if (GamePanel.gameEND)
            canvas.drawImage(textEnd, -W / 2, H / 2, W, H);

    }

    public void drawDebug(ru.spbu.arts.game.unknown.Canvas canvas) {
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-W / 2, H / 2, W / 2, H / 2);
        canvas.drawLine(-W / 2, -H / 2, W / 2, -H / 2);

        canvas.drawLine(-W / 2, H / 2, -W / 2, -H / 2);
        canvas.drawLine(W / 2, H / 2, W / 2, -H / 2);
    }
}
