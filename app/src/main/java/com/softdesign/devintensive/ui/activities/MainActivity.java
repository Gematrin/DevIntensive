package com.softdesign.devintensive.ui.activities;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main_Activity";
    private boolean mCurrentEditMode = false;
    private DataManager mDataManager;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText mMobilePhone, mEmail, mVk, mRepo, mAbout;
    private List<EditText> mInfo;
    private RoundedBitmapDrawable mRoundedBitmapDrawable;
    private ImageView mNavigationDrawerProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mDataManager = DataManager.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.floating_button);
        mMobilePhone = (EditText) findViewById(R.id.phone_et);
        mEmail = (EditText) findViewById(R.id.email_et);
        mVk = (EditText) findViewById(R.id.vk_et);
        mRepo = (EditText) findViewById(R.id.repo_et);
        mAbout = (EditText) findViewById(R.id.about_et);

        mInfo = new ArrayList<>();
        mInfo.add(mMobilePhone);
        mInfo.add(mEmail);
        mInfo.add(mVk);
        mInfo.add(mRepo);
        mInfo.add(mAbout);

        mFab.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        loadUserInfoValue();

        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserInfoValues();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.floating_button:
                if (mCurrentEditMode) {
                    changeEditMode(false);
                    mCurrentEditMode = false;
                }
                else {
                    changeEditMode(true);
                    mCurrentEditMode = true;
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mRoundedBitmapDrawable = getRoundedDrawable(R.drawable.av);
        mNavigationDrawerProfilePicture = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.prof_pic);
        mNavigationDrawerProfilePicture.setImageDrawable(mRoundedBitmapDrawable);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void changeEditMode (boolean mode){
        if (mode) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText value : mInfo) {
                value.setEnabled(true);
                value.setFocusable(true);
                value.setFocusableInTouchMode(true);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText value : mInfo) {
                value.setEnabled(false);
                value.setFocusable(false);
                value.setFocusableInTouchMode(false);
                saveUserInfoValues();
            }
        }
    }

    private void loadUserInfoValue(){
        List<String> userData = mDataManager.getPreferencesManager().loadProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mInfo.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValues(){
        List<String> userData = new ArrayList<>();
        for (EditText field : mInfo){
            userData.add(field.getText().toString());
        }
        mDataManager.getPreferencesManager().saveProfileData(userData);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private RoundedBitmapDrawable getRoundedDrawable(int drawableId){
        RoundedBitmapDrawable drawable;
        Resources res = getResources();
        drawable = RoundedBitmapDrawableFactory.create(res, BitmapFactory.decodeResource(res, drawableId));
        drawable.setCircular(true);
        return drawable;
    }
}
