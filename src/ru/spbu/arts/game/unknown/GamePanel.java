package ru.spbu.arts.game.unknown;


import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.WheelJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {

    private static final boolean DEBUG_MODE = false;
    private static final int START_POSITION = 0;
    private String debug = "hello";

    public static final double NANO_TO_BASE = 1.0e9;
    private long last;

    private World world;
    private Camera camera;


    private List<Floor> floors;

    private Pumpkin pumpkin;
    private Stone stone;

    private Stump stump;
    private Tree tree;

    private InvisibleObject spikes;
    private InvisibleObject spikes2;
    private InvisibleObject beforeStone;
    private InvisibleObject stopStone;

    private Cart cart;
    private CartWheel wheel1;
    private CartWheel wheel2;

    private SecondFloor secondFloor;

    private Branches branches;

    private Greg greg;
    private Beast beast;
    private Light light;

    private Hero hero;

    private Text text;

    private AllWorldGameObjects allObjects;
    private List<Vector2> saves = new ArrayList<>();

    int stoneMass = 0;
    int stoneKills = 1;

    public static boolean gameEND = false;

    public GamePanel() throws IOException {
        camera = new Camera();

        saves.add(new Vector2(20, 6)); //0 начало
        saves.add(new Vector2(80, 6)); //1 перед ямой
        saves.add(new Vector2(121, 3)); //2 перед камнем
        saves.add(new Vector2(250 + Camera.BG_W * 2, 6)); //3 в деревне
        saves.add(new Vector2(Camera.BG_W * 10 + Camera.BG_W * 2, 20)); //4 на горе перед ямой
        saves.add(new Vector2(250 + Camera.BG_W * 8, 6)); //5 конец

        this.initializeWorld(saves.get(START_POSITION).x, saves.get(START_POSITION).y);
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

    private void initializeWorld(double heroX, double heroY) throws IOException {
        this.world = new World();
        allObjects = new AllWorldGameObjects();

        loadFloor();
        for (Floor floor : floors) {
            allObjects.addObject(floor, null);
        }

        pumpkin = addGameObject(new Pumpkin(1), 200 + Camera.BG_W * 2, 6);
        //pumpkin.body.applyForce(new Vector2(100.0, 0.0));

        stone = addGameObject(new Stone(3), 167, 9);

        tree = addGameObject(new Tree(1, 14), 463.5 - Camera.BG_W, 13.5 + 7);
        stump = addGameObject(new Stump(1, 1.5), 463.5 - Camera.BG_W, 13);
        //tree.body.rotate(Math.PI / 6);
        RevoluteJoint rv = new RevoluteJoint(stump.body, tree.body, new Vector2(463.5 - Camera.BG_W, 13.5));
        world.addJoint(rv);


        spikes = addGameObject(new InvisibleObject(7, 1.5), 85.5, 2);
        spikes2 = addGameObject(new InvisibleObject(7, 2), 470  - Camera.BG_W, 3);
        beforeStone = addGameObject(new InvisibleObject(2, 2), 124, 2);
        stopStone = addGameObject(new InvisibleObject(2, 2), 112, 4);


        //TODO move to cart, use names to get wheels
        // ТЕЛЕГА
        Vector2 cartCenter = new Vector2(370 - Camera.BG_W, 5.4);
        Vector2 wheelCenter1 = new Vector2(cartCenter.x - 1.5, cartCenter.y - 1.4);
        Vector2 wheelCenter2 = new Vector2(cartCenter.x + 1.5, cartCenter.y - 1.4);

        cart = addGameObject(new Cart(5.5, 2), cartCenter.x, cartCenter.y);
        wheel1 = addGameObject(new CartWheel(1), wheelCenter1.x, wheelCenter1.y);
        wheel2 = addGameObject(new CartWheel(1), wheelCenter2.x, wheelCenter2.y);

        WheelJoint joint = new WheelJoint(cart.body, wheel1.body, wheelCenter1, wheelCenter1);
        world.addJoint(joint);
        WheelJoint joint2 = new WheelJoint(cart.body, wheel2.body, wheelCenter2, wheelCenter2);
        world.addJoint(joint2);

        secondFloor = addGameObject(new SecondFloor(28, 0.5), 403.5  - Camera.BG_W, 6.5);

        greg = addGameObject(new Greg(), 262 + Camera.BG_W * 8, 6);
        branches = addGameObject(new Branches(), "branches", 262 + Camera.BG_W * 8, 5.7);
        beast = addGameObject(new Beast(), "beast", 268 + Camera.BG_W * 8, 10);

        hero = addGameObject(new Hero(world, allObjects), heroX, heroY);

        light = addGameObject(new Light(), 255 + Camera.BG_W * 8, 6);


        text = addGameObject(new Text(), 542, 15);



        //прямо здесь создаем класс без имени, который переопределяет метод collided
        /*listenCollisions(hero, kolobok, new CollidingAction() {
            @Override
            public void collided() {
                System.out.println("kolobok and hero collided " + System.nanoTime());
            }
        });

        listenCollisions(hero, tree, () -> {
            System.out.println("tree and hero collided " + System.nanoTime());
        });*/

        listenCollisions(hero, spikes, () -> {
                    if (hero.alive()) {
                        hero.kill(true);
                        hero.setMovementStart(System.nanoTime());
                    }
                }
        );

        listenCollisions(hero, spikes2, () -> {
                    if (hero.alive()) {
                        hero.kill(true);
                        hero.setMovementStart(System.nanoTime());
                    }
                }
        );


        listenCollisions(hero, beforeStone, () -> {
                    if (stoneMass == 0){
                        stoneMass = 1;
                    }
                }
        );

        listenCollisions(stone, stopStone, () -> {
                        stoneKills = 0;
                }
        );

        listenCollisions(hero, stone, () -> {
                    //System.out.println("mgn = " + stone.body.getLinearVelocity().getMagnitude());
                    if (hero.alive() && stoneKills == 1) {
                        hero.kill(true);
                        hero.setMovementStart(System.nanoTime());
                        stoneMass = 0;
                        stoneKills = 1;
                    }
                }
        );



        listenCollisions(hero, greg, () -> {
                    if (!light.alive()) {
                        gameEND = true;
                        showGameOver();
                    }
                }
        );
    }

    private void showGameOver() {

    }

    private <T extends GameObject> T addGameObject(T o, double x, double y) throws IOException {
        return addGameObject(o, null, x, y);
    }

    private <T extends GameObject> T addGameObject(T o, String name, double x, double y) throws IOException {
        o.setAllWorldGameObjects(allObjects);

        o.getBody().translate(x, y);
        allObjects.addObject(o, name);
        world.addBody(o.getBody());
        return o;
    }

    private void loadFloor() throws IOException {
        floors = new ArrayList<>();
        PixelCoords last = null;
        for (int x = 0; x < 17; x++) {
            Floor f = new Floor(x, last);
            last = f.getLastPC();
            floors.add(f);
            world.addBody(f.getBody());
        }
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

    public void start() throws IOException {
        this.last = System.nanoTime();

        Timer.start();

        Thread thread = new Thread(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    gameLoop();
                } catch (IOException e) {
                    System.out.println("relife Ошибка!");
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    private void gameLoop() throws IOException {
        long time = System.nanoTime();
        long diff = time - this.last;

        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE; //сколько секунд прошло с прошлого раза

        if (!gameEND) {

            if (hero.alive()) {
                hero.move();
                hero.processCarryButtonPress();
            } else {
                hero.getBody().setMassType(MassType.NORMAL);

                if (time - hero.getMovementStart() > 2e9) {
                    Vector2 respawnPoint = getRespawnPoint();
                    initializeWorld(respawnPoint.x, respawnPoint.y);
                }
            }

            camera.move(hero, elapsedTime);

            if (stoneMass == 1){
                stone.getBody().setMassType(MassType.NORMAL);
                stone.getBody().setLinearDamping(0.05);
                stoneMass = 2;
            }
        }

        debug = (int) hero.body.getWorldCenter().x + ", " + (int) hero.body.getWorldCenter().y;

        repaint();
        this.world.update(elapsedTime);
    }

    private Vector2 getRespawnPoint() {
        double deathPointX = hero.body.getWorldCenter().x;

        Vector2 respawnPoint = new Vector2(0, 0);
        double differenceMin = deathPointX - saves.get(0).x;

        for (Vector2 save : saves) {
            if (save.x < deathPointX) {
                if (deathPointX - save.x < differenceMin) {
                    differenceMin = deathPointX - save.x;
                    respawnPoint = save;
                }
            }
        }
        return respawnPoint;
    }


    private void render(Graphics2D g) {
        //определяем время, прошедшее с момента запуска программы, превращаем его в номер кадра.
        // номер кадра передается как параметр draw() у всех GameObject
        int frame = Timer.getFrameFrom(Timer.getStart());
        ru.spbu.arts.game.unknown.Canvas canvas = new ru.spbu.arts.game.unknown.Canvas(g, camera);

        canvas.resetTransform();
        camera.drawBackground(canvas);


        for (GameObject object : allObjects.getList()) {
            canvas.transformBody(object.getBody());
            object.draw(canvas, frame);
            if (DEBUG_MODE) {
                object.drawDebug(canvas);
                setDebug(" coords: " + (int) hero.getBody().getWorldCenter().x + " , "
                        + (int) hero.getBody().getWorldCenter().y); //только для героя
            }
        }

        canvas.kill();


        if (DEBUG_MODE) {
            //рисуем текст дебаг
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString(debug, 30, 30);
        }
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public Hero getHero() {
        return hero;
    }

    public Beast getBeast() {
        return beast;
    }

    public Branches getBranches() {
        return branches;
    }

    public Light getLight() {
        return light;
    }
}

//TODO на весь экран
//TODO Проверить разные размеры экранов, для этого отключить режим всего экрана и задать разные размеры окну
//TODO запуск jar или лучше exe файла
//TODO один каталог с картинками, в нем подпапки, которые сейчас
//TODO сделать физическое тело героя уже - ближе к форме тела
