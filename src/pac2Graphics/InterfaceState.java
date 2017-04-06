package pac2Graphics;

public class InterfaceState {

    private boolean go = false;
    private boolean goBack = false;
    private boolean jump = false;

    private boolean stopGo = false;
    private boolean stopGoBack = false;

    private boolean right = false;
    private boolean left = true;

    private boolean carried = false;

    private long goPressed = 0;

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

    public void setGoPressed(long goPressed) {
        this.goPressed = goPressed;
    }

    public boolean isGo() {
        return go;
    }

    public boolean isGoBack() {
        return goBack;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isStopGo() {
        return stopGo;
    }

    public boolean isStopGoBack() {
        return stopGoBack;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isCarried() {
        return carried;
    }

    public long getGoPressed() {
        return goPressed;
    }
}
