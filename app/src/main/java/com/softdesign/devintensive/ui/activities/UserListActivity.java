package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.LoadUserFromDbOperation;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";
    @BindView(R.id.user_list_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.user_list_toolbar) Toolbar mToolbar;
    @BindView(R.id.user_list_navigation_drawer) DrawerLayout mNavigationDrawer;
    @BindView(R.id.user_list) RecyclerView mRecyclerView;
    private ImageView mNavigationDrawerProfilePicture;

    private NavigationView mNavigationView;
    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;
    private MenuItem mSearchItem;
    private String mQuery;
    private Handler mHandler;
    private ChronosConnector mChronosConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mHandler = new Handler();
        mChronosConnector = new ChronosConnector();
        mChronosConnector.onCreate(this, savedInstanceState);

        setTitle(R.string.team);
        setupToolbar();
        setupDrawer();
        loadUsersFromDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChronosConnector.onResume();
        mNavigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mChronosConnector.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mChronosConnector.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint(getString(R.string.enter_user_name));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(String message){
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadUsersFromDb() {
        try {
            mChronosConnector.runOperation(new LoadUserFromDbOperation(), false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //if (mDataManager.getUserListFromDb().size() == 0) {
        //    showSnackbar(getString(R.string.cant_get_user_list));

        //} else {
        //    showUsers(mDataManager.getUserListFromDb());
        //}
    }

    public void onOperationFinished(final LoadUserFromDbOperation.Result result) {
        if (result.isSuccessful()) {
            mUsers = result.getOutput();
            if (mUsers.size() == 0) {
                showSnackbar(getString(R.string.cant_get_user_list));
            } else {
                showUsers(mUsers);
            }
        }
    }

    private void setupDrawer() {
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationDrawerProfilePicture = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.prof_pic);
        TextView drawerMail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_email_txt);
        drawerMail.setText(mDataManager.getPreferencesManager().loadProfileData().get(1));
        TextView drawerName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_txt);
        drawerName.setText(mDataManager.getPreferencesManager().loadName());
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadAvatar())
                .into(mNavigationDrawerProfilePicture);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()){
                    case R.id.user_profile_menu:
                        Intent userIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(userIntent);
                        break;

                    case R.id.user_team_menu:
                        break;
                }
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        mNavigationView.getMenu().getItem(1).setChecked(true);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showUsersByQuery(newText);
        return false;
    }

    private void showUsers(List<User> users) {
        mUsers = users;
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position));

                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);

                startActivity(profileIntent);
            }
        });

        mRecyclerView.swapAdapter(mUsersAdapter, false);
    }

    private void showUsersByQuery(String query) {
        mQuery = query;

        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                showUsers(mDataManager.getUserListByName(mQuery));
            }
        };

        mHandler.removeCallbacks(searchUsers);


        if (mQuery.isEmpty()){
            mHandler.postDelayed(searchUsers, 0);
        } else {
            mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_DELAY);
        }
    }
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }
}
