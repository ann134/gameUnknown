package pac2Graphics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Link;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Hero extends GameObject {

    //TODO подумать, при каких условиях герой может прыгать. Как вариант: перебрать тела, которых он касается, определить, есть хотя бы одно касание в области ног
    //TODO анимировать героя. Отдельно верх, отдельно низ

    private BufferedImage[] wirts = new BufferedImage[10];
    private BufferedImage[] wirtsReturn = new BufferedImage[10];

    public static final double HERO_FRICTION = 6 * BodyFixture.DEFAULT_FRICTION;
    public final static double W = 1;
    public final static double H = 2.5;

    private List<Floor> floors;
    private Tree tree;
    private Kolobok kolobok;

    private boolean go = false;
    private boolean goBack = false;
    private boolean jump = false;

    private boolean stopGo = false;
    private boolean stopGoBack = false;
    //private boolean stopJump = false;

    private boolean right = false;
    private boolean left = true;
    private boolean move = false;

    private long goPressed;
    private long goBackPressed;

    public Hero(List<Floor> floors, Tree tree, Kolobok kolobok) throws IOException {
        for (int i = 0; i < wirts.length; i++) {
            String s = "hero/wirt" + "0" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirts[i] = b;
        }
        for (int i = 0; i < wirtsReturn.length; i++) {
            String s = "hero/wirt" + "1" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsReturn[i] = b;
        }

        this.floors = floors;
        this.tree = tree;
        this.kolobok = kolobok;

        Rectangle heroShape = new Rectangle(W, H);
        body = new Body();

        //hero.addFixture(heroShape, плотность, трение, ??прыгучесть);
        body.addFixture(heroShape, BodyFixture.DEFAULT_DENSITY, HERO_FRICTION, BodyFixture.DEFAULT_RESTITUTION);

        body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        body.translate(20, 5);
    }

    public void move() {
        //TODO не зависать на отвесных склонах, если нажато движение (вперед, назад)
        Vector2 linearVelocity = body.getLinearVelocity();

        //идем вперед
        if (go) {
            right = true;
            left = false;
            if (linearVelocity.x < 3) {
                if (isHeroContactSomething())
                    body.applyImpulse(new Vector2(2, 0));
            }
        }

        //идем назад
        if (goBack) {
            right = false;
            left = true;
            if (-3 < linearVelocity.x) {
                if (isHeroContactSomething())
                    body.applyImpulse(new Vector2(-2, 0));
            }
        }

        //прыжок
        if (jump) {
            if (linearVelocity.y < 2)
                if (isHeroContactSomething()) {
                    body.applyImpulse(new Vector2(0, 15));
                }
        }

        //тормозим когда идем вперед
        if (stopGo) {
            if (linearVelocity.x < 0)
                body.setLinearVelocity(0, linearVelocity.y);
        }

        //тормозим когда идем назад
        if (stopGoBack) {
            if (0 < linearVelocity.x)
                body.setLinearVelocity(0, linearVelocity.y);
        }
    }

    private boolean isHeroContactSomething() {

        List<ContactPoint> contacts = body.getContacts(false);
        for (ContactPoint contact : contacts) {
            Vector2 worldContactPoint = contact.getPoint();
            Vector2 heroContactPoint = body.getLocalPoint(worldContactPoint);
            if (heroContactPoint.y > -1)
                continue;

            Body bodyToTest;
            BodyFixture fixtureToTest;
            if (contact.getBody1() == body) {
                bodyToTest = contact.getBody2();
                fixtureToTest = contact.getFixture2();
            } else {
                bodyToTest = contact.getBody1();
                fixtureToTest = contact.getFixture1();
            }

            if (bodyToTest == tree.getBody())
                return true;

            if (bodyToTest == kolobok.getBody())
                return true;

            for (Floor floor : floors) {
                if (bodyToTest == floor.getBody()) {
                    if (!Floor.isVeryVertical((Link) fixtureToTest.getShape()))
                        return true;
                }
            }
        }

        return false;
    }



    public void draw(Canvas canvas, int frame) {
        /*(frame - frameWhenLeftPressed) % 10 //0 .. 9*/
        /*long now = System.nanoTime();
        int frameLeftPressed = (int) ((now - goBackPressed) / MyPanel2.NANO_TO_BASE * 10);
        */

        int frameLeftPressed = Timer.getFrameFrom(goBackPressed);

        int nowFrame = frameLeftPressed % 10;
        BufferedImage wirt;

        if ((go || goBack) && isHeroContactSomething()) {
            if (right)
                wirt = wirts[nowFrame];
            else //if (left)
                wirt = wirtsReturn[nowFrame];
        } else {
            if (right)
                wirt = wirts[0];
            else //if (left)
                wirt = wirtsReturn[0];
        }

        canvas.drawImage(wirt, -W / 2, H / 2, W, H);
    }

    public void drawDebug(Canvas canvas) {
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-W / 2, H / 2, W / 2, H / 2);
        canvas.drawLine(-W / 2, -H / 2, W / 2, -H / 2);

        canvas.drawLine(-W / 2, H / 2, -W / 2, -H / 2);
        canvas.drawLine(W / 2, H / 2, W / 2, -H / 2);
    }



    //getters
    public boolean getGo() {
        return go;
    }

    public boolean getGoBack() {
        return goBack;
    }

    public boolean getJump() {
        return jump;
    }

    public boolean getStopGo() {
        return stopGo;
    }

    public boolean getStopGoBack() {
        return stopGoBack;
    }

    public boolean getRight() {
        return right;
    }

    public boolean getLeft() {
        return left;
    }

    public long getGoPressed() {
        return goPressed;
    }

    public long getGoBackPressed() {
        return goBackPressed;
    }




    //setters
    public void setGo(boolean newGo) {
        go = newGo;
    }

    public void setGoBack(boolean newGoBack) {
        goBack = newGoBack;
    }

    public void setJump(boolean newJump) {
        jump = newJump;
    }

    public void setStopGo(boolean newStopGo) {
        stopGo = newStopGo;
    }

    public void setStopGoBack(boolean newStopGoBack) {
        stopGoBack = newStopGoBack;
    }

    public void setRight(boolean right) {
        right = right;
    }

    public void setLeft(boolean left) {
        left = left;
    }

    public void setMove(boolean right) {
        right = right;
    }

    public void setGoPressed(long goPressed) {
        goPressed = goPressed;
    }

    public void setGoBackPressed(long goBackPressed) {
        goBackPressed = goBackPressed;
    }

}
