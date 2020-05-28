package com.example.myapiconsumption;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapiconsumption.models.Comment;
import com.example.myapiconsumption.models.Post;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private ApiService apiService;
    private final String TAG = MainActivity.class.getSimpleName();

    private TextView post_id_results, user_id_results, commentResult, commentFromPost;
    private EditText post_id, user_id, commentFromPostEdit, getComment, add_post_id, add_post_title, add_post_content;
    private Button getPost, getUser, getCommentBtn,fetchCommentFromPost, add_post_btn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

//        Post post = new Post(2, "Posted Title", "Post Content");


        post_id = findViewById(R.id.post_id);
        user_id = findViewById(R.id.user_id_edit_text);
        post_id_results = findViewById(R.id.text_result);
        user_id_results = findViewById(R.id.user_id_text_result);

        add_post_id = findViewById(R.id.add_post_id);
        add_post_title = findViewById(R.id.add_post_title);
        add_post_content = findViewById(R.id.add_post_content);
        add_post_btn = findViewById(R.id.add_post_btn);

        commentResult = findViewById(R.id.comment_result);
        commentFromPost = findViewById(R.id.comment_result_from_post);
        commentFromPostEdit = findViewById(R.id.comment_from_post);
        getComment = findViewById(R.id.get_comment);

        add_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!add_post_id.getText().toString().isEmpty() && !add_post_title.getText().toString().isEmpty() && !add_post_content.getText().toString().isEmpty()){
                    int postID = Integer.parseInt(add_post_id.getText().toString());
                    String postTitle = (add_post_title.getText().toString());
                    String postContent = (add_post_content.getText().toString());
                    Post post = new Post(postID,postTitle,postContent);
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    addPost(post);
                }
            }
        });

        getPost = findViewById(R.id.send_request_button);
        getPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!post_id.getText().toString().isEmpty()){
                    int postID = Integer.parseInt(post_id.getText().toString());
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    getpost(postID);
                }
            }
        });

        getUser = findViewById(R.id.send_req_button_user_id);
        getUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user_id.getText().toString().isEmpty()) {
                    int userId = Integer.parseInt(user_id.getText().toString());
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    getPostsByUser(userId);
                }
            }
        });

        getCommentBtn = findViewById(R.id.get_comment_btn);
        getCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getComment.getText().toString().isEmpty()) {
                    int commentid = Integer.parseInt(getComment.getText().toString());
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    getcomment(commentid);
                }
            }
        });

        fetchCommentFromPost = findViewById(R.id.fetch_comment_from_post);
        fetchCommentFromPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentFromPostEdit.getText().toString().isEmpty()) {
                    int postId = Integer.parseInt(commentFromPostEdit.getText().toString());
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    getCommentfromUser(postId);
                }
            }
        });

        getPosts();

    }

    private void addPost( final Post post){
        apiService.addPost(post).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                int statusCode = response.code();
                Post returnedPost = response.body();

                Log.d(TAG, "Status Code: " + statusCode + "\nPost: " + returnedPost.toString());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "Error: " + t.getMessage());
            }
        });
    }

    private void getCommentfromUser(final int postId) {
        apiService.getCommentfromUser(postId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                progressDialog.dismiss();

                int statusCode = response.code();
                if (!response.isSuccessful()) {
                    commentFromPost.setText("Status Code: " + statusCode);
                    return;
                }

                commentFromPost.setText("");
                List<Comment> postsByUser = response.body();
                commentFromPost.append("Status Code: " + statusCode + "\n");
                commentFromPost.append("Number Of Comments By PostId " + postId + " is: " + postsByUser.size());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                progressDialog.dismiss();
                commentResult.setText("Error: " + t.getMessage());
            }
        });
    }

    private void getcomment(final int postId) {
        apiService.getcomment(postId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                progressDialog.dismiss();
                int StatusCode = response.code();
                if (!response.isSuccessful()){
                    commentResult.setText("Status Code: " + StatusCode);
                    return;
                }

                commentResult.setText("");
                List<Comment> comments = response.body();
                for (Comment comment: comments){
                    commentResult.setText("Comments are: " + comment.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                progressDialog.dismiss();
                commentResult.setText("Error: " + t.getMessage());
            }
        });
    }





    private void getpost(int postId){
        apiService.getPost(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                progressDialog.dismiss();
                int StatusCode = response.code();
                Post post = response.body();

                post_id_results.setText("");
                post_id_results.append("Status Code: " + StatusCode + "\n");
                post_id_results.append("\n" + post.toString());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                progressDialog.dismiss();
                post_id_results.setText("Error: " + t.getMessage());
            }
        });
    }

    private void getPostsByUser(final int userId) {
        apiService.getPostfromUser(userId).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                progressDialog.dismiss();

                int statusCode = response.code();
                if (!response.isSuccessful()) {
                    user_id_results.setText("Status Code: " + statusCode);
                    return;
                }

                user_id_results.setText("");
                List<Post> postsByUser = response.body();
                user_id_results.append("Status Code: " + statusCode + "\n");
                user_id_results.append("Number Of Posts By UserId " + userId + " is: " + postsByUser.size());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressDialog.dismiss();
                user_id_results.setText("Error: " + t.getMessage());
            }
        });
    }


    private void getPosts() {
        apiService.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    Log.d(TAG, "Request got bounced fro some reasons");
                    return;
                }

                Log.d(TAG, "Status Code: " +response.code());
                List<Post> posts = response.body();
                Log.d(TAG, "Post size: " + posts.size());
                for (Post post: posts){
                    Log.d(TAG, post.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d(TAG, "Message: " +t.getMessage());
            }
        });
    }
}
