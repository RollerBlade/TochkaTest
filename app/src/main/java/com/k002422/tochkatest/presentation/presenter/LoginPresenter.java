package com.k002422.tochkatest.presentation.presenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.k002422.tochkatest.presentation.view.LoginView;
import com.k002422.tochkatest.utils.PrefUtils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.Collections;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {
    public static final String TAG = "TestApp:LoginPresenter";
    private CallbackManager callbackManager;
    private Context context; //destroyable in this.onDestroy method

    public void init(Context baseContext) {
        context = baseContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void loginVK(Activity activity) {
        VKSdk.login(activity, "");
    }

    public void loginGoogle(Activity activity) {
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        activity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), 100);
    }

    public void loginFB(Activity activity) {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        PrefUtils.setLoggedInMode(context, "FB");
                        getViewState().closeThisView("FB");
                    }

                    @Override
                    public void onCancel() {
                        getViewState().showFailedLoginMessage("FaceBook authorization failed", "FB");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        getViewState().showFailedLoginMessage("FaceBook authorization failed", "FB");
                    }
                });
        LoginManager.getInstance().logInWithReadPermissions(activity, Collections.singletonList("public_profile"));
    }

    public void loginResultAnalyzer(int requestCode, int resultCode, Intent data) {
        if (!vkLoginCallback(requestCode, resultCode, data))
            if (!fbLoginCallback(requestCode, resultCode, data))
                googleLoginCallback(requestCode, data);
    }

    private boolean vkLoginCallback(int requestCode, int resultCode, Intent data) {
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                PrefUtils.setLoggedInMode(context, "VK");
                getViewState().closeThisView("VK");
            }

            @Override
            public void onError(VKError error) {
                getViewState().showFailedLoginMessage("VK authorization failed", "VK");
            }
        });
    }

    private boolean fbLoginCallback(int requestCode, int resultCode, Intent data) {
        if (callbackManager == null)
            return false;
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            return true;
        }
    }

    private void googleLoginCallback(int requestCode, Intent data) {
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                PrefUtils.setLoggedInMode(context, "G");
                getViewState().closeThisView("G");
            } catch (ApiException e) {
                getViewState().showFailedLoginMessage("Google authorization failed", "G");
            }
        }
    }
}
