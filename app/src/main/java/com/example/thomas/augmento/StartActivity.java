package com.example.thomas.augmento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button signInButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocalStorage localStorage=new LocalStorage(getApplicationContext());
        /*if(localStorage.getStorage(getString(R.string.username))!=null)
        {
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }*/


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        signInButton=findViewById(R.id.signInButton);
        signUpButton=findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SignUp1Activity.class);
                startActivity(intent);
            }
        });
    }
}
