package com.k002422.tochkatest.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.k002422.tochkatest.R;
import com.k002422.tochkatest.presentation.presenter.MainPresenterAccounts;
import com.k002422.tochkatest.presentation.presenter.MainPresenterGitHub;
import com.k002422.tochkatest.presentation.view.MainView;
import com.k002422.tochkatest.ui.activity.MainUtils.UsersAdapter;
import com.k002422.tochkatest.utils.AccountUtils;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class Main extends MvpAppCompatActivity implements MainView {
    public static final String TAG = "TestApp:MainView";
    @InjectPresenter
    MainPresenterAccounts mMainPresenterAccounts;
    @InjectPresenter
    MainPresenterGitHub mainPresenterGitHub;
    Toolbar toolbar;
    Intent data;
    IProfile profile;
    AccountHeader headerResult;
    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainPresenterAccounts.init(this.getBaseContext());
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.mainCoordinatorLayout);

        setSupportActionBar(toolbar);
        recyclerViewInit();
        drawerInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (data != null) {
            mMainPresenterAccounts.checkLoginState(data.getStringExtra("loginState"));
            data = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        Disposable searchViewDisposable = com.jakewharton.rxbinding2.support.v7.widget.RxSearchView.
                queryTextChangeEvents(searchView).
                skipInitialValue().
                filter(searchViewQueryTextEvent ->
                        !searchViewQueryTextEvent.queryText().equals("")).
                debounce(600, TimeUnit.MILLISECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(searchViewQueryTextEvent ->
                        mainPresenterGitHub.startQuery(
                                searchViewQueryTextEvent.queryText().toString()));
        disposables.add(searchViewDisposable);

        return super.onCreateOptionsMenu(menu);

    }

    public void recyclerViewInit() {
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        UsersAdapter adapter = new UsersAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
                if (lastvisibleitemposition == (adapter.getItemCount() - 1) &&
                        !adapter.footerVisible) {
                    mainPresenterGitHub.getNextPage();
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
    }

    private void drawerInit() {
        profile = new ProfileDrawerItem().withName("Login").withEmail("").withIdentifier(0);
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColor(Color.BLACK)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.main_artboard)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .addProfiles(profile)
                .withOnAccountHeaderListener((view, profile, current) -> {
                    startLoginActivity();
                    return false;
                })
                .withOnAccountHeaderSelectionViewClickListener((view, profile) -> {
                    startLoginActivity();
                    return false;
                })
                .build();

        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .build();

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });
    }

    @Override
    public void startLoginActivity() {
        Log.d("asdasd", "Main.startLogin");
        Intent intent = new Intent(this, Login.class);
        startActivityForResult(intent, 100);
    }

    @Override
    public void setAccountInfo(AccountUtils account) {
        profile.withName(account.getUserName());
        profile.withIcon(account.getUserPhotoUrl());
        profile.withEmail(account.getAuthType());
        headerResult.updateProfile(profile);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void showDataset(ArrayList<String> userNames) {
        UsersAdapter adapter = (UsersAdapter) recyclerView.getAdapter();
        int itemsToAdd = userNames.size();
        int currentDataPoolSize = adapter.usersList.size();
        adapter.usersList.addAll(userNames);
        adapter.notifyItemRangeInserted(currentDataPoolSize, itemsToAdd);
    }

    @Override
    public void recyclerViewReset() {
        UsersAdapter localAdapter = ((UsersAdapter) recyclerView.getAdapter());
        localAdapter.footerVisible = false;
        localAdapter.usersList.clear();
        localAdapter.notifyDataSetChanged();
    }

    @Override
    public void toggleRecyclerProgressBar(boolean enabled) {
        ((UsersAdapter) recyclerView.getAdapter()).footerVisible = enabled;
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showGitHubErrorMessage(String msg) {
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("RETRY", view -> mainPresenterGitHub.retryGetPage()).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.data = data;
        super.onActivityResult(requestCode, resultCode, data);
    }
}
