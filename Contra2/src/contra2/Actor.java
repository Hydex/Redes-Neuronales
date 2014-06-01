package contra2;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Actor {
    protected int x, y;// posicion del actor
    protected int ancho, largo;//ancho y largo del fotograma

    protected String[] nombreImagen; //El nombre de las imagenes
    protected int numeroImagen;

    protected int velocidadImagen; //velocidad del cambio de fotogramas
    protected int t;

    protected Escenario escenario;
    protected CargaDeImagen cargaDeImagen; ///permite la carga de los fotogramas

    protected boolean remover; // para remover un actor del juego

    public Actor(Escenario escenario) {
        this.escenario = escenario;
        cargaDeImagen = escenario.getCargaDeImagen();
        numeroImagen = 0;
    }

    public void remover() //remueve un actor
    {
        remover = true;
    }

    public boolean verificarRemover()//permite verificar si se debe remover un actor
    {
        return remover;
    }

    public void pintar(Graphics2D g) {
        g.drawImage(cargaDeImagen.getImagenes(nombreImagen[numeroImagen]), x, y, escenario);
    }

    public int getX() {
        return x;
    }

    public void setX(int i) {
        x = i;
    }

    public int getY() {
        return y;
    }

    public void setY(int i) {
        y = i;
    }

    public void setNombreImagen(String[] names) {
        nombreImagen = names;
        largo = 0;
        ancho = 0;
        for (int i = 0; i < names.length; i++) {
            BufferedImage image = cargaDeImagen.getImagenes(nombreImagen[i]);
            largo = Math.max(largo, image.getHeight());
            ancho = Math.max(ancho, image.getWidth());
        }
    }

    public int getLargo() {
        return largo;
    }

    public int getAncho() {
        return ancho;
    }

    public void setLargo(int i) {
        largo = i;
    }

    public void setAncho(int i) {
        ancho = i;
    }

    public int getCurrentFrame() {
        return numeroImagen;
    }

    public void setCurrentFrame(int i) {
        numeroImagen = i;
    }

    public void acto() {    }

    public Rectangle getDimension() //devuelve las dimensiones del fotograma
    {
        return new Rectangle(x, y, ancho, largo);
    }

    public void colision(Actor a) {
    }
}
