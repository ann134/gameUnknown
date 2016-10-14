package pac2Graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class HeroKeyListener implements KeyListener {

    private MyPanel2 p;
    private Hero hero;

    public HeroKeyListener(MyPanel2 p) {
        this.p = p;
        hero = p.getHero();
    }

    double y = 0;
    private int key;

    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();

        //идем вперед
        if (key == 39) {
            hero.setGo(true);
            hero.setGoBack(false);

            hero.setStopGo(false);
            hero.setStopGoBack(false);
        }

        //идем назад
        if (key == 37) {
            hero.setGoBack(true);
            hero.setGo(false);

            hero.setStopGoBack(false);
            hero.setStopGo(false);
        }

        // прыжок
        if (key == 32) {
            hero.setJump(true);
        }

        showButtonsDebug();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        key = e.getKeyCode();

        //больше не идем вперед
        if (key == 39) {
            hero.setStopGo(true);

            hero.setGo(false);
        }

        //больше не идем назад
        if (key == 37) {
            hero.setStopGoBack(true);

            hero.setGoBack(false);
        }

        //больше не прыгаем
        if (key == 32) {
            hero.setJump(false);
        }

        showButtonsDebug();
    }

    private void showButtonsDebug() {
        y = hero.getBody().getLinearVelocity().y;
        p.setDebug(String.format("go = %b, back = %b, jump = %b, stopGo = %b, stopBack = %b ", hero.getGo(), hero.getGoBack(), hero.getJump(), hero.getStopGo(), hero.getStopGoBack()) + y);
    }
}
