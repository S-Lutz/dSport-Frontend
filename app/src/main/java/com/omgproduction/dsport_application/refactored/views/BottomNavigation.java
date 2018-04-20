package com.omgproduction.dsport_application.refactored.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;

import com.omgproduction.dsport_application.R;

public class BottomNavigation extends LinearLayoutCompat{

    public BottomNavigation(@NonNull Context context) {
        super(context);
    }

    public BottomNavigation(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BottomNavigation(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.new_bottom_navigation, this);
    }
}