

package contra2;

public class BalaMoustro extends Actor{

    private static int velocidadBala;


    public static void setVelocidadBala(int aVelocidadBala) {
        velocidadBala = aVelocidadBala;
    }
    private int direccion=1; //driecion de la bala

	public BalaMoustro(Escenario escenario) {
		super(escenario);
		setNombreImagen( new String[] {"BalaMoustro.gif"});
	}

    public int getDireccion() { return direccion; }
    public void setDireccion(int a) {direccion= a;}

	public void acto() {
        super.acto();

        if(direccion==1){x -= velocidadBala;if(x<=0){remover();}}
        if(direccion==2){x += velocidadBala;if(x>800){remover();}}
        
    }

    public void colision(Actor a) {
        if (a instanceof Jugador ){
            remover();
        }
    }
}
