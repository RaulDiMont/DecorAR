package diaz.raul.decorar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public class PreviewModelActivity extends AppCompatActivity implements Scene.OnUpdateListener {



    private Gson gson;
    private List<Object> listaObjetos;
    private String chosenObject = "chair_1.sfb";
    private String name = new String();
    private SceneView previewSceneView;
    private Scene previewScene;
    private TextView nameTextView;
    private float degrees = 0;

    Node previewNode = new Node();
    Uri previewUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_model);

        gson = new Gson();

        try {
            InputStream stream = getAssets().open("_ModelList.json");
            InputStreamReader reader = new InputStreamReader(stream);
            listaObjetos = gson.fromJson(reader, List.class);
        }
        catch (
                IOException e) {
            Toast.makeText(this, "No he podido leer _ModelList.json", Toast.LENGTH_SHORT)
                    .show();
        }
        Iterator iterator = listaObjetos.iterator();

/**********************************FORMAS DE HACER LOOP CON LA LISTA*************************/
/*

        while (iterator.hasNext()){
            Object next = (Object) iterator.next();
        }
*/

/*

        for (Object next : listaObjetos){

        }
*/

/*

        for (int i=0; i<listaObjetos.size();i++){
            Object next = listaObjetos.get(i);
        }
*/
/****************************************************************************************/

        previewSceneView = findViewById(R.id.scene_view);
        nameTextView = findViewById(R.id.nameTextView);
        previewScene = previewSceneView.getScene();
        Camera camera = previewScene.getCamera();

        previewScene.addOnUpdateListener(this::onUpdate);

        /*for (String chosenObject: listaObjetos.contains()){
            if (listaObjetos.)
        }*/

        switch (chosenObject){
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

        previewUri = Uri.parse(chosenObject);
        render3DModel(previewUri);
        String nodeName = previewNode.getName();
        nameTextView.setText(previewNode.getName());
    }

    @Override
    protected void onPause(){
        super.onPause();
        previewSceneView.pause();

    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            previewSceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();

        }
    }

    private void render3DModel(Uri uri){
        ModelRenderable.builder()
                .setSource(this, uri)
                .build()
                .thenAccept(
                        renderable -> setNodeOnScene(renderable)
                )
                .exceptionally((throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage())
                            .setTitle("Error!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;
                }));

    }

    private void setNodeOnScene(ModelRenderable model){

        previewNode.setParent(previewSceneView.getScene());

        switch (chosenObject){
            case "chair_1.sfb":
                previewNode.setLocalPosition(new Vector3(0f, -2.5f, -3.5f));
                previewNode.setLocalScale(new Vector3(2.7f, 2.7f, 2.7f));
                break;

            case "table_1.sfb":
                previewNode.setLocalPosition(new Vector3(0f, -2.5f, -3.5f));
                previewNode.setLocalScale(new Vector3(2.7f, 2.7f, 2.7f));
                break;

            case "picture_1.sfb":
                previewNode.setLocalPosition(new Vector3(0f, -2.5f, -3.5f));
                previewNode.setLocalScale(new Vector3(2.7f, 2.7f, 2.7f));
                previewNode.setLookDirection(Vector3.down());
                break;
        }
        previewNode.setRenderable(model);

        previewSceneView.getScene().addChild(previewNode);

    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        if(degrees == 1) degrees=0f;
        degrees = degrees + 0.3f;

        switch (chosenObject){
            case "chair_1.sfb":
                previewNode.setLocalRotation(Quaternion.eulerAngles(new Vector3(0,degrees,0)));
                break;

            case "table_1.sfb":
                previewNode.setLocalRotation(Quaternion.eulerAngles(new Vector3(0,degrees,0)));
                break;

            case "picture_1.sfb":
                previewNode.setLocalRotation(Quaternion.eulerAngles(new Vector3(-90,180,degrees)));
                break;
        }

    }
}
