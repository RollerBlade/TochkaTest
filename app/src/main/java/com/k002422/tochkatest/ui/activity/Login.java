package com.k002422.tochkatest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.k002422.tochkatest.R;
import com.k002422.tochkatest.presentation.presenter.LoginPresenter;
import com.k002422.tochkatest.presentation.view.LoginView;

public class Login extends MvpAppCompatActivity implements LoginView {
    public static final String TAG = "TestApp:LoginView";
    @InjectPresenter
    LoginPresenter mLoginPresenter;
    CoordinatorLayout loginCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginCoordinatorLayout = findViewById(R.id.loginCoordinator);
    }

    public void login(View view) {
        switch (view.getId()) {
            case R.id.vkButton:
                mLoginPresenter.loginVK(this);
                break;
            case R.id.gButton:
                mLoginPresenter.loginGoogle(this);
                break;
            case R.id.fbButton:
                mLoginPresenter.loginFB(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginPresenter.loginResultAnalyzer(requestCode, resultCode, data, this);
    }

    @Override
    public void showFailedLoginMessage(String msg, String loginServiceType) {
        Snackbar.make(loginCoordinatorLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("RETRY", view -> {
                    switch (loginServiceType) {
                        case "VK":
                            mLoginPresenter.loginVK(this);
                            break;
                        case "G":
                            mLoginPresenter.loginGoogle(this);
                            break;
                        case "FB":
                            mLoginPresenter.loginFB(this);
                            break;
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        closeThisView("QUIT");
        super.onBackPressed();
    }

    @Override
    public void closeThisView(String loginState) {
        Intent intent = new Intent();
        intent.putExtra("loginState", loginState);
        setResult(RESULT_OK, intent);
        this.finish();
    }
}
