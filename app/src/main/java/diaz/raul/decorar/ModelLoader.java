package diaz.raul.decorar;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.lang.ref.WeakReference;

public class ModelLoader {
    private final WeakReference<PreviewModelActivity> ownerPreview;
    private final WeakReference<MainActivity> ownerMain;


    ModelLoader(WeakReference<PreviewModelActivity> ownerPreview, WeakReference<MainActivity> ownerMain) {
        this.ownerPreview = ownerPreview;
        this.ownerMain = ownerMain;
    }

    void render3DModel(Uri uri) {
        if (ownerPreview.get() == null) {
            Log.d("ModelLoader", "Activity is null.  Cannot load model.");
            return;
        }
        Context context = ownerPreview.get();
        ModelRenderable.builder()
                .setSource(context, uri)
                .build()
                .thenAccept(
                        renderable -> {
                            PreviewModelActivity previewActivity = ownerPreview.get();
                            previewActivity.setNodeOnScene(renderable);
                        }
                )
                .exceptionally((throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(throwable.getMessage())
                            .setTitle("Error!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;
                }));
        return;
    }

    void render3DModel(Anchor anchor, Uri uri) {
        if (ownerMain.get() == null) {
            Log.d("ModelLoader", "Actividad nula. No se puede cargar el modelo");
            return;
        }
        Context context = ownerMain.get();
        ModelRenderable.builder()
                .setSource(context, uri)
                .build()
                .thenAccept(
                        renderable -> {
                            MainActivity mainActivity = ownerMain.get();
                            mainActivity.addNodeToScene(anchor, renderable);
                        }
                )
                .exceptionally((throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(throwable.getMessage())
                            .setTitle("Error!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;
                }));
        return;
    }

}
