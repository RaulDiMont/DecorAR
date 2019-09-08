package diaz.raul.decorar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.lang.ref.WeakReference;

public class PreviewModelActivity extends AppCompatActivity implements Scene.OnUpdateListener {

    //Declaración de variables
    private Object chosenObject;
    private SceneView previewSceneView;
    private Scene previewScene;
    private TextView nameTextView;
    private float vectorRotacion = 0;
    private Node previewNode = new Node();
    private Uri previewUri;
    private ModelLoader modelLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previewmodel);

        //Añadimos la función de back en la toolbar por comodidad
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Obtenemos el objeto que se nos pasa desde la actividad Secondgallery
        Intent intent = getIntent();
        chosenObject = (Object) intent.getSerializableExtra("objeto_seleccionado");

        //Obtenemos la URI del fichero .sfb del modelo que se mostrará en la escena
        previewUri = Uri.parse(chosenObject.getFilepath());

        //Inicializamos las variables con las views e items del layout
        previewSceneView = findViewById(R.id.scene_view);
        nameTextView = findViewById(R.id.nameTextView);
        previewScene = previewSceneView.getScene();
        Camera camera = previewScene.getCamera();

        //Añadimos un listener para el método onUpdate descrito más abajo
        previewScene.addOnUpdateListener(this::onUpdate);

        //Establecemos la posición de la cámara en función del objeto a representar
        switch (chosenObject.getFilepath()) {
            case "chair_1.sfb":
                camera.setLocalRotation(Quaternion.axisAngle(Vector3.left(), 20.0f));
                previewNode.setName("Silla de comedor");
                break;

            case "table_1.sfb":
                camera.setLocalRotation(Quaternion.axisAngle(Vector3.left(), 20.0f));
                previewNode.setName("Mesa de comedor");
                break;

            case "picture_1.sfb":
                camera.setLocalRotation(Quaternion.axisAngle(Vector3.left(), 10.0f));
                previewNode.setName("Cuadro");
                break;
        }

        //Renderizamos en objeto en la escena creando un modelLoader y pasándole la Uri del .sfb
        modelLoader = new ModelLoader(new WeakReference<>(this), null);
        modelLoader.render3DModel(previewUri);
        nameTextView.setText(chosenObject.getNombre());
    }

    //En el método setNodeOnScene creamos el nodo que utilizaremos para crear el renderizable
    //de nuestro modelo en la escena
    public void setNodeOnScene(ModelRenderable model) {

        previewNode.setParent(previewSceneView.getScene());

        //Establecemos la posición del nodo en la escena en función del objetoa representar

        switch (chosenObject.getFilepath()) {
            case "chair_1.sfb":
                previewNode.setLocalPosition(new Vector3(0f, -2.5f, -3.5f));
                previewNode.setLocalScale(new Vector3(2.7f, 2.7f, 2.7f));
                break;

            case "table_1.sfb":
                previewNode.setLocalPosition(new Vector3(0f, -2.5f, -3.5f));
                previewNode.setLocalScale(new Vector3(2.7f, 2.7f, 2.7f));
                break;

            case "picture_1.sfb":
                previewNode.setLocalPosition(new Vector3(0f, 0.0f, -3f));
                previewNode.setLocalScale(new Vector3(2.7f, 2.7f, 2.7f));
                previewNode.setLookDirection(Vector3.down());
                break;
        }

        //Pasamos el modelo elegido para renderizar
        previewNode.setRenderable(model);
        //Establecemos el nodo creado como hijo de la escena en la que lo vamos a situar
        previewSceneView.getScene().addChild(previewNode);

    }

    //En el método onUpdate implementaremos la rotación del modelo en la escena
    @Override
    public void onUpdate(FrameTime frameTime) {

        if (vectorRotacion == 1) vectorRotacion = 0f;
        vectorRotacion = vectorRotacion + 0.3f;

        //Establecemos la forma en la que rota el modelo en en función del modelo que se está representando
        switch (chosenObject.getFilepath()) {
            case "chair_1.sfb":
                previewNode.setLocalRotation(Quaternion.eulerAngles(new Vector3(0, vectorRotacion, 0)));
                break;

            case "table_1.sfb":
                previewNode.setLocalRotation(Quaternion.eulerAngles(new Vector3(0, vectorRotacion, 0)));
                break;

            case "picture_1.sfb":
                previewNode.setLocalRotation(Quaternion.eulerAngles(new Vector3(-90, 180, vectorRotacion)));
                break;
        }
        //
    }

    @Override
    protected void onPause() {
        super.onPause();
        previewSceneView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            previewSceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();

        }
    }

    //En el método onOptionsItemSelected implementamos que el item añadido a la toolbar
    //vuelva exactamente a la anterior actividad tal y como quedó antes de pasar a la presente
    //Esto es necesario porque, si solo indicasemos en el manifest su parent activity, iniciaría
    //ésta en lugar de recuperarla

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
