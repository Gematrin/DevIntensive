package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.login_button) Button mSignIn;
    @BindView(R.id.forgot_password) TextView mRememberPassword;
    @BindView(R.id.login_email) EditText mLogin;
    @BindView(R.id.login_password) EditText mPassword;
    @BindView(R.id.login_coordinator_container) CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();

        mRememberPassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                signIn();
                break;
            case R.id.forgot_password:
                rememberPassword();
                break;
        }
    }

    private void showSnackbar(String message){
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword(){
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.remember_pass_url)));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserModelRes userModel){
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
        this.finish();
    }

    private void signIn(){
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 403) {
                        showSnackbar(getString(R.string.wrong_username_or_pass));
                    } else {
                        showSnackbar(getString(R.string.authorization_error));
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    showSnackbar(getString(R.string.no_correct_response_from_server));
                }
            });
        } else {
            showSnackbar(getString(R.string.network_unavailable));
        }
    }

    private void saveUserValues(UserModelRes userModel){
        String[] userValues = {
                String.valueOf(userModel.getData().getUser().getProfileValues().getRating()),
                String.valueOf(userModel.getData().getUser().getProfileValues().getLinesCode()),
                String.valueOf(userModel.getData().getUser().getProfileValues().getProjects()),
        };

        List<String> userInfo = new ArrayList<>();
        userInfo.add(userModel.getData().getUser().getContacts().getPhone());
        userInfo.add(userModel.getData().getUser().getContacts().getEmail());
        userInfo.add(userModel.getData().getUser().getContacts().getVk());
        userInfo.add(userModel.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userInfo.add(userModel.getData().getUser().getPublicInfo().getBio());
        setTitle(userModel.getData().getUser().getSecondName() + " " + userModel.getData().getUser().getFirstName());

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
        mDataManager.getPreferencesManager().saveProfileData(userInfo);
        mDataManager.getPreferencesManager().saveName(userModel.getData().getUser().getSecondName(),
                userModel.getData().getUser().getFirstName());
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(userModel.getData().getUser()
                .getPublicInfo().getPhoto()));
        mDataManager.getPreferencesManager().saveAvatar(Uri.parse(userModel.getData().getUser()
                .getPublicInfo().getAvatar()));
    }
}
