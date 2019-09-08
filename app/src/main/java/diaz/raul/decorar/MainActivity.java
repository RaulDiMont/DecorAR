package diaz.raul.decorar;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArFragment fragment;
    private ModelLoader modelLoader;
    private Object chosenObject;
    private Uri previewUri;
    private Session arSession;

    //Primera galería
    private RecyclerView galleryRecycler;
    private GalleryListAdapter adapter;
    private View includeFragment;

    //Segunda galeria
    private RecyclerView imageGalleryRecyclerView;
    private SecondGalleryAdapter secondAdapter;
    private List<Object> listaObjetos;

    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        fromJSONtoList();

        inicializarGaleria1();

        inicializarGaleria2();

        //getWindow().setStatusBarColor(Color.TRANSPARENT);

        includeFragment = findViewById(R.id.include_fragment);


        //////////////////////////////inicializamos galería 1

/*
        galleryRecycler = findViewById(R.id.galleryRecyclerView);

        String[] array = getResources().getStringArray(R.array.gallery_list);
        List<String> galleryList = Arrays.asList(array);

        galleryRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GalleryListAdapter(this, galleryList);
        galleryRecycler.setAdapter(adapter);
        galleryRecycler.setVisibility(View.INVISIBLE);
        galleryRecycler.setFocusable(false);
        */
        /////////////////////////

        ///////////////////////////inicializamos galeria 2

        ///Utilizamos Gson para crear Objects a partir del JSON


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

        secondAdapter = new SecondGalleryAdapter(this, selectedObjects);
        imageGalleryRecyclerView.setAdapter(secondAdapter);
        imageGalleryRecyclerView.setVisibility(View.INVISIBLE);
        imageGalleryRecyclerView.setFocusable(false);
*/

        /////////////////////////////////////////////////////////////////////
/*

        //Obtenemos el objeto que se nos pasa desde la actividad Secondgallery
        Intent intent = getIntent();
        chosenObject = (Object) intent.getSerializableExtra("objeto_seleccionado");
*/

        //Asignamos a la variable fragment el "sceneform_fragment definido en el layout content_main
        fragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        //Creamos un modelLoader para más tarde renderizar el objeto en la escena
        modelLoader = new ModelLoader(null, new WeakReference<>(this));

        //Asignamos un listener al fragment para que cada vez que toquemos en un plano detectado
        //llame al método render3DModel del modelLoader, pasándole el anchor del punto en el que
        //hemos tocado y la URI del modelo a renderizar
        fragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (chosenObject == null) {
                        Toast.makeText(MainActivity.this, "Primero seleccione un objeto de la galería", Toast.LENGTH_LONG).show();
                        return;
                    }

                    //Obtenemos la URI del fichero .sfb del modelo que se mostrará en la escena
                    previewUri = Uri.parse(chosenObject.getFilepath());

                    modelLoader.render3DModel(hitResult.createAnchor(), previewUri);
                });

    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }

    public void onClickToGallery(View view) {
        toGallery1();
        ////////////////////A LA GALERÍA EXTERNA
        /*
        Intent modelViewIntent = new Intent(this, GalleryScrolling.class);
        startActivityForResult(modelViewIntent, 0);
        */
        ////////////////////////////////////////////////////////


        /*
        galleryRecycler.setVisibility(View.VISIBLE);
        galleryRecycler.setFocusable(true);
        includeFragment.setVisibility(View.INVISIBLE);
        includeFragment.setFocusable(false);
        */
/*

        imageGalleryRecyclerView.setVisibility(View.VISIBLE);
        imageGalleryRecyclerView.setFocusable(true);
        includeFragment.setVisibility(View.INVISIBLE);
        includeFragment.setFocusable(false);
*/


    }

    public void toGallery1() {

        //Flecha atrás de la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Galeria 1
        galleryRecycler.setVisibility(View.VISIBLE);
        galleryRecycler.setFocusable(true);
        //Galeria 2
        imageGalleryRecyclerView.setVisibility(View.INVISIBLE);
        imageGalleryRecyclerView.setFocusable(false);
        //Fragment AR
        includeFragment.setVisibility(View.INVISIBLE);
        includeFragment.setFocusable(false);

    }

    public void toGallery2() {
        //Galeria 1
        galleryRecycler.setVisibility(View.INVISIBLE);
        galleryRecycler.setFocusable(false);
        //Galeria 2
        imageGalleryRecyclerView.setVisibility(View.VISIBLE);
        imageGalleryRecyclerView.setFocusable(true);
        //Fragment AR
        includeFragment.setVisibility(View.INVISIBLE);
        includeFragment.setFocusable(false);

    }

    public void toFragmentAR() {
        //Galeria 1
        galleryRecycler.setVisibility(View.INVISIBLE);
        galleryRecycler.setFocusable(false);
        //Galeria 2
        imageGalleryRecyclerView.setVisibility(View.INVISIBLE);
        imageGalleryRecyclerView.setFocusable(false);
        //Fragment AR
        includeFragment.setVisibility(View.VISIBLE);
        includeFragment.setFocusable(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void fromJSONtoList() {
        gson = new Gson();

        Type listaObjetosType = new TypeToken<List<Object>>() {
        }.getType();

        try {
            InputStream stream = getAssets().open("_ModelList.json");
            InputStreamReader reader = new InputStreamReader(stream);

            listaObjetos = gson.fromJson(reader, listaObjetosType);
        } catch (
                IOException e) {
            Toast.makeText(this, "No se ha podido leer _ModelList.json", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void inicializarGaleria1() {
        galleryRecycler = findViewById(R.id.galleryRecyclerView);

        String[] array = getResources().getStringArray(R.array.gallery_list);
        List<String> galleryList = Arrays.asList(array);

        galleryRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GalleryListAdapter(this, galleryList);
        galleryRecycler.setAdapter(adapter);
        galleryRecycler.setVisibility(View.INVISIBLE);
        galleryRecycler.setFocusable(false);
    }

    public void inicializarGaleria2() {
        imageGalleryRecyclerView = findViewById(R.id.imageRecyclerView);
        imageGalleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        imageGalleryRecyclerView.setHasFixedSize(true);
        imageGalleryRecyclerView.setVisibility(View.INVISIBLE);
        imageGalleryRecyclerView.setFocusable(false);

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
        secondAdapter = new SecondGalleryAdapter(this, objetosElegidos);
        imageGalleryRecyclerView.setAdapter(secondAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Cuando se haga click en la flecha de la toolbar, volverá a ocultarsela galería y mostrarse el fragment AR
            case android.R.id.home:
                if (imageGalleryRecyclerView.getVisibility() == View.VISIBLE)
                    toGallery1();
                else {
                    if (galleryRecycler.getVisibility() == View.VISIBLE)
                        toFragmentAR();
                }
/*

                includeFragment.setVisibility(View.VISIBLE);
                includeFragment.setFocusable(true);
                galleryRecycler.setVisibility(View.INVISIBLE);
                galleryRecycler.setFocusable(false);
*/

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setChosenObject(Object object) {
        this.chosenObject = object;
    }


}
