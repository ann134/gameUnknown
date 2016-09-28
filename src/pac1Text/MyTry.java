package pac1Text;


import java.io.IOException;

public class MyTry {
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
