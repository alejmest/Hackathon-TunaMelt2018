package alejandro.com.tuna_melt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private Button playbtn, learnbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        playbtn = findViewById(R.id.playbtn);
        learnbtn = findViewById(R.id.learnbtn);

        playbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goPlay = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(goPlay);
            }
        });

        learnbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goLearn = new Intent(MainActivity.this, LearnActivity.class);
                startActivity(goLearn);
            }
        });

    }

}

