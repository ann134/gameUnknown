package pac2Graphics;

import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Vector2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;


public class HeroKeyListener implements KeyListener {

    private MyPanel2 p;
    public HeroKeyListener(MyPanel2 p) {
        this.p = p;
    }

    private boolean go = false;
    private boolean goBack = false;
    private boolean jump = false;

    private boolean stopGo = false;
    private boolean stopGoBack = false;

    private int key;

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();

        //идем вперед
        if (key == 39){
            go = true;
            goBack = false;
        }

        //идем назад
        if (key == 37){
            goBack = true;
            go = false;
        }

        // прыжок
        if (key == 32){
            jump = true;
        }

        p.setDebug(String.format("go = %b, back = %b, jump = %b, stopGo = %b, stopBack = %b", go, goBack, jump, stopGo, stopGoBack));
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {
        key = e.getKeyCode();

        //больше не идем вперед
        if (key == 39){
            go = false;
            stopGo = true;
        }

        //больше не идем назад
        if (key == 37){
            goBack = false;
            stopGoBack = true;
        }
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


    //setters
    public void setGo(boolean newGo){
        go = newGo;
    }

    public void setGoBack(boolean newGoBack){
        goBack = newGoBack;
    }

    public void setJump(boolean newJump){
        jump = newJump;
    }


    public void setStopGo(boolean newStopGo){
        stopGo = newStopGo;
    }

    public void setStopGoBack(boolean newStopGoBack){
        stopGoBack = newStopGoBack;
    }

}
