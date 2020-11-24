package com.example.studentsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class HomeActivity extends AppCompatActivity {

    ImageView logout;
    UserSessionManager sessionManager;
    RelativeLayout addData,viewData,mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout=findViewById(R.id.logout);

        addData=findViewById(R.id.rl1);

        viewData=findViewById(R.id.rl2);

        mapView=findViewById(R.id.rl3);

        // User Session Manager
        sessionManager = new UserSessionManager(getApplicationContext());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Clear the User session data
                // and redirect user to LoginActivity
                sessionManager.logoutUser();
            }
        });

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this,AddDataActivity.class));
            }
        });


    }
}
