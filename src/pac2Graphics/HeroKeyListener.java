package pac2Graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


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
    //private boolean stopJump = false;

    double y = 0;

    private int key;

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();

        //идем вперед
        if (key == 39){
            go = true;
            goBack = false;

            stopGo = false;
            stopGoBack = false;
        }

        //идем назад
        if (key == 37){
            goBack = true;
            go = false;

            stopGoBack = false;
            stopGo = false;
        }

        // прыжок
        if (key == 32){
            jump = true;

            //stopJump = false;
        }

        showButtonsDebug();
    }



    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        key = e.getKeyCode();

        //больше не идем вперед
        if (key == 39){
            stopGo = true;

            go = false;
        }

        //больше не идем назад
        if (key == 37){
            stopGoBack = true;

            goBack = false;
        }

        //больше не прыгаем
        if (key == 32){
            //stopJump = true;

            jump = false;
        }

        showButtonsDebug();
    }

    private void showButtonsDebug() {
        y = p.getHero().getLinearVelocity().y;
        p.setDebug(String.format("go = %b, back = %b, jump = %b, stopGo = %b, stopBack = %b ", go, goBack, jump, stopGo, stopGoBack) + y);
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
    /*public void setGo(boolean newGo){
        go = newGo;
        showButtonsDebug();
    }

    public void setGoBack(boolean newGoBack){
        goBack = newGoBack;
        showButtonsDebug();
    }

    public void setJump(boolean newJump){
        jump = newJump;
        showButtonsDebug();
    }

    public void setStopGo(boolean newStopGo){
        stopGo = newStopGo;
        showButtonsDebug();
    }

    public void setStopGoBack(boolean newStopGoBack){
        stopGoBack = newStopGoBack;
        showButtonsDebug();
    }*/

}
