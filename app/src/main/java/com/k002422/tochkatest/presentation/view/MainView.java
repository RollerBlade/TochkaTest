package com.k002422.tochkatest.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.k002422.tochkatest.utils.AccountUtils;
import com.k002422.tochkatest.utils.GitHubUtils.ResponseEntity.Item;

import java.util.ArrayList;

public interface MainView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void startLoginActivity();

    void setAccountInfo(AccountUtils account);

    void closeActivity();

    void showDataset(ArrayList<Item> userNames);

    void recyclerViewReset();

    void toggleRecyclerProgressBar(boolean enabled);

    @StateStrategyType(SkipStrategy.class)
    void showGitHubErrorMessage(String msg);
}
