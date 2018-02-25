package alejandro.com.tuna_melt;

/**
 * Created by Gina on 2/24/2018.
 */

import android.graphics.Bitmap;
import android.os.SystemClock;



public class ImageProcessor {
    Bitmap mImage;
    boolean mIsError = false, isDone = false;

    public ImageProcessor(final Bitmap image) {
        mImage = image.copy(image.getConfig(), image.isMutable());
        if(mImage == null) {
            mIsError = true;
        }
    }

    public boolean isError() {
        return mIsError;
    }

    public void setImage(final Bitmap image) {
        mImage = image.copy(image.getConfig(), image.isMutable());
        if(mImage == null) {
            mIsError = true;
        } else {
            mIsError = false;
        }
    }

    public Bitmap getImage() {
        if(mImage == null){
            return null;
        }
        return mImage.copy(mImage.getConfig(), mImage.isMutable());
    }

    public void free() {
        if(mImage != null && !mImage.isRecycled()) {
            mImage.recycle();
            mImage = null;
        }
    }

    public Bitmap replaceColor(int fromColor, int targetColor) {
        if(mImage == null) {
            return null;
        }

        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int x = 0; x < pixels.length; ++x) {
            pixels[x] = (pixels[x] == fromColor) ? targetColor : pixels[x];
        }

        Bitmap newImage = Bitmap.createBitmap(width, height, mImage.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);

        return newImage;
    }

    //perform a selection sort on the pixels
    public Bitmap selectionSort(){
        if(mImage == null) {
            return null;
        }

        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);

        //selection sort on the pixels
        for(int i = 0; i < pixels.length; i++){
            int min_idx=i;
            for(int j=i+1; j<pixels.length; j++){
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

    //perform a selection sort from i=start to i=stop
    public Bitmap partialSelectionSort(int start, int incr){
        if(mImage == null) {
            return null;
        }

        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);
        int stop = incr+start;
        if(start>pixels.length)start=pixels.length;
        if(stop>=pixels.length){
            isDone = true;
            stop=pixels.length;
        }

        //selection sort on the pixels
        for(int i = start; i < stop; i++){
            int min_idx=i;
            for(int j=i+1; j<pixels.length; j++){
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
        SystemClock.sleep(1000);
        return newImage;
    }


}