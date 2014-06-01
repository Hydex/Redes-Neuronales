/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contra2;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class Fondo extends Actor {

    private int x;
    private int y;
    private BufferedImage auxFondo;///permite el desplazmiento del fondo
    private int velocidadFondo = 0; //velocidad del desplazamiento del fondo

    public Fondo(Escenario escenario) {
        super(escenario);
        setNombreImagen(new String[]{"fondo.jpg"});
    }

    public void pintar(Graphics2D g) {
        super.pintar(g);

        auxFondo = cargaDeImagen.getImagenes("fondo.jpg");
        g.setPaint(new TexturePaint(auxFondo, new Rectangle(getVelocidadFondo(), 0, auxFondo.getWidth(), auxFondo.getHeight())));
    }

    /**
     * @return the velocidadFondo
     */
    public int getVelocidadFondo() {
        return velocidadFondo;
    }

    /**
     * @param velocidadFondo the velocidadFondo to set
     */
    public void setVelocidadFondo(int velocidadFondo) {
        this.velocidadFondo = velocidadFondo;
    }
}
