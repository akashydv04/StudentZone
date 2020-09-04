package in.astudentzone.akash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewProperty extends AppCompatActivity {
    private Intent intent;
    private TextView propertyName, preferredLanguage, locationName, localityName, ownerNumber, ownerName, applicationStatus;
    private Spinner changeStatusSpin;
    private Button callOwner_btn, updateStatus_btn;
    private String pName, planguage, locateName, localName, ownName, ownNum, appStatus;
    private String [] statusSet= {"Select status", "Pending", "Approved", "Rejected"};
    private ArrayAdapter adapter;
    private ModelEntry modelEntry;
    private FirebaseDatabase fireDb;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);
        init();

        intent = getIntent();

        getAllData();
        modelEntry = new ModelEntry();
        fireDb = FirebaseDatabase.getInstance();

        dbRef = fireDb.getReference().child("property_entry");
        updateStatus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelEntry.setMobileNumber(ownNum);
                modelEntry.setPropertyName(pName);
                modelEntry.setCityName(locateName);
                modelEntry.setLocalityName(localName);
                modelEntry.setMobileNumber(ownNum);
                modelEntry.setOwnerName(ownName);
                modelEntry.setPreferredLanguage(planguage);
                modelEntry.setApplicationStatus(changeStatusSpin.getSelectedItem().toString());

                if (changeStatusSpin.getSelectedItem().toString() == "Select status"){
                    Toast.makeText(ViewProperty.this, "Status Not changed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (changeStatusSpin.getSelectedItem().toString().equals("Pending")){
                    applicationStatus.setText(changeStatusSpin.getSelectedItem().toString());
                    FirebaseDatabase.getInstance().getReference().child("approve_data").child(ownNum).child(ownNum).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("rejected_data").child(ownNum).child(ownNum).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("pending_data").child(ownNum).child(ownNum).setValue(modelEntry);
                    dbRef.child(ownNum).child(ownNum).setValue(modelEntry);
                    Toast.makeText(ViewProperty.this, "Status Updated", Toast.LENGTH_SHORT).show();
                }
                if (changeStatusSpin.getSelectedItem().toString().equals("Approved")){
                    applicationStatus.setText(changeStatusSpin.getSelectedItem().toString());
                    FirebaseDatabase.getInstance().getReference().child("pending_data").child(ownNum).child(ownNum).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("rejected_data").child(ownNum).child(ownNum).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("approve_data").child(ownNum).child(ownNum).setValue(modelEntry);
                    dbRef.child(ownNum).child(ownNum)
                            .child("applicationStatus").setValue(changeStatusSpin.getSelectedItem().toString());
                    Toast.makeText(ViewProperty.this, "Status Updated", Toast.LENGTH_SHORT).show();
                }
                if (changeStatusSpin.getSelectedItem().toString().equals("Rejected")){
                    applicationStatus.setText(changeStatusSpin.getSelectedItem().toString());
                    FirebaseDatabase.getInstance().getReference().child("pending_data").child(ownNum).child(ownNum).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("approve_data").child(ownNum).child(ownNum).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("rejected_data").child(ownNum).child(ownNum).setValue(modelEntry);
                    dbRef.child(ownNum).child(ownNum).setValue(modelEntry);
                    Toast.makeText(ViewProperty.this, "Status Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        callOwner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ownNum));
                startActivity(intent);
            }
        });

    }

    private void getAllData() {
        pName = intent.getStringExtra("property_name");
        planguage = intent.getStringExtra("preferred_language");
        locateName = intent.getStringExtra("location_name");
        localName = intent.getStringExtra("locality_name");
        ownName = intent.getStringExtra("owner_name");
        ownNum = intent.getStringExtra("owner_number");
        appStatus = intent.getStringExtra("application_status");

        propertyName.setText(pName);
        preferredLanguage.setText(planguage);
        locationName.setText(locateName);
        localityName.setText(localName);
        ownerName.setText(ownName);
        ownerNumber.setText(ownNum);
        applicationStatus.setText(appStatus);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusSet);
        changeStatusSpin.setAdapter(adapter);

    }

    private void init() {
        propertyName = findViewById(R.id.property_name_txt);
        preferredLanguage = findViewById(R.id.language_txt);
        locationName = findViewById(R.id.location_name_txt);
        localityName = findViewById(R.id.locality_txt);
        ownerNumber = findViewById(R.id.owner_number_txt);
        ownerName = findViewById(R.id.owner_name_txt);
        applicationStatus = findViewById(R.id.application_status_txt);
        changeStatusSpin = findViewById(R.id.change_status_spin);
        callOwner_btn = findViewById(R.id.call_owner_btn);
        updateStatus_btn = findViewById(R.id.update_btn);
    }
}