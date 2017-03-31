package pac2Graphics;

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
import java.util.ArrayList;
import java.util.List;

public class Hero extends GameObject {

    //TODO подумать, при каких условиях герой может прыгать. Как вариант: перебрать тела, которых он касается, определить, есть хотя бы одно касание в области ног
    //TODO анимировать героя. Отдельно верх, отдельно низ

    /*private BufferedImage[] wirts = new BufferedImage[10];
    private BufferedImage[] wirtsReturn = new BufferedImage[10];*/


    private BufferedImage[] wirtsStay = new BufferedImage[5];
    private BufferedImage[] wirtsStayReturn = new BufferedImage[5];

    private BufferedImage[] wirtsRun = new BufferedImage[8];
    private BufferedImage[] wirtsRunReturn = new BufferedImage[8];



    public static final double HERO_FRICTION = 10 * BodyFixture.DEFAULT_FRICTION;
    public final static double W = 1;
    public final static double H = 2.5;

    private List<GameObject> allGameObjects;
    private World world;

    private boolean carried = false;
    private GameObject carriedObject = null;
    private WeldJoint joint = null;

    //нужен метод setCarriedObject 1) отвязывает старый объект, если он был 2) привязывает новый. И нужен метод removeCarriedObject - отвязывеает объект
    //setCarriedObject и removeCarriedObject вызываются при нажатии на кнопку взятия объектка.

    //TODO these fields are set in the User Interface (UI) thread, and are read in the Game Loop thread
    private boolean go = false;
    private boolean goBack = false;
    private boolean jump = false;

    private boolean stopGo = false;
    private boolean stopGoBack = false;
    
    private boolean right = false;
    private boolean left = true;

    //TODO these fields are set in the Game Loop thread, and are read in the UI thread
    private boolean isHeroContactSomething = false;

    //private boolean stopJump = false;
    //private boolean move = false;

    //private long goPressed;

    private long wirtReverse;

    public Hero(World world, AllWorldGameObjects allObjects) throws IOException {
        /*for (int i = 0; i < wirts.length; i++) {
            String s = "hero/wirt" + "0" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirts[i] = b;
        }
        for (int i = 0; i < wirtsReturn.length; i++) {
            String s = "hero/wirt" + "1" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsReturn[i] = b;
        }*/


        for (int i = 1; i < wirtsStay.length + 1; i++) {
            String s = "hero/wirtStay" + "0" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsStay[i - 1] = b;
        }
        for (int i = 1; i < wirtsStayReturn.length + 1; i++) {
            String s = "hero/wirtStay" + "1" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsStayReturn[i - 1] = b;
        }
        for (int i = 1; i < wirtsRun.length + 1; i++) {
            String s = "hero/wirtRun" + "0" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsRun[i - 1] = b;
        }
        for (int i = 1; i < wirtsRunReturn.length + 1; i++) {
            String s = "hero/wirtRun" + "1" + i + ".png";
            BufferedImage b = ImageIO.read(new File(s));
            wirtsRunReturn[i - 1] = b;
        }



        this.world = world;
        allGameObjects = new ArrayList<>(allObjects.getList());


        Rectangle heroShape = new Rectangle(W, H);
        body = new Body();

        //hero.addFixture(heroShape, плотность, трение, ??прыгучесть);
        body.addFixture(heroShape, BodyFixture.DEFAULT_DENSITY, HERO_FRICTION, BodyFixture.DEFAULT_RESTITUTION);

        body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
    }

