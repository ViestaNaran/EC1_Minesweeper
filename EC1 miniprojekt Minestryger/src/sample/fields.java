package sample;

import javafx.scene.control.Button;


/* Field klassen skal indeholde forskellige variabler vi skal bruge til at bestemme hvorledes felterne i vores
minestryger er bomber og om de er trykkede på. Variablerne er private så de ikke pludselig er ændret et sted i koden
og vi henter dem med get/ set metoder.
*/
public class fields {
    public int numMines;
    private int x;
    private int y;
    private boolean pressed =false;
    private boolean bomb = false;
    public boolean flag = false;
    Button button;

    // set metoder
    public void setPressed(boolean pressed_){pressed = pressed_;}
    public void setBomb(boolean bomb_){ bomb = bomb_;}
    public void setX (int x_){x = x_;}
    public void setY (int y_){ y = y_;}
    public void setFlag (boolean flag_) {flag = flag_;}



    // get metoder
    public boolean getPressed(){ return pressed; }
    public boolean getBomb() {return bomb;}
    public boolean getFlag() {return flag;}
    public int getX (){return x;}
    public int getY (){return y;}


} // Class end
