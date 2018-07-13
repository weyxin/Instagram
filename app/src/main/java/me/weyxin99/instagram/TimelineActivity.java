package me.weyxin99.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.parse.ParseUser;

import me.weyxin99.instagram.model.createFragment;
import me.weyxin99.instagram.model.myProfileFragment;
import me.weyxin99.instagram.model.postListFragment;

public class TimelineActivity extends AppCompatActivity {

    ImageView logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment createFragment = new createFragment();
        final Fragment postListFragment = new postListFragment();
        final Fragment myProfileFragment = new myProfileFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, postListFragment);
        ft.commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeNav:
                        FragmentTransaction homeFragmentTransaction = fragmentManager.beginTransaction();
                        homeFragmentTransaction.replace(R.id.flContainer, postListFragment).commit();
                        return true;
                    case R.id.newPostNav:
                        FragmentTransaction newPostFragmentTransaction = fragmentManager.beginTransaction();
                        newPostFragmentTransaction.replace(R.id.flContainer, createFragment).commit();
                        return true;
                    case R.id.profileNav:
                        FragmentTransaction myProfileFragmentTransaction = fragmentManager.beginTransaction();
                        myProfileFragmentTransaction.replace(R.id.flContainer, myProfileFragment).commit();
                        return true;
                }
                return false;
            }
        });

        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(TimelineActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
