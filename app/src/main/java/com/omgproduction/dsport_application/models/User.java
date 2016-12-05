package com.omgproduction.dsport_application.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.io.Serializable;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

public class User  implements Serializable {
    private String id, username, email, picture, firstname, lastname, created, agbversion;

    public User(String id, String username, String email, String picture, String firstname, String lastname, String created, String agbversion) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.firstname = firstname;
        this.lastname = lastname;
        this.created = created;
        this.agbversion = agbversion;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCreated() {
        return created;
    }

    public String getAgbversion() {
        return agbversion;
    }

    public Bitmap getBitmap(Context context){

        if(picture.isEmpty()){
            return BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.logo);
        }
        return BitmapUtils.getBitmapFromString(context,picture);
    }
}
