package diaz.raul.decorar;

import java.io.Serializable;

//Implementamos la interfaz Serializable para poder compartir los Objects entre actividades de forma
//sencilla con Intents

public class Object implements Serializable {
    private String filePath, onlineFilePath, iconPath, onlineIconPath, nombre, escalable, tipo, superficie;

    //Obtenemos la ruta del fichero.sfb
    public String getFilePath() {
        return filePath;
    }

    //Obtenemos la ruta del fichero .sfb en la BBDD de firebase
    public String getOnlineFilePath() {
        return onlineFilePath;
    }

    //Obtenemos la ruta del thumbnail del objeto
    public String getIconpath() {
        return iconPath;
    }

    //Obtenemos la ruta del thumbnail en la BBDD de firebase
    public String getOnlineIconPath() {
        return onlineIconPath;
    }

    //Obtenemos el nombre asignado al objeto
    public String getNombre() {
        return nombre;
    }

    //Obtenemos si el objeto es escalable o no (se puede modificar su tamaño)
    public String getEscalable() {
        return escalable;
    }

    //Obtenemos el tipo de objeto (Mesas, Sillas, Decoración de pared...etc), que se corresponderá
    //con una de las categorías de la Galeria 1
    public String getTipo() {
        return tipo;
    }

    //Obtenemos en qué tipo de superficie se puede situar el objeto (suelo, techo o pared)
    public String getSuperficie() {
        return superficie;
    }


}
