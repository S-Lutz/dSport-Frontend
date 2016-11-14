package com.omgproduction.dsport_application.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.omgproduction.dsport_application.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Florian on 07.11.2016.
 */

public class BitmapUtils {


    public static String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBitmapFromString(Context context, String stringPicture) {
        try{
            byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        }catch (IllegalArgumentException ex){
            return BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.test_profile);
        }
    }
}
