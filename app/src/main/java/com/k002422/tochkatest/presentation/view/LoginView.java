package com.k002422.tochkatest.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface LoginView extends MvpView {
    void showFailedLoginMessage(String msg, String loginServiceType);

    @StateStrategyType(SkipStrategy.class)
    void closeThisView(String loginState);
}
