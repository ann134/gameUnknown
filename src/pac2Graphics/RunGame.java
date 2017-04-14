package pac2Graphics;


import java.io.IOException;

public class RunGame {
    public static void main(String[] args) {
        try {
            GameFrame myFrame = new GameFrame();
            myFrame.setVisible(true);
        } catch (IOException e) {
            System.out.println("Main Ошибка!");
            e.printStackTrace();
        }
    }
}
