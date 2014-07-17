package contra2;

import java.awt.event.KeyEvent;

public class Jugador extends Actor {

    public static final int VIDA_MAXIMA = 800;

    protected Imagen[] imagenes; //Imagenes
    protected static final int VELOCIDAD_JUGADOR = 3;//velocidad del desplazamiento
    protected static final int VELOCIDAD_SALTO_JUGADOR = 12;//velocidad de salto
    protected int vx;//almacena la velocidad del jugador  para el desplazamiento horizontal
    protected int vy;//almacena la velocidad del jugador  para el desplazamiento vertical
    private boolean arriba, abajo, izquierda, derecha;

    private static int j = 0, w = 7;//Auxiliar para carga de imagenes (j = derecha) - (w: izquierda)
    private static int ju = 0, wu = 7;//Auxiliar para carga de imagenes diagonal (ju = arriba derecha) - (wu: arriba izquierda)
    private boolean posIzq = false;
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
            new Imagen(286, 451, 26, 21), new Imagen(260, 448, 26, 21), new Imagen(228, 451, 26, 21), new Imagen(201, 448, 26, 21),
            //Diagonal arriba derecha
            new Imagen(318, 214, 30, 48), new Imagen(356, 214, 30, 48), new Imagen(397, 214, 30, 48), new Imagen(438, 214, 30, 48),
            new Imagen(483, 214, 30, 48),
            //Diagonal arriba izquierda
            new Imagen(285, 214, 30, 48), new Imagen(244, 214, 30, 48), new Imagen(204, 214, 30, 48), new Imagen(160, 214, 30, 48),
            new Imagen(114, 214, 30, 48)
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
            if (!(x + getAncho() + (0 - this.escenario.getFondo().getX()) > this.escenario.getFondo().getAncho())) {
                vx = 3;
            }
        }
    }

    public void rebobinar() {
        t++;
        if (t % velocidadImagen == 0) {
            t = 0;
            if (!salto) {
                if (izquierda) {
                    if (arriba) {
                        if (wu == 35) {
                            wu = 31;
                        }
                        setCurrentFrame(wu);
                        wu++;
                        ju = 26;
                    } else {
                        if (w == 14) {
                            w = 7;
                        }
                        setCurrentFrame(w);
                        w++;
                        j = 0;
                    }
                    posIzq = true;
                } else if (derecha) {
                    if (arriba) {
                        if (ju == 30) {
                            ju = 26;
                        }
                        setCurrentFrame(ju);
                        ju++;
                        wu = 30;
                    } else {
                        if (j == 7) {
                            j = 0;
                        }
                        setCurrentFrame(j);
                        j++;
                        w = 7;
                    }
                    posIzq = false;
                } else if (arriba) {
                    setCurrentFrame(posIzq ? 15 : 14);
                } else if (abajo) {
                    setCurrentFrame(posIzq ? 17 : 16);
                }

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
        if (esEnemigo) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (auxContSalto == 0) {
                    super.setY(Escenario.ALTO - super.getAlto());
                }
                izquierda = true;
                break;

            case KeyEvent.VK_RIGHT:
                if (auxContSalto == 0) {
                    super.setY(Escenario.ALTO - super.getAlto());
                }
                derecha = true;
                break;

            case KeyEvent.VK_UP:
                if (auxContSalto == 0) {
                    super.setY(Escenario.ALTO - super.getAlto());
                }
                arriba = true;
                break;

            case KeyEvent.VK_DOWN:
                if (auxContSalto == 0) {
                    super.setY(Escenario.ALTO - super.getAlto());
                }
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
        if (esEnemigo) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (auxContSalto == 0) {
                    super.setY(Escenario.ALTO - super.getAlto());
                }
                izquierda = false;
                break;

            case KeyEvent.VK_RIGHT:
                if (auxContSalto == 0) {
                    super.setY(Escenario.ALTO - super.getAlto());
                }
                derecha = false;
                break;

            case KeyEvent.VK_UP:
                if (auxContSalto == 0) {
                    super.setY(Escenario.ALTO - super.getAlto());
                }
                setCurrentFrame(posIzq ? 7 : 0);
                arriba = false;
                break;

            case KeyEvent.VK_DOWN:
                if (auxContSalto == 0) {
                    super.setY(Escenario.ALTO - super.getAlto());
                }
                setCurrentFrame(posIzq ? 7 : 0);
                abajo = false;
                break;
        }
        updateSpeed();
    }

    public void movimientos(boolean izquierda, boolean derecha, boolean arriba, boolean abajo, boolean salto, boolean disparo) {
        if (!esEnemigo) {
            return;
        }

        if (auxContSalto == 0) {
            super.setY(Escenario.ALTO - super.getAlto());
        }

        this.izquierda = izquierda;
        this.derecha = derecha;
        this.arriba = arriba;
        this.abajo = abajo;

        if ((this.arriba && !arriba) || (this.abajo && !abajo)) {
            setCurrentFrame(posIzq ? 7 : 0);
        }

        if (salto) {
            if (auxContSalto == 0) {
                this.salto = true;
                subiendo = !subiendo;
            }
        }

        if (disparo) {
            fuego();
        }
    }

    public void fuego() {
        Bala b = new Bala(escenario);
        Bala.setVelocidadBala(5);
        b.esEnemigo = this.esEnemigo;
        if (posIzq == true) {
            b.setDireccion(1);
            b.setX(x + 15);
            b.setY(y + 20);
        }
        if (posIzq == false) {
            b.setDireccion(2);
            b.setX(x + 15);
            b.setY(y + 20);
        }
        if (posIzq == true && abajo == true) {
            b.setDireccion(1);
            b.setX(x + 15);
            b.setY(y + 40);
        }
        if (posIzq == false && abajo == true) {
            b.setDireccion(2);
            b.setX(x + 25);
            b.setY(y + 41);
        }
        if (posIzq == true && arriba == true) {
            b.setDireccion(3);
            b.setX(x + 10);
            b.setY(y + 5);
        }
        if (posIzq == false && arriba == true) {
            b.setDireccion(3);
            b.setX(x + 15);
            b.setY(y + 5);
        }

        if (izquierda == true && arriba == true) {
            b.setDireccion(4);
            b.setX(x + 5);
            b.setY(y + 15);
        }

        if (derecha == true && arriba == true) {
            b.setDireccion(5);
            b.setX(x + 10);
            b.setY(y + 15);
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
        /*if (a instanceof Enemigo || a instanceof BalaMoustro) {
         a.remover();
         addPuntaje(40);
         addVida(-20);
         if (getVida() < 0) {
         escenario.finDelJuego();
         }
         }*/

        if (a instanceof Bala) {
            if (this.esEnemigo != a.esEnemigo) {
                a.remover();
                addVida(-10);
                if (getVida() < 0) {
                    escenario.finDelJuego();
                }
            }
        }
    }

    public void obtenerEntradas() {        
        if (!esEnemigo) {
            return;
        }

        int estado, enemigoProximidad, ubicacionEnemigoX, ubicacionEnemigoY;
        int balaProximidad = 0, direccionBalaX = 0, direccionBalaY = 0, ubicacionBalaX = 0, ubicacionBalaY = 0;
        //Personaje estado
        estado = (abajo ? -1 : (salto ? 1 : 0));
        System.out.println("Personaje estado: " + estado);

        //ENEMIGO
        double distanciaEnemigoX = escenario.getJugador().x - this.x;
        double distanciaEnemigoY = escenario.getJugador().y - this.y;
        //Enemigo posicion-distancia
        double auxEnemigoProximidad = Math.sqrt((Math.pow(distanciaEnemigoX, 2) + Math.pow(distanciaEnemigoY, 2)));
        enemigoProximidad = (auxEnemigoProximidad < 90 ? -1 : (auxEnemigoProximidad < 170 ? 0 : 1));
        System.out.println("Enemigo posicion-distancia: " + enemigoProximidad);

        //Enemigo ubicacion X
        ubicacionEnemigoX = (distanciaEnemigoX > 0 ? 1 : (distanciaEnemigoX < 0 ? -1 : 0));
        System.out.println("Enemigo ubicacion X: " + ubicacionEnemigoX);

        //Enemigo ubicacion Y
        ubicacionEnemigoY = (distanciaEnemigoY > 0 ? 1 : (distanciaEnemigoY < 0 ? -1 : 0));
        System.out.println("Enemigo ubicacion Y: " + ubicacionEnemigoY);

        //BALAS
        int auxCont = 0;
        while (auxCont < escenario.getBalas().size()) {
            auxCont++;

            Bala bala = (Bala) escenario.getBalas().get(auxCont);
            if (!bala.esEnemigo) {
                double distanciaBalaX = bala.x - this.x;
                double distanciaBalaY = bala.y - this.y;
                //Bala posicion-distancia
                double auxBalaProximidad = Math.sqrt((Math.pow(distanciaBalaX, 2) + Math.pow(distanciaBalaY, 2)));
                balaProximidad = (auxBalaProximidad < 90 ? -1 : (auxBalaProximidad < 170 ? 0 : 1));
                System.out.println("Bala posicion-distancia: " + balaProximidad);

                int auxBalaDireccion = bala.getDireccion();
                //Bala direccion X
                direccionBalaX = ((auxBalaDireccion == 1 || auxBalaDireccion == 4) ? -1 : ((auxBalaDireccion == 2 || auxBalaDireccion == 5) ? 1 : 0));
                System.out.println("Bala direccion X: " + direccionBalaX);

                //Bala direccion Y
                direccionBalaY = ((auxBalaDireccion == 1 || auxBalaDireccion == 2) ? 0 : 1);
                System.out.println("Bala direccion Y: " + direccionBalaY);
                
                //Bala ubicacion X
                ubicacionBalaX = (distanciaBalaX > 0 ? 1 : (distanciaBalaX < 0 ? -1 : 0));
                System.out.println("Bala ubicacion X: " + ubicacionBalaX);

                //Bala ubicacion Y
                ubicacionBalaY = (distanciaBalaY > 0 ? 1 : (distanciaBalaY < 0 ? -1 : 0));
                System.out.println("Bala ubicacion Y: " + ubicacionBalaY);

                break;
            }
        }
        
        System.out.println("Vector final: ["+estado+","+enemigoProximidad+","+ubicacionEnemigoX+","+ubicacionEnemigoY+","+
                           balaProximidad+","+direccionBalaX+","+direccionBalaY+","+ubicacionBalaX+","+ubicacionBalaY+"]");
    }
}
