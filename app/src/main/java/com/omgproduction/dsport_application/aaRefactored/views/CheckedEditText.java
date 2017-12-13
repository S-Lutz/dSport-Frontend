package com.omgproduction.dsport_application.aaRefactored.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;
import android.widget.EditText;

import com.omgproduction.dsport_application.R;

public class CheckedTextInput extends TextInputLayout {

    private EditText et;

    public CheckedTextInput(Context context) {
        super(context);
    }

    public CheckedTextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CheckedTextInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CheckedTextInput);

        et = new EditText(context);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et.setTextColor(a.getColor(R.styleable.CheckedEditText_android_textColor, Color.BLACK));
        et.setMaxLines(a.getInt(R.styleable.CheckedEditText_android_maxLines, 1));
        et.setInputType(a.getInt(R.styleable.CheckedEditText_android_inputType, InputType.TYPE_CLASS_TEXT));

        int textId = a.getResourceId(R.styleable.CheckedEditText_android_text, -1);
        if (textId != -1) {
            et.setText(textId);
        }

        addView(et);
    }

    public boolean checkRequired() {
        boolean valid = !getTextString().trim().isEmpty();
        if(!valid)
            setError(getContext().getString(R.string.required));
        return valid;
    }

    public boolean checkEmail() {
        boolean valid = Patterns.EMAIL_ADDRESS.matcher(getTextString()).matches();
        if(!valid)
            setError(getContext().getString(R.string.invalid_email));
        return valid;
    }

    public boolean checkContentEquals(CheckedTextInput editText){
        boolean equals = getTextString().equals(editText.getTextString());
        if(!equals){
            String error = getContext().getString(R.string.fields_missmatch);
            setError(error);
            editText.setError(error);
        }
        return equals;
    }

    public String getTextString(){
        return et.getText().toString();
    }

    public void setText(String text) {
        this.et.setText(text);
    }
}