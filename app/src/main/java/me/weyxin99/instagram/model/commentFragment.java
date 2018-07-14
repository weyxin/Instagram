package me.weyxin99.instagram.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import me.weyxin99.instagram.CommentAdapter;
import me.weyxin99.instagram.R;

public class commentFragment extends Fragment {

    Post currentPost;
    CommentAdapter commentAdapter;
    ArrayList<Post> posts;
    RecyclerView rvComments;
    List<String> comments;
    Button postComment;
    EditText typed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Bundle bundledPost = getArguments();
        currentPost = (Post) bundledPost.getSerializable("comment");
        return inflater.inflate(R.layout.fragment_comment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        postComment = view.findViewById(R.id.sendButton);
        typed = view.findViewById(R.id.tvComment);
        rvComments = (RecyclerView) view.findViewById(R.id.commentList);
        posts = new ArrayList<>();
        commentAdapter = new CommentAdapter(currentPost.getComments());
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComments.setAdapter(commentAdapter);

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = postComment.getText().toString();
                comments = currentPost.getComments();
                comments.add(0, text);
                commentAdapter.clearCommentsList();
                commentAdapter.addCommentsToList(comments);
            }
        });
    }
}
