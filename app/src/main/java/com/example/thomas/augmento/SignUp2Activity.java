package com.example.thomas.augmento;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp2Activity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{

    ConstraintLayout background;
    TextView signUpTextView, numeratorTextView, denominatorTextView;
    EditText usernameEditText, passwordEditText;
    ImageView cancelButton;
    ImageView finishButton,backButon;

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
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);

            User user=new User(usernameEditText.getText().toString(), getIntent().getStringExtra("firstName"), getIntent().getStringExtra("lastName"), getIntent().getStringExtra("email"));

            //adding in the local storage
            LocalStorage localStorage=new LocalStorage(getApplicationContext());
            localStorage.addStorage(getString(R.string.username),user.getUsername());
            localStorage.addStorage(getString(R.string.firstName),user.getFirstName());
            localStorage.addStorage(getString(R.string.lastName),user.getLastName());
            localStorage.addStorage(getString(R.string.email),user.getEmail());

            startActivity(intent);
        }
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
