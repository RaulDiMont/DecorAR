package diaz.raul.decorar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClickToGallery(View view) {
        Intent modelViewIntent = new Intent(this, GalleryScrolling.class);
        startActivityForResult(modelViewIntent, 0);
    }
}
