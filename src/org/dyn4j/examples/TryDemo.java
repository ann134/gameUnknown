package org.dyn4j.examples;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;


public class TryDemo extends JFrame {

    public static final double SCALE = 45.0;
    public static final double NANO_TO_BASE = 1.0e9;

    protected Canvas canvas;
    protected World world;
    protected boolean stopped;
    protected long last;


    public TryDemo() {
        super("Graphics2D Example");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
                super.windowClosing(e);
            }
        });

        /*this.setSize(1900, 1000);*/
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        /*Dimension size = new Dimension(1900, 1000);*/
        /*this.setLocationRelativeTo( null );*/

        this.canvas = new Canvas();
        this.canvas.setPreferredSize(size);
//        this.canvas.setMinimumSize(size);
//        this.canvas.setMaximumSize(size);

        this.add(this.canvas);
//        this.setResizable(false);
//        this.pack();
        this.stopped = false;

        this.initializeWorld();
    }

    protected void initializeWorld() {
        this.world = new World();

        Rectangle floorRect = new Rectangle(13.0, 3.0);
        GameObject floor = new GameObject();
        floor.addFixture(new BodyFixture(floorRect));
        floor.setMass(MassType.INFINITE);

        floor.translate(0.0, -6.0);
        this.world.addBody(floor);



        Triangle triShape = new Triangle(
                new Vector2(0.0, 0.5),
                new Vector2(-0.5, -0.5),
                new Vector2(0.5, -0.5));
        GameObject triangle = new GameObject();
        triangle.addFixture(triShape);
        triangle.setMass(MassType.NORMAL);
        triangle.translate(-1.0, 2.0);
        triangle.getLinearVelocity().set(5.0, 0.0);
        this.world.addBody(triangle);


        Circle cirShape = new Circle(0.5);
        GameObject circle = new GameObject();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.translate(2.0, 2.0);

        circle.applyForce(new Vector2(-100.0, 0.0));
        circle.setLinearDamping(0.05);
        this.world.addBody(circle);


        Rectangle rectShape = new Rectangle(1.0, 1.0);
        GameObject rectangle = new GameObject();
        rectangle.addFixture(rectShape);
        rectangle.setMass(MassType.NORMAL);
        rectangle.translate(0.0, 2.0);
        rectangle.getLinearVelocity().set(-5.0, 0.0);
        this.world.addBody(rectangle);


        Polygon polyShape = Geometry.createUnitCirclePolygon(10, 1.0);
        GameObject polygon = new GameObject();
        polygon.addFixture(polyShape);
        polygon.setMass(MassType.NORMAL);
        polygon.translate(-2.5, 2.0);
        polygon.setAngularVelocity(Math.toRadians(-20.0));
        this.world.addBody(polygon);


        Circle c1 = new Circle(0.5);
        BodyFixture c1Fixture = new BodyFixture(c1);
        c1Fixture.setDensity(0.5);
        Circle c2 = new Circle(0.5);
        BodyFixture c2Fixture = new BodyFixture(c2);
        c2Fixture.setDensity(0.5);
        Rectangle rm = new Rectangle(2.0, 1.0);

        c1.translate(-1.0, 0.0);
        c2.translate(1.0, 0.0);
        GameObject capsule = new GameObject();
        capsule.addFixture(c1Fixture);
        capsule.addFixture(c2Fixture);
        capsule.addFixture(rm);
        capsule.setMass(MassType.NORMAL);
        capsule.translate(0.0, 4.0);
        this.world.addBody(capsule);

        GameObject issTri = new GameObject();
        issTri.addFixture(Geometry.createIsoscelesTriangle(1.0, 3.0));
        issTri.setMass(MassType.NORMAL);
        issTri.translate(2.0, 3.0);
        this.world.addBody(issTri);

        GameObject equTri = new GameObject();
        equTri.addFixture(Geometry.createEquilateralTriangle(2.0));
        equTri.setMass(MassType.NORMAL);
        equTri.translate(3.0, 3.0);
        this.world.addBody(equTri);

        GameObject rightTri = new GameObject();
        rightTri.addFixture(Geometry.createRightTriangle(2.0, 1.0));
        rightTri.setMass(MassType.NORMAL);
        rightTri.translate(4.0, 3.0);
        this.world.addBody(rightTri);

        GameObject cap = new GameObject();
        cap.addFixture(new Capsule(1.0, 0.5));
        cap.setMass(MassType.NORMAL);
        cap.translate(-3.0, 3.0);
        this.world.addBody(cap);

        GameObject slice = new GameObject();
        slice.addFixture(new Slice(0.5, Math.toRadians(120)));
        slice.setMass(MassType.NORMAL);
        slice.translate(-3.0, 3.0);
        this.world.addBody(slice);
    }


    public void start() {
        this.last = System.nanoTime();
//        this.canvas.setIgnoreRepaint(true);
//        this.canvas.createBufferStrategy(2);

        Thread thread = new Thread() {
            public void run() {
                while (!isStopped()) {
                    gameLoop();

                }
            }
        };

        thread.setDaemon(true);
        thread.start();
    }


    protected void gameLoop() {
        Graphics2D g = (Graphics2D)this.canvas.getBufferStrategy().getDrawGraphics();

        AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
        AffineTransform move = AffineTransform.getTranslateInstance(400, -300);
        g.transform(yFlip);
        g.transform(move);


        this.render(g);
        g.dispose();


        BufferStrategy strategy = this.canvas.getBufferStrategy();
        if (!strategy.contentsLost()) {
            strategy.show();
        }

        Toolkit.getDefaultToolkit().sync();

        long time = System.nanoTime();
        long diff = time - this.last;
        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE;
        this.world.update(elapsedTime);
    }


    protected void render(Graphics2D g) {

        g.setColor(Color.WHITE);
        g.fillRect(-900, -600, 1900 + 900, 1000 + 1600);

        g.translate(0.0, -1.0 * SCALE);

        for (int i = 0; i < this.world.getBodyCount(); i++) {
            GameObject go = (GameObject) this.world.getBody(i);
            go.render(g);
        }
    }


    public synchronized void stop() {
        this.stopped = true;
    }

    public synchronized boolean isStopped() {
        return this.stopped;
    }








    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        TryDemo window = new TryDemo();

        window.setVisible(true);
        /*window.setExtendedState(MAXIMIZED_BOTH);*/
        window.start();
    }


}