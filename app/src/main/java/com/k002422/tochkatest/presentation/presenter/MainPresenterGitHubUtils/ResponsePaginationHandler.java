package com.k002422.tochkatest.presentation.presenter.MainPresenterGitHubUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;

public class ResponsePaginationHandler {
    private boolean multiPage = false;
    private boolean lastPageFetched = true;
    public int nextPage = 1;
    public String query = "";

    public ResponsePaginationHandler(String query) {
        this.query = query;
    }

    public void pushPage(Headers headers) {
        String linkHeader;
        try {
            linkHeader = headers.get("Link");
            linkHeaderParser(linkHeader);
            multiPage = true;
        } catch (Exception e) {
            multiPage = false;
        }
    }

    public boolean allows() {
        return (multiPage && !lastPageFetched);
    }

    private void linkHeaderParser(String linkHeader) {
        Pattern p = Pattern.compile("<[^>]*page=(\\d+)[^>]*>; rel=\"next\"");
        Matcher match = p.matcher(linkHeader);
        if (match.find()) {
            nextPage = Integer.parseInt(match.group(1));
            lastPageFetched = false;
        } else
            lastPageFetched = true;
    }
}
