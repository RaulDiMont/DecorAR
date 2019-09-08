package diaz.raul.decorar;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Galeria1ViewHolder extends RecyclerView.ViewHolder {
    private TextView galleryView;
    private Context context;

    public Galeria1ViewHolder(View itemView, Context context) {
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
            Intent toSecondGalleryIntent = new Intent(context, Galeria2Activity.class);
            toSecondGalleryIntent.putExtra("tipoModelo", objetosSeleccionados);
            context.startActivity(toSecondGalleryIntent);
        }

    }
}