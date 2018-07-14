package me.weyxin99.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.weyxin99.instagram.model.Post;
import me.weyxin99.instagram.model.commentFragment;
import me.weyxin99.instagram.model.myProfileFragment;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<Post> mPosts;
    Context context;
    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.tvHandle.setText(post.getUser().getUsername());
        holder.tvDescription.setText(post.getDescription());
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvTime.setText(post.getTime());
        holder.tvHearts.setText(post.getString("hearts") + " likes");
        holder.ivPostImage.layout(0,0,0,0);
        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPostImage);
        Glide.with(context)
                .load(post.getUser().getParseFile("profileImage").getUrl())
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivProfileImage) CircleImageView ivProfileImage;
        @BindView(R.id.tvHandle) TextView tvHandle;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.ivPostImage) ParseImageView ivPostImage;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.heartButton) ImageButton heartButton;
        @BindView(R.id.tvHearts) TextView tvHearts;
        @BindView(R.id.commentButton) ImageButton commentButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    heartButton.setSelected(!heartButton.isSelected());
                    int position = getAdapterPosition();
                    Post post = mPosts.get(position);
                    if(position != RecyclerView.NO_POSITION) {
                        if(heartButton.isSelected()) {
                            heartButton.setImageResource(R.drawable.ufi_heart_active);
                            updateLikes(post, 1);
                        }
                        else {
                            heartButton.setImageResource(R.drawable.ufi_heart);
                            updateLikes(post, 0);
                        }
                    }
                }
            });

            tvHandle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Post post = mPosts.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user_post", post);

                        myProfileFragment userFragment = new myProfileFragment();
                        userFragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().
                                replace(R.id.flContainer, userFragment).addToBackStack(null).commit();
                    }
                }
            });

            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Post post = mPosts.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("comment", post);

                        commentFragment commentFragment = new commentFragment();
                        commentFragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().
                                replace(R.id.flContainer, commentFragment).addToBackStack(null).commit();
                    }
                }
            });
        }

        public void updateLikes(Post post, final int addLike) {
            final Post.Query postQuery = new Post.Query();
            postQuery.whereEqualTo("description", post.getDescription());
            postQuery.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> objects, ParseException e) {
                    if(e == null) {
                        int hearts;
                        if(addLike == 1) {
                            hearts = Integer.parseInt(objects.get(0).getHearts()) + 1;
                            tvHearts.setText(Integer.toString(hearts) + " likes");
                        }
                        else {
                            hearts = Integer.parseInt(objects.get(0).getHearts()) - 1;
                            tvHearts.setText(Integer.toString(hearts) + " likes");
                        }
                        objects.get(0).put("hearts", Integer.toString(hearts));
                        objects.get(0).saveInBackground();
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            return;
        }
    }

    public void clearPostsList() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    public void addPostsToList(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
