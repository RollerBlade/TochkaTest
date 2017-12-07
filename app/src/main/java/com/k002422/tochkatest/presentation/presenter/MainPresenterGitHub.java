package com.k002422.tochkatest.presentation.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.k002422.tochkatest.presentation.presenter.MainPresenterGitHubUtils.ResponsePaginationHandler;
import com.k002422.tochkatest.presentation.view.MainView;
import com.k002422.tochkatest.utils.GitHubUtils.GitHub;
import com.k002422.tochkatest.utils.GitHubUtils.ResponseEntity.Item;
import com.k002422.tochkatest.utils.GitHubUtils.ResponseEntity.UsersResponse;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;

@InjectViewState
public class MainPresenterGitHub extends MvpPresenter<MainView> {
    private static final String TAG = "TestApp:MainPrsntrGtHb";
    private ResponsePaginationHandler responsePaginationHandler = new ResponsePaginationHandler("");

    public void startQuery(String query) {
        responsePaginationHandler = new ResponsePaginationHandler(query);
        getViewState().recyclerViewReset();
        getUsersPage(query, 1);
    }

    public void getNextPage() {
        if (responsePaginationHandler.allows())
            getUsersPage(responsePaginationHandler.query, responsePaginationHandler.nextPage);
    }

    private void getUsersPage(String query, int page) {
        GitHub gitHub = new GitHub();
        getViewState().toggleRecyclerProgressBar(true);
        Single<Response<UsersResponse>> single = gitHub.getUsers(query, page).retry(2);
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    try {
                        if (response.code() == 403)
                            throw new HttpException(response);
                        ArrayList<Item> names = new ArrayList<>(response.body().items);
                        getViewState().toggleRecyclerProgressBar(false);
                        getViewState().showDataset(names);
                        responsePaginationHandler.pushPage(response.headers());
                    } catch (NullPointerException e) {
                        getViewState().toggleRecyclerProgressBar(false);
                        getViewState().showGitHubErrorMessage("Corrupted response");
                    } catch (HttpException e) {
                        getViewState().toggleRecyclerProgressBar(false);
                        getViewState().showGitHubErrorMessage("GitHub banned us, try 1 min later");
                    }

                }, e -> {
                    Log.d("asdasd", e.getLocalizedMessage());
                    getViewState().toggleRecyclerProgressBar(false);
                    getViewState().showGitHubErrorMessage("Network error");
                });
    }

    public void retryGetPage() {
        getUsersPage(responsePaginationHandler.query, responsePaginationHandler.nextPage);
    }
}
