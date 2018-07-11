package me.weyxin99.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.weyxin99.instagram.model.Post;

public class PostListActivity extends AppCompatActivity {

    Button createPostButton;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;

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
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    postAdapter.clearPostsList();
                    posts.clear();
                    for(int i = objects.size()-1; i > 0; i--) {
                        Log.e("PostListActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = objects.get(i);
                        posts.add(post);
                        postAdapter.notifyItemInserted(posts.size() - 1);
                    }
                    postAdapter.addPostsToList(posts);
                }
                else {
                    Log.d("PostListActivity", "Failed to load top posts.");
                    e.printStackTrace();
                }
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Log.d("PostListActivity", "Successfully refreshed feed!");
            Post post = (Post) Parcels.unwrap(data.getParcelableExtra("post"));
            posts.add(0, post);
            postAdapter.notifyItemInserted(0);
            rvPosts.scrollToPosition(0);
        }
        else {
            Log.d("PostListActivity", "Error on automatic refreshing feed.");
        }
    }*/
}
