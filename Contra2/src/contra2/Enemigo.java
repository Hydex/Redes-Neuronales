/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contra2;

public class Enemigo extends Actor {

    protected int vx;
    private int i = 0, d = 4;
    private int velocidadImagen = 6, t = 0, velocidadCambioBala = 10, t2 = 0;

    private boolean derecha = true, izquierda = false;

    public Enemigo(Escenario escenario) {
        super(escenario);
        setNombreImagen(new String[]{"Moustro1.gif", "Moustro2.gif", "Moustro3.gif",
            "Moustro4.gif", "Moustro1Izq.gif", "Moustro2Izq.gif", "Moustro3Izq.gif", "Moustro4Izq.gif"});
    }

    public void acto() {
        super.acto();

        x += vx;
        if (x < 0 || x > Escenario.ANCHO) {
            vx = -vx;
        }
        t++;

        t2++;
        if (t % velocidadImagen == 0) {
            if (izquierda == true) {
                if (i == 3) {
                    i = 0;
                }
                setCurrentFrame(i);
                fuego();
                i++;
            }
            if (derecha == true) {
                if (d == 7) {
                    d = 4;
                }
                setCurrentFrame(d);
                fuego();
                d++;
            }
            t = 0;
        }
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int i) {
        vx = i;
    }

    public void colision(Actor a) {

        if (a instanceof Bala || a instanceof Jugador) {
            if (derecha == true) {
                setCurrentFrame(7);
            }
            if (izquierda == true) {
                setCurrentFrame(3);
            }

            remover();
            escenario.getJugador().addPuntaje(20);
        }
    }

    public void llamada(int aux)//se crea un nuevo enemigo cuando muere uno
    {
        Enemigo m = new Enemigo(escenario);

        if (aux % 2 == 0) {
            m.setX(0);
            m.setIzquierda(true);
            m.setDerecha(false);
        } else {
            m.setX(700);
            m.setDerecha(true);
            m.setIzquierda(false);
        }

        m.setY((int) (Escenario.ALTO - 160));
        m.setVx(2);
        escenario.agregarActor(m);
    }

    public void fuego() {
        if (t2 % velocidadCambioBala == 0) {
            BalaMoustro b = new BalaMoustro(escenario);
            b.setVelocidadBala(6);

            if (derecha == true) {
                b.setDireccion(1);
                b.setX(x - 30);
                b.setY(y + 50);
                escenario.agregarActor(b);
            }
            if (izquierda == true) {
                b.setDireccion(2);
                b.setX(x + 130);
                b.setY(y + 50);
                escenario.agregarActor(b);
            }
            t2 = 0;
        }
    }

    public void setDerecha(boolean derecha) {
        this.derecha = derecha;
    }

    public void setIzquierda(boolean izquierda) {
        this.izquierda = izquierda;
    }

}
