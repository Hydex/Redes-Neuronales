package contra2;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class CargaDeImagen {

    private final HashMap resources;

    public CargaDeImagen() {
        resources = new HashMap();
    }

    //Carga de imagen simple    
    public BufferedImage getImagenes(String name) {
        Object res = resources.get(name);
        if (res == null) {
            res = cargarDato(name);
            resources.put(name, res);
        }
        return (BufferedImage) res;
    }

    protected Object cargarDato(String name) {
        URL url = getClass().getResource("/imagenes/" + name);
        if (url == null) {
            return null;
        }
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println("No se pudo cargar la imagen de " + url);
            System.out.println("El error fue : " + e.getClass().getName() + " " + e.getMessage());
            System.exit(0);
            return null;
        }
    }

    //Carga de imagenes "sprite"    
    public void getImagenes(Imagen imagen) {
        try {
            cargarDato(imagen);
        } catch (IOException ex) {
            Logger.getLogger(CargaDeImagen.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No se pudo cargar la imagen de ");
            System.out.println("El error fue : " + ex.getClass().getName() + " " + ex.getMessage());
            System.exit(0);
        }
    }

    protected void cargarDato(Imagen imagen) throws IOException {
        URL url = getClass().getResource("/imagenes/Contra3TheAlineWarsSheet0.gif");
        BufferedImage bigImg = ImageIO.read(url);
        if (bigImg != null) {
            imagen.setImagen(bigImg.getSubimage(imagen.getX(), imagen.getY(), imagen.getWidth(), imagen.getHeight()));
        }
    }
}
