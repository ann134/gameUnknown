package pac2Graphics;


import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.examples.GameObject;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyPanel2 extends JPanel{
    public BufferedImage smile;


    public static final double SCALE = 45.0;
    public static final double NANO_TO_BASE = 1.0e9;

    private World world;
    private long last;

    private Body floor;
    private Body circle;


    public MyPanel2() throws IOException {
        smile = ImageIO.read(new File("smile.png"));

        this.initializeWorld();
        start();
    }

    @Override
    protected void paintComponent(Graphics g){
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

        /*org.dyn4j.geometry.Rectangle floorRect = new org.dyn4j.geometry.Rectangle(13, 3);
        GameObject floor = new GameObject();
        floor.addFixture(new BodyFixture(floorRect));
        floor.setMass(MassType.INFINITE);

        this.world.addBody(floor);

        Circle cirShape = new Circle(0.5);
        GameObject circle = new GameObject();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);

        circle.applyForce(new Vector2(-100.0, 0.0));
        circle.setLinearDamping(0.05);
        this.world.addBody(circle);
        System.out.println("complitely initialized");*/


        Rectangle foorShape = new Rectangle(1500, 300);
        floor = new Body();
        floor.addFixture(foorShape);
        floor.setMass(MassType.INFINITE);
        floor.translate(0.0, -700);
        this.world.addBody(floor);


        // create a circle
        Circle cirShape = new Circle(0.5);
        circle = new Body();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.translate(2.0, 2.0);
        // test adding some force
        circle.applyForce(new Vector2(-100.0, 0.0));
        // set some linear damping to simulate rolling friction
        circle.setLinearDamping(0.05);
        this.world.addBody(circle);

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

        repaint();


        long time = System.nanoTime();
        long diff = time - this.last;

        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE;

        this.world.update(elapsedTime);
    }


    public void render(Graphics2D g){
        AffineTransform transform = g.getTransform();

        int x = (int) circle.getTransform().getTranslation().x * (int) -SCALE;
        int y = (int) circle.getTransform().getTranslation().y * (int) -SCALE;


        g.drawImage(smile, x, y, null);




        g.setTransform(transform);


        int x2 = (int) floor.getTransform().getTranslation().x * (int) -SCALE;
        int y2 = (int) floor.getTransform().getTranslation().y * (int) -SCALE;
        /*int w = floor.;
        int h = ;*/


        g.setColor(new Color(0, 0, 0));
        g.fillRect(x2, y2, 1500, 300);







       /* AffineTransform transform2 = g.getTransform();

        int x2 = (int) floor.getTransform().getTranslation().x * (int) -SCALE;
        int y2 = (int) floor.getTransform().getTranslation().y * (int) -SCALE;
        *//*int w = floor.getFixture(0).getShape().get;
        int h = *//*


        g.setColor(new Color(0, 0, 0));
        g.fillRect(x2, y2, 200, 3);


        g.setTransform(transform2);*/


    }

//___________________________________________________________________________________________________________



    private void drawAxes(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(0, 0, 100, 0);
        g2d.setColor(Color.RED);
        g2d.drawLine(0, 0, 0, 100);
    }

    private void drawFigure(Graphics2D g){
        g.setColor(new Color(50, 50, 50));
        g.fillRect(250, 250, 300, 300);
    }

    private void drawFloor(Graphics2D g){
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 980, 1920, 300);
    }


    private void drawImage(Graphics2D g){
        /*
        Graphics2D gg = (Graphics2D) g.create();
        g.translate(100, 100);
        gg.rotate(Math.PI / 4, 1000, 300);
        gg.drawImage(smile, 1000, 300, null);
        gg.dispose();
        */

        AffineTransform transform = g.getTransform();
        g.rotate(Math.PI / 4, 1000, 300);
        g.drawImage(smile, 1000, 300, null);
        g.setTransform(transform);
    }

}
