package diaz.raul.decorar;

import java.io.Serializable;

//Implementamos la interfaz Serializable para poder compartir los Objects entre actividades de forma
//sencilla con Intents

public class Object implements Serializable {
    private String filePath, iconPath, nombre, escalable, tipo, superficie;

    //Obtenemos la ruta del fichero.sfb
    public String getFilepath() {
        return filePath;
    }

    //Obtenemos la ruta del thumbnail que aparecerá en la Secondgallery
    public String getIconpath() {
        return iconPath;
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
    //con una de las categorías de la GalleryScrolling
    public String getTipo() {
        return tipo;
    }

    //Obtenemos en qué tipo de superficie se puede situar el objeto (suelo, techo o pared)
    public String getSuperficie() {
        return superficie;
    }
}
