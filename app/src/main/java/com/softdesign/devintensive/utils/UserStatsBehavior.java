package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

public class UserStatsBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    private Context mContext;

    public UserStatsBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        float position0 = dependency.getY();
        float position1 = position0 - 50;
        float ratio = (position1/position0 - 0.6874f)*4.6f;
        int padding = (int)(getPixels(24)*ratio);
        child.setY(dependency.getY());
        child.setPadding(0, padding, 0, padding);
        dependency.setPadding(0, child.getHeight(), 0, 0);
        return true;
        //это было ужасно
    }

    public int getPixels(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}