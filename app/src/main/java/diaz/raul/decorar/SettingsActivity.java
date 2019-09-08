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

public class SettingsActivity extends AppCompatActivity {

    private RecyclerView listRecycler;
    private RecyclerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        inicializarGaleria1();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void inicializarGaleria1() {
        listRecycler = findViewById(R.id.galleryRecyclerView);

        String[] array = getResources().getStringArray(R.array.settings_list);
        List<String> settingsList = Arrays.asList(array);

        listRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerListAdapter(this, settingsList);
        listRecycler.setAdapter(adapter);
    }
}
