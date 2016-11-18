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

        //идем вперед вправо
        if (key == 39) {
            /*hero.setRight(true);
            hero.setLeft(false);*/
            hero.setGoPressed(System.nanoTime());

            hero.setGo(true);
            hero.setGoBack(false);

            hero.setStopGo(false);
            hero.setStopGoBack(false);
        }

        //идем назад влево
        if (key == 37) {
            /*hero.setLeft(true);
            hero.setRight(false);*/
            hero.setGoBackPressed(System.nanoTime());

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
        p.setDebug(String.format("go = %b, back = %b, jump = %b, right = %b, left = %b ", hero.getGo(), hero.getGoBack(), hero.getJump(), hero.getRight(), hero.getLeft()) + y);
    }
}
