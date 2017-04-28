package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Hero extends GameObject {

    public static final double HERO_FRICTION = 10 * BodyFixture.DEFAULT_FRICTION;
    public final static double W = 1;
    public final static double H = 2.5;

    private BufferedImage[] wirtsStay = new BufferedImage[8];
    private BufferedImage[] wirtsStayReturn = new BufferedImage[8];

    private BufferedImage[] wirtsRun = new BufferedImage[8];
    private BufferedImage[] wirtsRunReturn = new BufferedImage[8];

    private BufferedImage[] wirtsJump = new BufferedImage[8];
    private BufferedImage[] wirtsJumpReturn = new BufferedImage[8];

    private BufferedImage[] wirtsDead = new BufferedImage[1];
    private BufferedImage[] wirtsDeadReturn = new BufferedImage[1];

    private BufferedImage[] wirtsHand = new BufferedImage[2];

    private AllWorldGameObjects allGameObjects;
    private World world;

    private long movementStart;
    private boolean dead = false;

    //TODO these fields are set in the Game Loop thread, and are read in the UI thread
    private boolean isHeroContactSomething = false;

    private GameObject carriedObject = null;
    private WeldJoint joint = null;
    private boolean carriedObjectIsToTheRight = false;


    //interface state
    private boolean carried = false;

    private boolean right = true;

    private boolean go = false;
    private boolean goBack = false;
    private boolean jump = false;

    private boolean stopGo = false;
    private boolean stopGoBack = false;


    public Hero(World world, AllWorldGameObjects allObjects) throws IOException {

        for (int i = 1; i < wirtsStay.length + 1; i++) {
            String s = "images/hero/wirtStay" + "0" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsStay[i - 1] = b;
        }
        for (int i = 1; i < wirtsStayReturn.length + 1; i++) {
            String s = "images/hero/wirtStay" + "1" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsStayReturn[i - 1] = b;
        }
        for (int i = 1; i < wirtsRun.length + 1; i++) {
            String s = "images/hero/wirtRun" + "0" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsRun[i - 1] = b;
        }
        for (int i = 1; i < wirtsRunReturn.length + 1; i++) {
            String s = "images/hero/wirtRun" + "1" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsRunReturn[i - 1] = b;
        }
        for (int i = 1; i < wirtsJump.length + 1; i++) {
            String s = "images/hero/wirtJump" + "0" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsJump[i - 1] = b;
        }
        for (int i = 1; i < wirtsJumpReturn.length + 1; i++) {
            String s = "images/hero/wirtJump" + "1" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsJumpReturn[i - 1] = b;
        }
        for (int i = 1; i < wirtsDead.length + 1; i++) {
            String s = "images/hero/wirtDead" + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsDead[i - 1] = b;
        }
        for (int i = 1; i < wirtsDeadReturn.length + 1; i++) {
            String s = "images/hero/wirtDeadReturn" + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsDeadReturn[i - 1] = b;
        }

        for (int i = 1; i < wirtsHand.length + 1; i++) {
            String s = "images/hero/hand" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsHand[i - 1] = b;
        }

        this.world = world;
        this.allGameObjects = allObjects;


        Rectangle heroShape = new Rectangle(W, H);
        body = new Body();

        //hero.addFixture(heroShape, плотность, трение, ??прыгучесть);
        body.addFixture(heroShape, BodyFixture.DEFAULT_DENSITY, HERO_FRICTION, BodyFixture.DEFAULT_RESTITUTION);

        body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
    }

    //TODO продолжать оптимизировать движение героя


    public void move() {
        Vector2 linearVelocity = body.getLinearVelocity();

        //идем вперед
        if (go) {
            right = true;
            if (linearVelocity.x < 4) {  // тут было 3
                if (isHeroContactSomething())
                    body.applyImpulse(new Vector2(2, 0));
            }
            if (carriedObject != null && !carriedObjectIsToTheRight)
                positionCarriedObject();
        }

        //идем назад
        if (goBack) {
            right = false;
            if (-4 < linearVelocity.x) {  // тут было -3
                if (isHeroContactSomething())
                    body.applyImpulse(new Vector2(-2, 0));
            }
            if (carriedObject != null && carriedObjectIsToTheRight)
                positionCarriedObject();
        }

        //прыжок
        if (jump) {
            if (linearVelocity.y < 2)
                if (isHeroContactSomething()) {
                    body.applyImpulse(new Vector2(0, 20)); // y был 15
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

        isHeroContactSomething = isHeroContactSomething();
    }

    private boolean isHeroContactSomething() {
        Body heroBody = body;

        List<ContactPoint> contacts = heroBody.getContacts(false); //TODO убрать java.lang.IndexOutOfBoundsException
        for (ContactPoint contact : contacts) {
            Vector2 worldContactPoint = contact.getPoint();
            Vector2 heroContactPoint = heroBody.getLocalPoint(worldContactPoint);
            if (heroContactPoint.y > -1)
                continue;

            Body bodyToTest;
            BodyFixture fixtureToTest;
            if (contact.getBody1() == heroBody) {
                bodyToTest = contact.getBody2();
                fixtureToTest = contact.getFixture2();
            } else {
                bodyToTest = contact.getBody1();
                fixtureToTest = contact.getFixture1();
            }

            if (allGameObjects.getObjectByBody(bodyToTest) instanceof Floor){
                if (!Floor.isVeryVertical((Link) fixtureToTest.getShape()))
                    return true;
            } else {
                return true;
            }
        }

        return false;
    }

    public void processCarryButtonPress() {
        if (getCarried()) {
            if (carriedObject == null) {
                carriedObject = getHeroContactSomethingTakeable();
                positionCarriedObject();
            }
        } else {
            if (joint != null) {
                world.removeJoint(joint);
                joint = null;
                carriedObject = null;
            }
        }
    }

    private void positionCarriedObject() {
        if (carriedObject != null) {
            carriedObject.body.setTransform(new Transform());

            Vector2 heroCarryPoint;
            Vector2 objectCarryPoint;

            if (right){
                carriedObjectIsToTheRight = true;
                heroCarryPoint = body.getWorldPoint(new Vector2(W/2 , 0.1));
                objectCarryPoint = carriedObject.getCarriedLeftPoint();//new Vector2(-1.25/4 , 0.5);
            } else {
                carriedObjectIsToTheRight = false;
                heroCarryPoint = body.getWorldPoint(new Vector2(-W/2, 0.1));
                objectCarryPoint = carriedObject.getCarriedRightPoint();//new Vector2(1.25/4 , 0.5);
            }

            if (joint != null)
                world.removeJoint(joint);

            carriedObject.body.translate(new Vector2(heroCarryPoint.x - objectCarryPoint.x, heroCarryPoint.y - objectCarryPoint.y));

            joint = new WeldJoint(body, carriedObject.body, heroCarryPoint);
            world.addJoint(joint);
        }
    }

    private GameObject getHeroContactSomethingTakeable() {
        Body heroBody = body;

        List<ContactPoint> contacts = heroBody.getContacts(false); //TODO убрать java.lang.IndexOutOfBoundsException
        for (ContactPoint contact : contacts) {

            Body bodyToTest;
            if (contact.getBody1() == heroBody)
                bodyToTest = contact.getBody2();
            else
                bodyToTest = contact.getBody1();

            if (allGameObjects.getObjectByBody(bodyToTest).isTakeable())
                return allGameObjects.getObjectByBody(bodyToTest);

        }

        return null;
    }


    //interface thread
    public void draw(ru.spbu.arts.game.unknown.Canvas canvas, int frameWhy) {

        if (carriedObject != null){
            if (right){
                canvas.drawImage(wirtsHand[1], W / 2 - 0.45, 0.25, 0.7, 0.35);
            } else {
                canvas.drawImage(wirtsHand[0], -W / 2 - 0.2, 0.25, 0.7, 0.35);
            }
        }

        int frame = Timer.getFrameFrom(movementStart); // вычисляем какой кадр с начала движения (глобальный кадр?)

        BufferedImage[] wirtsArray = getWirtsArray(); //вибираем нужный массив
        BufferedImage wirt = wirtsArray[frame % wirtsArray.length]; // делим на длинну массива, т е на число отрисованных повторяющихся кадров (локальный кадр?)

        canvas.drawImage(wirt, -W / 2, H / 2, W, H);
    }

    private BufferedImage[] getWirtsArray(){
        if (dead) {
            if (right)
                return wirtsDead;
            else //if (left)
                return wirtsDeadReturn;
        }

        if ((go || goBack) && isHeroContactSomething) {
            if (right)
                return wirtsRun;
            else //if (left)
                return wirtsRunReturn;
        } else {
            if (!isHeroContactSomething){
                if (right)
                    return wirtsJump;
                else //if (left)
                    return wirtsJumpReturn;
            } else {
                if (right)
                    return wirtsStay;
                else //if (left)
                    return wirtsStayReturn;
            }
        }
    }


    //interface thread
    public void drawDebug(ru.spbu.arts.game.unknown.Canvas canvas) {
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

    public boolean getCarried() {
        return carried;
    }

    public long getMovementStart() {
        return movementStart;
    }

    public boolean alive() {
        return !dead;
    }

    public GameObject getCarriedObject() {
        return carriedObject;
    }

    //setters
    public void setMovementStart(long movementStart) {
        this.movementStart = movementStart;
    }

    public void kill(boolean death){
        this.dead = death;
    }

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

    public void setCarried(boolean newCarried){
        carried = newCarried;
    }
}
