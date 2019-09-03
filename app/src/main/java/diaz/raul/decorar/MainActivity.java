package diaz.raul.decorar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ArFragment fragment;
    private ModelLoader modelLoader;
    private Object chosenObject;
    private Uri previewUri;
    private Session arSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        //Obtenemos el objeto que se nos pasa desde la actividad Secondgallery
        Intent intent = getIntent();
        chosenObject = (Object) intent.getSerializableExtra("objeto_seleccionado");

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
                        Toast.makeText(MainActivity.this, "Primero seleccione un objeto de la galería", Toast.LENGTH_LONG);
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
        Intent modelViewIntent = new Intent(this, GalleryScrolling.class);
        startActivityForResult(modelViewIntent, 0);
    }
}
