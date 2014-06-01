package contra2;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class CargaDeImagen extends Carga {

    protected Object cargarDato(URL url) {
        if(url == null)
            return null;
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println("No se pudo cargar la imagen de " + url);
            System.out.println("El error fue : " + e.getClass().getName() + " " + e.getMessage());
            System.exit(0);
            return null;
        }
    }

    public BufferedImage getImagenes(String name) {
        return (BufferedImage) getDato(name);
    }
}
