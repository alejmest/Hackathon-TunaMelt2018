package alejandro.com.tuna_melt;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageProcessor {
    Bitmap mImage;
    boolean mIsError = false, isDone=false;
    int[] pixels;

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
    public Bitmap selectionSorter(){
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
                if(pixels[j] > pixels[min_idx]){
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
        return newImage;
    }

    public Bitmap insertionSort(){
        if(mImage == null) {
            return null;
        }

        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int i=1; i<pixels.length; i++){
            int key = pixels[i];
            int j=i-1;
            while(j>=0 && pixels[j]>key){
                pixels[j+1] = pixels[j];
                j --;
            }
            pixels[j+1] = key;
        }

        Bitmap newImage = Bitmap.createBitmap(width, height, mImage.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);

        return newImage;
    }

    public Bitmap partialInsertionSort(int start, int incr){
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

        for(int i=1; i<stop; i++){
            int key = pixels[i];
            int j=i-1;
            while(j>=0 && pixels[j]>key){
                pixels[j+1] = pixels[j];
                j --;
            }
            pixels[j+1] = key;
        }

        Bitmap newImage = Bitmap.createBitmap(width, height, mImage.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);

        return newImage;
    }


    public Bitmap mergeSort(){
        if(mImage == null) {
            return null;
        }

        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);
        int r = width*height;
        sort(pixels, 0, r-1);

        Bitmap newImage = Bitmap.createBitmap(width, height, mImage.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);
        isDone = true;
        return newImage;
    }

    private void merge(int[] myList, int l, int m, int r){
        int n1=m-l+1;
        int n2=r-m;
        //create temp arrays
        int L[]=new int[n1];
        int R[]=new int[n2];
        //copy to temp arrays
        for(int i=0; i<n1; ++i){
            L[i]=myList[l+i];
        }
        for(int j=0; j<n2; ++j){
            R[j]=myList[m+j+1];
        }
        //merge the temp arrays
        int i=0, j=0;
        int k=l;
        while(i<n1 && j<n2){
            if(L[i]<=R[i]){
                myList[k] = L[i];
                i++;
            }
            else{
                myList[k] = R[j];
                j++;
            }
            k++;
        }
        while(i<n1){
            myList[k] = L[i];
            i++;
            k++;
        }
        while(j<n2){
            myList[k] = R[j];
            j++;
            k++;
        }
    }
    private void sort(int[] myList, int l, int r){
        if(l<r){
            int m=(l+r)/2;//middle
            sort(myList, l, m);//sort first half
            sort(myList, m+1, r);//sort second half
            merge(myList, l, m, r);//merge the sorted halves
        }
    }


}
