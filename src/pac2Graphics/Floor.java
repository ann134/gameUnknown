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

    public static final double FLOOR_FRICTION = BodyFixture.DEFAULT_FRICTION * 3;
    private PixelCoords lastPC = null;

    public Floor(int bgx, PixelCoords firstPC) throws IOException {
        PixelCoords nowPC = null;

        //грузим картинку
        BufferedImage floorImage;
        try {
            String s = "bgFloor/bgFloor_" + bgx + ".png";
            floorImage = ImageIO.read(new File(s));
        } catch (Exception e) {
            floorImage = ImageIO.read(new File("bgFloor/bgFloor.png"));
        }

        //читаем картинку
        ArrayList<PixelCoords> floorPoints = new ArrayList<>();
        //перебираем пиксели
        for (int x = 1; x < floorImage.getWidth(); x++) {
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
                    if (!exist) {
                        floorPoints.add(new PixelCoords(x + 2, y + 6));
                        nowPC = new PixelCoords(x + 2, y + 6);
                    }
                }
            }
        }

        // создаем список точек пола
        List<Vector2> floorPointsInWorld = new ArrayList<>();
        //добавляем первую точку от предыдущего пола
        if (firstPC != null)
            floorPointsInWorld.add(new Vector2(
                    (Camera.BG_W * bgx + Camera.BG_W * firstPC.x / floorImage.getWidth()) - Camera.BG_W,
                    Camera.BG_H * (floorImage.getHeight() - firstPC.y) / floorImage.getHeight()
            ));

        //последняя точка этого пола становится предыдущей
        if (nowPC == null)
            throw new AssertionError("nowPC must not be null");
        lastPC = new PixelCoords(nowPC.x, nowPC.y);

        // создаем точки для пола в мировых координатах
        for (PixelCoords floorPoint : floorPoints)
            floorPointsInWorld.add(new Vector2(
                    Camera.BG_W * bgx + Camera.BG_W * floorPoint.x / floorImage.getWidth(),
                    Camera.BG_H * (floorImage.getHeight() - floorPoint.y) / floorImage.getHeight()
            ));

        List<Link> links = Geometry.createLinks(floorPointsInWorld, false);

        body = new Body();
        for (Link link : links) {
            double friction = isVeryVertical(link) ? 0 : FLOOR_FRICTION; //TODO может, не делать 0
            body.addFixture(link, BodyFixture.DEFAULT_DENSITY, friction, BodyFixture.DEFAULT_RESTITUTION);
        }
        body.translate(0, 0);
        body.setMass(MassType.INFINITE);
    }

    public static boolean isVeryVertical(Link link) { //с очень вертикальных отрезков герой должен сползать
        Vector2[] vertices = link.getVertices();
        Vector2 p0 = vertices[0];
        Vector2 p1 = vertices[1];
        return Math.atan2(Math.abs(p0.y - p1.y), Math.abs(p0.x - p1.x)) > Math.PI / 6;
    }

    public void draw(Canvas canvas, int frame) {

    }

    public void drawDebug(Canvas canvas) {
        List<BodyFixture> links = body.getFixtures();
        for (BodyFixture b : links) {
            Link l = (Link) b.getShape();
            Vector2[] vertices = l.getVertices();

            for (int i = 0; i < vertices.length - 1; i++) {
                canvas.setColor(isVeryVertical(l) ? new Color(207, 188, 0) : new Color(46, 133, 24));
                canvas.drawLine(
                        vertices[i].x,
                        vertices[i].y,
                        vertices[i + 1].x,
                        vertices[i + 1].y
                );
            }
        }
    }

    public PixelCoords getLastPC(){
        return lastPC;
    }
}
