package pac2Graphics;


import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {

    //TODO головоломка: падающее дерево
    //TODO указывать положение объектов с помощью файлов. Аналогично файлам для пола

    private static final boolean DEBUG_MODE = true;
    private String debug = "hello";

    public static final double NANO_TO_BASE = 1.0e9;
    private long last;
    /*public Timer timer;*/

    private World world;
    private Camera camera;

    private List<Floor> floors;
    private Hero hero;
    private Kolobok kolobok;
    private Tree tree;
    private InvisibleObject spikes;

    private AllWorldGameObjects allObjects = new AllWorldGameObjects();
    private List<Vector2> saves = new ArrayList<>();

    public GamePanel() throws IOException {
        camera = new Camera();
        /*timer = new Timer();*/
        this.initializeWorld();

        saves.add(new Vector2(20, 6));
        saves.add(new Vector2(90, 6));
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

    protected void initializeWorld() throws IOException {
        this.world = new World();

        loadFloor();
        for (Floor floor: floors){
            allObjects.addObject(floor);
        }

        kolobok = new Kolobok(0.5);
        kolobok.body.translate(21, 6); //TODO do not use the field 'body' directly
        kolobok.body.applyForce(new Vector2(100.0, 0.0));
        allObjects.addObject(kolobok);
        world.addBody(kolobok.getBody());

        tree = new Tree(1, 14);
        tree.body.rotate(Math.PI/6);
        tree.body.translate(64, 10);
        allObjects.addObject(tree);
        world.addBody(tree.getBody());

        spikes = new InvisibleObject(5, 1);
        spikes.body.translate(85, 6);

        allObjects.addObject(spikes);
        world.addBody(spikes.getBody());

        hero = new Hero(world, allObjects);
        hero.body.translate(75, 6); // было 20 и 6
        world.addBody(hero.getBody());



        //прямо здесь создаем класс без имени, который переопределяет метод collided
        /*listenCollisions(hero, kolobok, new CollidingAction() {
            @Override
            public void collided() {
                System.out.println("kolobok and hero collided " + System.nanoTime());
            }
        });*/

        /*listenCollisions(hero, kolobok, () -> System.out.println("kolobok and hero collided " + System.nanoTime()));
        listenCollisions(hero, tree, () -> {
            System.out.println("tree and hero collided " + System.nanoTime());
        });*/

        listenCollisions(hero, spikes, () -> hero.kill(true));

        /*WeldJoint joint = new WeldJoint(hero.body, kolobok.body, new Vector2(21, 5));
        world.addJoint(joint);*/
//        world.removeJoint(joint);
    }

    private void loadFloor() throws IOException {
        floors = new ArrayList<>();
        PixelCoords last = null;
        for (int x = 0 ; x < 10; x++){
            Floor f = new Floor(x, last);
            last = f.getLastPC();
            floors.add(f);
            world.addBody(f.getBody());
        }
    }

    public void start() {
        this.last = System.nanoTime();

        Timer.start = System.nanoTime();

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
        long time = System.nanoTime();
        long diff = time - this.last;

        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE; //сколько секунд прошло с прошлого раза

        hero.move();
        camera.move(hero, elapsedTime);
        hero.processCarryButtonPress();

        //hero.body.

        /*world.addListener(new CollisionListener() {
            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
                System.out.println("collided " + body1 + " and " + body2);
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Manifold manifold) {
                return true;
            }

            @Override
            public boolean collision(ContactConstraint contactConstraint) {
                return true;
            }
        });*/

        repaint();
        this.world.update(elapsedTime);
    }


    private void render(Graphics2D g) {

        //определяем время, прошедшее с момента запуска программы, превратщаем его в номер кадра. номер кадра передается как параметр draw() у всех GameObject
        int frame = Timer.getFrameFrom(Timer.start);
        Canvas canvas = new Canvas(g, camera);

        canvas.resetTransform();
        camera.drawBackground(canvas);

        if (DEBUG_MODE){
            for (Floor floor: floors){
                canvas.transformBody(floor.getBody());
                floor.drawDebug(canvas);
            }
        }

        canvas.transformBody(hero.getBody());
        hero.draw(canvas, frame);
        if (DEBUG_MODE){
            hero.drawDebug(canvas);
        }
        setDebug(" coords: " + (int) hero.getBody().getWorldCenter().x + " , " + (int) hero.getBody().getWorldCenter().y);

        canvas.transformBody(kolobok.getBody());
        kolobok.draw(canvas, frame);


        canvas.transformBody(tree.getBody());
        tree.draw(canvas, frame);
        if (DEBUG_MODE){
            tree.drawDebug(canvas);
        }

        canvas.transformBody(spikes.getBody());
        /*spikes.draw(canvas, frame);*/
        if (DEBUG_MODE){
            spikes.drawDebug(canvas);
        }



        canvas.kill();

        //рисуем текст дебаг
        g.setFont(new Font("Arial", 0, 16));
        g.drawString(debug, 30, 30);
    }

    public Hero getHero() {
        return hero;
    }

    /*public Timer getTimer() {
        return timer;
    }*/

    public void setDebug(String debug) {
        this.debug = debug;
    }

    private void listenCollisions(GameObject o1, GameObject o2, CollidingAction action) {
        world.addListener(new CollisionListener() {
            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
                if (body1 == o1.body && body2 == o2.body || body2 == o1.body && body1 == o2.body)
                    action.collided();
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Manifold manifold) {
                return true;
            }

            @Override
            public boolean collision(ContactConstraint contactConstraint) {
                return true;
            }
        });
    }
}
