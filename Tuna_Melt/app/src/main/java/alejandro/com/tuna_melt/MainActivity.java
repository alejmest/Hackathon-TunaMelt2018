package alejandro.com.tuna_melt;

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

                Intent photoPickerIntent = new Intent();
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent,"Select Picture:"), 0);

            }

        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        Toast.makeText(this, "entered method:  "+resultCode+" "+Activity.RESULT_OK, Toast.LENGTH_SHORT).show();
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && resultCode == GALLERY_REQUEST)
        {
            if(data==null||data.getData()==null)
            {
                Toast.makeText(MainActivity.this,"nothing in data",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(MainActivity.this,"something in data: "+data.toString().substring(0,10),Toast.LENGTH_LONG).show();
                try {
                    final Uri imageURI = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);


                    ImageView img = findViewById(R.id.unaltered);
                    img.setImageBitmap(bitmap);

                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            Toast.makeText(this, "error:  "+resultCode+" "+Activity.RESULT_OK, Toast.LENGTH_SHORT).show();
        }
    }
}

