package com.k002422.tochkatest.utils.GitHubUtils;

import com.k002422.tochkatest.utils.GitHubUtils.ResponseEntity.UsersResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GitHub {
    private static final String TOKEN = "1b2cfb8c1ad13e9b73a839aa5b391b8a980918cb";

    private interface GitHubApi {
        @GET("users?access_token=" + TOKEN)
        Single<Response<UsersResponse>> searchUsers(@Query("q") String query, @Query("page") int page);
    }

    public Single<Response<UsersResponse>> getUsers(String query, int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        GitHubApi gitHubApi = retrofit.create(GitHubApi.class);
        return gitHubApi.searchUsers(query, page);
    }
}
