package diaz.raul.decorar;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GalleryListViewHolder extends RecyclerView.ViewHolder {
    private TextView galleryView;

    public GalleryListViewHolder(View itemView) {
        super(itemView);
        this.galleryView = itemView.findViewById(R.id.galleryTextView);
    }

    public void bind(String galleryItem) {
        galleryView.setText(galleryItem);
    }
}