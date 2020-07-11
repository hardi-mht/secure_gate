package com.securegate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 2000;


    private FirebaseAuth mAuth;
    private FirebaseUser userr;

    private Button invisbutton;

    Executor executor = Executors.newSingleThreadExecutor();

    final MainActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       invisbutton = findViewById(R.id.invisible);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser().isEmailVerified()) {

             /*   invisbutton.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {

                                                       Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                                                       BiometricPrompt biometricPrompt1 = new BiometricPrompt.Builder(MainActivity.this)
                                                               .setTitle("Authenticaton")
                                                               .setSubtitle("Subtitles")
                                                               .setDescription("Description")
                                                               .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {

                                                                   @Override
                                                                   public void onClick(DialogInterface dialogueinterface, int i) {

                                                                       finish();

                                                                   }
                                                               }).build();

                                                       biometricPrompt1.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {

                                                           @Override
                                                           public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {

                                                               activity.runOnUiThread(new Runnable() {
                                                                   @Override
                                                                   public void run() {
                                                                       Toast.makeText(MainActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();
                                                                       invisbutton.setEnabled(false);
                                                                       new Handler().postDelayed(new Runnable() {
                                                                           @Override
                                                                           public void run() {
                                                                               Intent dashintent = new Intent(MainActivity.this, DashboardActivity.class);
                                                                               startActivity(dashintent);
                                                                               finish();
                                                                               Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();


                                                                           }
                                                                       }, SPLASH_TIMEOUT);

                                                                   }
                                                               });

                                                           }
                                                       });
                                                   }
                                               }); */
                  /*      BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(MainActivity.this)
                                .setTitle("Authenticaton")
                                .setSubtitle("Subtitles")
                                .setDescription("Description")
                                .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogueinterface, int i) {

                                        finish();

                                    }
                                }).build();


                        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {

                            @Override
                            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent dashintent = new Intent(MainActivity.this, DashboardActivity.class);
                                                startActivity(dashintent);
                                                finish();
                                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                                            }
                                        }, SPLASH_TIMEOUT);

                                    }
                                });


                            }
                        }); */

                Intent dashintent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(dashintent);
                finish();
                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                    }
            } else {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent logg = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logg);
                        finish();
                    }
                }, SPLASH_TIMEOUT);
            }

        }
    }