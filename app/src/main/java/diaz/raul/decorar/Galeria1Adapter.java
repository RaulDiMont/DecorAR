package diaz.raul.decorar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Galeria1Adapter extends RecyclerView.Adapter<Galeria1ViewHolder> {
    Context context;
    List<String> gallerylist;

    public Galeria1Adapter(Context context, List<String> gallerylist) {
        this.context = context;
        this.gallerylist = gallerylist;
    }

    @NonNull
    @Override
    public Galeria1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_galeria1, parent, false);
        return new Galeria1ViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull Galeria1ViewHolder holder, int position) {
        holder.bind(gallerylist.get(position));
    }

    @Override
    public int getItemCount() {
        return gallerylist.size();
    }
}