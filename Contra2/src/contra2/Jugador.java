package contra2;

import java.awt.event.KeyEvent;

public class Jugador extends Actor {

    public static final int VIDA_MAXIMA = 1000;

    protected static final int VELOCIDAD_JUGADOR = 3;//velocidad del desplazamiento
    protected int vx;//almacena la velocidad del jugador  para el desplazamiento horizontal
    protected int vy;//almacena la velocidad del jugador  para el desplazamiento vertical
    private boolean arriba, abajo, izquierda, derecha;

    private static int j = 0, w = 7;//Auxiliar para carga de imagenes (w: izquierda) - (j = derecha)
    private boolean posIzq;
    private int velocidadImagen = 6, t = 0;

    private int puntaje;
    private int vida;

    public Jugador(Escenario escenario) {
        super(escenario);
        setNombreImagen(new String[]{
            "Contra1.gif", "Contra2.gif", "Contra3.gif", "Contra4.gif", "Contra5.gif", "Contra6.gif", "Contra7.gif", 
            "Contra1Izq.gif", "Contra2Izq.gif", "Contra3Izq.gif", "Contra4Izq.gif", "Contra5Izq.gif", "Contra6Izq.gif", 
            "Contra7Izq.gif", "ContraArriba.gif", "ContraArribaIzq.gif", "ContraAbajo.gif", "ContraAbajoIzq.gif"});

        vida = VIDA_MAXIMA;
    }

    public void aux(int i) {
        x -= i;
    }

    public void acto() {
        super.acto();
        x += vx;
        y += vy;
        if (x < -200 || x > Escenario.ANCHO) {
            vx = 0;
        }
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int i) {
        vx = i;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int i) {
        vy = i;
    }

    protected void updateSpeed() {
        vx = 0;
        vy = 0;
        /*if (down) vy = PLAYER_SPEED;
         if (up) vy = -PLAYER_SPEED;*/
        if (izquierda) {
            if (!(x < 0)) {
                vx = -VELOCIDAD_JUGADOR;
            }
        };
        if (derecha) {
            if (!(x + getAncho() + 300 > Escenario.ANCHO)) {
                vx = VELOCIDAD_JUGADOR;
            }
        };
    }

    public void rebobinar() {
        t++;
        if (t % velocidadImagen == 0) {
            t = 0;
            if (izquierda == true) {
                if (w == 14) {
                    w = 7;
                }
                setCurrentFrame(w);
                w++;
                posIzq = true;
            }
            if (derecha == true) {
                if (j == 6) {
                    j = 1;
                }
                setCurrentFrame(j);
                j++;
                escenario.desplazamientoFondo();
                posIzq = false;
            }
        }
    }
public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                super.setY(Escenario.LARGO - super.getLargo());
                abajo = true;
                if (posIzq) {
                    setCurrentFrame(17);
                } else {
                    setCurrentFrame(16);
                }
                break;

            case KeyEvent.VK_UP:
                super.setY(Escenario.LARGO - super.getLargo());
                arriba = true;
                if (posIzq) {
                    setCurrentFrame(15);
                } else {
                    setCurrentFrame(14);
                }
                break;

            case KeyEvent.VK_LEFT:
                super.setY(Escenario.LARGO - super.getLargo());
                izquierda = true;
                break;

            case KeyEvent.VK_RIGHT:
                super.setY(Escenario.LARGO - super.getLargo());
                derecha = true;
                escenario.desplazamientoFondo();
                break;

            case KeyEvent.VK_SPACE:
                fuego();
                break;

        }
        updateSpeed();
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                super.setY(Escenario.LARGO - super.getLargo());
                abajo = false;
                arriba = false;
                if (posIzq) {
                    setCurrentFrame(7);
                } else {
                    setCurrentFrame(0);
                }
                break;

            case KeyEvent.VK_UP:
                super.setY(Escenario.LARGO - super.getLargo());
                arriba = false;
                if (posIzq) {
                    setCurrentFrame(7);
                } else {
                    setCurrentFrame(0);
                }
                w = 7;
                j = 0;
                break;

            case KeyEvent.VK_LEFT:
                super.setY(Escenario.LARGO - super.getLargo());
                if (w == 14) {
                    w = 7;
                }
                setCurrentFrame(w);
                izquierda = false;
                break;

            case KeyEvent.VK_RIGHT:
                super.setY(Escenario.LARGO - super.getLargo());
                if (j == 6) {
                    j = 1;
                }
                setCurrentFrame(j);
                escenario.desplazamientoFondo();
                derecha = false;
                break;
        }
        updateSpeed();
    }

    

    public void fuego() {
        Bala b = new Bala(escenario);
        b.setVelocidadBala(20);
        if (posIzq == true) {
            b.setDireccion(1);
            b.setX(x - 10);
            b.setY(y + 35);
        }
        if (posIzq == false) {
            b.setDireccion(2);
            b.setX(x + 155);
            b.setY(y + 48);
        }
        if (posIzq == true && abajo == true) {
            b.setDireccion(1);
            b.setX(x - 10);
            b.setY(y + 28);
        }
        if (posIzq == false && abajo == true) {
            b.setDireccion(2);
            b.setX(x + 155);
            b.setY(y + 28);
        }
        if (posIzq == true && arriba == true) {
            b.setDireccion(3);
            b.setX(x + 38);
            b.setY(y);
        }
        if (posIzq == false && arriba == true) {
            b.setDireccion(3);
            b.setX(x + 50);
            b.setY(y);
        }

        escenario.agregarActor(b);

        escenario.getCargaDeSonidos().reproducirSonido("missile.wav");
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int i) {
        puntaje = i;
    }

    public void addPuntaje(int i) {
        setPuntaje(getPuntaje() + i);
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int i) {
        vida = i;
    }

    public void addVida(int i) {
        if (!(vida == VIDA_MAXIMA) || i < 0) {
            vida += i;
        }
    }

    public void colision(Actor a) {
        if (a instanceof Enemigo || a instanceof BalaMoustro) {
            a.remover();
            addPuntaje(40);
            addVida(-20);
            if (getVida() < 0) {
                escenario.finDelJuego();
            }
        }
    }
}
