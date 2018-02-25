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
    private int imgIterator;

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
        bar.setProgress(0);
        barText=findViewById(R.id.seekBarAmt);

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

        if(reset.isCursorVisible()) {
            reset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    img.setImageBitmap(myBitmap);
                    start = 0;
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

                    if (imgIterator == 10) {
                        Bitmap selectedimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
                        ImageProcessor alteredimg = new ImageProcessor(selectedimg);
                        Toast.makeText(PlayActivity.this, "Image melting...", Toast.LENGTH_SHORT).show();
                        img.setImageBitmap(alteredimg.insertionSort());
                    }
                    else {
                        Bitmap selectedimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
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

                                default:
                                    break;
                            }
                        }
                        else {
                            Toast.makeText(PlayActivity.this, "Image melted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        //Toast.makeText(this, "entered method:  "+resultCode+" "+Activity.RESULT_OK, Toast.LENGTH_SHORT).show();
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && resultCode == GALLERY_REQUEST) {
            final Uri imageURI = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                float hwRat = (float) selectedImage.getHeight() / selectedImage.getWidth();
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