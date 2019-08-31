package diaz.raul.decorar;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GalleryListViewHolder extends RecyclerView.ViewHolder {
    private TextView galleryView;
    private Context context;

    public GalleryListViewHolder(View itemView, Context context) {
        super(itemView);
        this.galleryView = itemView.findViewById(R.id.galleryTextView);
        this.context = context;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtenemos el tipo del objeto del ViewHolder
                String modelType = galleryView.getText().toString();
                onItemClick(modelType);
            }
        });
    }

    public void bind(String galleryItem) {
        galleryView.setText(galleryItem);
    }

    public void onItemClick(String modelType) {
        Intent toSecondGalleryIntent = new Intent(context, SecondGalleryActivity.class);
        toSecondGalleryIntent.putExtra("tipoModelo", modelType);
        context.startActivity(toSecondGalleryIntent);

    }
}