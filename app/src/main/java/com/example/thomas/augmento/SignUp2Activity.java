package com.example.thomas.augmento;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp2Activity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{

    ConstraintLayout background;
    TextView signUpTextView, numeratorTextView, denominatorTextView;
    EditText usernameEditText, passwordEditText;
    ImageView cancelButton;
    ImageView finishButton,backButon;


    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    String currentUserID;
    ProgressBar progressBarSignUp;

    public void initialize()
    {
        background=findViewById(R.id.background);
        signUpTextView=findViewById(R.id.signUpTextView);
        numeratorTextView=findViewById(R.id.numeratorTextView);
        denominatorTextView=findViewById(R.id.denominatorTextView);
        usernameEditText=findViewById(R.id.usernameEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        finishButton=findViewById(R.id.finishButton);
        backButon=findViewById(R.id.backButton);
        cancelButton=findViewById(R.id.cancelButton);
        progressBarSignUp=findViewById(R.id.progressBarSignUp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        usersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
    }

    public void listeners()
    {
        background.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
        numeratorTextView.setOnClickListener(this);
        denominatorTextView.setOnClickListener(this);
        finishButton.setOnClickListener(this);
        backButon.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private boolean isEmpty(EditText etText)
    {
        //FUNCTION TO CHECK IF EDIT TEXT IS EMPTY

        if (etText.getText().toString().trim().length() > 0)
        {
            return false;
        }
        return true;
    }

    public void hideKeyboard()
    {
        //FUNCTION THAT HIDES THE KEYBOARD

        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText())
        {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

    }

    public void finishClicked()
    {
        hideKeyboard();

        if(isEmpty(usernameEditText) && isEmpty(passwordEditText))
        {
            usernameEditText.setError("Cannot be empty");
            passwordEditText.setError("Cannot be empty");
        }
        else if(isEmpty(usernameEditText))
        {
            usernameEditText.setError("Cannot be empty");
        }
        else if(isEmpty(passwordEditText))
        {
            passwordEditText.setError("Cannot be empty");
        }
        else
        {
            progressBarSignUp.setVisibility(View.VISIBLE);
            signUpUsers();

        }
    }

    public void saveUserInfo()
    {
        final int[] flag = {0};

        String firstName=getIntent().getStringExtra("firstName");
        String lastName=getIntent().getStringExtra("lastName");
        String username=usernameEditText.getText().toString();

        HashMap userMap=new HashMap();
        userMap.put(getString(R.string.firstName),firstName);
        userMap.put(getString(R.string.lastName),lastName);
        userMap.put(getString(R.string.username),username);

        usersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                progressBarSignUp.setVisibility(View.GONE);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarSignUp.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"SignUp Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void signUpUsers()
    {
        //Method to sign up users

        mAuth.createUserWithEmailAndPassword(getIntent().getStringExtra("email"), passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            User user=new User(usernameEditText.getText().toString(), getIntent().getStringExtra("firstName"), getIntent().getStringExtra("lastName"), getIntent().getStringExtra("email"));

                            //adding in the local storage
                            LocalStorage localStorage=new LocalStorage(getApplicationContext());
                            localStorage.addStorage(getString(R.string.username),user.getUsername());
                            localStorage.addStorage(getString(R.string.firstName),user.getFirstName());
                            localStorage.addStorage(getString(R.string.lastName),user.getLastName());
                            localStorage.addStorage(getString(R.string.email),user.getEmail());

                            saveUserInfo();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressBarSignUp.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"SignUp Failed",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.finishButton)
        {
            finishClicked();
        }
        else if(v.getId()==R.id.backButton)
        {
            finish();
        }
        else if(v.getId()==R.id.background || v.getId()==R.id.signUpTextView || v.getId()==R.id.numeratorTextView || v.getId()==R.id.denominatorTextView)
        {
            hideKeyboard();
        }
        else if(v.getId()==R.id.cancelButton)
        {
            hideKeyboard();

            Intent intent=new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN)
        {
            finishClicked();
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        initialize();
        listeners();
    }


}
