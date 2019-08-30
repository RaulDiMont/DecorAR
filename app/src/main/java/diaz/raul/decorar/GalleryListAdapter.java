package diaz.raul.decorar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListViewHolder> {
    Context context;
    List<String> gallerylist;

    public GalleryListAdapter(Context context, List<String> gallerylist) {
        this.context = context;
        this.gallerylist = gallerylist;
    }

    @NonNull
    @Override
    public GalleryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.gallery_item, parent, false);
        return new GalleryListViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryListViewHolder holder, int position) {
        holder.bind(gallerylist.get(position));
    }

    @Override
    public int getItemCount() {
        return gallerylist.size();
    }
}