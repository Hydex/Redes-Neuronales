package contra2;

import java.net.URL;
import java.util.HashMap;

public abstract class Carga {
    protected HashMap resources;

    public Carga() {
        resources = new HashMap();
    }
    
    protected Object cargarDato(String name) {
        URL url = getClass().getResource("/Imagenes/"+name);
        return cargarDato(url);
    }
    
    protected Object getDato(String name) {
        Object res = resources.get(name);
        if (res == null) {
            res = cargarDato(name);
            resources.put(name, res);
        }
        return res;
    }

    protected abstract Object cargarDato(URL url);
}
