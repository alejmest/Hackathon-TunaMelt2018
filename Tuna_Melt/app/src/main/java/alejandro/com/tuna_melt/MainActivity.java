package alejandro.com.tuna_melt;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import java.io.IOException;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.net.Uri;

public class MainActivity extends AppCompatActivity
{


    ImageView img;
    Button imgsel;
    int GALLERY_REQUEST=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgsel = findViewById(R.id.imgselButton);
        img = findViewById(R.id.unaltered);
        img.setImageResource(R.drawable.ic_launcher_background);
        imgsel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 0);
                Toast.makeText(MainActivity.this, "image selected", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        Toast.makeText(this, "entered method:  "+resultCode+" "+Activity.RESULT_OK, Toast.LENGTH_SHORT).show();
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && resultCode == GALLERY_REQUEST) {
            final Uri imageURI = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                img.setImageBitmap(selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
        else {
            Toast.makeText(MainActivity.this, "you haven't picked an image  " + resultCode + "  " + GALLERY_REQUEST, Toast.LENGTH_LONG).show();
        }
    }
}

