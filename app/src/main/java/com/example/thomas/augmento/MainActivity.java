package com.example.thomas.augmento;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ImageView settingsImageView, messageImageView;
    FirebaseAuth mAuth;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                LocalStorage localStorage=new LocalStorage(getApplicationContext());
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_home:


                        fragment=new HomeFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_search:


                        fragment=new SearchFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_notification:


                        fragment=new NotificationsFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_augment:
                        Intent intent=new Intent(getApplicationContext(),CameraActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_profile:
                        fragment=new ProfileFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            };

    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void logout()
    {
        FirebaseAuth.getInstance().signOut();

        Intent intent=new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
        finish();
    }
    private void startDemo()
    {
        Intent intent=new Intent(getApplicationContext(), SearchNewActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        mAuth=FirebaseAuth.getInstance();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        settingsImageView=findViewById(R.id.settingsImageView);
        messageImageView=findViewById(R.id.messageImageView);

        settingsImageView.setOnClickListener(v -> logout());
        messageImageView.setOnClickListener(v -> startDemo());

        loadFragment(new HomeFragment());
    }

}
