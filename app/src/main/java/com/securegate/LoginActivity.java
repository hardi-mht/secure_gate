package com.securegate;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class LoginActivity extends AppCompatActivity {

    private EditText loginfield1, loginfield2;
    private TextView loginforgotpassword;
    private Button loginbutton, tologin, tosignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initializeUI();

    /*    View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {

                if (view == findViewById(R.id.tosignup)) {
                    tologin.setBackgroundResource(R.drawable.btn_unsel);
                    tosignup.setBackgroundResource(R.drawable.pinky);
                }
                if (view == findViewById(R.id.tologin)) {
                    tosignup.setBackgroundResource(R.drawable.btn_unsel);
                    tologin.setBackgroundResource(R.drawable.pinky);

                }
            }
        };
        tologin.setOnClickListener(listener);
        tosignup.setOnClickListener(listener); */


        mAuth = FirebaseAuth.getInstance();



    /*    Executor executor = Executors.newSingleThreadExecutor();
     if (mAuth.getCurrentUser() != null) {
            if(mAuth.getCurrentUser().isEmailVerified()){

                BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(LoginActivity.this)
                .setTitle("Authenticaton")
                .setSubtitle("Subtitles")
                .setDescription("Description")
                .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogueinterface, int i) {

                    }
                }).build();

            final LoginActivity activity = this;
            biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"Authenticated", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });

        }


            } */

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

        loginforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toforgotpassword = new Intent(LoginActivity.this,Forgotpassword.class);
                startActivity(toforgotpassword);
            }
        });

        tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tosignup =  new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(tosignup);
            }
        });
    }
        private void loginUserAccount(){

            String email, password;
            email = loginfield1.getText().toString();
            password = loginfield2.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please verify!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        private void initializeUI () {
            loginfield1 = findViewById(R.id.loginfield1);
            loginfield2 = findViewById(R.id.loginfield2);
            loginbutton = findViewById(R.id.loginbutton);
            loginforgotpassword = findViewById(R.id.loginforgotpassword);

            tologin = (Button) findViewById(R.id.tologin);
            tosignup = (Button) findViewById(R.id.tosignup);

        }
    }

      /*  emailTV.addTextChangedListener(LoginWatcher);
        passwordTV.addTextChangedListener(LoginWatcher); */

 /*   private TextWatcher LoginWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String email, password;
            email = emailTV.getText().toString().trim();
            password = passwordTV.getText().toString().trim();

            loginBtn.setEnabled(!email.isEmpty() && !password.isEmpty());
            if(loginBtn.isEnabled()){
                loginBtn.setTextColor(getApplicationContext().getResources().getColor(R.color.redanger));
            } else {
                loginBtn.setTextColor(getApplicationContext().getResources().getColor(R.color.grey));
            }



        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }; */


