package pac2;


import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.examples.GameObject;
import org.dyn4j.geometry.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyPanel extends JPanel{
    public BufferedImage smile;


    public static final double SCALE = 45.0;
    public static final double NANO_TO_BASE = 1.0e9;

    public World world;
    public boolean stopped;
    public long last;


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawImage(g2d);
        drawFigure(g2d);
        drawAxes(g2d);
        drawFloor(g2d);


        start();
    }


    public MyPanel() throws IOException {
        smile = ImageIO.read(new File("smile.png"));

        this.initializeWorld();
    }


    protected void initializeWorld() {
        this.world = new World();


        org.dyn4j.geometry.Rectangle floorRect = new org.dyn4j.geometry.Rectangle(13, 3);
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
        System.out.println("complitely initialized");
    }



    public synchronized void stop() {
        this.stopped = true;
    }
    public synchronized boolean isStopped() {
        return this.stopped;
    }



    public void start() {
        this.last = System.nanoTime();

        Thread thread = new Thread() {
            public void run() {
                while (!isStopped()) {
                    gameLoop();
                }
            }
        };
        thread.start();

        System.out.println("looop started");
    }

    protected void gameLoop() {


        long time = System.nanoTime();
        long diff = time - this.last;
        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE;
        this.world.update(elapsedTime);

        System.out.println(elapsedTime);
    }


    public void render(Graphics2D g){
        AffineTransform transform = g.getTransform();





        g.setTransform(transform);
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
