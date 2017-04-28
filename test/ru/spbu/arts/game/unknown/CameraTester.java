package ru.spbu.arts.game.unknown;

import org.dyn4j.geometry.Vector2;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class CameraTester {

    @Test
    public void testCameraBgImages() throws IOException {
        Camera c = new Camera();
        c.setPosition(new Vector2(2 * Camera.BG_W + Camera.BG_W / 3, 1 + Camera.HERO_POSITION_H));

        List<BgCoords> visibleBackgrounds = c.getVisibleBackgrounds();


        for (BgCoords bg: visibleBackgrounds){
            System.out.println(bg.x + " "  + bg.y);
        }

        //проверим, что получились два результата
        Assert.assertEquals(visibleBackgrounds.size(), 2);

        BgCoords bg1 = visibleBackgrounds.get(0);
        BgCoords bg2 = visibleBackgrounds.get(1);

        //проверим, что это (1, 0) and (2, 0)
        Assert.assertTrue(bg1.y == 0 && bg2.y == 0 && (bg1.x == 1 && bg2.x == 2 || bg1.x == 2 && bg2.x == 1));
    }

    @Test
    public void testCameraBgImages4() throws IOException {
        Camera c = new Camera();
        c.setPosition(new Vector2(2 * Camera.BG_W + Camera.BG_W / 3, Camera.BG_H - 1));

        List<BgCoords> visibleBackgrounds = c.getVisibleBackgrounds();

        for (BgCoords bg: visibleBackgrounds){
            System.out.println(bg.x + " "  + bg.y);
        }

        Assert.assertEquals(visibleBackgrounds.size(), 4);


        //проверим, что это (1, 0) and (2, 0) (1, 1) (2, 1)
        BgCoords bg1 = visibleBackgrounds.get(0);
        BgCoords bg2 = visibleBackgrounds.get(1);
        BgCoords bg3 = visibleBackgrounds.get(2);
        BgCoords bg4 = visibleBackgrounds.get(3);

        Assert.assertEquals(bg1.x + bg2.x + bg3.x + bg4.x, 1 + 1 + 2 + 2);
        Assert.assertEquals(bg1.x * bg2.x * bg3.x * bg4.x, 1 * 1 * 2 * 2);
        Assert.assertEquals(bg1.y + bg2.y + bg3.y + bg4.y, 0 + 0 + 1 + 1);
        Assert.assertEquals(bg1.y * bg2.y * bg3.y * bg4.y, 0 * 0 * 1 * 1);
    }

}
