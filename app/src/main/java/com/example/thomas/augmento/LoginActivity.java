package com.example.thomas.augmento;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{
    ConstraintLayout background;
    TextView signInTextView;
    EditText emailEditText, passwordEditText;
    Button signInButton;
    ImageView cancelButton;

    public void initialize()
    {
        background=findViewById(R.id.background);
        signInTextView=findViewById(R.id.signInTextView);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        signInButton=findViewById(R.id.signInButton);
        cancelButton=findViewById(R.id.cancelButton);
    }

    public void listeners()
    {
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
    {
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
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.signInButton)
        {
            loginClicked();
        }
        else if(v.getId()==R.id.background || v.getId()==R.id.signInTextView)
        {
            hideKeyboard();
        }
        else if(v.getId()==R.id.cancelButton)
        {
            Intent intent=new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {

        if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN)
        {
            loginClicked();
        }

        return false;
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
