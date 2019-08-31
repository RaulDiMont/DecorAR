package diaz.raul.decorar;

import java.io.Serializable;

public class Object implements Serializable {
    private String filePath, iconPath, nombre, escalable, tipo, superficie;

    public String getFilepath() {
        return filePath;
    }

    public String getIconpath() {
        return iconPath;
    }
    public String getNombre() {
        return nombre;
    }
    public String getEscalable() {
        return escalable;
    }
    public String getTipo() {
        return tipo;
    }
    public String getSuperficie() {
        return superficie;
    }
}
