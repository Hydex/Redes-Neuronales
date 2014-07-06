package contra2;

import java.awt.event.KeyEvent;

public class Jugador extends Actor {

    public static final int VIDA_MAXIMA = 300;

    protected Imagen[] imagenes; //Imagenes
    protected static final int VELOCIDAD_JUGADOR = 3;//velocidad del desplazamiento
    protected static final int VELOCIDAD_SALTO_JUGADOR = 12;//velocidad de salto
    protected int vx;//almacena la velocidad del jugador  para el desplazamiento horizontal
    protected int vy;//almacena la velocidad del jugador  para el desplazamiento vertical
    private boolean arriba, abajo, izquierda, derecha;

    private static int j = 0, w = 7;//Auxiliar para carga de imagenes (j = derecha) - (w: izquierda) - (sj: salto derecha)
    private boolean posIzq;
    private final int velocidadImagen = 2;
    private int t = 0;

    private boolean subiendo;
    private int auxContSalto = 0;
    private int auxContImagenSalto = 1;

    private int puntaje;
    private int vida;

    public Jugador(Escenario escenario) {
        super(escenario);
        imagenes = new Imagen[]{
            //Recorrido a la derecha
            new Imagen(319, 6, 33, 41), new Imagen(364, 6, 32, 41), new Imagen(405, 6, 32, 41), new Imagen(452, 6, 30, 41),
            new Imagen(319, 57, 36, 41), new Imagen(367, 57, 40, 41), new Imagen(419, 57, 32, 45),
            //Recorrido a la izquierda
            new Imagen(284, 6, 33, 41), new Imagen(236, 6, 32, 41), new Imagen(193, 6, 32, 41), new Imagen(150, 6, 30, 41),
            new Imagen(276, 57, 36, 41), new Imagen(224, 57, 40, 41), new Imagen(182, 57, 32, 41),
            //Apuntar arriba <- ->
            new Imagen(319, 151, 28, 56), new Imagen(284, 151, 28, 56),
            //Tirarse al suelo <- ->
            new Imagen(400, 127, 46, 17), new Imagen(184, 127, 46, 17),
            //Saltar derecha 18
            new Imagen(319, 451, 26, 21), new Imagen(349, 448, 26, 21), new Imagen(377, 451, 26, 21), new Imagen(408, 448, 26, 21),
            //Saltar izquierda   22
            new Imagen(286, 451, 26, 21), new Imagen(260, 448, 26, 21), new Imagen(228, 451, 26, 21), new Imagen(201, 448, 26, 21)
        };
        setNombreImagen(imagenes);
        vida = VIDA_MAXIMA;
    }

    public void setVx(int i) {
        vx = i;
    }

    public int getVx() {
        return vx;
    }

    public void setVy(int i) {
        vy = i;
    }

    public int getVy() {
        return vy;
    }

    public void acto() {
        super.acto();
        x += vx;
        y += vy;
        if (x < 0 || x > Escenario.ANCHO - getAncho()) {
            vx = 0;
        }
    }

    protected void updateSpeed() {
        vx = 0;
        vy = 0;
        if (izquierda) {
            if (!(x < 0)) {
                vx = -VELOCIDAD_JUGADOR;
            }
        }
        if (derecha) {
            if (!(x + getAncho() > Escenario.ANCHO)) {
                vx = VELOCIDAD_JUGADOR;
            }
        }
    }

    public void rebobinar() {
        t++;
        if (t % velocidadImagen == 0) {
            t = 0;
            if (!salto && izquierda) {
                if (w == 14) {
                    w = 7;
                }
                setCurrentFrame(w);
                w++;
                j = 0;
                posIzq = true;
            }
            if (!salto && derecha) {
                if (j == 7) {
                    j = 0;
                }
                setCurrentFrame(j);
                j++;
                w = 7;
                posIzq = false;
            }
            if (!salto && arriba) {
                setCurrentFrame(posIzq ? 15 : 14);
            }

            if (!salto && abajo) {
                setCurrentFrame(posIzq ? 17 : 16);
            }

            if (salto) {
                if (auxContSalto == 6) {
                    subiendo = false;
                }

                auxContSalto = (subiendo ? auxContSalto + 1 : auxContSalto - 1);
                super.setY(Escenario.ALTO - super.getAlto() - auxContSalto * VELOCIDAD_SALTO_JUGADOR);

                if (auxContSalto == 0) {
                    salto = false;
                    if (!posIzq) {
                        setCurrentFrame(0);
                    }
                    if (posIzq) {
                        setCurrentFrame(7);
                    }
                    auxContImagenSalto = 1;
                }
            }
            if (salto && !posIzq) {
                if (auxContImagenSalto + 17 == 21) {
                    auxContImagenSalto = 1;
                }

                setCurrentFrame(auxContImagenSalto + 17);
                auxContImagenSalto++;
            }
            if (salto && posIzq) {
                if (auxContImagenSalto + 21 == 25) {
                    auxContImagenSalto = 1;
                }

                setCurrentFrame(auxContImagenSalto + 21);
                auxContImagenSalto++;
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (auxContSalto == 0)super.setY(Escenario.ALTO - super.getAlto());
                izquierda = true;
                break;

            case KeyEvent.VK_RIGHT:
                if (auxContSalto == 0)super.setY(Escenario.ALTO - super.getAlto());
                derecha = true;
                break;

            case KeyEvent.VK_UP:
                if (auxContSalto == 0)super.setY(Escenario.ALTO - super.getAlto());
                arriba = true;
                break;

            case KeyEvent.VK_DOWN:
                if (auxContSalto == 0)super.setY(Escenario.ALTO - super.getAlto());
                abajo = true;
                break;

            case KeyEvent.VK_SPACE:
                if (auxContSalto == 0) {
                    salto = true;
                    subiendo = !subiendo;
                }
                break;

            case KeyEvent.VK_CONTROL:
                fuego();
                break;
        }
        updateSpeed();
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (auxContSalto == 0)super.setY(Escenario.ALTO - super.getAlto());
                izquierda = false;
                break;

            case KeyEvent.VK_RIGHT:
                if (auxContSalto == 0)super.setY(Escenario.ALTO - super.getAlto());
                derecha = false;
                break;

            case KeyEvent.VK_UP:
                if (auxContSalto == 0)super.setY(Escenario.ALTO - super.getAlto());
                setCurrentFrame(posIzq ? 7 : 0);
                arriba = false;
                break;

            case KeyEvent.VK_DOWN:
                if (auxContSalto == 0)super.setY(Escenario.ALTO - super.getAlto());
                setCurrentFrame(posIzq ? 7 : 0);
                abajo = false;
                break;
        }
        updateSpeed();
    }

    public void fuego() {
        Bala b = new Bala(escenario);
        Bala.setVelocidadBala(20);
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
    }

    public void addPuntaje(int i) {
        setPuntaje(getPuntaje() + i);
    }

    public void setPuntaje(int i) {
        puntaje = i;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void addVida(int i) {
        if (!(vida == VIDA_MAXIMA) || i < 0) {
            vida += i;
        }
    }

    public void setVida(int i) {
        vida = i;
    }

    public int getVida() {
        return vida;
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
