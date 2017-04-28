package ru.spbu.arts.game.unknown;


import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Camera {

    private Vector2 position = new Vector2(0, 0);

    private BufferedImage black;
    public final static BufferedImage NOTHING_IMG = new BufferedImage
            (1920, 1080, BufferedImage.TYPE_BYTE_BINARY);

    private Map<BgCoords, BufferedImage> bgImagesCache = new HashMap<>();


    public final static double SCREEN_W = 35;
    public final static double SCREEN_H = 18;

    public final static double BG_W = 35;
    public final static double BG_H = 35.0 * 1080 / 1920; //примерно 20;

    public final static int HERO_POSITION_H = 5; //от низа экрана до героя

    public Camera() throws IOException {
        black = ImageIO.read(new File("images/bg/black.jpg"));
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

    private BufferedImage getBgImage(BgCoords bgC) {
        if (bgImagesCache.containsKey(bgC)) {
            return bgImagesCache.get(bgC);
        } else {
            bgImagesCache.put(bgC, loadBgImage(bgC));

            if (bgImagesCache.size() > 10) {
                Map<BgCoords,BufferedImage> newCash = new HashMap<>();
                for (BgCoords coords : bgImagesCache.keySet()) {
                    if (Math.abs(bgC.x - coords.x) < 4)
                        newCash.put(coords, bgImagesCache.get(coords));
                }
                bgImagesCache = newCash;
            }

            return bgImagesCache.get(bgC);
        }
    }

    private BufferedImage loadBgImage(BgCoords bgC) {
        try {
            String s = "images/bg/bg_" + bgC.x + "_" + bgC.y + ".jpg";
            return ImageIO.read(new File(s));
        } catch (Exception e) {
            try {
                return black;
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

    public void move(Hero hero, double elapsedTime){
        Vector2 camPos = getPosition();
        Vector2 heroPos = hero.getBody().getTransform().getTranslation();

        double dx = heroPos.x - camPos.x;
        double dy = heroPos.y - camPos.y;
        double d = Math.sqrt(dx * dx + dy * dy); // расстояние между героем и камерой

        double speed =  0.5 + d * 5; //чем больше 5, тем быстрее камера долетает до героя
        double dd = speed * elapsedTime; // на сколько уменьшаем

        if (Math.abs(d) < 1e-8 || d <= dd)
            setPosition(heroPos);
        else {
            double k = dd / d;
            setPosition(new Vector2(camPos.x + k * dx, camPos.y + k * dy));
        }
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }
}
