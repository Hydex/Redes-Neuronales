/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contra2;

/**
 *
 * @author Administrador
 */
import java.awt.image.ImageObserver;

public interface Escenario extends ImageObserver {
    
    public static final int ANCHO = 256;//Ancho de la ventana
    public static final int ALTO = 192;//Largo de la ventana

    public static final int SPEED = 10;

    public Fondo getFondo();
    
    public CargaDeImagen getCargaDeImagen();

    public void agregarActor(Actor a);

    public Jugador getJugador();

    public void finDelJuego();

    public void desplazamientoFondo();
}
