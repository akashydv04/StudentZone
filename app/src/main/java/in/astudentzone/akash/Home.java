package in.astudentzone.akash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Home extends AppCompatActivity {

    private Button dataEntry_btn, dataValidation_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        dataEntry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,DataEntry.class));
            }
        });

        dataValidation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,DataValidate.class));
            }
        });
    }

    private void init() {
        dataEntry_btn = findViewById(R.id.data_entry_btn);
        dataValidation_btn = findViewById(R.id.data_validation);
    }
}