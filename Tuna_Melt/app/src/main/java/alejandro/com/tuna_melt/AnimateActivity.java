package alejandro.com.tuna_melt;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by Gina on 2/24/2018.
 */

public class AnimateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new Animate(this));
    }
}
