package pac2Graphics;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class MyFrame2 extends JFrame {

    public MyFrame2() throws IOException {
        super("My Frame");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize((int)(Camera.SCREEN_W * Canvas.SCALE), (int)(Camera.SCREEN_H * Canvas.SCALE));
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
        GridLayout l = new GridLayout(1, 1);
        setLayout(l);
        GamePanel p = new GamePanel();
        p.setBackground(new Color(0, 0, 0));
        add(p);
        setLocationRelativeTo(null);

        HeroKeyListener heroKeyListener = new HeroKeyListener(p);
        addKeyListener(heroKeyListener);

        p.start();
    }
}
