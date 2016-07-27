package com.softdesign.devintensive.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.ArrayList;
import java.util.List;

public class SaveUserInDbOperation extends ChronosOperation<Void> {

    private UserListRes mUserListRes;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;

    public SaveUserInDbOperation(UserListRes userListRes, RepositoryDao repositoryDao, UserDao userDao) {
        mUserListRes = userListRes;
        mRepositoryDao = repositoryDao;
        mUserDao = userDao;
    }

    @Nullable
    @Override
    public Void run() {
        List<Repository> allRepositories = new ArrayList<>();
        List<User> allUsers = new ArrayList<>();

        for (UserListRes.UserData userRes : mUserListRes.getData()) {
            allRepositories.addAll(getRepoListFromUserRes(userRes));
            allUsers.add(new User(userRes));
        }

        mRepositoryDao.insertOrReplaceInTx(allRepositories);
        mUserDao.insertOrReplaceInTx(allUsers);
        return null;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Void>> getResultClass() {
        return Result.class;
    }

    private List<Repository> getRepoListFromUserRes(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repositoryRes : userData.getRepositories().getRepo()){
            repositories.add(new Repository(repositoryRes, userId));
        }

        return repositories;
    }

    public final static class Result extends ChronosOperationResult<Void>{

    }
}
