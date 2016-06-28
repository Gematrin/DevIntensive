package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class UserStatsBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    private int mMaxHeight;
    private int mDifference;
    private boolean mValuesReceived = false;
    private CoordinatorLayout.LayoutParams mLayoutParams;

    public UserStatsBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        if (!mValuesReceived) {
            mMaxHeight = child.getHeight();
            mDifference = mMaxHeight/7;
            mLayoutParams = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
            mValuesReceived = true;
        }
        if (dependency.getY()-mDifference < mMaxHeight) mLayoutParams.height = (int)dependency.getY()-mDifference;
        child.setY(dependency.getY());
        child.setLayoutParams(mLayoutParams);
        dependency.setPadding(0, child.getHeight(), 0, 0);
        return true;
    }
}