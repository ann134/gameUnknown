package pac2Graphics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Link;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Floor extends GameObject {

    /*protected Body body;*/

    private int bgx;

    public Floor(int bgx) throws IOException {
        this.bgx = bgx;

        //грузим картинку
       BufferedImage floorImage; /* = ImageIO.read(new File("backgroundFloor.png"))*/;
        try {
            String s = "bgFloor_" + bgx + ".png";
            floorImage = ImageIO.read(new File(s));
        } catch (Exception e){
            System.out.println("не прочитали пол");
            floorImage = ImageIO.read(new File("bg_0_0.png"));
        }

        //читаем картинку
        ArrayList<PixelCoords> floorPoints = new ArrayList<>();

        for (int x = 1; x < floorImage.getWidth(); x++) {     //перебираем пиксели
            for (int y = 1; y < floorImage.getHeight(); y++) {
                int white = 0xFFFFFFFF;
                int black = 0xFF000000;
                int color = floorImage.getRGB(x, y);
                if (color != white) {
                    boolean exist = false;
                    //переюираем floorPoints, fp, у нее координаты fp.x, fp.y.  Math.abs(x - fp.x) + Math.abs(y - fp.y) <= 9 значит, уже знаем эту точку, пропускаем
                    for (PixelCoords point : floorPoints) {
                        if (Math.abs(x - point.x) + Math.abs(y - point.y) <= 20) {
                            exist = true;
                        }
                    }
                    //добавляем если не существует
                    if (!exist)
                        floorPoints.add(new PixelCoords(x + 2, y + 6));
                }
            }
        }

        // создаем точки для пола в мировых координатах
        List<Vector2> floorPointsInWorld = new ArrayList<>();
        for (PixelCoords floorPoint : floorPoints)
            floorPointsInWorld.add(new Vector2(
                    Camera.BG_W * bgx + Camera.BG_W * floorPoint.x / floorImage.getWidth(),
                    Camera.BG_H * (floorImage.getHeight() - floorPoint.y) / floorImage.getHeight()
            ));

        List<Link> links = Geometry.createLinks(floorPointsInWorld, false);

        body = new Body();
        for (Link link : links) {
            body.addFixture(link);
        }
        body.translate(0, 0);
        body.setMass(MassType.INFINITE);
    }

    public void draw(Canvas canvas) {
        List<BodyFixture> links = body.getFixtures();
        for (BodyFixture b : links) {
            Link l = (Link) b.getShape();
            Vector2[] vertices = l.getVertices();

            for (int i = 0; i < vertices.length - 1; i++) {
                canvas.setColor(new Color(29, 17, 12));
                canvas.drawLine(
                        vertices[i].x,
                        vertices[i].y,
                        vertices[i + 1].x,
                        vertices[i + 1].y
                );
            }
        }
    }
}
