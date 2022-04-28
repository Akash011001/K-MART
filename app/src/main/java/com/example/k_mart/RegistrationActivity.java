package com.example.k_mart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    String verificationId;
    FirebaseFirestore db;

    EditText nameInput, panInput, emailInput, mobileInput, locationInput;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.register_name);
        panInput = findViewById(R.id.register_pan);
        emailInput = findViewById(R.id.register_email);
        mobileInput = findViewById(R.id.register_mobile);
        locationInput = findViewById(R.id.register_location);
        bar = findViewById(R.id.registration_activity_progressbar);

        Button verify = (Button) findViewById(R.id.verify_btn_Registration);




        if(mauth.getCurrentUser()!=null){
            Toast.makeText(RegistrationActivity.this, mauth.getUid(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(RegistrationActivity.this, "not logged in", Toast.LENGTH_LONG).show();
        }


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameInput.getText().toString().isEmpty() && panInput.getText().toString().isEmpty() && emailInput.getText().toString().isEmpty() && locationInput.getText().toString().isEmpty() && mobileInput.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "field can not be empty", Toast.LENGTH_LONG).show();
                }else{
                    bar.setVisibility(View.VISIBLE);
                    sendVerificationCode("+91"+mobileInput.getText().toString().trim());
                }



            }
        });


    }

    private void sendVerificationCode(String phone){

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mauth).setPhoneNumber(phone).setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(RegistrationActivity.this).setCallbacks(mCallbacks).build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(

    ) {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(RegistrationActivity.this, s, Toast.LENGTH_LONG).show();
            verificationId = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(RegistrationActivity.this, "code: "+ phoneAuthCredential.getSmsCode(), Toast.LENGTH_LONG).show();
            String code = phoneAuthCredential.getSmsCode();
            if(code !=null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            bar.setVisibility(View.GONE);
            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mauth.signInWithCredential(credential).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //firestore
                    addData(nameInput.getText().toString(), panInput.getText().toString(), emailInput.getText().toString(), locationInput.getText().toString(), mobileInput.getText().toString());
                    Intent i = new Intent(RegistrationActivity.this, PlatformActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else {

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(RegistrationActivity.this, "invalid code", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void addData(String name, String pan, String email, String location, String mobile){

        if(!(name.isEmpty() && pan.isEmpty() && email.isEmpty() && location.isEmpty() && mobile.isEmpty())){

            String[] emailTest = email.split("@");

            if (emailTest != null && emailTest.length == 2 && emailTest[1].equals("gmail.com")) {
                Map<String, Object> user = new HashMap<>();
                user.put("Name", name);
                user.put("Pan", pan);
                user.put("Email", email);
                user.put("Location", location);
                user.put("Mobile", mobile);

                db.collection("users").document(mauth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegistrationActivity.this, "registered", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    }
                });

            }else{
                Toast.makeText(getApplicationContext(), "Email is incorrect!", Toast.LENGTH_SHORT).show();
            }

            }else {
            Toast.makeText(getApplicationContext(), "Field can not be empty", Toast.LENGTH_SHORT).show();

        }

    }
}