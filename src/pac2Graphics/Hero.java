package pac2Graphics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Hero extends GameObject {

    private BufferedImage wirt;

    private Floor floor;

    private boolean go = false;
    private boolean goBack = false;
    private boolean jump = false;

    private boolean stopGo = false;
    private boolean stopGoBack = false;
    //private boolean stopJump = false;


    public Hero(Floor floor) throws IOException {
        wirt = ImageIO.read(new File("wirt.png"));
        this.floor = floor;

        Rectangle heroShape = new Rectangle(1, 2);
        body = new Body();
        //hero.addFixture(heroShape, плотность, трение, ??прыгучесть);
        body.addFixture(heroShape, BodyFixture.DEFAULT_DENSITY, 10, BodyFixture.DEFAULT_RESTITUTION);
        //hero.applyImpulse(new Vector2(100, 100));
        body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        body.translate(2, 3);
    }


    public void draw(Canvas canvas) {
        double w = ((Rectangle) body.getFixture(0).getShape()).getWidth();
        double h = ((Rectangle) body.getFixture(0).getShape()).getHeight();

        canvas.drawImage(wirt, -w / 2 , h / 2, w , h);
    }

    public void move() {
        Vector2 linearVelocity = body.getLinearVelocity();

        //идем вперед обратно
        if (go) {
            /*if (-3 < linearVelocity.x) {
                if (linearVelocity.x == 0)
                    hero.applyImpulse(new Vector2(-3, 0));
                hero.applyImpulse(new Vector2(-2, 0));
            }*/

            if (linearVelocity.x < 3) {
                if (linearVelocity.x == 0)
                    body.applyImpulse(new Vector2(3, 0));
                body.applyImpulse(new Vector2(2, 0));
            }
        }

        //идем назад обратно
        if (goBack) {
            /*if (linearVelocity.x < 3) {
                if (linearVelocity.x == 0)
                    hero.applyImpulse(new Vector2(3, 0));
                hero.applyImpulse(new Vector2(2, 0));
            }*/

            if (-3 < linearVelocity.x) {
                if (linearVelocity.x == 0)
                    body.applyImpulse(new Vector2(-3, 0));
                body.applyImpulse(new Vector2(-2, 0));
            }
        }

        //прыжок
        if (jump) {
            if (linearVelocity.y < 2)
                if (body.isInContact(floor.getBody())) {
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

        /*тормозим прыжок
        if (heroKeyListener.getStopJump()) {
            //hero.setLinearVelocity(linearVelocity.x, 0);
            //heroKeyListener.setJump(false);
        }*/
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

    /*public boolean getStopJump() {
        return stopJump;
    }*/

    //setters
    public void setGo(boolean newGo){
        go = newGo;
        /*showButtonsDebug();*/
    }

    public void setGoBack(boolean newGoBack){
        goBack = newGoBack;
        /*showButtonsDebug();*/
    }

    public void setJump(boolean newJump){
        jump = newJump;
        /*showButtonsDebug();*/
    }

    public void setStopGo(boolean newStopGo){
        stopGo = newStopGo;
        /*showButtonsDebug();*/
    }

    public void setStopGoBack(boolean newStopGoBack){
        stopGoBack = newStopGoBack;
        /*showButtonsDebug();*/
    }
}
