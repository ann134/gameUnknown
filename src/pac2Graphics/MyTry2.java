package pac2Graphics;


import java.io.IOException;

public class MyTry2 {
    public static void main(String[] args) {

        try {
            MyFrame2 myFrame = new MyFrame2();
            myFrame.setVisible(true);
        } catch (IOException e) {
            System.out.println("Ошибка!");
            e.printStackTrace();
        }
    }
}