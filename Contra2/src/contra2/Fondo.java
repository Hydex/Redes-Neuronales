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
        setNombreImagen(new String[]{"escena.png"});
        x = 0;
        y = 0;
    }

    public void pintar(Graphics2D g) {
        super.pintar(g);
        auxFondo = cargaDeImagen.getImagenes(nombreImagen[0]);
        g.setPaint(new TexturePaint(auxFondo, new Rectangle(x, y, auxFondo.getWidth(), auxFondo.getHeight())));
    }

    public int getVelocidadFondo() {
        return velocidadFondo;
    }

    public void setVelocidadFondo(int velocidadFondo) {
        this.velocidadFondo = velocidadFondo;
    }
}
