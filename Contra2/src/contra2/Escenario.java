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
    public static final int ANCHO = 750;//Ancho de la ventana
    public static final int LARGO = 550;//Largo de la ventana

    public static final int SPEED = 10;

    public CargaDeImagen getCargaDeImagen();

    public CargaDeSonidos getCargaDeSonidos();

    public void agregarActor(Actor a);

    public Jugador getJugador();

    public void finDelJuego();

    public void desplazamientoFondo();
}
