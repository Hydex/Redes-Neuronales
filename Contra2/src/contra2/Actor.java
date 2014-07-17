package contra2;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Actor {

    protected int x, y;// posicion del actor
    protected int ancho, alto;//ancho y largo del fotograma

    protected Imagen[] imagenes; //Imagenes
    protected int numeroImagen;
    protected String[] nombreImagen; //El nombre de las imagenes

    protected int velocidadImagen; //velocidad del cambio de fotogramas
    protected int t;

    protected Escenario escenario;
    protected CargaDeImagen cargaDeImagen; ///permite la carga de los fotogramas

    protected boolean remover; // para remover un actor del juego
    
    protected boolean salto;
    public boolean esEnemigo = false;

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
        if (imagenes == null) {
            g.drawImage(cargaDeImagen.getImagenes(nombreImagen[numeroImagen]), x, y, escenario);
        } else {
            if (imagenes[numeroImagen].getImagen() == null) 
                cargaDeImagen.getImagenes(this.imagenes[numeroImagen]);            
            g.drawImage(imagenes[numeroImagen].getImagen(), x,(salto ? y - 20 : y-(imagenes[numeroImagen].getHeight()-20))+30, escenario);
        }
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
        alto = 0;
        ancho = 0;
        for (int i = 0; i < names.length; i++) {
            BufferedImage image = cargaDeImagen.getImagenes(nombreImagen[i]);
            alto = Math.max(alto, image.getHeight());
            ancho = Math.max(ancho, image.getWidth());
        }
    }

    public void setNombreImagen(Imagen[] imagenes) {
        this.imagenes = imagenes;
        alto = 0;
        ancho = 0;
        for (int i = 0; i < this.imagenes.length; i++) {
            cargaDeImagen.getImagenes(this.imagenes[i]);
            alto = Math.max(alto, this.imagenes[i].getHeight());
            ancho = Math.max(ancho, this.imagenes[i].getWidth());
        }
    }

    public int getAlto() {
        return alto;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAlto(int i) {
        alto = i;
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

    public void acto() {
    }

    public Rectangle getDimension() //devuelve las dimensiones del fotograma
        {
        return new Rectangle(x, y, ancho, alto);
    }

    public void colision(Actor a) {
    }
}
