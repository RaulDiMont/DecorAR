package diaz.raul.decorar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private AnchorNode chosenNode;
    private Uri previewUri;
    private Session arSession;
    public Toolbar toolbar;
    private FloatingActionButton deleteButton;
    private FloatingActionButton galleryButton;

    //Primera galería
    private RecyclerView galleryRecycler;
    private RecyclerListAdapter adapter;
    private View includeFragment;

    //Segunda galeria
    private RecyclerView imageGalleryRecyclerView;
    private Galeria2Adapter secondAdapter;
    private List<Object> listaObjetos;

    private Gson gson;
    String localFileName = "_ModelList.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Decor[AR]");
        deleteButton = findViewById(R.id.deleteNode_button);
        galleryButton = findViewById(R.id.to_gallery_button);
        galleryButton.show();

        deleteButton.hide();

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getColor(android.R.color.transparent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        fromJSONtoList();

        inicializarGaleria1();

        inicializarGaleria2();

        //getWindow().setStatusBarColor(Color.TRANSPARENT);

        includeFragment = findViewById(R.id.include_fragment);

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
                        chosenNode = null;
                        deleteButton.hide();
                        Toast.makeText(MainActivity.this, "Seleccione un objeto de la galería", Toast.LENGTH_LONG).show();

                        return;
                    }

                    switch (chosenObject.getSuperficie()) {
                        case "Techo":
                            if (plane.getType().equals(Plane.Type.HORIZONTAL_DOWNWARD_FACING)) {
                                //Obtenemos la URI del fichero .sfb del modelo que se mostrará en la escena
                                previewUri = Uri.parse(chosenObject.getFilePath());
                                //Posicionamos el modelo renderizano en la escena
                                modelLoader.render3DModel(hitResult.createAnchor(), previewUri);
                            } else
                                Toast.makeText(this, "El objeto " + chosenObject.getNombre() + " solo puede situarse en el techo", Toast.LENGTH_LONG).show();
                            break;
                        case "Suelo":
                            if (plane.getType().equals(Plane.Type.HORIZONTAL_UPWARD_FACING)) {
                                //Obtenemos la URI del fichero .sfb del modelo que se mostrará en la escena
                                previewUri = Uri.parse(chosenObject.getFilePath());
                                //Posicionamos el modelo renderizano en la escena
                                modelLoader.render3DModel(hitResult.createAnchor(), previewUri);
                            } else
                                Toast.makeText(this, "El objeto " + chosenObject.getNombre() + " solo puede situarse en el suelo", Toast.LENGTH_LONG).show();

                            break;
                        case "Pared":
                            if (plane.getType().equals(Plane.Type.VERTICAL)) {
                                //Obtenemos la URI del fichero .sfb del modelo que se mostrará en la escena
                                previewUri = Uri.parse(chosenObject.getFilePath());
                                //Posicionamos el modelo renderizano en la escena
                                modelLoader.render3DModel(hitResult.createAnchor(), previewUri);
                            } else
                                Toast.makeText(this, "El objeto " + chosenObject.getNombre() + " solo puede situarse en la pared", Toast.LENGTH_LONG).show();

                            break;
                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        galleryButton.show();
        fromJSONtoList();
    }


    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        chosenNode = anchorNode;
        deleteButton.show();
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        node.setName(chosenObject.getNombre());

        node.setOnTapListener((v, event) -> {
            chosenNode = anchorNode;
            deleteButton.show();
        });
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
        setChosenObject(null);
    }

    public void onClickToGallery(View view) {
        toGallery1();
    }

    public void toGallery1() {
        deleteButton.hide();

        //Flecha atrás de la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Elige decoración");
        toolbar.setBackgroundColor(getColor(R.color.colorPrimaryDark));
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

        toolbar.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        toolbar.setTitle("Elige un objeto");
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

        toolbar.setBackgroundColor(getColor(android.R.color.transparent));
        toolbar.setTitle("Decor[AR]");
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
            InputStream stream = openFileInput(localFileName);
            InputStreamReader reader = new InputStreamReader(stream);
            listaObjetos = gson.fromJson(reader, listaObjetosType);
        } catch (
                IOException e) {
            Toast.makeText(this, "Por favor, sincronice datos desde Configuracion", Toast.LENGTH_LONG).show();
            galleryButton.hide();
        }

    }

    public void inicializarGaleria1() {
        galleryRecycler = findViewById(R.id.galleryRecyclerView);

        String[] array = getResources().getStringArray(R.array.gallery_list);
        List<String> galleryList = Arrays.asList(array);

        galleryRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerListAdapter(this, galleryList);
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
        secondAdapter = new Galeria2Adapter(this, objetosElegidos);
        imageGalleryRecyclerView.setAdapter(secondAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
                return true;
            case R.id.settings_menu_item:
                Intent toSettingsIntent = new Intent(this, SettingsActivity.class);
                this.startActivity(toSettingsIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setChosenObject(Object object) {
        this.chosenObject = object;
    }

    public Context getContext() {
        return this;
    }

    public Session getArSession() {
        return arSession;
    }

    public void deleteNode(View view) {
        if (chosenNode.getAnchor() != null) {
            fragment.getArSceneView().getScene().removeChild(chosenNode);
            chosenNode.getAnchor().detach();
            chosenNode.setParent(null);
            deleteButton.hide();
        }
    }
}
