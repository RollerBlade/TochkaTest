package com.k002422.tochkatest.utils.GitHubUtils.ResponseEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersResponse {

    @SerializedName("total_count")
    @Expose
    public Integer totalCount;
    @SerializedName("incomplete_results")
    @Expose
    public Boolean incompleteResults;
    @SerializedName("items")
    @Expose
    public List<Item> items = null;
}