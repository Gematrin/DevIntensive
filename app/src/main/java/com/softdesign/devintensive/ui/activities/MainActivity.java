package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main_Activity";
    private boolean mCurrentEditMode = false;
    private DataManager mDataManager;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
    @BindView(R.id.floating_button) FloatingActionButton mFab;
    @BindView(R.id.phone_et) EditText mMobilePhone;
    @BindView(R.id.email_et) EditText mEmail;
    @BindView(R.id.vk_et) EditText mVk;
    @BindView(R.id.repo_et) EditText mRepo;
    @BindView(R.id.about_et) EditText mAbout;
    @BindView(R.id.profile_placeholder) RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo) ImageView mProfileImage;
    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.call_button) ImageView mCallButton;
    @BindView(R.id.send_button) ImageView mSendButton;
    @BindView(R.id.vk_button) ImageView mVkButton;
    @BindView(R.id.repo_button) ImageView mRepoButton;
    @BindView(R.id.rating_et) TextView mUserValueRating;
    @BindView(R.id.codelines_et) TextView mUserValueCodelines;
    @BindView(R.id.projects_et) TextView mUserValueProjects;

    private File mPhotoFile;
    private Uri mSelectedImage;
    private AppBarLayout.LayoutParams mAppBarParams;
    private List<EditText> mInfo;
    private List<TextView> mUserValueViews;
    private ImageView mNavigationDrawerProfilePicture;
    private NavigationView mNavigationView;

    private AwesomeValidation mValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        ButterKnife.bind(this);

        mValidation = new AwesomeValidation(ValidationStyle.BASIC);

        mValidation.addValidation(mMobilePhone, "\\+[\\d]{1,3} [(]{0,1}[\\d]{3}[)]{0,1} [\\d]{3}-[\\d]{2}-[\\d]{2}",
                "Введите номер в формате +x xxx xxx-xx-xx");
        mValidation.addValidation(mEmail, "[\\w]{3,}@[\\w]{3,}\\.[\\w]{2,3}", "Введите адрес в формате \"xxx@xxx.xx\"");
        mValidation.addValidation(mVk, "vk.com/[\\w]{3,}", "Введите адрес в формате \"vk.com/xxx\"");
        mValidation.addValidation(mRepo, "github.com/[\\w/]{3,}", "Введите адрес в формате \"github.com/xxx\"");

        mDataManager = DataManager.getInstance();
        setTitle(mDataManager.getPreferencesManager().loadName());

        mInfo = new ArrayList<>();
        mInfo.add(mMobilePhone);
        mInfo.add(mEmail);
        mInfo.add(mVk);
        mInfo.add(mRepo);
        mInfo.add(mAbout);

        mUserValueViews = new ArrayList<>();
        mUserValueViews.add(mUserValueRating);
        mUserValueViews.add(mUserValueCodelines);
        mUserValueViews.add(mUserValueProjects);

        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mCallButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
        mVkButton.setOnClickListener(this);
        mRepoButton.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        initUserFields();
        initUserInfoValues();

        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.userphoto0)
                .into(mProfileImage);

        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
        saveUserFields();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_button:
                if (mCurrentEditMode) {
                    changeEditMode(false);
                } else {
                    changeEditMode(true);
                }
                break;

            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

            case R.id.call_button:
                makeCall(mMobilePhone.getText().toString());
                break;

            case R.id.send_button:
                sendEmail(mEmail.getText().toString());
                break;

            case R.id.vk_button:
                openLink(mVk.getText().toString());
                break;

            case R.id.repo_button:
                openLink(mRepo.getText().toString());
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    //устанавливает тулбар
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    //устанавливает боковое меню
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
                        break;

                    case R.id.user_team_menu:
                        Intent teamIntent = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(teamIntent);
                        break;
                }
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        mNavigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
        }
    }

    //включает и отключает режим редактирования профиля
    private void changeEditMode(boolean mode) {
        if (mode) {
            mCurrentEditMode = true;
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText value : mInfo) {
                value.setEnabled(true);
                value.setFocusable(true);
                value.setFocusableInTouchMode(true);
            }

            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            mInfo.get(0).requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mInfo.get(0), InputMethodManager.SHOW_IMPLICIT);

        } else {
            if (mValidation.validate()){
                mValidation.clear();
                mCurrentEditMode = false;
                mFab.setImageResource(R.drawable.ic_create_black_24dp);
                for (EditText value : mInfo) {
                    value.setEnabled(false);
                    value.setFocusable(false);
                    value.setFocusableInTouchMode(false);
                }

                saveUserFields();
                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
            }
        }
    }

    //загружает сохраненную информацию профиля
    private void initUserFields() {
        List<String> userData = mDataManager.getPreferencesManager().loadProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mInfo.get(i).setText(userData.get(i));
        }
    }

    //сохраняет информацию профиля
    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText field : mInfo) {
            userData.add(field.getText().toString());
        }
        mDataManager.getPreferencesManager().saveProfileData(userData);
    }

    private void initUserInfoValues(){
        List<String> userData = mDataManager.getPreferencesManager().loadUserInfoValues();
        for (int i = 0; i < userData.size(); i++){
            mUserValueViews.get(i).setText(userData.get(i));
        }
    }
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    //запрос фотографии для профиля из галереи
    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent,
                getString(R.string.user_profile_choice_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    //запрос фотографии для профиля с камеры
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.toast_camera_makefile_error, Toast.LENGTH_LONG).show();
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, R.string.permissions_request_description, Snackbar.LENGTH_LONG)
                    .setAction(R.string.allow, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openAppSettings();
                        }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.camera_request_success, Toast.LENGTH_LONG).show();
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.write_request_success, Toast.LENGTH_LONG).show();            }
        }
    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.user_profile_dialog_title);
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                dialogInterface.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    //создаёт и возвращает новый .jpg файл
    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    //вставляет фотографию профиля в activity_main
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .placeholder(R.drawable.userphoto0)
                .into(mProfileImage);

        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    //открывает настройки приложения
    private void openAppSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    //запрос на совершение звонка по номеру, переданному в метод
    private void makeCall(String number) {
        Intent makeCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, ConstantManager.CALL_REQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, R.string.permissions_request_description, Snackbar.LENGTH_LONG)
                    .setAction(R.string.allow, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openAppSettings();
                        }
                    }).show();
            return;
        }
        startActivityForResult(makeCallIntent, ConstantManager.MAKE_CALL_CODE);
    }

    //запрос на открытие ссылки, переданной в метод
    private void openLink(String link){
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + link));
        startActivity(openLinkIntent);
    }

    //запрос на отправку почты по адресу, переданному в метод
    private void sendEmail(String email){
        Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
        sendMailIntent.setData(Uri.parse("mailto:" + email));
        sendMailIntent.setType("message/rfc822");
        sendMailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

        if (sendMailIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(sendMailIntent, ConstantManager.SEND_MAIL_CODE);
        } else {
            Toast.makeText(this, R.string.no_mail_client_error, Toast.LENGTH_LONG).show();
        }
    }
}