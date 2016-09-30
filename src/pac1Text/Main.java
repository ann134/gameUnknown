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

    private Body floor;
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


        /*Circle cirShape = new Circle(0.5);
        circle = new Body();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.applyForce(new Vector2(-100.0, 0.0));
        circle.setLinearDamping(0.05);

        this.world.addBody(circle);*/


        Rectangle foorShape = new Rectangle(1500, 300);
        floor = new Body();
        floor.addFixture(foorShape);
        floor.setMass(MassType.INFINITE);
        floor.translate(0.0, -700);
        this.world.addBody(floor);


        // create a circle
        Circle cirShape = new Circle(0.5);
        circle = new Body();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.translate(2.0, 2.0);
        // test adding some force
        circle.applyForce(new Vector2(-100.0, 0.0));
        // set some linear damping to simulate rolling friction
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

//        for (BodyFixture b: circle.getFixtures()) {
//            System.out.println(b);
//        }

//        Vector2 center = circle.getFixture(0);//..getShape().getCenter();
//        System.out.println(center.x + " " + center.y);


        System.out.println(circle.getFixture(0));
        System.out.println(circle.getChangeInOrientation());
        System.out.println(circle.getChangeInPosition());
        System.out.println(circle.isStatic());
        System.out.println();
        System.out.println(circle.getTransform().getTranslation().x * -SCALE);
        System.out.println(circle.getTransform().getTranslation().getXComponent());
        System.out.println();
        System.out.println(circle.getTransform().getTranslationY());
        System.out.println(circle.getTransform().getTranslation().getYComponent());
        System.out.println();
        System.out.println(floor);


        long time = System.nanoTime();
        long diff = time - last;

        last = time;
        double elapsedTime = diff / NANO_TO_BASE;

        world.update(elapsedTime);
    }
}
