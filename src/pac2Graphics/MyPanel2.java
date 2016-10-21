package pac2Graphics;


import org.dyn4j.dynamics.World;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MyPanel2 extends JPanel {

    public static final double NANO_TO_BASE = 1.0e9;

    private Camera camera;
    private World world;
    private long last;

    private Floor floor;
    private List<Floor> floors;
    private Kolobok kolobok;
    private Hero hero;

    private String debug = "hello";

    public MyPanel2() throws IOException {
        camera = new Camera();
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


        floors = new ArrayList<>();
        Set<Integer> xforfloor = new HashSet<>();
        for (BgCoords bg : camera.getVisibleBackgrounds()){
            xforfloor.add(bg.x);
        }

        for (int x : xforfloor){
            Floor f = new Floor(x);
            floors.add(f);
            world.addBody(f.getBody());
        }

        /*floor = new Floor();
        world.addBody(floor.getBody());*/

        kolobok = new Kolobok(0.5);
        world.addBody(kolobok.getBody());

        hero = new Hero(floors);
        world.addBody(hero.getBody());
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
        long time = System.nanoTime();
        long diff = time - this.last;

        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE; //сколько секунд прошло с прошлого раза

        hero.move();

        //TODO сделать побольше склеиваемых картинок фона
        //TODO сделать пол отдельно на каждом "экране"
        //TODO придумать одну простую головоломку, чтобы попробовать на ней, как создавать объекты и взаимодействия между ними

        camera.move(hero, elapsedTime);

        repaint();

        this.world.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        Canvas canvas = new Canvas(g, camera);

        canvas.resetTransform();
        camera.drawBackground(canvas);

        canvas.transformBody(kolobok.getBody());
        kolobok.draw(canvas);


        for (Floor floor: floors){
            canvas.transformBody(floor.getBody());
            floor.draw(canvas);
        }

        /*canvas.transformBody(floor.getBody());
        floor.draw(canvas);*/

        canvas.transformBody(hero.getBody());
        hero.draw(canvas);

        canvas.kill();

        //рисуем текст дебаг
        g.setFont(new Font("Arial", 0, 16));
        g.drawString(debug, 30, 30);
    }

    public Hero getHero() {
        return hero;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}
