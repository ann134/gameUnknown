package pac2Graphics;


import org.dyn4j.dynamics.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyPanel2 extends JPanel {
    /*private BufferedImage smile;
    private BufferedImage wirt;*/

    /*public static final double SCALE = 45.0;*/
    public static final double NANO_TO_BASE = 1.0e9;

    private World world;
    private long last;

    private Floor floor;
    private Kolobok kolobok;
    private Hero hero;

    private Camera camera;

    private String debug = "hello";

    public MyPanel2() throws IOException {
        /*smile = ImageIO.read(new File("smile.png"));
        wirt = ImageIO.read(new File("wirt.png"));*/

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

        // новый пол
        floor = new Floor();
        world.addBody(floor.getBody());

        kolobok = new Kolobok(0.5);
        world.addBody(kolobok.getBody());

        hero = new Hero(floor);
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
        hero.move();

        repaint();

        long time = System.nanoTime();
        long diff = time - this.last;

        this.last = time;
        double elapsedTime = diff / NANO_TO_BASE;

        this.world.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        //рисуем на холсте
        Canvas canvas = new Canvas(g, camera);

        canvas.resetTransform();
        camera.drawBackground(canvas);

        canvas.transformBody(kolobok.getBody());
        kolobok.draw(canvas);

        canvas.transformBody(floor.getBody());
        floor.draw(canvas);

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
