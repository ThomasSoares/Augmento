package com.example.thomas.augmento;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            LocalStorage localStorage=new LocalStorage(getApplicationContext());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("Welcome "+localStorage.getStorage(getString(R.string.firstName))+" "+localStorage.getStorage(getString(R.string.lastName)));
                    return true;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_notification:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_augment:
                    mTextMessage.setText(R.string.title_augment);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile: "+localStorage.getStorage(getString(R.string.username)));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
