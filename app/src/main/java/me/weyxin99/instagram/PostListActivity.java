package me.weyxin99.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.weyxin99.instagram.model.Post;

public class PostListActivity extends AppCompatActivity {

    Button createPostButton;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);
        loadTopPosts();
        createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HomeActivity", "Creating a post!");
                Intent intent = new Intent(PostListActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
/*        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragment1 = new FirstFragment();
        final Fragment fragment2 = new SecondFragment();
        final Fragment fragment3 = new ThirdFragment();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeNav:
                        // switch to home feed
                        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                        fragmentTransaction1.replace(R.id.flContainer, fragment1).commit();
                        return true;
                    case R.id.newPostNav:
                        // switch to create activity
                        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                        fragmentTransaction2.replace(R.id.flContainer, fragment2).commit();
                        return true;
                    case R.id.profileNav:
                        // switch to profile activity
                        FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                        fragmentTransaction3.replace(R.id.flContainer, fragment3).commit();
                        return true;
                }
                return false;
            }
        });*/
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.orderByDescending("createdAt");
        postQuery.getTop().withUser();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    postAdapter.clearPostsList();
                    posts.clear();
                    for(int i = 0; i < objects.size(); i++) {
                        Log.e("PostListActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = objects.get(i);
                        posts.add(post);
                    }
                }
                else {
                    Log.d("PostListActivity", "Failed to load top posts.");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Log.d("PostListActivity", "Successfully refreshed feed!");
            Post post = (Post) Parcels.unwrap(data.getParcelableExtra("post"));
            //posts.add(post);
            posts.add(0, post);
            postAdapter.notifyItemInserted(0);
            //postAdapter.notifyItemInserted(posts.size()-1);
            rvPosts.scrollToPosition(0);
        }
        else {
            Log.d("PostListActivity", "Error on automatic refreshing feed.");
        }
    }
}
