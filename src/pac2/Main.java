package pac2;


import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.examples.GameObject;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            MyFrame myFrame = new MyFrame();
            myFrame.setVisible(true);
        } catch (IOException e) {
            System.out.println("Ошибка!");
            e.printStackTrace();
        }
    }
}
