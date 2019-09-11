package diaz.raul.decorar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private RecyclerView listRecycler;
    private RecyclerListAdapter adapter;
    private Gson gsonIn;
    private Gson gsonOut;
    private List<Object> listaObjetos;
    private Context context;
    File tempFile = null;
    String tempFileName = "_tempModelList";
    String localFileName = "_ModelList.json";
    String localModelDirName = "Models";
    String localImageDirName = "Images";


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference imagesStorageRef = storage.getReferenceFromUrl("gs://decorar-bb01c.appspot.com/Imagenes");
    StorageReference modelsStorageRef = storage.getReferenceFromUrl("gs://decorar-bb01c.appspot.com/Modelos");
    StorageReference jsonStorageRef = storage.getReferenceFromUrl("gs://decorar-bb01c.appspot.com").child("_ModelList.json");

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Opciones");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        inicializarGaleria1();
    }

    public void inicializarGaleria1() {
        listRecycler = findViewById(R.id.galleryRecyclerView);

        String[] array = getResources().getStringArray(R.array.settings_list);
        List<String> settingsList = Arrays.asList(array);

        listRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerListAdapter(this, settingsList);
        listRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sincronizar(View view) {
        Toast.makeText(this, "Sincronizando con la nube...", Toast.LENGTH_LONG).show();
        fromJSONtoList();
        actualizarJSON();

    }

    public void actualizarJSON() {

        //actualizamos JSON
        File localFile = new File(getFilesDir(), localFileName);
        String newFN = localFile.getAbsolutePath();

        jsonStorageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Fichero descargado correctamente

                        //readWriteJson();
                        Toast.makeText(context, "Se ha actualizado el archivo " + localFileName, Toast.LENGTH_SHORT).show();

                        //Actualizamos los modelos e imagenes si el JSON se ha actualizado correctamente
                        actualizarModelos();
                        actualizarImagenes();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                Toast.makeText(context, "No se ha podido descargar el .json " + localFileName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarModelos() {


        //actualizamos modelos
        Iterator<Object> iterator = listaObjetos.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            File modelsFolder = new File(getFilesDir() + File.separator + "Modelos");

            if (!modelsFolder.exists()) {
                modelsFolder.mkdir();
            }

            File modelFile = new File(modelsFolder, next.getFilePath());
            String newID = modelFile.getAbsolutePath();

            StorageReference modelStorageRef = storage.getReferenceFromUrl(next.getOnlineFilePath());
            modelStorageRef.getFile(modelFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            Toast.makeText(context, "Se han actualizado los modelos", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    Toast.makeText(context, "No se han podido actualizar los modelos", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void actualizarImagenes() {


        //actualizamos modelos
        Iterator<Object> iterator = listaObjetos.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            File imagesFolder = new File(getFilesDir() + File.separator + "Imagenes");

            if (!imagesFolder.exists()) {
                imagesFolder.mkdir();
            }

            File imageFile = new File(imagesFolder, next.getIconpath());
            String newID = imageFile.getAbsolutePath();

            StorageReference imageStorageRef = storage.getReferenceFromUrl(next.getOnlineIconPath());
            imageStorageRef.getFile(imageFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            Toast.makeText(context, "Se han actualizado los thumbnails", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    Toast.makeText(context, "No se han podido actualizar los thumbnails", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void readWriteJson() {
        try {
            FileInputStream fileInput = openFileInput(localFileName);
            int c;
            String inputFileString = "";
            while ((c = fileInput.read()) != -1) {
                inputFileString = inputFileString + Character.toString((char) c);
            }
            fileInput.close();
            FileOutputStream fileOutput = openFileOutput(localFileName, Context.MODE_PRIVATE);

            fileOutput.write(inputFileString.getBytes());
            fileOutput.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
            Toast.makeText(this, "Error al pasar de JSON a lista de objetos", Toast.LENGTH_LONG).show();

        }

    }
}
