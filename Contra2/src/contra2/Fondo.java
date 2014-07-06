package contra2;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class Fondo extends Actor {

//    private int x;
//    private int y;
    private BufferedImage auxFondo;///permite el desplazmiento del fondo
    private int velocidadFondo = 0; //velocidad del desplazamiento del fondo

    public Fondo(Escenario escenario) {
        super(escenario);
	setNombreImagen(new String[]{"fondo.jpg"});
		
	auxFondo = cargaDeImagen.getImagenes("fondo.jpg");
	this.ancho = auxFondo.getWidth();
	this.alto = auxFondo.getHeight();
	this.x = -(auxFondo.getWidth()-this.escenario.ANCHO)/2;
	this.y = 0;
    }

    public void pintar(Graphics2D g) {
        super.pintar(g);
        g.setPaint(new TexturePaint(auxFondo, new Rectangle(x, y, ancho, alto)));
    }

    public int getVelocidadFondo() {
        return velocidadFondo;
    }

    public void setVelocidadFondo(int velocidadFondo) {
        this.velocidadFondo = velocidadFondo;
    }
}