    public void move() {
        //TODO продолжать оптимизировать движение героя
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

            //TODO refactor, move test to game objects
            for (GameObject gameObject : allGameObjects) {
                if (bodyToTest == gameObject.getBody()) {
                    if (gameObject instanceof Floor) {
                        if (!Floor.isVeryVertical((Link) fixtureToTest.getShape()))
                            return true;
                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    //TODO почему, если держать шарик под собой, то герой не может подпрыгнуть
    //TODO можно ли как-то при взятии объекта переносить его в область рук
    //TODO move this inside Hero (make Hero link to world, пусть, например, все GameObject имеют ссылку на world)
    public void processCarryButtonPress() {
        if (getCarried()) {
            if (carriedObject == null) {
                carriedObject = getHeroContactSomethingTakeable();
            }


            if (carriedObject != null) {
                carriedObject.body.setTransform(new Transform());

                if (right){

                    Vector2 carriedPoint = body.getWorldPoint(new Vector2(W/2, 0.1));
                    carriedObject.body.translate(new Vector2(carriedPoint.x - carriedObject.getCarriedPoint().x, carriedPoint.y - carriedObject.getCarriedPoint().y));
                } else {

                    Vector2 carriedPoint = body.getWorldPoint(new Vector2(-W/2, 0.1));
                    carriedObject.body.translate(new Vector2(carriedPoint.x + carriedObject.getCarriedPoint().x, carriedPoint.y - carriedObject.getCarriedPoint().y));
                }

                if (joint == null) {
                    joint = new WeldJoint(body, carriedObject.body, new Vector2(0, 0));
                    world.addJoint(joint);
                }
            }

            /*if (getCarriedObject() == null) {
                GameObject obj = getHeroContactSomethingTakeable();
                setCarriedObject(obj);
//                obj.body.setTransform(new Transform()); //убираем все преобразования объекта, он поворачивается прямо и переходит в 0,0
//                точка держания = hero.body.getWorldPoint(*//*сюда ставим точку держания в локальных координатах героя*//*)
//                obj.body.translate(точка держания минус точка держания колобка);
            }

            //TODO вот это действие очень логично для setCarriedObject.
            if (getCarriedObject() != null) {
                if (getJoint() == null)
                    world.addJoint(createJoint());
            }*/

        } else {
            if (joint != null) {
                world.removeJoint(joint);
                joint = null;
                carriedObject = null;
            }
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

            for (GameObject gameObject : allGameObjects) {
                if (bodyToTest == gameObject.getBody()) {
                    if (gameObject.isTakeable())
                        return gameObject;
                }
            }
        }

        return null;
    }




    //interface thread
    public void draw(Canvas canvas, int frame) {
        
        //вибираем картинку в зависимости от номера кадра
        //добавится проверка на скольжение и рандомное моргание 
        
        int frameLeftPressed = Timer.getFrameFrom(wirtReverse);

        int nowFrame = frameLeftPressed % 10;
        BufferedImage wirt;

        if ((go || goBack) && isHeroContactSomething) {
            if (right)
                wirt = wirtsRun[nowFrame - 1];
            else //if (left)
                wirt = wirtsRunReturn[nowFrame - 1];
        } else {
            if (right)
                wirt = wirtsStay[nowFrame - 1];
            else //if (left)
                wirt = wirtsStayReturn[nowFrame - 1];
        }


        canvas.drawImage(wirt, -W / 2, H / 2, W, H);
    }

    //interface thread
    public void drawDebug(Canvas canvas) {
        canvas.setColor(new Color(46, 133, 24));

        canvas.drawLine(-W / 2, H / 2, W / 2, H / 2);
        canvas.drawLine(-W / 2, -H / 2, W / 2, -H / 2);

        canvas.drawLine(-W / 2, H / 2, -W / 2, -H / 2);
        canvas.drawLine(W / 2, H / 2, W / 2, -H / 2);
    }

    //getters
    /*public boolean getGo() {
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

    public long getWirtReverse() {
        return wirtReverse;
    }*/


    public boolean getCarried() {
        return carried;
    }

    public GameObject getCarriedObject() {
        return carriedObject;
    }

    public WeldJoint getJoint(){
        return joint;
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

    public void setCarried(boolean newCarried){
        carried = newCarried;
    }

    /*public void setRight(boolean right) {
        right = right;
    }

    public void setLeft(boolean left) {
        left = left;
    }

    public void setMove(boolean right) {
        right = right;
    }*/

    public void setGoPressed(long goPressed) {
        goPressed = goPressed;
    }

    public void setWirtReverse(long wirtReverse) {
        this.wirtReverse = wirtReverse;
    }





}
