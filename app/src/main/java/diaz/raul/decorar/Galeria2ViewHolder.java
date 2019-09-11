package diaz.raul.decorar;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class Galeria2ViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageItem;
    private Context context;


    public Galeria2ViewHolder(View itemView, Context context) {
        super(itemView);

        this.imageItem = itemView.findViewById(R.id.galleryImageView);
        this.context = context;

    }

    public void bind(Object selectedObject, Context context) {
        //Obtenemos la ubicación del fichero de la imagen para mostrarla con Glide
        String path = context.getFilesDir() + File.separator + "Imagenes" + File.separator + selectedObject.getIconpath();

        Glide.with(context)
                .load(path)
                .apply(new RequestOptions().override(200, 200))
                .into(imageItem);

        //Añadimos un onClick a cada item del recyclerView que pasará el objecto elegido a la actividad Main
        //para posicionarlo en AR

        imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enviamos el objeto seleccionado a la actividad
                //deseada con el onItemClick

                onItemClick(selectedObject);
            }
        });

        //Añadimos un onLongClick que nos llevará a la actividad de previewModel de el modelo elegido

        imageItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Obtenemos el string del nombre del objeto seleccionado para enviarlo a la actividad
                //deseada con el onItemClick
                onItemLongClick(selectedObject);
                return true;
            }
        });


    }

    public void onItemClick(Object selectedObject) {

        if (this.context instanceof MainActivity) {
            Toast.makeText(context, "Ha elegido el objeto: " + selectedObject.getNombre(), Toast.LENGTH_LONG).show();
            MainActivity main = (MainActivity) context;
            main.setChosenObject(selectedObject);
            main.toFragmentAR();
        }
        if (this.context instanceof Galeria2Activity) {
            Intent topreviewModelIntent = new Intent(context, PreviewModelActivity.class);
            topreviewModelIntent.putExtra("objeto_seleccionado", selectedObject);
            context.startActivity(topreviewModelIntent);

        }


    }

    public void onItemLongClick(Object selectedObject) {


    }
}
