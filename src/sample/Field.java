package sample;


import javafx.scene.control.Button;

public class Field {

    public int numNmines;
    private boolean bomb = false;
    private  boolean pressed = false;
    private int x;
    private int y;
    Button button;

    public boolean getBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb_) {
        bomb = bomb_;
    }

    public boolean getPressed(){ return pressed; }

    public void setPressed(boolean pressed_) {
        pressed = pressed_;
    }
    public int x() {
        return x;
    }

    public void setX(int x_) {
        x = x_;
    }

    public int y() {
        return y;
    }

    public void setY(int y_) {
        y = y_;
    }
} // Field end
