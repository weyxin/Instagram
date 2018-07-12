package me.weyxin99.instagram.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.weyxin99.instagram.PostAdapter;
import me.weyxin99.instagram.R;

public class postListFragment extends Fragment {
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    SwipeRefreshLayout swipeContainer;
    int RESULT_OK = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_postlist, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(postAdapter);
        loadTopPosts();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopPosts();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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
                        Log.e("PostListFragment", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = objects.get(i);
                        posts.add(post);
                    }
                    swipeContainer.setRefreshing(false);
                }
                else {
                    Log.d("PostListFragment", "Failed to load top posts.");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Log.d("PostListFragment", "Successfully refreshed feed!");
            Post post = (Post) Parcels.unwrap(data.getParcelableExtra("post"));
            //posts.add(post);
            posts.add(0, post);
            postAdapter.notifyItemInserted(0);
            //postAdapter.notifyItemInserted(posts.size()-1);
            rvPosts.scrollToPosition(0);
        }
        else {
            Log.d("PostListFragment", "Error on automatic refreshing feed.");
        }
    }
}