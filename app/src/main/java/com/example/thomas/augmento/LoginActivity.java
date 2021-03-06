package com.example.thomas.augmento;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{
    ConstraintLayout background;
    TextView signInTextView;
    EditText emailEditText, passwordEditText;
    Button signInButton;
    ImageView cancelButton;
    private FirebaseAuth mAuth;
    ProgressBar loginProgressBar;
    private DatabaseReference usersRef;

    public void initialize()
    {//INITIALIZE COMPONENTS
        background=findViewById(R.id.background);
        signInTextView=findViewById(R.id.signInTextView);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        signInButton=findViewById(R.id.signInButton);
        cancelButton=findViewById(R.id.cancelButton);
        loginProgressBar=findViewById(R.id.loginProgressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        usersRef=FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void listeners()
    {//SET LISTENERS
        background.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
        signInButton.setOnClickListener(this);
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

    public void loginClicked()
    {//FUNCTION TO BE EXECUTED WHEN LOGIN HAS BEEN CLICKED

        hideKeyboard();

        if(isEmpty(emailEditText) && isEmpty(passwordEditText))
        {
            emailEditText.setError("Cannot be empty");
            passwordEditText.setError("Cannot be empty");
        }
        else if(isEmpty(emailEditText))
        {
            emailEditText.setError("Cannot be empty");
        }
        else if(isEmpty(passwordEditText))
        {
            passwordEditText.setError("Cannot be empty");
        }
        else
        {
            loginProgressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                checkUserExistence();
                                loginProgressBar.setVisibility(View.GONE);

                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                loginProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Signin Failed!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.signInButton)
        {//WHEN SIGN IN BUTTON CLICKED
            loginClicked();
        }
        else if(v.getId()==R.id.background || v.getId()==R.id.signInTextView)
        {//WHEN BACKGROUND OR SIGN IN TEXT IS CLICKED
            hideKeyboard();
        }
        else if(v.getId()==R.id.cancelButton)
        {//WHEN CANCEL BUTTON IS CLICKED
            Intent intent=new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {

        if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN)
        {//WHEN THE ENTER KEY IS PRESSED ON THE VIRTUAL KEYBOARD
            loginClicked();
        }

        return false;
    }

    public void checkUserExistence()
    {//CHECK IF USER EXESTS OR NOT
        final String current_user_id=mAuth.getCurrentUser().getUid();

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_id))
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
        listeners();
    }
}
