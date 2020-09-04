package in.astudentzone.akash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewDataEntry extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout form_layout;
    private EditText client_mobile_number_edt, property_name_edt, city_name_edt, locality_edt, owner_name_edt, preferred_language_edt;
    private Button validate_number_btn, submitEntry;
    private DatabaseReference dbRef;
    private String mobileNumber, propertyName, cityName, localityName, ownerName, preferredLanguage;
    private FirebaseDatabase fireDb;
    private ModelEntry modelEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data_entry);
        init();

        fireDb = FirebaseDatabase.getInstance();
        dbRef = fireDb.getReference().child("property_entry");
        modelEntry = new ModelEntry();

        validate_number_btn.setOnClickListener(this);

        submitEntry.setOnClickListener(this);
    }

    private void validateNum() {
        mobileNumber = client_mobile_number_edt.getText().toString().trim();
        dbRef.child(mobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewDataEntry.this);
                    builder.setTitle("Warning")
                            .setMessage("User Already Exists!!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    form_layout.setVisibility(View.GONE);
                                }
                            }).show();
                }
                else {
                    form_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        form_layout = findViewById(R.id.form_panel);
        validate_number_btn = findViewById(R.id.validate_number_btn);
        client_mobile_number_edt = findViewById(R.id.client_mobile_number);
        property_name_edt = findViewById(R.id.property_name_edt);
        city_name_edt = findViewById(R.id.property_city_edt);
        locality_edt = findViewById(R.id.property_locality_edt);
        owner_name_edt = findViewById(R.id.owner_name_edt);
        preferred_language_edt = findViewById(R.id.preferred_language_edt);
        submitEntry = findViewById(R.id.submit_btn);
    }

    private void setAllData(){
        mobileNumber = client_mobile_number_edt.getText().toString().trim();
        propertyName = property_name_edt.getText().toString().trim();
        cityName = city_name_edt.getText().toString().trim();
        localityName = locality_edt.getText().toString().trim();
        ownerName = owner_name_edt.getText().toString().trim();
        preferredLanguage = preferred_language_edt.getText().toString().trim();

        modelEntry.setMobileNumber(mobileNumber);
        modelEntry.setPropertyName(propertyName);
        modelEntry.setCityName(cityName);
        modelEntry.setLocalityName(localityName);
        modelEntry.setOwnerName(ownerName);
        modelEntry.setPreferredLanguage(preferredLanguage);
        modelEntry.setApplicationStatus("Pending");
    }

    @Override
    protected void onStart() {
        super.onStart();
        form_layout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.validate_number_btn){
            validateNum();
        }
        if (view.getId() == R.id.submit_btn){
            setAllData();
            if (mobileNumber.isEmpty()){
                client_mobile_number_edt.setError("required");
                client_mobile_number_edt.requestFocus();
                return;
            }
            if (propertyName.isEmpty()){
                property_name_edt.setError("required");
                property_name_edt.requestFocus();
                return;
            }
            if (cityName.isEmpty()){
                city_name_edt.setError("required");
                city_name_edt.requestFocus();
                return;
            }
            if (localityName.isEmpty()){
                locality_edt.setError("required");
                locality_edt.requestFocus();
                return;
            }
            if (ownerName.isEmpty()){
                modelEntry.setOwnerName("NA");
                if (preferredLanguage.isEmpty()){
                    modelEntry.setPreferredLanguage("NA");
                    submitData();
                }
                else {
                    submitData();
                }
            }
            if (preferredLanguage.isEmpty()){
                modelEntry.setPreferredLanguage("NA");
                submitData();
            }
            else {
                submitData();
            }
        }
    }

    private void submitData() {
        dbRef.child(mobileNumber).child(mobileNumber).setValue(modelEntry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("pending_data").child(mobileNumber).child(mobileNumber).setValue(modelEntry);
                    client_mobile_number_edt.setText("");
                    property_name_edt.setText("");
                    city_name_edt.setText("");
                    locality_edt.setText("");
                    owner_name_edt.setText("");
                    preferred_language_edt.setText("");
                    form_layout.setVisibility(View.VISIBLE);

                    Toast.makeText(NewDataEntry.this, "Data Successfully Submitted!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(NewDataEntry.this, "Try Againg..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}