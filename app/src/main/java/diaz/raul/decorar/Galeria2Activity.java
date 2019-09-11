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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Galeria2Activity extends AppCompatActivity {

    private RecyclerView imageGalleryRecyclerView;
    private Galeria2Adapter secondAdapter;
    private List<Object> listaObjetos;
    private Gson gson;
    private String tipoObjeto;
    private String localFileName = "_ModelList.json";
    private String localFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Galería");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        localFilePath = getFilesDir() + File.separator + localFileName;

        Intent intent = getIntent();
        tipoObjeto = intent.getStringExtra("tipoModelo");

        ///Utilizamos Gson para crear Objects a partir del JSON

        fromJSONtoList();

        inicializarGaleria2();

        crearGaleria2(tipoObjeto);

        //////////////////////////////////////////

        ///Creamos un iterador que comprobarála lista de objetos y extraerá una nueva lista
        // de las URIs de las imagenes de los Objetos 3D
/*

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

*/

    }

    public void fromJSONtoList() {

        gson = new Gson();

        Type listaObjetosType = new TypeToken<List<Object>>() {
        }.getType();

        try {
            InputStream stream = openFileInput(localFileName);
            InputStreamReader reader = new InputStreamReader(stream);
            listaObjetos = gson.fromJson(reader, listaObjetosType);
        } catch (
                IOException e) {
            Toast.makeText(this, "Por favor, sincronice datos desde Configuracion", Toast.LENGTH_LONG).show();
        }

    }

    public void inicializarGaleria2() {
        imageGalleryRecyclerView = findViewById(R.id.imageRecyclerView);
        imageGalleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        imageGalleryRecyclerView.setHasFixedSize(true);

    }

    public void crearGaleria2(String tipoObjeto) {

        //Creamos un arrayList ya que la clase List<E> no es inicializable si darle valores

        List<Object> objetosElegidos = new ArrayList<Object>();

        Iterator<Object> iterator = listaObjetos.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next.getTipo().equals(tipoObjeto))
                objetosElegidos.add(next);
        }
        secondAdapter = new Galeria2Adapter(this, objetosElegidos);
        imageGalleryRecyclerView.setAdapter(secondAdapter);
    }

    public void actualizarGaleria(Object objectoEliminado) {
        Iterator<Object> iterator = listaObjetos.iterator();
        Object objectToDelete = new Object();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next.getFilePath().equals(objectoEliminado.getFilePath())) {
                objectToDelete = next;
                break;
            }
        }
        if (objectToDelete != null)
            listaObjetos.remove(objectToDelete);

        crearGaleria2(tipoObjeto);
        fromListToJson(listaObjetos);
    }

    public void fromListToJson(List<Object> nuevaListaObjetos) {

        gson = new Gson();

        // Java objects to File

        try (FileWriter writer = new FileWriter(localFilePath)) {
            gson.toJson(nuevaListaObjetos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
