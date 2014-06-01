package contra2;

public class Vida extends Actor {

    private int vy;

    public Vida(Escenario escenario) {
        super(escenario);
        setNombreImagen(new String[]{"bala.gif"});
    }

    public void acto() {
        super.acto();
        if (y == 570) {
            remover();
        }
        y += vy;
    }

    public void colision(Actor a) {

        if (a instanceof Jugador) {
            remover();

            escenario.getJugador().addVida(5);
        }
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

}
