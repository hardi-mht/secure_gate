package com.securegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class fragmentlogin extends Fragment {

    private EditText loginfield1, loginfield2;
    Button loginbutton;

    private FirebaseAuth mAuth;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragmentlogin, container, false);

        initializeUI();
        mAuth = FirebaseAuth.getInstance();


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Pressed", Toast.LENGTH_SHORT).show();

                loginUserAccount();
            }
        });
        return view;

    }

    private void loginUserAccount() {

        String email, password;
        email = loginfield1.getText().toString();
        password = loginfield2.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                Intent intent = new Intent(getContext(), DashboardActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Please verify!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void initializeUI() {
        loginfield1 = view.findViewById(R.id.loginfield1);
        loginfield2 = view.findViewById(R.id.loginfield2);
        loginbutton = view.findViewById(R.id.loginbutton);
        // loginforgotpassword=findViewById(R.id.forgotpass);

    }
}