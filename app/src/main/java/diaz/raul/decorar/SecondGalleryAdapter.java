package diaz.raul.decorar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class SecondGalleryAdapter extends RecyclerView.Adapter<SecondGalleryViewHolder> {

    Context context;
    List<Object> selectedObjects;

    public SecondGalleryAdapter(Context context, List<Object> selectedObjects) {
        this.context = context;
        this.selectedObjects = selectedObjects;
    }

    @Override
    public SecondGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.image_gallery_item, parent, false);

        return new SecondGalleryViewHolder(itemView, context);

    }


    @Override
    public void onBindViewHolder(@NonNull SecondGalleryViewHolder holder, int position) {
        holder.bind(selectedObjects.get(position), context);
    }

    @Override
    public int getItemCount() {
        return selectedObjects.size();
    }
}
