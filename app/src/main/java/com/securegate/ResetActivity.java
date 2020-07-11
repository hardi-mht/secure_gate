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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ResetActivity extends AppCompatActivity {

    private Button changemail, changepass, removeuser, signout, sendresetmail;

    Button sign, userauthokay;
    EditText userchangefield1, userchangefield2, userauthfield;

    private static final String TAG = "ResetActivity`";

    Dialog epicdialogue,epicd1;

    private FirebaseAuth mAuth;

    FirebaseUser userr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        epicdialogue = new Dialog(this);
        epicd1 = new Dialog(this);

        initializeUI();

        mAuth = FirebaseAuth.getInstance();
        userr = FirebaseAuth.getInstance().getCurrentUser();

        changemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                permpass();

                userauthokay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String passtring = userauthfield.getText().toString();
                        AuthCredential credential = EmailAuthProvider.getCredential(userr.getEmail(), passtring);

                        if (TextUtils.isEmpty(passtring)) {
                            Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (passtring.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        } else {

                            userr.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        epicd1.dismiss();

                                        epicdialogue.setContentView(R.layout.custom_popup);
                                        sign = epicdialogue.findViewById(R.id.userchangeokay);
                                        userchangefield1 = epicdialogue.findViewById(R.id.userchangefield1);
                                        userchangefield2 = epicdialogue.findViewById(R.id.userchangefield2);

                                        userchangefield1.setHint("Enter existing Email");
                                        userchangefield2.setHint("Enter new Email");

                                        epicdialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        epicdialogue.show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Incorrect Password!", Toast.LENGTH_SHORT).show();
                                    }

                                    sign.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            final String changemail = userchangefield1.getText().toString();
                                            final String changemail1 = userchangefield2.getText().toString();

                                            // Get auth credentials from the user for re-authentication
                                            AuthCredential credential = EmailAuthProvider
                                                    .getCredential(Objects.requireNonNull(userr.getEmail()), passtring); // Current Login Credentials \\

                                            // Prompt the user to re-provide their sign-in credentials
                                            userr.reauthenticate(credential)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            //Now change your email address \\
                                                            //----------------Code for Changing Email Address----------\\

                                                            if (changemail.equals(userr.getEmail())) {
                                                                if (changemail1.equals(changemail)) {
                                                                    Toast.makeText(getApplicationContext(), "Please enter another email", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    epicdialogue.dismiss();
                                                                    Toast.makeText(getApplicationContext(), "Email updated", Toast.LENGTH_SHORT).show();

                                                                    userr.updateEmail(changemail1)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {

                                                                                        userr.sendEmailVerification()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Log.d(TAG, "Email sent.");
                                                                                                        }
                                                                                                    }
                                                                                                });

                                                                                        Toast.makeText(getApplicationContext(), "Please verify and Re-login", Toast.LENGTH_SHORT).show();
                                                                                        mAuth.signOut();
                                                                                        Intent loggedout = new Intent(ResetActivity.this, LoginActivity.class);
                                                                                        startActivity(loggedout);
                                                                                        finish();

                                                                                    } else {
                                                                                        Toast.makeText(getApplicationContext(), "Email change failure!", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Existing mail incorrect!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permpass();

                userauthokay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String passtring = userauthfield.getText().toString();
                        if (TextUtils.isEmpty(passtring)) {
                            Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (passtring.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        } else {
                            AuthCredential credential = EmailAuthProvider.getCredential(userr.getEmail(), passtring);

                            userr.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        epicd1.dismiss();

                                        epicdialogue.setContentView(R.layout.custom_popup);
                                        sign = epicdialogue.findViewById(R.id.userchangeokay);
                                        userchangefield1 = epicdialogue.findViewById(R.id.userchangefield1);
                                        userchangefield2 = epicdialogue.findViewById(R.id.userchangefield2);

                                        userchangefield1.setHint("Enter New Password");
                                        userchangefield2.setHint("Reenter New Password");

                                        String newpass = userchangefield1.getText().toString();
                                        String newpass1 = userchangefield2.getText().toString();

                                        Objects.requireNonNull(epicdialogue.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        epicdialogue.show();
                                    } else {
                                        Toast.makeText(ResetActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }

                                    sign.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String newpass = userchangefield1.getText().toString();
                                            final String newpass1 = userchangefield2.getText().toString();

                                            if (TextUtils.isEmpty(newpass) || TextUtils.isEmpty(newpass1)) {
                                                Toast.makeText(getApplicationContext(), "Passwords are required!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (newpass.length() < 6 || newpass1.length() < 6) {
                                                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                                            }

                                            if (newpass.equals(newpass1)) {
                                                AuthCredential credential = EmailAuthProvider
                                                        .getCredential(Objects.requireNonNull(userr.getEmail()), passtring); // Current Login Credentials \\

                                                // Prompt the user to re-provide their sign-in credentials
                                                userr.reauthenticate(credential)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {
                                                                    userr.updatePassword(newpass1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                epicdialogue.dismiss();
                                                                                Toast.makeText(ResetActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                                            } else {
                                                                                Toast.makeText(ResetActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(ResetActivity.this, "Your Passwords doesn't match", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });

        sendresetmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               permpass();

                userauthokay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String passtring = userauthfield.getText().toString();
                        if (TextUtils.isEmpty(passtring)) {
                            Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (passtring.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        } else {
                            AuthCredential credential = EmailAuthProvider.getCredential(userr.getEmail(), passtring);

                            userr.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        epicd1.dismiss();

                                        mAuth.sendPasswordResetEmail(userr.getEmail())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "Email sent.");
                                                            Toast.makeText(ResetActivity.this, "Reset Email sent", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(ResetActivity.this, "An Error Occured", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        removeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permpass();

                userauthokay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String passtring = userauthfield.getText().toString();
                        if (TextUtils.isEmpty(passtring)) {
                            Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (passtring.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        } else {
                            AuthCredential credential = EmailAuthProvider.getCredential(userr.getEmail(), passtring);

                            userr.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        epicd1.dismiss();

                                        userr.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(ResetActivity.this, "Account Deleted Succesfully", Toast.LENGTH_SHORT).show();
                                                    Intent AcEnd = new Intent(ResetActivity.this,LoginActivity.class);
                                                    startActivity(AcEnd);
                                                    finish();

                                                } else {
                                                    Toast.makeText(ResetActivity.this, "Account Deletion Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent loggedout1 =  new Intent(ResetActivity.this,LoginActivity.class);
                startActivity(loggedout1);
                finish();
            }

        });
    }

    private void initializeUI() {
        changemail = findViewById(R.id.changemail);
        changepass = findViewById(R.id.changepassword);
        sendresetmail = findViewById(R.id.sendresetmail);
        removeuser = findViewById(R.id.removeuser);
        signout = findViewById(R.id.signout);
    }

    private void permpass(){

        epicd1.setContentView(R.layout.custom_password);
        userauthokay = epicd1.findViewById(R.id.userauthokay);
        userauthfield = epicd1.findViewById(R.id.userauthfield);

        userauthfield.setHint("Enter Account Password");

        epicd1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicd1.show();

    }
}
