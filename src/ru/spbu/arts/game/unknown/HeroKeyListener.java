package ru.spbu.arts.game.unknown;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class HeroKeyListener implements KeyListener {

    private GamePanel p;

    public HeroKeyListener(GamePanel p) {
        this.p = p;
    }

    private int key;

    public void keyPressed(KeyEvent e) {
        Hero hero = p.getHero();

        key = e.getKeyCode();

        //идем вперед
        if (key == 39 && !hero.getGo()) {
            hero.setMovementStart(System.nanoTime());

            hero.setGo(true);
            hero.setGoBack(false);

            hero.setStopGo(false);
            hero.setStopGoBack(false);
        }

        //идем назад
        if (key == 37 && !hero.getGoBack()) {
            hero.setMovementStart(System.nanoTime());

            hero.setGoBack(true);
            hero.setGo(false);

            hero.setStopGoBack(false);
            hero.setStopGo(false);
        }

        // прыжок
        if (key == 32 && !hero.getJump() || key == 38 && !hero.getJump()) {
            hero.setJump(true);
        }

        // взять
        if (key == 16 && !hero.getCarried()) {
            hero.setCarried(true);
        }

        //задуть фонарь
        if (key == 17 && hero.getCarried() && hero.getCarriedObject() instanceof Light) {
            if (p.getLight().alive()){
                /*p.getLight().setMovementStart(System.nanoTime());
                p.getBranches().setMovementStart(System.nanoTime());*/
                //p.getBeast().setMovementStart(System.nanoTime());
                p.getLight().startDeath();
                p.getLight().kill();
                p.getBranches().startDeath();
                p.getBeast().startDeath();
            }
        }

        showButtonsDebug();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        Hero hero = p.getHero();

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
        if (key == 32 || key == 38) {
            hero.setJump(false);
        }


        // отпустить
        if (key == 16) {
            hero.setCarried(false);
        }

        showButtonsDebug();
    }

    private void showButtonsDebug() {
        /*y = p.getHero().getBody().getLinearVelocity().y;
        p.setDebug(String.format("go = %b, back = %b, jump = %b, right = %b, left = %b ", hero.getGo(), hero.getGoBack(), hero.getJump()*//*, hero.getRight(), hero.getLeft()) + y*//*));*/
    }
}
