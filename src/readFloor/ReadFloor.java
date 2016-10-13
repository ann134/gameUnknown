package readFloor;


import javafx.scene.paint.Color;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFloor {
    public static void main(String[] args) {
        try {

            BufferedImage floor = ImageIO.read(new File("floor.png"));


            ArrayList<Vector2> floorPoints = new ArrayList<>();
            //перебираем пиксели
            for (int x = 1; x < floor.getWidth(); x++) {
                for (int y = 1; y < floor.getHeight(); y++) {
                    int white = 0xFFFFFFFF;
                    int black = 0xFF000000;
                    int color = floor.getRGB(x, y);

                    if (color != white) {
                        boolean exist = false;

                        //переюираем floorPoints, fp, у нее координаты fp.x, fp.y.  Math.abs(x - fp.x) + Math.abs(y - fp.y) <= 9 значит, уже знаем эту точку, пропускаем
                        for (Vector2 point: floorPoints) {
                            if (Math.abs(x - point.x) +  Math.abs(y - point.y) <= 20){
                                exist = true;
                            }
                        }

                        if (!exist)
                        floorPoints.add(new Vector2(x+2, y+6));
                    }
                }
            }

            System.out.println(floorPoints);

        } catch (IOException e) {
            System.out.println("No");
        }


    }


}
