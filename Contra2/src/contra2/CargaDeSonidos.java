package contra2;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class CargaDeSonidos extends Carga {

    protected Object cargarDato(URL url) {
        return Applet.newAudioClip(url);
    }

    public AudioClip getAudio(String name) {
        return (AudioClip) getDato(name);
    }

    public void reproducirSonido(final String name) {
        new Thread(
                new Runnable() {
                    public void run() {
                        getAudio(name).play();
                    }
                }
        ).start();

    }

    public void nombreSonido(final String name) {
        new Thread(
                new Runnable() {
                    public void run() {
                        getAudio(name).loop();
                    }
                }
        ).start();
    }
}
