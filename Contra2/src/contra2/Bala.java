/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contra2;

public class Bala extends Actor {

    private static int velocidadBala;

    public static void setVelocidadBala(int aVelocidadBala) {
        velocidadBala = aVelocidadBala;
    }
    private int direccion = 1; //driecion de la bala

    public Bala(Escenario escenario) {
        super(escenario);
        setNombreImagen(new String[]{"bala.gif"});
    }

    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int a) {
        direccion = a;
    }

    public void acto() {
        super.acto();

        if (direccion == 1) {
            x -= velocidadBala;
            if (x <= 0) {
                remover();
            }
        }
        if (direccion == 2) {
            x += velocidadBala;
            if (x > 800) {
                remover();
            }
        }
        if (direccion == 3) {
            y -= velocidadBala;
            if (y <= 0) {
                remover();
            }
        }
        if (direccion == 4) {
            x -= velocidadBala;
            if (x <= 0) {
                remover();
            }  
            
            y -= velocidadBala;
            if (y <= 0) {
                remover();
            }
        }
        if (direccion == 5) {
            x += velocidadBala;
            if (x > 800) {
                remover();
            } 
            
            y -= velocidadBala;
            if (y <= 0) {
                remover();
            }
        }
        
        
    }

    public void colision(Actor a) {
        if (a instanceof Enemigo) {
            remover();
        }
    }
}
