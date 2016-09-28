package pac1Text;


import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.*;

public class Main {

    public static final double SCALE = 45.0;
    public static final double NANO_TO_BASE = 1.0e9;

    private World world;
    private long last;

    private Body circle;

    public Main(){
        this.initializeWorld();
    }

    protected void initializeWorld() {
        this.world = new World();

        /*Rectangle floorRect = new Rectangle(13, 3);
        Body floor = new Body();
        floor.addFixture(new BodyFixture(floorRect));
        floor.setMass(MassType.INFINITE);

        this.world.addBody(floor);*/


        Circle cirShape = new Circle(0.5);
        circle = new Body();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.applyForce(new Vector2(-100.0, 0.0));
        circle.setLinearDamping(0.05);

        this.world.addBody(circle);

    }


    public void start() {
        this.last = System.nanoTime();

        Thread thread = new Thread() {
            public void run() {
                while (true)
                    gameLoop();
            }
        };
        thread.start();
    }


    private void gameLoop() {

        long time = System.nanoTime();
        long diff = time - last;

        last = time;
        double elapsedTime = diff / NANO_TO_BASE;



        System.out.println("elapsed: " + elapsedTime);

//        for (BodyFixture b: circle.getFixtures()) {
//            System.out.println(b);
//        }

//        Vector2 center = circle.getFixture(0);//..getShape().getCenter();
//        System.out.println(center.x + " " + center.y);


        System.out.println(circle.getFixture(0));
        System.out.println(circle);




        world.update(elapsedTime);
    }
}
