package in.astudentzone.akash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataEntry extends AppCompatActivity {
    private Button submitNewDataBtn;
    private DatabaseReference dbRef;
    private FirebaseDatabase fireDb;
    private ModelEntry modelEntry;
    private ArrayList<ModelEntry> list;
    private TextView applicationCount, pendingData, approvedData, rejectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        init();

        modelEntry = new ModelEntry();
        list = new ArrayList<>();
        fireDb = FirebaseDatabase.getInstance();
        dbRef = fireDb.getReference();

        submitNewDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataEntry.this, NewDataEntry.class));
            }
        });

    }

    private void getCountValues() {
        dbRef.child("property_entry").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("pending_data")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pendingData.setText(String.valueOf(snapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("approve_data")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        approvedData.setText(String.valueOf(snapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("rejected_data")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        rejectedData.setText(String.valueOf(snapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {
        submitNewDataBtn = findViewById(R.id.submit_newDataBtn);
        applicationCount = findViewById(R.id.count_application_submit_txt);
        pendingData = findViewById(R.id.count_pending_approval_txt);
        approvedData = findViewById(R.id.count_application_approved_txt);
        rejectedData = findViewById(R.id.count_application_rejected_txt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCountValues();
    }
}