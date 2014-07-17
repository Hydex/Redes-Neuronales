package contra2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Contra extends Canvas implements Escenario, KeyListener {

    private final BufferStrategy strategy;
    private long usedTime;
    private int velocidadFPS = 40;
    private ArrayList actores;
    private final CargaDeImagen cargaDeImagen;
    boolean finDelJuego = false, pausa = false;
    private int aux1 = 0, aux2 = 0, t = 0, velocidadMuerte = 4, tAct = 0, velocidadActualizacion = 10;
    Jugador jugador;
    Jugador enemigo;
    Fondo fondo;
    Actor actor;//Enemigos, balas enemigas y las balas del jugador    Actor actor;//Enemigos, balas enemigas y las balas del jugador
    public int escala;

    RJava rJava;//objeto utilizado para utilizar las propiedades de la red neuronal

    public Contra() {
        escala = 3;
        cargaDeImagen = new CargaDeImagen();

        JFrame ventana = new JFrame("Contra");
        ventana.setVisible(true);
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        ventana.setResizable(false);
        JPanel panel = (JPanel) ventana.getContentPane();
        panel.setPreferredSize(new Dimension(ANCHO * escala, ALTO * escala));
        panel.setLayout(null);
        panel.add(this);
        panel.setBackground(Color.white);
        ventana.pack();

        createBufferStrategy(2);
        strategy = getBufferStrategy();
        setBounds(0, 0, Escenario.ANCHO * escala, Escenario.ALTO * escala);
        setBackground(Color.black);
        requestFocus();
        setIgnoreRepaint(true);

        addKeyListener(this);
    }

    public static void main(String[] args) {
        Contra c = new Contra();
        c.juego();
    }

    public void juego() {
        usedTime = 100;
        iniciarMundo();
        new Thread(new Runnable() {
            public void run() {
                while (isVisible() && !finDelJuego) {
                    long startTime = System.currentTimeMillis();
                    if (pausa) {
                        pintarPausaJuego();
                    } else {
                        actualizarMundo();
                        verificarColision();
                        pintarMundo();
                    }
                    jugador.rebobinar();
                    enemigo.rebobinar();
                    do {
                        Thread.yield();
                    } while (System.currentTimeMillis() - startTime < 1000 / velocidadFPS);
                }
                pintarFinDeJuego();
            }
        }).run();
    }

    public void iniciarMundo() {
        //Incializar fondo     
        fondo = new Fondo(this);

        //Incializar enemigos
        actores = new ArrayList();

        //Incializar jugador principal
        jugador = new Jugador(this);
        jugador.setX(Escenario.ANCHO / 8);
        jugador.setY(Escenario.ALTO - jugador.getAlto());

        //Inicializar enemigo principal
        enemigo = new Jugador(this);
        enemigo.esEnemigo = true;
        enemigo.setX((Escenario.ANCHO / 8) + 50);
        enemigo.setY(Escenario.ALTO - jugador.getAlto());

        strategy.show();

        iniciarRedNeuronal();
    }

    public void iniciarRedNeuronal() {
        int entradas = 11;
        int salidas = 6;
        int ejemplos = 128;
        int ocultas = 0;
        int pasos = 2500;
        rJava = new RJava();
        rJava.init("D:\\datos.csv", entradas, salidas, ejemplos, ocultas, pasos);
    }

    //genera los actores y marca a los que tiene que ser  removidos
    public void actualizarMundo() {
        int i = 0;
        t++;//Variable auxiliar que permite que la desaparicion de un enemigo/bala tras su muerte no sea rapida
        while (i < actores.size()) {
            Actor m = (Actor) actores.get(i);
            if (m.verificarRemover() && t % velocidadMuerte == 0) {
                actores.remove(i);
                t = 0;
            } else {
                m.acto();
                i++;
            }
        }
        jugador.acto();

        //Establecer movimientos del enemigo con la red neuronal
        tAct++;
        if (tAct % velocidadActualizacion == 0) {
            enemigo.movimientosEnemigo(rJava.obtenerSalidas(enemigo.obtenerEntradas()));
            tAct = 0;
        }
        enemigo.acto();
    }

    public void verificarColision() {
        Rectangle dimensionJugador = jugador.getDimension();//dimensiones del fotograma del jugador
        Rectangle dimensionEnemigo = enemigo.getDimension();//dimensiones del fotograma del enemigo
        for (int i = 0; i < actores.size(); i++) {
            Actor a1 = (Actor) actores.get(i);
            Rectangle r1 = a1.getDimension();

            if (r1.intersects(dimensionJugador)) {
                jugador.colision(a1);
                a1.colision(jugador);
            }

            if (r1.intersects(dimensionEnemigo)) {
                enemigo.colision(a1);
                a1.colision(enemigo);
            }
        }
    }

    public void pintarMundo() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.scale(escala, escala);

        fondo.pintar(g);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < actores.size(); i++) {
            Actor m = (Actor) actores.get(i);
            m.pintar(g);
        }

        pintarEstado(g);
        jugador.pintar(g);
        enemigo.pintar(g);
        strategy.show();
    }

    //pinta puntaje y vida
    public void pintarEstado(Graphics2D g) {
        pintarVida(g);
    }

    public void pintarVida(Graphics2D g) {
        //VIDA HEROE
        g.setPaint(Color.red);
        g.fillRect(2, (Escenario.ALTO / 15) + 2, Jugador.VIDA_MAXIMA / 8, 5);
        g.setPaint(Color.blue);
        g.fillRect(2, (Escenario.ALTO / 15) + 2, jugador.getVida() / 8, 5);
        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.setPaint(Color.green);
        g.drawString("Heroe", 2, Escenario.ALTO / 15);

        //VIDA ENEMIGO
        g.setPaint(Color.red);
        g.fillRect(Escenario.ANCHO - 102, (Escenario.ALTO / 15) + 2, Jugador.VIDA_MAXIMA / 8, 5);
        g.setPaint(Color.blue);
        g.fillRect(Escenario.ANCHO - 102, (Escenario.ALTO / 15) + 2, enemigo.getVida() / 8, 5);
        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.setPaint(Color.green);
        g.drawString("Enemigo", Escenario.ANCHO - 37, Escenario.ALTO / 15);
    }

    public void finDelJuego() {
        finDelJuego = true;
    }

    public void agregarActor(Actor a) {
        actores.add(a);
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Jugador getEnemigo() {
        return enemigo;
    }

    public ArrayList getBalas() {
        return actores;
    }

    public void pintarFinDeJuego() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("FIN DEL JUEGO", 300, 200);
        strategy.show();
    }

    public void pintarPausaJuego() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("PAUSA", 300, 200);
        strategy.show();
    }

    public CargaDeImagen getCargaDeImagen() {
        return cargaDeImagen;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            pausa = !(pausa);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (fondo.getX() < 0) {
                        fondo.setX(fondo.getX() + 3);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (jugador.getX() + jugador.getAncho() + (0 - fondo.getX()) < fondo.getAncho()) {
                        fondo.setX(fondo.getX() - 3);
                    }
                    break;
            }
            jugador.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        jugador.keyReleased(e);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void desplazamientoFondo() {
        if (aux1 == 1000) {
            finDelJuego();
        }

        aux1++;
        if (jugador.getX() >= 200) {
            fondo.setVelocidadFondo(fondo.getVelocidadFondo() - 7);
        }
        if (aux1 % 100 == 0) {
            aux2++;
            Enemigo n = new Enemigo(this);
            n.llamada(aux2);
        }
        if (aux1 % 150 == 0) {
            NaveVida nv = new NaveVida(this);
            nv.setVx(5);
            nv.setX(750);
            nv.setY(100);
            agregarActor(nv);
        }
    }

    public Fondo getFondo() {
        return fondo;
    }
}
