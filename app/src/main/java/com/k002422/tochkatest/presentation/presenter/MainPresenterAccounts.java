package com.k002422.tochkatest.presentation.presenter;


import android.content.Context;
import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.k002422.tochkatest.presentation.view.MainView;
import com.k002422.tochkatest.utils.AccountUtils;
import com.k002422.tochkatest.utils.PrefUtils;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

import org.json.JSONException;

@InjectViewState
public class MainPresenterAccounts extends MvpPresenter<MainView> {
    private static final String TAG = "TestApp:MainPrsntrAcc";
    private Context context; //destroyable in this.onDestroy method
    private AccountUtils currentAccount;

    public void init(Context baseContext) {
        context = baseContext;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        String loginMode = PrefUtils.getLoggedInMode(context);
        checkLoginState(loginMode);
    }

    @Override
    public void attachView(MainView view) {
        super.attachView(view);
    }

    public void checkLoginState(String loginMode) {
        switch (loginMode) {
            case "VK":
                initCurrentAccountVK();
                break;
            case "G":
                initCurrentAccountG();
                break;
            case "FB":
                initCurrentAccountFB();
                break;
            case "QUIT":
                getViewState().closeActivity();
                break;
            default:
                //not here
                getViewState().startLoginActivity();
                break;
        }
    }

    private void initCurrentAccountVK() {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_max"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList list = (VKList) response.parsedModel;
                try {
                    String firstName = list.get(0).fields.get("first_name").toString(),
                            lastName = list.get(0).fields.get("last_name").toString(),
                            photoUrl = list.get(0).fields.get("photo_max").toString();
                    setupAccount("VK account", firstName + " " + lastName, Uri.parse(photoUrl));
                } catch (JSONException e) {
                    getViewState().startLoginActivity();
                } catch (IndexOutOfBoundsException e) {
                    getViewState().startLoginActivity();
                }
            }

            @Override
            public void onError(VKError error) {
                getViewState().startLoginActivity();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                getViewState().startLoginActivity();
            }
        });
    }

    private void initCurrentAccountFB() {
        Profile currentProfile = Profile.getCurrentProfile();
        if (currentProfile == null) {
            if (AccessToken.getCurrentAccessToken() == null)
                getViewState().startLoginActivity();
            else {
                new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        String name = currentProfile.getFirstName() + currentProfile.getLastName();
                        Uri photoUrl = currentProfile.getProfilePictureUri(500, 500);
                        setupAccount("FB account", name, photoUrl);
                        this.stopTracking();
                    }
                };
            }
        } else {
            String name = currentProfile.getFirstName() + currentProfile.getLastName();
            Uri photoUrl = currentProfile.getProfilePictureUri(500, 500);
            setupAccount("FB account", name, photoUrl);
        }
    }

    private void initCurrentAccountG() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account == null) {
            getViewState().startLoginActivity();
        } else {
            String name = account.getDisplayName();
            Uri photoUrl = account.getPhotoUrl();
            setupAccount("Google account", name, photoUrl);
        }
    }

    private void setupAccount(String accType, String name, Uri photoUri) {
        currentAccount = new AccountUtils(accType, name, photoUri);
        getViewState().setAccountInfo(currentAccount);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }
}
