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
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    Intent intent=new Intent(getApplicationContext(),AugmentActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_profile:
                    fragment=new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
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

        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        messageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Message Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        loadFragment(new HomeFragment());
    }

}
