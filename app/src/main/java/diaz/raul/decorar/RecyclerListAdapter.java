package diaz.raul.decorar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListViewHolder> {
    Context context;
    List<String> gallerylist;

    public RecyclerListAdapter(Context context, List<String> gallerylist) {
        this.context = context;
        this.gallerylist = gallerylist;
    }

    @NonNull
    @Override
    public RecyclerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_galeria1, parent, false);
        return new RecyclerListViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListViewHolder holder, int position) {
        holder.bind(gallerylist.get(position));
    }

    @Override
    public int getItemCount() {
        return gallerylist.size();
    }
}