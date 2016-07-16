package com.softdesign.devintensive.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Parcelable {
    private String mPhoto;
    private String mFullName;
    private String mRating;
    private String mCodelines;
    private String mProjects;
    private String mAbout;
    private List<String> mRepositories;

    public UserDTO(UserListRes.UserData userData) {
        List<String> repoList = new ArrayList<>();

        this.mPhoto = userData.getPublicInfo().getPhoto();
        this.mFullName = userData.getFullName();
        this.mRating = String.valueOf(userData.getProfileValues().getRating());
        this.mCodelines = String.valueOf(userData.getProfileValues().getLinesCode());
        this.mProjects = String.valueOf(userData.getProfileValues().getProjects());
        this.mAbout = userData.getPublicInfo().getBio();

        for (UserModelRes.Repo gitLink : userData.getRepositories().getRepo()){
            repoList.add(gitLink.getGit());
        }
        this.mRepositories = repoList;
    }

    protected UserDTO(Parcel in) {
        mPhoto = in.readString();
        mFullName = in.readString();
        mRating = in.readString();
        mCodelines = in.readString();
        mProjects = in.readString();
        mAbout = in.readString();
        if (in.readByte() == 0x01) {
            mRepositories = new ArrayList<String>();
            in.readList(mRepositories, String.class.getClassLoader());
        } else {
            mRepositories = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPhoto);
        dest.writeString(mFullName);
        dest.writeString(mRating);
        dest.writeString(mCodelines);
        dest.writeString(mProjects);
        dest.writeString(mAbout);
        if (mRepositories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRepositories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserDTO> CREATOR = new Parcelable.Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public String getPhoto() {
        return mPhoto;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getRating() {
        return mRating;
    }

    public String getCodelines() {
        return mCodelines;
    }

    public String getProjects() {
        return mProjects;
    }

    public String getAbout() {
        return mAbout;
    }

    public List<String> getRepositories() {
        return mRepositories;
    }
}