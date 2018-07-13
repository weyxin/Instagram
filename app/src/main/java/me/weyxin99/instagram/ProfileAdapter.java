package me.weyxin99.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.weyxin99.instagram.model.Post;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private List<Post> myPosts;
    Context context;
    public ProfileAdapter(List<Post> posts) {
        myPosts = posts;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_my_post, parent, false);
        ProfileAdapter.ViewHolder viewHolder = new ProfileAdapter.ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = myPosts.get(position);
        holder.ivPostImage.layout(0,0,0,0);
        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPostImage);
    }

    @Override
    public int getItemCount() {
        return myPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivPostImage) ImageView ivPostImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = myPosts.get(position);
                /*Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);*/
            }
        }
    }

    public void clearPostsList() {
        myPosts.clear();
        notifyDataSetChanged();
    }

    public void addPostsToList(List<Post> list) {
        myPosts.addAll(list);
        notifyDataSetChanged();
    }
}
