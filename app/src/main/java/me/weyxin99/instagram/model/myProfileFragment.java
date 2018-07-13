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
    TextView followerNum;
    TextView followingNum;
    TextView postNum;
    Post userInfo;
    int posts;
    int numOfColumns = 3;
    Boolean ownProfile = true;
    ParseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Bundle bundledUser = getArguments();
        if(bundledUser != null) {
            userInfo = (Post) bundledUser.getSerializable("user_post");
            ownProfile = false;
        }
        return inflater.inflate(R.layout.fragment_my_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myPosts = new ArrayList<>();
        profileAdapter = new ProfileAdapter(myPosts);
        rvMyPosts = (RecyclerView) view.findViewById(R.id.rvMyPosts);
        rvMyPosts.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        rvMyPosts.setAdapter(profileAdapter);

        postNum = view.findViewById(R.id.postNum);
        ivProfileImage = view.findViewById(R.id.myProfileImage);
        tvUsername = view.findViewById(R.id.myUsername);
        followerNum = view.findViewById(R.id.followerNum);
        followingNum = view.findViewById(R.id.followingNum);

        if(ownProfile){
            user = ParseUser.getCurrentUser();
        }

        else {
            user = userInfo.getUser();
        }

        ParseFile profileImage = (ParseFile) user.get("profileImage");
        Glide.with(getContext())
                .load(profileImage.getUrl())
                .into(ivProfileImage);
        tvUsername.setText(user.getUsername());
        followerNum.setText(user.getString("followers"));
        followingNum.setText(user.getString("following"));
        loadMyPosts();
    }

    private void loadMyPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.orderByDescending("createdAt");
        postQuery.withUser();
        postQuery.whereEqualTo("user", user);
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
                    posts = objects.size();
                    postNum.setText(Integer.toString(posts));
                }
                else {
                    Log.d("MyProfileFragment", "Failed to load my posts.");
                    e.printStackTrace();
                }
            }
        });
    }
}
