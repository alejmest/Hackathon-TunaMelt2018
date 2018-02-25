package alejandro.com.tuna_melt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Gina on 2/25/2018.
 */

public class LearnActivity extends AppCompatActivity {
    private Button selection, insertion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_activity);

        selection = findViewById(R.id.selection);
        insertion = findViewById(R.id.insertion);

        selection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goPlay = new Intent(LearnActivity.this, SelectionActivity.class);
                startActivity(goPlay);
            }
        });


        insertion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goLearn = new Intent(LearnActivity.this, InsertionActivity.class);
                startActivity(goLearn);
            }
        });

    }


}
