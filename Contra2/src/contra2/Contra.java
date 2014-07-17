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
    private int aux1 = 0, aux2 = 0, t = 0, velocidadMuerte = 4;
    Jugador jugador;
    Fondo fondo;
    Actor actor;//Enemigos, balas enemigas y las balas del jugador    Actor actor;//Enemigos, balas enemigas y las balas del jugador
    public int escala;

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
        panel.setPreferredSize(new Dimension(ANCHO*escala, ALTO*escala));
        panel.setLayout(null);
        panel.add(this);
        panel.setBackground(Color.white);
        ventana.pack();
        
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        setBounds(0,0,Escenario.ANCHO*escala,Escenario.ALTO*escala);
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
                    do {
                        Thread.yield();
                    } while (System.currentTimeMillis() - startTime < 1000 / velocidadFPS);
                }
                if (aux1 >= 1000) {
                    pintarGanarJuego();
                } else {
                    pintarFinDeJuego();
                }
            }
        }).run();
    }

    public void iniciarMundo() {
        //Incializar fondo     
        fondo = new Fondo(this);

        //Incializar enemigos
        actores = new ArrayList();
        /*for (int i = 0; i < 1; i++) {
            Enemigo m = new Enemigo(this);
            m.setX(700);
            m.setY((int) (Escenario.LARGO - 200));
            m.setVx(1);
            actores.add(m);
        }*/

        //Incializar jugador principal
        jugador = new Jugador(this);
        jugador.setX(Escenario.ANCHO / 8);
        jugador.setY(Escenario.ALTO - jugador.getAlto());

        strategy.show();
    }
    
    //genera los actores y marca a los que tiene que ser  removidos
    public void actualizarMundo()
    {
        int i = 0;
        t++;//Variable auxiliar que permite que la desaparicion de un enemigo tras su muerte no sea rapida
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
    }

    public void verificarColision() {
        Rectangle dimensioJugador = jugador.getDimension();//dimensiones del fotograma del jugador
        for (int i = 0; i < actores.size(); i++) {
            Actor a1 = (Actor) actores.get(i);
            Rectangle r1 = a1.getDimension();
            if (r1.intersects(dimensioJugador)) {
                jugador.colision(a1);
                a1.colision(jugador);
            }  
            
            for (int j = i + 1; j < actores.size(); j++) {
                Actor a2 = (Actor) actores.get(j);
                Rectangle r2 = a2.getDimension();
                if (r1.intersects(r2)) {
                    a1.colision(a2);
                    a2.colision(a1);
                }
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
        strategy.show();
    }
    
    //pinta puntaje y vida
    public void pintarEstado(Graphics2D g) 
    {
        pintarPuntaje(g);
        pintarVida(g);
        pintarfps(g);
    }

    public void pintarPuntaje(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.setPaint(Color.green);
        g.drawString("Puntaje:", 10, Escenario.ALTO / 15);
        g.setPaint(Color.red);
        g.drawString(jugador.getPuntaje() + "", 45, Escenario.ALTO / 15);
    }

    public void pintarVida(Graphics2D g) {
        g.setPaint(Color.red);
        g.fillRect(280, Escenario.ALTO / 15 - 17, Jugador.VIDA_MAXIMA, 30);

        g.setPaint(Color.blue);
        g.fillRect(280/*+Jugador.MAX_VIDA-jugador.getVida()*/, Escenario.ALTO / 15 - 17, jugador.getVida(), 30);

        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.setPaint(Color.green);
        g.drawString("Vida", 80, Escenario.ALTO / 15);

    }

    public void pintarfps(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.white);
        if (usedTime > 0) {
            g.drawString(String.valueOf(1000 / usedTime) + " fps", Escenario.ANCHO - 50, Escenario.ALTO);
        } else {
            g.drawString("--- fps", Escenario.ANCHO - 50, Escenario.ALTO);
        }
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

    public void pintarFinDeJuego() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("FIN DEL JUEGO", Escenario.ANCHO / 2 - 50, Escenario.ALTO / 2 - 100);
        strategy.show();
    }

    public void pintarGanarJuego() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("     GANASTE", Escenario.ANCHO / 2 - 50, Escenario.ALTO / 2 - 100);
        g.drawString("Puntaje total : " + jugador.getPuntaje(), Escenario.ANCHO / 2 - 50, Escenario.ALTO / 2 - 70);
        strategy.show();
    }

    public void pintarPausaJuego() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("PAUSA", Escenario.ANCHO / 2 - 50, Escenario.ALTO / 2 - 100);
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
                    if(fondo.getX()<0){
			fondo.setX(fondo.getX()+3);
                    }
                    break;
		case KeyEvent.VK_RIGHT: 
                    if(jugador.getX()+jugador.getAncho()+(0-fondo.getX())<fondo.getAncho()){ 
			fondo.setX(fondo.getX()-3);
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
            fondo.setVelocidadFondo(fondo.getVelocidadFondo()-7);
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
