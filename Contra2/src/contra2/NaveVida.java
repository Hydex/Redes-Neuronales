

package contra2;


public class NaveVida extends Actor{
    
    private int vx;

    public NaveVida(Escenario escenario) {
		super(escenario);
		setNombreImagen( new String[] {"bala.gif"});
	}
    public void acto(){
        super.acto();
        x-=vx;
        if(x<0){remover();}

    }

    public void colision(Actor a) {

        if (a instanceof Bala ){
            remover();

            Vida v = new Vida(escenario);
            v.setX(getX()+10);
            v.setY(getY());
            v.setVy(5);
            escenario.agregarActor(v);
        }
    }

    public int getVx() { return vx; }
	public void setVx(int i) {vx = i;}






}
