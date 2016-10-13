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

import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.MAXIMIZED_HORIZ;

public class MyPanel2 extends JPanel {
    private BufferedImage smile;
    private BufferedImage wirt;
    private BufferedImage background;
    private BufferedImage floorImage;

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
        background = ImageIO.read(new File("background.jpg"));
        floorImage = ImageIO.read(new File("backgroundFloor.png"));

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

        // новый пол
        ArrayList<PixelCoords> floorPoints = new ArrayList<>();
        //перебираем пиксели
        for (int x = 1; x < floorImage.getWidth(); x++) {
            for (int y = 1; y < floorImage.getHeight(); y++) {
                int white = 0xFFFFFFFF;
                int black = 0xFF000000;
                int color = floorImage.getRGB(x, y);
                if (color != white) {
                    boolean exist = false;
                    //переюираем floorPoints, fp, у нее координаты fp.x, fp.y.  Math.abs(x - fp.x) + Math.abs(y - fp.y) <= 9 значит, уже знаем эту точку, пропускаем
                    for (PixelCoords point : floorPoints) {
                        if (Math.abs(x - point.x) + Math.abs(y - point.y) <= 20) {
                            exist = true;
                        }
                    }
                    //добавляем если не существует
                    if (!exist)
                        floorPoints.add(new PixelCoords(x + 2, y + 6));
                }
            }
        }
        /*List<Link> links = Geometry.createLinks(new Vector2[] {
                new Vector2(-8.0,  -2),
                new Vector2( -0,  -1),
                new Vector2( 4.0,  0.0),
                new Vector2( 4.0,  0.2),
                new Vector2( 9,  2),
                new Vector2( 10.0, 2)
        }, false);*/
        List<Vector2> floorPointsInWorld = new ArrayList<>();
        for (PixelCoords floorPoint : floorPoints)
            floorPointsInWorld.add(new Vector2(floorPoint.x / SCALE, floorPoint.y / -SCALE));
        List<Link> links = Geometry.createLinks(floorPointsInWorld, false);

        newFloor = new Body();
        for (Link link : links) {
            newFloor.addFixture(link);
        }
        newFloor.translate(0, 0);
        newFloor.setMass(MassType.INFINITE);
        this.world.addBody(newFloor);


        //старый пол
        /*Rectangle foorShape = new Rectangle(40, 3);
        floor = new Body();
        floor.addFixture(foorShape);
        floor.setMass(MassType.INFINITE);
        floor.translate(15, -22);
        this.world.addBody(floor);*/


        //круг
        Circle cirShape = new Circle(0.5);
        circle = new Body();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.translate(10, -1);
        circle.applyForce(new Vector2(100.0, 0.0));
        circle.setLinearDamping(0.05);
        this.world.addBody(circle);


        //герой
        Rectangle heroShape = new Rectangle(1, 2);
        hero = new Body();
        //hero.addFixture(heroShape, плотность, трение, ??прыгучесть);
        hero.addFixture(heroShape, BodyFixture.DEFAULT_DENSITY, 10, BodyFixture.DEFAULT_RESTITUTION);
        //hero.applyImpulse(new Vector2(100, 100));
        hero.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        hero.translate(2, -10);
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

        //идем вперед обратно
        if (heroKeyListener.getGo()) {
            /*if (-3 < linearVelocity.x) {
                if (linearVelocity.x == 0)
                    hero.applyImpulse(new Vector2(-3, 0));
                hero.applyImpulse(new Vector2(-2, 0));
            }*/

            if (linearVelocity.x < 3) {
                if (linearVelocity.x == 0)
                    hero.applyImpulse(new Vector2(3, 0));
                hero.applyImpulse(new Vector2(2, 0));
            }
        }

        //идем назад обратно
        if (heroKeyListener.getGoBack()) {
            /*if (linearVelocity.x < 3) {
                if (linearVelocity.x == 0)
                    hero.applyImpulse(new Vector2(3, 0));
                hero.applyImpulse(new Vector2(2, 0));
            }*/

            if (-3 < linearVelocity.x) {
                if (linearVelocity.x == 0)
                    hero.applyImpulse(new Vector2(-3, 0));
                hero.applyImpulse(new Vector2(-2, 0));
            }
        }

