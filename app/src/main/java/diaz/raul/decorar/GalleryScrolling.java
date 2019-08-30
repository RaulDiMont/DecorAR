package diaz.raul.decorar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class GalleryScrolling extends AppCompatActivity {
    private RecyclerView galleryRecycler;
    private GalleryListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        galleryRecycler = findViewById(R.id.galleryRecyclerView);

        String[] array = getResources().getStringArray(R.array.gallery_list);
        List<String> galleryList = Arrays.asList(array);


        galleryRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GalleryListAdapter(this, galleryList);
        galleryRecycler.setAdapter(adapter);




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
