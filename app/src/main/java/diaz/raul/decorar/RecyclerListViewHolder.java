package diaz.raul.decorar;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerListViewHolder extends RecyclerView.ViewHolder {
    private TextView galleryView;
    private Context context;

    public RecyclerListViewHolder(View itemView, Context context) {
        super(itemView);
        this.galleryView = itemView.findViewById(R.id.galleryTextView);
        this.context = context;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtenemos el tipo del objeto del ViewHolder
                String objetosSeleccionados = galleryView.getText().toString();
                onItemClick(objetosSeleccionados);

            }
        });
    }

    public void bind(String galleryItem) {
        galleryView.setText(galleryItem);
    }

    public void onItemClick(String objetosSeleccionados) {

        if (this.context instanceof MainActivity) {
            MainActivity main = (MainActivity) context;
            main.crearGaleria2(objetosSeleccionados);
            main.toGallery2();
        }
        if (this.context instanceof Galeria1Activity) {
            Intent toGallery2Intent = new Intent(context, Galeria2Activity.class);
            toGallery2Intent.putExtra("tipoModelo", objetosSeleccionados);
            context.startActivity(toGallery2Intent);
        }
        if (this.context instanceof SettingsActivity) {
            switch (objetosSeleccionados) {
                case "Editar galería":
                    Intent toGallery1Intent = new Intent(context, Galeria1Activity.class);
                    toGallery1Intent.putExtra("tipoModelo", objetosSeleccionados);
                    context.startActivity(toGallery1Intent);

                    break;
                case "Configuración":
                    Intent toSettings = new Intent(context, Galeria1Activity.class);
                    toSettings.putExtra("tipoModelo", objetosSeleccionados);
                    context.startActivity(toSettings);

                    break;
                case "Información":
                    Intent toAbout = new Intent(context, Galeria1Activity.class);
                    toAbout.putExtra("tipoModelo", objetosSeleccionados);
                    context.startActivity(toAbout);

                    break;
                case "Sincronizar":
                    Intent toSync = new Intent(context, Galeria1Activity.class);
                    toSync.putExtra("tipoModelo", objetosSeleccionados);
                    context.startActivity(toSync);

                    break;
            }
        }

    }
}