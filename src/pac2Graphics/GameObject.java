package pac2Graphics;


import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

public abstract class GameObject {
    protected Body body;

    protected boolean takeable = false;

    //TODO add field можно или нельзя взять объект

    public abstract void draw(Canvas canvas, int frame);

    public void drawDebug(Canvas canvas) {
        //get body, draw all fixtures inside
    }

    public Body getBody() {
        return body;
    }

    public boolean isTakeable() {
        return takeable;
    }

    public Vector2 getCarriedPoint(){
        return new Vector2(0, 0);
    }
}
