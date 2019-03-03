package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    EditText emailEditText, usernameEditText, firstNameEditText, lastNameEditText,descriptionEditText;
    Button resetButton, saveButton, logoutButton;
    ImageView cancelButton;
    ProgressBar settingsProgressbar;

    private DatabaseReference settingsUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private String currentUserEmail;

    public void initialize()
    {
        emailEditText=findViewById(R.id.emailEditText);
        usernameEditText=findViewById(R.id.usernameEditText);
        firstNameEditText=findViewById(R.id.firstNameEditText);
        lastNameEditText=findViewById(R.id.lastNameEditText);
        descriptionEditText=findViewById(R.id.descriptionEditText);
        resetButton=findViewById(R.id.resetButton);
        saveButton=findViewById(R.id.saveButton);
        logoutButton=findViewById(R.id.logoutButton);
        cancelButton=findViewById(R.id.cancelButton);
        settingsProgressbar=findViewById(R.id.settingsProgressBar);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        currentUserEmail=mAuth.getCurrentUser().getEmail();

        emailEditText.setText(currentUserEmail);
        emailEditText.setEnabled(false);

        settingsUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
    }

    public void listeners()
    {
        settingsUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String userName=dataSnapshot.child("Username").getValue().toString();
                    String firstName=dataSnapshot.child("FirstName").getValue().toString();
                    String lastName=dataSnapshot.child("LastName").getValue().toString();
                    String description=dataSnapshot.child("Description").getValue().toString();

                    usernameEditText.setText(userName);
                    firstNameEditText.setText(firstName);
                    lastNameEditText.setText(lastName);
                    descriptionEditText.setText(description);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        resetButton.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(),"Cannot reset at the moment!",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(getApplicationContext(), AugmentActivity.class);
            startActivity(intent);
        });

        saveButton.setOnClickListener(v->{

            validateAccountInfo();
        });

        logoutButton.setOnClickListener(v->{
            logout();
        });

        cancelButton.setOnClickListener(v->{
            finish();
        });
    }

    private void validateAccountInfo()
    {
        String userName=usernameEditText.getText().toString();
        String firstName=firstNameEditText.getText().toString();
        String lastName=lastNameEditText.getText().toString();
        String description=descriptionEditText.getText().toString();

        if(TextUtils.isEmpty(userName))
        {
            usernameEditText.setError("Cannot be empty");
        }
        else if(TextUtils.isEmpty(firstName))
        {
            usernameEditText.setError("Cannot be empty");
        }
        else if(TextUtils.isEmpty(lastName))
        {
            usernameEditText.setError("Cannot be empty");
        }
        else
        {
            updateAccountInfo(userName, firstName, lastName, description);
        }
    }

    private void updateAccountInfo(String userName, String firstName, String lastName,String description)
    {
        settingsProgressbar.setVisibility(View.VISIBLE);
        HashMap userMap=new HashMap();
        userMap.put("Username",userName);
        userMap.put("FirstName",firstName);
        userMap.put("LastName",lastName);
        userMap.put("Description",description);

        settingsUserRef.updateChildren(userMap).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                settingsProgressbar.setVisibility(View.GONE);
                Toast.makeText(SettingsActivity.this,"Save Successfully!",Toast.LENGTH_SHORT).show();


            }
            else
            {
                settingsProgressbar.setVisibility(View.GONE);
                Toast.makeText(SettingsActivity.this,"Error occured while saving!",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void logout()
    {
        FirebaseAuth.getInstance().signOut();

        Intent intent=new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
        listeners();
    }
}
