package me.weyxin99.instagram.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.weyxin99.instagram.ProfileAdapter;
import me.weyxin99.instagram.R;

public class myProfileFragment extends Fragment {

    TextView tvUsername;
    CircleImageView ivProfileImage;
    ProfileAdapter profileAdapter;
    ArrayList<Post> myPosts;
    RecyclerView rvMyPosts;
    int numOfColumns = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tvUsername = view.findViewById(R.id.myUsername);
        tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        ivProfileImage = view.findViewById(R.id.myProfileImage);
        ParseFile profileImage = (ParseFile) ParseUser.getCurrentUser().get("profileImage");
        Glide.with(getContext())
                .load(profileImage.getUrl())
                .into(ivProfileImage);

        rvMyPosts = (RecyclerView) view.findViewById(R.id.rvMyPosts);
        myPosts = new ArrayList<>();
        profileAdapter = new ProfileAdapter(myPosts);
        rvMyPosts.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        rvMyPosts.setAdapter(profileAdapter);
        loadMyPosts();
    }

    private void loadMyPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.orderByDescending("createdAt");
        postQuery.withUser();
        postQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    profileAdapter.clearPostsList();
                    myPosts.clear();
                    Log.d("MyProfileFragment", Integer.toString(objects.size()));
                    for(int i = 0; i < objects.size(); i++) {
                        Log.e("MyProfileFragment", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = objects.get(i);
                        myPosts.add(post);
                    }
                }
                else {
                    Log.d("MyProfileFragment", "Failed to load my posts.");
                    e.printStackTrace();
                }
            }
        });
    }
}
