package alejandro.com.tuna_melt;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.*;
import java.io.IOException;
import android.widget.Button;
import android.widget.ImageView;
import java.lang.Thread;
import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    private Bitmap mImage=null;
    private boolean mIsError = false;
    private ImageView img;
    private Button imgsel, imgmelt;
    private int GALLERY_REQUEST=-1;
    private int imgIterator;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgsel = findViewById(R.id.imgsel);
        imgmelt = findViewById(R.id.imgmelt);
        img = findViewById(R.id.unaltered);

        imgmelt.setVisibility(View.GONE);//make the melt button invisible until an image is selected
        img.setImageResource(R.drawable.ic_launcher_background);
        imgsel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 0);
                //Toast.makeText(MainActivity.this, "image selected", Toast.LENGTH_LONG).show();
            }

        });

        if(imgmelt.isCursorVisible()){
            imgmelt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bitmap selectedimg = ((BitmapDrawable)img.getDrawable()).getBitmap();
                    //ImageProcessor alteredimg = new ImageProcessor(selectedimg);
                    //Toast.makeText(MainActivity.this, "Image melting...", Toast.LENGTH_SHORT).show();
                    img.setImageBitmap(selectionSort(selectedimg));


                }
            });
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        //Toast.makeText(this, "entered method:  "+resultCode+" "+Activity.RESULT_OK, Toast.LENGTH_SHORT).show();
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && resultCode == GALLERY_REQUEST) {
            final Uri imageURI = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                float hwRat=(float)selectedImage.getHeight()/selectedImage.getWidth();
                if(selectedImage.getHeight()>=420||selectedImage.getWidth()>=300)
                {
                    Toast.makeText(MainActivity.this,"Image too large!("+selectedImage.getWidth()+","+selectedImage.getHeight()+"). Resizing...",Toast.LENGTH_LONG).show();
                    selectedImage=resizeBitmap(selectedImage,300,Math.round(300*hwRat));
                }
                img.setImageBitmap(selectedImage);
                imgmelt.setVisibility(View.VISIBLE);//make the melt button visible

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
        else {
            //Toast.makeText(MainActivity.this, "you haven't picked an image  " + resultCode + "  " + GALLERY_REQUEST, Toast.LENGTH_LONG).show();
        }
    }
    public Bitmap resizeBitmap(Bitmap large,int newW,int newH)
    {
        int width=large.getWidth();
        int height=large.getHeight();
        float scaleWidth=((float)newW)/width;
        float scaleHeight=((float)newH)/height;
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        Bitmap resized=Bitmap.createBitmap(large,0,0,width,height,matrix,false);
        large.recycle();
        return resized;

    }
    public Bitmap selectionSort(Bitmap mImage){
        if(mImage == null) {
            return null;
        }
        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);
        int stepSize=pixels.length/10;
        //selection sort on the pixels
        for(int i = 0; i < pixels.length; i++)
        {
            int min_idx=i;
            for(int j=i+1; j<pixels.length; j++){
                if(pixels[j] > pixels[min_idx]){
                    min_idx=j;
                }
            }
            //swap the min with the first element
            int temp = pixels[min_idx];
            pixels[min_idx] = pixels[i];
            pixels[i] = temp;
        }
        Bitmap newImage=Bitmap.createBitmap(width,height,mImage.getConfig());
        newImage.setPixels(pixels,0,width,0,0,width,height);
        return newImage;
    }

}

