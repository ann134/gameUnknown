package pac2Graphics;


import org.dyn4j.dynamics.World;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MyPanel2 extends JPanel {

    private boolean debugMode = true;
    private String debug = "hello";

    public static final double NANO_TO_BASE = 1.0e9;

    private long last;
    /*public static long start;*/
    public Timer timer;

    private World world;
    private Camera camera;

    private List<Floor> floors;
    private Hero hero;
    private Kolobok kolobok;
    private Tree tree;

    public MyPanel2() throws IOException {
        camera = new Camera();
        /*timer = new Timer();*/
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

    protected void initializeWorld() throws IOException {
        this.world = new World();

        loadFloor();

        kolobok = new Kolobok(0.5);
        world.addBody(kolobok.getBody());

        tree = new Tree();
        world.addBody(tree.getBody());

        hero = new Hero(floors, tree, kolobok);
        world.addBody(hero.getBody());
    }

    private void loadFloor() throws IOException {
        floors = new ArrayList<>();
        PixelCoords last = null;
        for (int x = -1 ; x < 10; x++){
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

        //TODO придумать одну простую головоломку, чтобы попробовать на ней, как создавать объекты и взаимодействия между ними
        //TODO головоломка: падающее дерево
        //TODO указывать положение объектов с помощью файлов. Аналогично файлам для пола
        //TODO чтобы пол склеивался непрерывно между экранами

        camera.move(hero, elapsedTime);

        repaint();

        this.world.update(elapsedTime);
    }

    private void render(Graphics2D g) {
        //определить время, прошедшее с момента запуска программы, превратить его в номер кадра. Передавать номер кадра в draw() у всех GameObject (hero, kolobok)
        /*long now = System.nanoTime();
        int frame = (int) ((now - timer.start) / NANO_TO_BASE * 10);*/

        int frame = Timer.getFrameFrom(Timer.start);
        Canvas canvas = new Canvas(g, camera);

        canvas.resetTransform();
        camera.drawBackground(canvas);

        if (debugMode){
            for (Floor floor: floors){
                canvas.transformBody(floor.getBody());
                floor.drawDebug(canvas);
            }
        }

        canvas.transformBody(hero.getBody());
        hero.draw(canvas, frame);
        if (debugMode){
            hero.drawDebug(canvas);
        }

        canvas.transformBody(kolobok.getBody());
        kolobok.draw(canvas, frame);


        canvas.transformBody(tree.getBody());
        tree.draw(canvas, frame);
        if (debugMode){
            tree.drawDebug(canvas);
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
}
