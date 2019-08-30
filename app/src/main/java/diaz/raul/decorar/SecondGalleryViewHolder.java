package diaz.raul.decorar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class SecondGalleryViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageItem;


    public SecondGalleryViewHolder(View itemView) {
        super(itemView);

        this.imageItem = (ImageView) itemView.findViewById(R.id.galleryImageView);
    }

    public void bind(String imageURI, Context context) {
        String path = new File(imageURI).getPath();

        Glide.with(context)
                .load(imageURI)
                .apply(new RequestOptions().override(200, 200))
                .into(imageItem);
    }
}
