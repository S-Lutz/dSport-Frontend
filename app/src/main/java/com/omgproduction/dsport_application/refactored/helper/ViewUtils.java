package com.omgproduction.dsport_application.refactored.helper;

import android.view.View;

/**
 * Created by Florian on 16.11.2016.
 */

public class ViewUtils {
    public static void toggleVisibility(boolean flag, View... views){
        for(View current: views){
            if(!flag){
                current.setVisibility(View.INVISIBLE);
            }else{
                current.setVisibility(View.VISIBLE);
            }
        }
    }
    public static void toggleExistence(boolean flag, View... views){
        for(View current: views){
            if(!flag){
                current.setVisibility(View.GONE);
            }else{
                current.setVisibility(View.VISIBLE);
            }
        }
    }
}
