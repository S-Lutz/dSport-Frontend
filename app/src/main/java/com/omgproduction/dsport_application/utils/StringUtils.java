package com.omgproduction.dsport_application.utils;

import android.text.TextUtils;

/**
 * Created by Florian on 14.11.2016.
 */

public class StringUtils {
    /**
     * Check if Email is Valid. User Util-Patterns from Anroid
     * @param email Email to Check
     * @return True id Email is a Valid email and False if Email isnt a Valid Email
     */
    public final static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
