package pac2Graphics;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class GameFrame extends JFrame {

    HeroKeyListener heroKeyListener;

    public GameFrame() throws IOException {
        super("My Frame");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize((int)(Camera.SCREEN_W * Canvas.SCALE), (int)(Camera.SCREEN_H * Canvas.SCALE));
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
        GridLayout l = new GridLayout(1, 1);
        setLayout(l);
        GamePanel p = new GamePanel();
        p.setBackground(new Color(11, 13, 13));
        add(p);
        setLocationRelativeTo(null);

        heroKeyListener = new HeroKeyListener(p);
        addKeyListener(heroKeyListener);

        p.start();
    }
}
