package com.example.myapiconsumption;

import com.example.myapiconsumption.models.Comment;
import com.example.myapiconsumption.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{postId}")
    Call<Post> getPost(
        @Path("postId") int id
    );

    @GET("posts")
    Call<List<Post>> getPostfromUser(
            @Query("userId") int UID
    );

    @GET("posts/{postId}/comments")
    Call<List<Comment>> getcomment(
            @Path("postId") int id
    );

    @GET("comments")
    Call<List<Comment>> getCommentfromUser(
            @Query("postId") int UID
    );

    @POST("posts")
    Call<Post> addPost(
            @Body Post post
    );

}
