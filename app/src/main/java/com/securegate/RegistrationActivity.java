package com.securegate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userauthfield;
    private Button userauthokay;

    private EditText regusername,regemail,regpass;
    private Button regBtn;
    private TextView loginact;
    private static final String TAG = "RegistrationActivity`";

    Dialog dialogOTP;
    private FirebaseAuth AUTH;
    private FirebaseUser USER;
    private DatabaseReference reference;

    private String mVerificationId;

    String regphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        AUTH = FirebaseAuth.getInstance();
        USER = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        initializeUI();



        dialogOTP = new Dialog(this);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  regphone = regemail.getText().toString();

                if(regphone.isEmpty() || regphone.length() < 10){
                    regemail.setError("Enter a valid mobile");
                    regemail.requestFocus();
                }
                else {

                    sendVerificationCode(regphone);

                    userOTP();

                    userauthokay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String code = userauthfield.getText().toString().trim();
                            if (code.isEmpty() || code.length() < 6) {
                                userauthfield.setError("Enter valid code");
                                userauthfield.requestFocus();
                                return;
                            }
                            //verifying the code entered manually
                            verifyVerificationCode(code);

                        }
                    });
                } */

                loginUserAccount();
            }
        });

        loginact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(register);
                finish();
            }
        });

    }

    private void sendVerificationCode(String regphone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + this.regphone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                userauthfield.setText(code);
                //verifying the codez
                dialogOTP.dismiss();
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        AUTH.signInWithCredential(credential)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }


                        }
                    }
                });
    }





    private void loginUserAccount() {

        final String username, email, password;
        username = regusername.getText().toString().trim();
        email = regemail.getText().toString().trim();
        password = regpass.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter User Name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        AUTH.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegistrationActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {

                            String UID = AUTH.getCurrentUser().getUid();
                            DatabaseReference cureent_user_db = reference.child(UID);

                            cureent_user_db.child("username").setValue(username);
                            cureent_user_db.child("email").setValue(email);
                            cureent_user_db.child("password").setValue(password);

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                            }
                                        }
                                    });
                            Toast.makeText(getApplicationContext(), "Check your email!", Toast.LENGTH_LONG).show();



                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(RegistrationActivity.this, "Authentication failed! Email is already registered.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initializeUI() {
        regusername = findViewById(R.id.regcred0);
        regemail = findViewById(R.id.regcred1);
        regpass = findViewById(R.id.regcred2);
        regBtn = findViewById(R.id.regbutton);
        loginact = findViewById(R.id.backtologintext);
    }

    private void userOTP(){

        dialogOTP.setContentView(R.layout.custom_password);

        userauthfield = dialogOTP.findViewById(R.id.userauthfield);
        userauthokay = dialogOTP.findViewById(R.id.userauthokay);

        userauthfield.setHint("Enter OTP");

        dialogOTP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogOTP.show();

    }

}
