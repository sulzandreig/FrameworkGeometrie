package labgeo1;

import java.awt.Graphics2D;

/**
 *
 * @author Dragos-Alexandru
 */
public interface Drawable {

    /**
     * Afiseaza figura geometrica ce implementeaza aceasta interfata<br>
     * <p><b style="color:red">!!Warning!! se  va implementa:</b><br> -schimbarea culori lui graphics<br>
     *                                 -scrierea numelui daca este necesara (drawName)<br>
     *                                 -afisarea obiectului relativ la centrulX si centrulY al axei si cu zoom-ul perceput de functie<br></p>
     * @param graphics
     * @param centrulX
     * @param centrulY
     * @param zoom
     * @param drawName
     */
    public void draw(Graphics2D graphics,int centrulX, int centrulY, int zoom, boolean drawName);
}
