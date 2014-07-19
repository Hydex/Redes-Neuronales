package contra2;

import java.awt.image.ImageObserver;

public interface Stage extends ImageObserver {

    public static final int WIDTH = 750;
    public static final int HEIGHT = 550;

    public static final int SPEED = 10;

    public SpriteCache getSpriteCache();

    public void addActor(Actor a);

    public Jugador getJugador();

    public void gameOver();

    public void aux();

}
