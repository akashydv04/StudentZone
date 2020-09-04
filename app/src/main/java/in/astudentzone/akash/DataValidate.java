package in.astudentzone.akash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DataValidate extends AppCompatActivity {

    private RecyclerView dataRecVeu;
    private DatabaseReference dbRef;
    private FirebaseDatabase fireDb;
    private ArrayList<ModelEntry> list;
    private ModelEntry modelEntry;
    private DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_validate);

        dataRecVeu = findViewById(R.id.data_list_rec_Veu);

        fireDb = FirebaseDatabase.getInstance();
        dbRef = fireDb.getReference().child("property_entry");
        list = new ArrayList<>();
        modelEntry = new ModelEntry();
        adapter = new DataAdapter(this, R.layout.data_list_card, list);

    }

    private void getAllData(){
        list.clear();
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    modelEntry = snapshot1.getValue(ModelEntry.class);
                    list.add(modelEntry);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(DataValidate.this);
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                dataRecVeu.setLayoutManager(layoutManager);
                dataRecVeu.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllData();
    }
}