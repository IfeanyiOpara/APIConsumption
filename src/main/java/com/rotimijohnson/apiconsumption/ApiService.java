package com.rotimijohnson.apiconsumption;

import com.rotimijohnson.apiconsumption.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{postId}")
    Call<Post> getpost(
            @Path("postId") int id
    );

    @GET("posts")
    Call<List<Post>> getPostsfromUser(
            @Query("userId") int UID
    );

    @GET("posts/1/comments")
    Call<List<Post>> getcomment();

    @GET("comments")
    Call<List<Post>> getCommentfromUser(
            @Query("postId") int UID
    );


}
