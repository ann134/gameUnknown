package pac2Graphics;


import org.dyn4j.dynamics.Body;

public abstract class GameObject {
    protected Body body;

    public abstract void draw(Canvas canvas);

    public Body getBody() {
        return body;
    }
}
