package com.softdesign.devintensive.utils;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;

public class UserStatsBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    private void modifyLayoutSize(LinearLayout layout, View dependency){
        
    }
}
