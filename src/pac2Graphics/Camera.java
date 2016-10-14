package pac2Graphics;


import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Camera {
    private Vector2 position = new Vector2(0, 0);


    /*private BufferedImage white;
    private BufferedImage white;
    private BufferedImage white;
    private BufferedImage white;*/

    private BufferedImage white;
    public final static BufferedImage NOTHING_IMG = new BufferedImage(1920, 1080, BufferedImage.TYPE_BYTE_BINARY);

    public final static double SCREEN_W = 35;
    public final static double SCREEN_H = 35.0 * 1080 / 1920; //примерно 20;

    public final static int BG_W = 35;
    public final static int BG_H = 25;

    public final static int HERO_POSITION_H = 5; //от низа экрана до героя

    public Camera() throws IOException {
        white = ImageIO.read(new File("white.jpg"));
    }

    public List<BgCoords> getVisibleBackgrounds() {
        List<BgCoords> list = new ArrayList<>();

        Vector2 leftTop = new Vector2(position.x - SCREEN_W / 2, position.y - HERO_POSITION_H + SCREEN_H);

        int x1 = (int) Math.floor(leftTop.x / BG_W);
        int y1 = (int) Math.floor(leftTop.y / BG_H);

        int x2 = (int) Math.floor((leftTop.x + SCREEN_W) / BG_W);
        int y2 = (int) Math.floor((leftTop.y - SCREEN_H) / BG_H);

        for (int x = x1; x <= x2; x++)
            for (int y = y2; y <= y1; y++)
                list.add(new BgCoords(x, y));

        return list;
    }

    public BufferedImage getBgImage(BgCoords bgC) {
        try {
            String s = "bg_" + bgC.x + "_" + bgC.y + ".jpg";
            return ImageIO.read(new File(s));
        } catch (Exception e) {
            try {
                return white;
            } catch (Exception e2) {
                return NOTHING_IMG;
            }
        }
    }

    public void drawBackground(Canvas canvas) {
        List<BgCoords> lbg = getVisibleBackgrounds();
        for (BgCoords bg : lbg)
            canvas.drawImage(getBgImage(bg), bg.x * BG_W, (bg.y + 1) * BG_H, BG_W, BG_H);
    }


    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }
}
