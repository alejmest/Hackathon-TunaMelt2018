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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    private Bitmap myBitmap;
    private boolean mIsError = false;
    private ImageView img;
    private Button imgsel, imgmelt, reset;
    private RadioGroup sortgroup;
    private int GALLERY_REQUEST=-1;
    int start=0;
    private SeekBar bar;
    private TextView barText;
    private RadioGroup hv;
    private int imgIterator;
    private int sortedRows,sortedCols;
    private int partitions;
    float hwRat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        imgsel = findViewById(R.id.imgsel);
        imgmelt = findViewById(R.id.imgmelt);
        img = findViewById(R.id.unaltered);
        reset = findViewById(R.id.reset);
        sortgroup = findViewById(R.id.radioSort);
        bar=findViewById(R.id.incAmt);
        bar.setMax(10);
        bar.setProgress(5);
        barText=findViewById(R.id.seekBarAmt);
        hv=findViewById(R.id.horizVert);
        sortedRows=0;
        partitions=1;
        sortedCols=0;
        imgmelt.setVisibility(View.GONE);//make the melt button invisible until an image is selected
        reset.setVisibility(View.GONE);
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

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                barText.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        hv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId){
                int hCheck=hv.getCheckedRadioButtonId();
                if(hCheck==R.id.fullsort){
                    sortgroup.setVisibility(View.VISIBLE);
                }else{
                    sortgroup.setVisibility(View.INVISIBLE);
                }
            }
        });
        if(reset.isCursorVisible()) {
            reset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    img.setImageBitmap(myBitmap);
                    start = 0;
                    sortedCols=0;
                    sortedRows=0;
                    partitions=1;
                }
            });
        }

        if(imgmelt.isCursorVisible()){
            imgmelt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                /*
                //Melts the whole image
                Bitmap selectedimg = ((BitmapDrawable)img.getDrawable()).getBitmap();
                ImageProcessor alteredimg = new ImageProcessor(selectedimg);
                Toast.makeText(MainActivity.this, "Image melting...", Toast.LENGTH_SHORT).show();
                img.setImageBitmap(alteredimg.insertionSort());
                */
                    try {
                        imgIterator = Integer.parseInt(barText.getText().toString());
                    } catch (Exception e) {
                        imgIterator = 0;
                    }
                    Bitmap image=((BitmapDrawable)img.getDrawable()).getBitmap();
                    boolean horizontal=false;
                    int hCheck=hv.getCheckedRadioButtonId();
                    switch(hCheck)
                    {
                        case R.id.fullsort:
                            Bitmap selectedimg = myBitmap;
                            ImageProcessor alteredimg = new ImageProcessor(selectedimg);
                            boolean notDone = !alteredimg.isDone;
                            if (notDone) {
                                int checked = sortgroup.getCheckedRadioButtonId();
                                switch (checked) {
                                    case R.id.selection://melts by parts, selection
                                        selectedimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
                                        alteredimg = new ImageProcessor(selectedimg);
                                        img.setImageBitmap(alteredimg.partialSelectionSort(start, 1000 * (imgIterator + 1)));
                                        start += 1000 * (imgIterator + 1);
                                        notDone = !alteredimg.isDone;
                                        break;

                                    case R.id.insertion://melts by parts, insertion
                                        selectedimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
                                        alteredimg = new ImageProcessor(selectedimg);
                                        img.setImageBitmap(alteredimg.partialInsertionSort(start, 1000 * (imgIterator + 1)));
                                        start += 1000 * (imgIterator + 1);
                                        notDone = !alteredimg.isDone;
                                        break;

                                    case R.id.merge://melts by parts, merge
                                        selectedimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
                                        alteredimg = new ImageProcessor(selectedimg);
                                        img.setImageBitmap(alteredimg.mergeSort());
                                        //start += 1000 * (imgIterator + 1);
                                        notDone = !alteredimg.isDone;
                                        break;

                                    default:
                                        break;
                                }

                            }
                            /*else
                            {
                                for(int x=0;x<myBitmap.getWidth();x++)
                                {
                                    image = vertPartialSelectionSort(image, x);
                                }
                                img.setImageBitmap(image);
                            }*/
                        case R.id.horizontal:

                            horizontal=true;
                            break;
                        default:

                            break;
                    }

                    if(imgIterator==10)
                    {
                        if (horizontal)
                        {
                            for (int x = 0; x < myBitmap.getHeight(); x++)
                            {
                                image = horzPartialSelectionSort(image, x * myBitmap.getWidth());
                            }
                            img.setImageBitmap(image);
                        }
                        else
                        {
                            for(int x=0;x<myBitmap.getWidth();x++)
                            {
                                image = vertPartialSelectionSort(image, x);
                            }
                            img.setImageBitmap(image);
                        }
                    }
                    else
                    {
                        try{
                            partitions=calculatePartitionSize(imgIterator,horizontal,myBitmap.getHeight(),myBitmap.getWidth(),hwRat);
                        }catch(Exception e)
                        {
                            Toast.makeText(PlayActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }

                        int sortCheck=hv.getCheckedRadioButtonId();
                        switch(sortCheck)
                        {
                            case R.id.horizontal:
                                if(sortedRows+partitions>myBitmap.getHeight())
                                {
                                    for(int x=sortedRows;x<myBitmap.getHeight();x++)
                                    {
                                        image = horzPartialSelectionSort(image, x * myBitmap.getWidth());
                                    }
                                    Toast.makeText(PlayActivity.this,"Fully glitched",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    for (int x = sortedRows; x < partitions + sortedRows; x++)
                                    {
                                        image = horzPartialSelectionSort(image, x * myBitmap.getWidth());
                                    }
                                    sortedRows += partitions;
                                }
                                img.setImageBitmap(image);
                                break;
                            case R.id.vertical:
                                if(sortedCols+partitions>myBitmap.getWidth())
                                {
                                    for(int x=sortedCols;x<myBitmap.getWidth();x++)
                                    {
                                        image=vertPartialSelectionSort(image,x);
                                    }
                                    Toast.makeText(PlayActivity.this,"Fully glitched",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    for (int x = sortedCols; x < partitions + sortedCols; x++)
                                    {
                                        image = vertPartialSelectionSort(image, x);
                                    }
                                    sortedCols += partitions;
                                }
                                img.setImageBitmap(image);
                                break;
                            default:
                                break;
                        }
                    }
                }
            });
        }
    }
    public Bitmap vertPartialSelectionSort(Bitmap mImage, int start){
        boolean isDone=false;
        if(mImage == null) {
            return null;
        }

        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);

        //selection sort on the pixels
        for(int i = start; i < pixels.length; i+=width){
            int min_idx=i;
            for(int j=i+1; j<pixels.length; j+=width){
                if(pixels[j] < pixels[min_idx]){
                    min_idx=j;
                }
            }
            //swap the min with the first element
            int temp = pixels[min_idx];
            pixels[min_idx] = pixels[i];
            pixels[i] = temp;
        }

        Bitmap newImage = Bitmap.createBitmap(width, height, mImage.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return newImage;
    }
    public Bitmap horzPartialSelectionSort(Bitmap mImage,int start)
    {
        boolean isDone=false;
        if(mImage == null) {
            return null;
        }

        int width = mImage.getWidth();
        int height = mImage.getHeight();

        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);

        //selection sort on the pixels
        for(int i = start; i < mImage.getWidth()+start; i++){
            int min_idx=i;
            for(int j=i+1; j<mImage.getWidth()+start; j++){
                if(pixels[j] < pixels[min_idx]){
                    min_idx=j;
                }
            }
            //swap the min with the first element
            int temp = pixels[min_idx];
            pixels[min_idx] = pixels[i];
            pixels[i] = temp;
        }

        Bitmap newImage = Bitmap.createBitmap(width, height, mImage.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return newImage;
    }
    int calculatePartitionSize(int speed,boolean horizontal,int h,int w,float ratio)
    {
        if(horizontal)
            return h/(40-(4*speed));
        else
            return w/(30-(2*speed));
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        //Toast.makeText(this, "entered method:  "+resultCode+" "+Activity.RESULT_OK, Toast.LENGTH_SHORT).show();
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && resultCode == GALLERY_REQUEST) {
            final Uri imageURI = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                hwRat = (float) selectedImage.getHeight() / selectedImage.getWidth();
                if (selectedImage.getHeight() >= 420 || selectedImage.getWidth() >= 300) {
                    Toast.makeText(PlayActivity.this, "Image too large!(" + selectedImage.getWidth() + "," + selectedImage.getHeight() + "). Resizing...", Toast.LENGTH_LONG).show();
                    selectedImage = resizeBitmap(selectedImage, 300, Math.round(300 * hwRat));
                }
                start = 0;
                myBitmap = selectedImage;
                img.setImageBitmap(selectedImage);
                imgmelt.setVisibility(View.VISIBLE);//make the melt button visible
                reset.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        } else {
            //Toast.makeText(MainActivity.this, "you haven't picked an image  " + resultCode + "  " + GALLERY_REQUEST, Toast.LENGTH_LONG).show();
        }
    }


    public Bitmap resizeBitmap(Bitmap large, int newW, int newH) {
        int width = large.getWidth();
        int height = large.getHeight();
        float scaleWidth = ((float) newW) / width;
        float scaleHeight = ((float) newH) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resized = Bitmap.createBitmap(large, 0, 0, width, height, matrix, false);
        large.recycle();
        return resized;

    }
}