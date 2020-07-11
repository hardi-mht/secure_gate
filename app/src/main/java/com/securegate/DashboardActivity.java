package com.securegate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private Button activitynav,householdnav,communitynav;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private ImageView signmeout;
    private long backPressedtime;
    private TextView usert;
    private TextView curruser;
    private FirebaseUser userr;

   private Fragment fragment = null;



    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        activitynav = findViewById(R.id.activitynav);
        householdnav = findViewById(R.id.householdnav);
        communitynav =  findViewById(R.id.communitynav);

        fragment = new Activitytab();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.placeholder, fragment);
        transaction.addToBackStack(null);
        transaction.commit();


        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {

                if (view == findViewById(R.id.activitynav)) {
                    householdnav.setBackgroundColor(Color.TRANSPARENT);
                    householdnav.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
                    activitynav.setBackgroundResource(R.drawable.current_sel);
                    activitynav.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                    communitynav.setBackgroundColor(Color.TRANSPARENT);
                    communitynav.setTextColor(getApplicationContext().getResources().getColor(R.color.white));

                    fragment = new Activitytab();

                }
                if (view == findViewById(R.id.householdnav)) {
                    activitynav.setBackgroundColor(Color.TRANSPARENT);
                    activitynav.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
                    householdnav.setBackgroundResource(R.drawable.current_sel);
                    householdnav.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                    communitynav.setBackgroundColor(Color.TRANSPARENT);
                    communitynav.setTextColor(getApplicationContext().getResources().getColor(R.color.white));

                    fragment = new Householdtab();

                }
                if (view == findViewById(R.id.communitynav)) {
                    activitynav.setBackgroundColor(Color.TRANSPARENT);
                    activitynav.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
                    communitynav.setBackgroundResource(R.drawable.current_sel);
                    communitynav.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                    householdnav.setBackgroundColor(Color.TRANSPARENT);
                    householdnav.setTextColor(getApplicationContext().getResources().getColor(R.color.white));

                    fragment = new Communitytab();
                }

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.placeholder, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        activitynav.setOnClickListener(listener);
        householdnav.setOnClickListener(listener);
        communitynav.setOnClickListener(listener);


        mAuth = FirebaseAuth.getInstance();
        userr = FirebaseAuth.getInstance().getCurrentUser();
        initializeUI();


/*        Intent intent = getIntent();
        String str = intent.getStringExtra("message");
        usert.setText(str); */

        signmeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i= new Intent(DashboardActivity.this,ResetActivity.class);
                startActivity(i);

             /*  signOut();
                Intent i= new Intent(DashboardActivity.this,LoginActivity.class);
                startActivity(i);
                finish(); */
            }
        });
    }

    //sign out method
    public void signOut() {
        mAuth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(backPressedtime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to Exit", Toast.LENGTH_SHORT).show();
        }
        backPressedtime= System.currentTimeMillis();
    }

    private void initializeUI() {
        signmeout = findViewById(R.id.dashboardsettings);
        curruser = findViewById(R.id.houseinfo);
    }

}
