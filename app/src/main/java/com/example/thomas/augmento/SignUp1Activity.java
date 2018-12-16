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

public class SignUp1Activity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{

    ConstraintLayout background;
    TextView signUpTextView, numeratorTextView, denominatorTextView;
    EditText emailEditText, firstNameEditText, lastNameEditText;
    ImageView cancelButton;
    ImageButton nextButton;

    public void initialize()
    {
        background=findViewById(R.id.background);
        signUpTextView=findViewById(R.id.signUpTextView);
        numeratorTextView=findViewById(R.id.numeratorTextView);
        denominatorTextView=findViewById(R.id.denominatorTextView);
        emailEditText=findViewById(R.id.emailEditText);
        firstNameEditText=findViewById(R.id.usernameEditText);
        lastNameEditText=findViewById(R.id.lastNameEditText);
        nextButton=findViewById(R.id.nextButton);
        cancelButton=findViewById(R.id.cancelButton);
    }

    public void listeners()
    {
        background.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
        numeratorTextView.setOnClickListener(this);
        denominatorTextView.setOnClickListener(this);
        nextButton.setOnClickListener(this);
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

    public void nextClicked()
    {
        hideKeyboard();

        if(isEmpty(emailEditText) && isEmpty(firstNameEditText) && isEmpty(lastNameEditText))
        {
            emailEditText.setError("Cannot be empty");
            firstNameEditText.setError("Cannot be empty");
            lastNameEditText.setError("Cannot be empty");
        }
        else if(isEmpty(emailEditText))
        {
            emailEditText.setError("Cannot be empty");
        }
        else if(isEmpty(firstNameEditText))
        {
            firstNameEditText.setError("Cannot be empty");
        }
        else if(isEmpty(firstNameEditText))
        {
            lastNameEditText.setError("Cannot be empty");
        }
        else
        {
            Intent intent=new Intent(getApplicationContext(), SignUp2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.nextButton)
        {
            nextClicked();
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
            nextClicked();
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);


        initialize();
        listeners();
    }


}
