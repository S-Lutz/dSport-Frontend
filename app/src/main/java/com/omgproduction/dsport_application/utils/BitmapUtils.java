package com.omgproduction.dsport_application.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.omgproduction.dsport_application.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Florian on 07.11.2016.
 */

public class BitmapUtils {


    public static String getStringFromBitmap(final Bitmap bitmapPicture) {

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;

    }

    public static Bitmap getBitmapFromString(String stringPicture) {
        try{
            byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        }catch (IllegalArgumentException ex){
            return null;
        }
    }
}
