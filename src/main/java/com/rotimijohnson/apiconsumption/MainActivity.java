package com.rotimijohnson.apiconsumption;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
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

import com.rotimijohnson.apiconsumption.models.Post;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private ApiService apiService;
    private final String TAG = MainActivity.class.getSimpleName();

    private EditText postEdit, userEdit, commentFromPostEdit;
    private Button getPost, getUser, getComment,fetchCommentFromPost;
    private TextView result, userTextResult, commentResult, commentFromPost;
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
        postEdit = findViewById(R.id.id_edit_text);
        userEdit = findViewById(R.id.user_id_edit_text);
        commentFromPostEdit = findViewById(R.id.comment_from_post);
        result = findViewById(R.id.text_result);
        userTextResult = findViewById(R.id.user_id_text_result);
        commentResult = findViewById(R.id.comment_result);
        commentFromPost = findViewById(R.id.comment_result_from_post);

        getPost = findViewById(R.id.send_req_button);
        getPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!postEdit.getText().toString().isEmpty()){
                    int postID = Integer.parseInt(postEdit.getText().toString());
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
                if (!userEdit.getText().toString().isEmpty()) {
                    int userId = Integer.parseInt(userEdit.getText().toString());
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    getPostsByUser(userId);
                }
            }
        });

        getComment = findViewById(R.id.get_comment);
        getComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getcomment();
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

    private void getCommentfromUser(int postId) {
        apiService.getCommentfromUser(postId).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                progressDialog.dismiss();

                int statusCode = response.code();
                if (!response.isSuccessful()) {
                    commentFromPost.setText("Status Code: " + statusCode);
                    return;
                }

                commentFromPost.setText("");
                List<Post> postsByUser = response.body();
                commentFromPost.append("Status Code: " + statusCode + "\n");
                commentFromPost.append("Number Of Comments By PostId " + postId + " is: " + postsByUser.size());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressDialog.dismiss();
                commentFromPost.setText("Error: " + t.getMessage());
            }
        });
    }

    private void getcomment() {
        apiService.getcomment().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                progressDialog.dismiss();
                int StatusCode = response.code();
                if (!response.isSuccessful()){
                    commentResult.setText("Status Code: " + StatusCode);
                    return;
                }

                commentResult.setText("");
                List<Post> posts = response.body();
                for (Post post: posts){
                    commentResult.setText("Comments are: " + post.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressDialog.dismiss();
                commentResult.setText("Error: " + t.getMessage());
            }
        });
    }

    private void getPostsByUser(int userId) {
        apiService.getPostsfromUser(userId).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                progressDialog.dismiss();

                int statusCode = response.code();
                if (!response.isSuccessful()) {
                    userTextResult.setText("Status Code: " + statusCode);
                    return;
                }

                userTextResult.setText("");
                List<Post> postsByUser = response.body();
                userTextResult.append("Status Code: " + statusCode + "\n");
                userTextResult.append("Number Of Posts By UserId " + userId + " is: " + postsByUser.size());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressDialog.dismiss();
                userTextResult.setText("Error: " + t.getMessage());
            }
        });
    }

    private void getpost(int postId){
        apiService.getpost(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                progressDialog.dismiss();
                int StatusCode = response.code();
                Post post = response.body();

                result.setText("");
                result.append("Status Code: " + StatusCode + "\n");
                result.append("\n" + post.toString());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                progressDialog.dismiss();
                result.setText("Error: " + t.getMessage());
            }
        });
    }


    private void getPosts() {
        apiService.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    Log.d(TAG,"Request got bounced for some reason!");
                    return;
                }

                Log.d(TAG,"Status Code: " +response.code());
                List<Post> posts = response.body();
                Log.d(TAG, "Posts size: " +posts.size());
                for (Post post: posts){
                    Log.d(TAG, post.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // tell the user that the request failed!
                Log.d(TAG, "Message: " +t.getMessage());
            }
        });
    }
}
