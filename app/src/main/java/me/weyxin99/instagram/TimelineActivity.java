package me.weyxin99.instagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.File;

import me.weyxin99.instagram.model.createFragment;
import me.weyxin99.instagram.model.postListFragment;

public class TimelineActivity extends AppCompatActivity {

    public final String APP_TAG = "CameraFunction";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment createFragment = new createFragment();
        final Fragment postListFragment = new postListFragment();

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
                        //onLaunchCamera();
                        FragmentTransaction newPostFragmentTransaction = fragmentManager.beginTransaction();
                        newPostFragmentTransaction.replace(R.id.flContainer, createFragment).commit();
                        return true;
                    case R.id.profileNav:
                        return true;
                }
                return false;
            }
        });
    }
}