        //прыжок
        if (heroKeyListener.getJump()) {
            if (linearVelocity.y < 2)
                if (hero.isInContact(floor) || hero.isInContact(newFloor)) {
                    hero.applyImpulse(new Vector2(0, 15));
                }
        }


        //тормозим когда идем вперед
        if (heroKeyListener.getStopGo()) {
            if (linearVelocity.x < 0)
                hero.setLinearVelocity(0, linearVelocity.y);
        }

        //тормозим когда идем назад
        if (heroKeyListener.getStopGoBack()) {
            if (0 < linearVelocity.x)
                hero.setLinearVelocity(0, linearVelocity.y);
        }

        /*тормозим прыжок
        if (heroKeyListener.getStopJump()) {
            //hero.setLinearVelocity(linearVelocity.x, 0);
            //heroKeyListener.setJump(false);
        }*/

        repaint();

        long time = System.nanoTime();
        long diff = time - this.last;

        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE;

        this.world.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        g.drawImage(background, 0, 0, this);

        //рисуем круг
        AffineTransform saveTransform = g.getTransform();

        g.translate(circle.getTransform().getTranslationX() * SCALE, circle.getTransform().getTranslationY() * -SCALE);
        g.rotate(-circle.getTransform().getRotation());

        double r = circle.getFixture(0).getShape().getRadius();
        g.setColor(new Color(0, 0, 0));
        g.drawImage(
                smile,
                (int) (-r * SCALE),
                (int) (-r * SCALE),
                (int) (r * 2 * SCALE),
                (int) (r * 2 * SCALE),
                null
        );

        g.setTransform(saveTransform);

        //рисуем пол
       /* saveTransform = g.getTransform();

        g.translate(floor.getTransform().getTranslationX() * SCALE, floor.getTransform().getTranslationY() * -SCALE);
        g.rotate(-floor.getTransform().getRotation());

        double h = ((Rectangle) floor.getFixture(0).getShape()).getHeight();
        double w = ((Rectangle) floor.getFixture(0).getShape()).getWidth();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(
                (int) (-w/2 * SCALE),
                (int) (-h/2 * SCALE),
                (int) (w * SCALE),
                (int) (h * SCALE)
        );
        g.setTransform(saveTransform);*/


        //рисуем героя
        saveTransform = g.getTransform();

        g.translate(hero.getTransform().getTranslationX() * SCALE, hero.getTransform().getTranslationY() * -SCALE);
        g.rotate(-hero.getTransform().getRotation());

        double hh = ((Rectangle) hero.getFixture(0).getShape()).getHeight();
        double wh = ((Rectangle) hero.getFixture(0).getShape()).getWidth();
        g.setColor(new Color(0, 255, 0));
        g.drawImage(
                wirt,
                (int) (-wh / 2 * SCALE),
                (int) (-hh / 2 * SCALE),
                (int) (wh * SCALE),
                (int) (hh * SCALE),
                null
        );
        g.setTransform(saveTransform);


        //рисуем новый пол
        saveTransform = g.getTransform();

        g.translate(newFloor.getTransform().getTranslationX() * SCALE, newFloor.getTransform().getTranslationY() * -SCALE);
        g.rotate(-newFloor.getTransform().getRotation());

        List<BodyFixture> links = newFloor.getFixtures();
        for (BodyFixture b : links) {
            Link l = (Link) b.getShape();
            Vector2[] vertices = l.getVertices();

            for (int i = 0; i < vertices.length - 1; i++) {
                g.setColor(new Color(29, 17, 12));
                g.drawLine(
                        (int) (vertices[i].x * SCALE),
                        (int) (vertices[i].y * -SCALE),
                        (int) (vertices[i + 1].x * SCALE),
                        (int) (vertices[i + 1].y * -SCALE)
                );
            }
        }
        g.setTransform(saveTransform);


        //рисуем текст дебаг
        g.setFont(new Font("Arial", 0, 16));
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
