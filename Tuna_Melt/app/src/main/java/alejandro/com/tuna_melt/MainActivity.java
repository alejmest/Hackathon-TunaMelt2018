package alejandro.com.tuna_melt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import java.io.FileNotFoundException;
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

        imgsel = findViewById(R.id.imgsel);
        img=findViewById(R.id.unaltered);
        img.setImageResource(R.drawable.ic_launcher_background);
        imgsel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                Toast.makeText(MainActivity.this, "image selected",Toast.LENGTH_LONG).show();
            }

        });
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK&&resultCode==GALLERY_REQUEST)
        {
            try {
                final Uri imageURI = data.getData();
                final InputStream imageStream =getContentResolver().openInputStream(imageURI);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


                img.setImageBitmap(selectedImage);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(MainActivity.this, "you haven't picked an image  "+resultCode+"  "+GALLERY_REQUEST, Toast.LENGTH_LONG).show();
        }
    }
}

