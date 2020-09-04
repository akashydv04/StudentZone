package in.astudentzone.akash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private EditText mobile_number_edt, otp_edt;
    private Button get_otp_btn, login_btn;
    private String mobileNumber, otpNum, codeSent;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mAuth = FirebaseAuth.getInstance();

        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNumber = mobile_number_edt.getText().toString().trim();
                if (mobileNumber.isEmpty()){
                    mobile_number_edt.setError("required");
                    return;
                }
                else {
                    getOtp(mobileNumber);
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpNum = otp_edt.getText().toString().trim();
                if (otpNum.isEmpty()){
                    otp_edt.setError("required");
                    otp_edt.requestFocus();
                    return;
                }
                verifyNumber(otpNum);
            }
        });


    }

    private void verifyNumber(String otpNum) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, otpNum);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            startActivity(new Intent(this, Home.class));
            finish();
        }
    }

    private void getOtp(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mobileNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private void init() {
        mobile_number_edt = findViewById(R.id.mobile_number);
        otp_edt = findViewById(R.id.edt_otp);
        get_otp_btn = findViewById(R.id.get_otp);
        login_btn = findViewById(R.id.login_btn);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            codeSent = s;
            Toast.makeText(MainActivity.this, "Otp Sent Successfully", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }
}