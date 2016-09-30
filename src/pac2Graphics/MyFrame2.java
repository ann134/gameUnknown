package pac2Graphics;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class MyFrame2 extends JFrame {

    private static HeroKeyListener heroKeyListener;

    public MyFrame2() throws IOException {
        super("My Frame");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        GridLayout l = new GridLayout(1, 1);
        setLayout(l);
        MyPanel2 p = new MyPanel2();
        p.setBackground(new Color(255, 241, 198));
        add(p);

        heroKeyListener = new HeroKeyListener(p);
        addKeyListener(heroKeyListener);

        p.start();
    }

    public static HeroKeyListener getHeroKeyListener(){
        return heroKeyListener;
    }
}
