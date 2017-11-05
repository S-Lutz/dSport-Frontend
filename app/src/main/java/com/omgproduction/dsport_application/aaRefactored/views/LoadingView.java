package com.omgproduction.dsport_application.aaRefactored.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;

public class LoadingView extends LinearLayout {

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit(attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit(attrs);
    }

    private void onInit(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);

        inflate(getContext(), R.layout.loading_screen, this);

        AppCompatTextView textView = (AppCompatTextView) findViewById(R.id.loading_msg_tv);
        textView.setText(a.getString(R.styleable.LoadingView_message));
        textView.setTextColor(a.getColor(R.styleable.LoadingView_font_color, Color.WHITE));

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);

        a.recycle();
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(INVISIBLE);
    }


}