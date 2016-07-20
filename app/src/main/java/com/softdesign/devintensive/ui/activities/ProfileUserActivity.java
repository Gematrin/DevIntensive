package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends BaseActivity {
    @BindView(R.id.user_profile_toolbar) Toolbar mToolbar;
    @BindView(R.id.user_profile_photo) ImageView mProfileImage;
    @BindView(R.id.user_about_et) EditText mUserBio;
    @BindView(R.id.user_rating_txt) TextView mUserRating;
    @BindView(R.id.user_codelines_txt) TextView mUserCodeLines;
    @BindView(R.id.user_projects_txt) TextView mUserProjects;
    @BindView(R.id.repositories_list) ListView mRepoListView;
    @BindView(R.id.user_collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.user_coordinator_container) CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);
        setupToolbar();
        initProfileData();
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void openLink(String link){
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + link));
        startActivity(openLinkIntent);
    }

    private void initProfileData(){
        UserDTO userDto = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);
        final List<String> repositories = userDto.getRepositories();
        RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
        mRepoListView.setAdapter(repositoriesAdapter);

        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                openLink(repositories.get(position));
            }
        });

        mUserBio.setText(userDto.getAbout());
        mUserRating.setText(userDto.getRating());
        mUserCodeLines.setText(userDto.getCodelines());
        mUserProjects.setText(userDto.getProjects());
        mCollapsingToolbar.setTitle(userDto.getFullName());

        if (!userDto.getPhoto().isEmpty()) {
            Picasso.with(this)
                    .load(userDto.getPhoto())
                    .placeholder(R.drawable.userphoto0)
                    .error(R.drawable.userphoto0)
                    .fit()
                    .centerCrop()
                    .into(mProfileImage);
        }else{
            Picasso.with(this)
                    .load(R.drawable.userphoto0)
                    .into(mProfileImage);
        }
    }
}
