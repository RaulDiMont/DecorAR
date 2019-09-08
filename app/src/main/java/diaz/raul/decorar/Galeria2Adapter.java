package diaz.raul.decorar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class Galeria2Adapter extends RecyclerView.Adapter<Galeria2ViewHolder> {

    Context context;
    List<Object> selectedObjects;

    public Galeria2Adapter(Context context, List<Object> selectedObjects) {
        this.context = context;
        this.selectedObjects = selectedObjects;
    }

    @Override
    public Galeria2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_galeria2, parent, false);

        return new Galeria2ViewHolder(itemView, context);

    }


    @Override
    public void onBindViewHolder(@NonNull Galeria2ViewHolder holder, int position) {
        holder.bind(selectedObjects.get(position), context);
    }

    @Override
    public int getItemCount() {
        return selectedObjects.size();
    }
}
