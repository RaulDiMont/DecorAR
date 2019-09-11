package diaz.raul.decorar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Galeria2Activity extends AppCompatActivity {

    private RecyclerView imageGalleryRecyclerView;
    private Galeria2Adapter adapter;
    private List<Object> listaObjetos;
    private Gson gson;
    private String modelType;
    private String localFileName = "_ModelList.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Galería");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        modelType = intent.getStringExtra("tipoModelo");

        ///Utilizamos Gson para crear Objects a partir del JSON

        gson = new Gson();

        Type listaObjetosType = new TypeToken<List<Object>>() {
        }.getType();

        try {
            InputStream stream = openFileInput(localFileName);
            InputStreamReader reader = new InputStreamReader(stream);

            listaObjetos = gson.fromJson(reader, listaObjetosType);
        } catch (
                IOException e) {
            Toast.makeText(this, "No he podido leer _ModelList.json", Toast.LENGTH_SHORT)
                    .show();
        }

        //////////////////////////////////////////

        ///Creamos un iterador que comprobarála lista de objetos y extraerá una nueva lista
        // de las URIs de las imagenes de los Objetos 3D

        //Creamos un arrayList ya que la clase List<E> no es inicializable si darle valores
        List<Object> selectedObjects = new ArrayList<Object>();

        Iterator<Object> iterator = listaObjetos.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next.getTipo().equals(modelType))
                selectedObjects.add(next);
        }

        imageGalleryRecyclerView = findViewById(R.id.imageRecyclerView);
        imageGalleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        imageGalleryRecyclerView.setHasFixedSize(true);

        adapter = new Galeria2Adapter(this, selectedObjects);
        imageGalleryRecyclerView.setAdapter(adapter);


    }


}
