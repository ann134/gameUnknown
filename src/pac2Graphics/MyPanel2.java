package pac2Graphics;


import com.sun.org.apache.xpath.internal.SourceTree;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.examples.GameObject;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MyPanel2 extends JPanel {
    private BufferedImage smile;
    private BufferedImage wirt;

    public static final double SCALE = 45.0;
    public static final double NANO_TO_BASE = 1.0e9;

    private World world;
    private long last;

    private Body newFloor;
    private Body floor;
    private Body circle;
    private Body hero;

    private String debug = "hello";

    public MyPanel2() throws IOException {
        smile = ImageIO.read(new File("smile.png"));
        wirt = ImageIO.read(new File("wirt.png"));

        this.initializeWorld();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        render(g2d);
    }

    protected void initializeWorld() {
        this.world = new World();

        List<Link> links = Geometry.createLinks(new Vector2[] {
                new Vector2(-6.0,  0.5),
                new Vector2( 0.0,  0.0),
                new Vector2( 2.0,  0.0),
                new Vector2( 4.0,  0.2),
                new Vector2( 4.5,  0.3),
                new Vector2( 6.0, -0.5)
        }, false);
        newFloor = new Body();
        for (Link link : links) {
            newFloor.addFixture(link);
        }
        newFloor.translate(-15, -15);
        newFloor.setMass(MassType.INFINITE);
        this.world.addBody(newFloor);

        Rectangle foorShape = new Rectangle(40, 3);
        floor = new Body();
        floor.addFixture(foorShape);
        floor.setMass(MassType.INFINITE);
        floor.translate(-15, -22);
        this.world.addBody(floor);

        Circle cirShape = new Circle(0.5);
        circle = new Body();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.translate(-10, -1);
        circle.applyForce(new Vector2(-100.0, 0.0));
        circle.setLinearDamping(0.05);
        this.world.addBody(circle);

        Rectangle heroShape = new Rectangle(1, 2);
        hero = new Body();
        //hero.addFixture(heroShape, плотность, трение, ??прыгучесть);
        hero.addFixture(heroShape, BodyFixture.DEFAULT_DENSITY, BodyFixture.DEFAULT_FRICTION, BodyFixture.DEFAULT_RESTITUTION);
        //hero.applyImpulse(new Vector2(100, 100));
        hero.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        hero.translate(-2, -10);
        world.addBody(hero);

    }

    public void start() {
        this.last = System.nanoTime();

        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    gameLoop();
                }
            }
        };
        thread.start();
    }

    private void gameLoop() {

        Vector2 linearVelocity = hero.getLinearVelocity();
        HeroKeyListener heroKeyListener = MyFrame2.getHeroKeyListener();

        //идем вперед
        if (heroKeyListener.getGo()) {
            if (-3 < linearVelocity.x && linearVelocity.x < 3){
                if (linearVelocity.x == 0)
                    hero.applyImpulse(new Vector2(-3, 0));
                hero.applyImpulse(new Vector2(-2, 0));
            }

            heroKeyListener.setGo(false);
        }

        //идем назад
        if (heroKeyListener.getGoBack()) {
            if (-3 < linearVelocity.x && linearVelocity.x < 3){
                if (linearVelocity.x == 0)
                    hero.applyImpulse(new Vector2(3, 0));
                hero.applyImpulse(new Vector2(2, 0));
            }

            heroKeyListener.setGoBack(false);
        }

        //прыжок
        if (heroKeyListener.getJump()) {
            if (hero.isInContact(floor)){
                hero.applyImpulse(new Vector2(0, 15));
            }

            heroKeyListener.setJump(false);
        }

        //тормозим когда идем вперед
        if (heroKeyListener.getStopGo()) {
            hero.setLinearVelocity(0, linearVelocity.y);

            heroKeyListener.setStopGo(false);
        }

        //тормозим когда идем назад
        if (heroKeyListener.getStopGoBack()) {
            hero.setLinearVelocity(0, linearVelocity.y);

            heroKeyListener.setStopGoBack(false);
        }

        repaint();

        long time = System.nanoTime();
        long diff = time - this.last;

        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE;

        this.world.update(elapsedTime);
    }




    public void render(Graphics2D g) {

        //рисуем круг
        AffineTransform ot = g.getTransform();

        g.translate(circle.getTransform().getTranslationX() * -SCALE, circle.getTransform().getTranslationY() * -SCALE);
        g.rotate(circle.getTransform().getRotation());

        double r = circle.getFixture(0).getShape().getRadius();
        g.setColor(new Color(0, 0, 0));
        g.fillOval(
                (int) (-r * SCALE),
                (int) (-r * SCALE),
                (int) (r * 2 * SCALE),
                (int) (r * 2 * SCALE)
        );
        g.drawImage(
                smile,
                (int) (-r * SCALE),
                (int) (-r * SCALE),
                (int) (r * 2 * SCALE),
                (int) (r * 2 * SCALE),
                null
        );

        g.setTransform(ot);



        //рисуем пол
        ot = g.getTransform();

        g.translate(floor.getTransform().getTranslationX() * -SCALE, floor.getTransform().getTranslationY() * -SCALE);
        g.rotate(floor.getTransform().getRotation());

        double h = ((Rectangle) floor.getFixture(0).getShape()).getHeight();
        double w = ((Rectangle) floor.getFixture(0).getShape()).getWidth();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(
                (int) (-w/2 * SCALE),
                (int) (-h/2 * SCALE),
                (int) (w * SCALE),
                (int) (h * SCALE)
        );
        g.setTransform(ot);



        //рисуем героя
        ot = g.getTransform();

        g.translate(hero.getTransform().getTranslationX() * -SCALE, hero.getTransform().getTranslationY() * -SCALE);
        g.rotate(hero.getTransform().getRotation());

        double hh = ((Rectangle) hero.getFixture(0).getShape()).getHeight();
        double wh = ((Rectangle) hero.getFixture(0).getShape()).getWidth();
        g.setColor(new Color(0, 255, 0));
        /*g.fillRect(
                (int) (-wh/2 * SCALE),
                (int) (-hh/2 * SCALE),
                (int) (wh * SCALE),
                (int) (hh * SCALE));*/
        g.drawImage(
                wirt,
                (int) (-wh/2 * SCALE),
                (int) (-hh/2 * SCALE),
                (int) (wh * SCALE),
                (int) (hh * SCALE),
                null
        );
        g.setTransform(ot);


        //рисуем новый пол
        ot = g.getTransform();

        g.translate(newFloor.getTransform().getTranslationX() * -SCALE, newFloor.getTransform().getTranslationY() * -SCALE);
        g.rotate(newFloor.getTransform().getRotation());


        List<BodyFixture> links = newFloor.getFixtures();
        for (BodyFixture b : links) {
            Link l =  (Link) b.getShape();
            Vector2[] vertices = l.getVertices();

            for (int i = 0; i < vertices.length - 1; i++) {
                g.setColor(new Color(0, 0, 0));
                g.drawLine(
                        (int) (vertices[i].x * -SCALE),
                        (int) (vertices[i].y * -SCALE),
                        (int) (vertices[i+1].x * -SCALE),
                        (int) (vertices[i+1].y * -SCALE)
                );
            }
        }

        //g.drawLine(x1, y1, x2, y2);

        /*g.translate(floor.getTransform().getTranslationX() * -SCALE, floor.getTransform().getTranslationY() * -SCALE);
        g.rotate(floor.getTransform().getRotation());

        double h = ((Rectangle) floor.getFixture(0).getShape()).getHeight();
        double w = ((Rectangle) floor.getFixture(0).getShape()).getWidth();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(
                (int) (-w/2 * SCALE),
                (int) (-h/2 * SCALE),
                (int) (w * SCALE),
                (int) (h * SCALE)
        );*/

        g.setTransform(ot);


        //show debug
        g.setFont(new Font("Arial", 0, 16));
        g.setColor(Color.BLACK);
        g.drawString(debug, 30, 30);

    }


    public Body getHero() {
        return hero;
    }

    public Body getFloor() {
        return floor;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}
