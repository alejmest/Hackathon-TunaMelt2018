package alejandro.com.tuna_melt;

/**
 * Created by Gina on 2/24/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

public class Animate extends View{
    Canvas drawCanvas;
    Bitmap bm;
    boolean isDone = false;
    int start=0;
    public Animate(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
        ImageView img = findViewById(R.id.unaltered);
        //bm = MainActivity.myBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
/*
        Rect myrect = new Rect(0, 0, canvas.getWidth(),canvas.getHeight()/2);
        Paint pa = new Paint();
        pa.setColor(Color.BLUE);
        pa.setStyle(Paint.Style.FILL);
        canvas.drawRect(myrect, pa);
*/
/*
        if(!isDone){
            ImageProcessor alteredimg = new ImageProcessor(this.getDrawingCache());
            bm = alteredimg.partialSelectionSort(start, 500);
            start += 500;
        }
*/

        ImageProcessor alteredimg = new ImageProcessor(this.getDrawingCache());
        bm = alteredimg.selectionSort();

        canvas.drawBitmap(bm.copy(Bitmap.Config.ARGB_8888, true), 0, 0, new Paint());
        //SystemClock.sleep(1000);

        invalidate();//calls this method again and again
    }


}