package com.omgproduction.dsport_application.supplements.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.IntentKeys;
import com.omgproduction.dsport_application.config.LocalErrorCodes;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.SessionService;
import com.omgproduction.dsport_application.services.UserService;

import java.io.File;


/**
 * Created by Florian on 21.10.2016.
 */
public abstract class AbstractFragmentActivity extends FragmentActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LocalErrorCodes, IntentKeys{

    protected SwipeRefreshLayout refresher;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PIC_CROP = 2;
    private static final int SELECT_PICTURE = 3;
    private Uri mImageUri;
    private File imageFile;
    private String mCurrentPhotoPath;

    protected UserService userService;
    protected SessionService sessionService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = new UserService(this);
        sessionService = new SessionService(this);

    }

    /**
     * Print some Error with Snackbar but Without any Control-Element
     * @param errorCode ErrorCode (See in error_codes)
     */
    protected void printError(int layoutID, String errorCode){
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(findViewById(layoutID), getString(resId), Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    /**
     * Print some Error with Snackbar with Button
     * @param errorCode ErrorCode (See in error_codes)
     * @param buttonLabelId StringID for Button Label
     * @param listener OnClickListener for Button-Click
     */
    protected void printError(int layoutID, String errorCode, int buttonLabelId, View.OnClickListener listener){
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(findViewById(layoutID), getString(resId), Snackbar.LENGTH_LONG)
                .setAction(getString(buttonLabelId), listener);

        snackbar.show();
    }

    /**
     * Print some Error in Helper-Field from android.support.design.widget.TextInputLayout
     * @param id Ressource id from Layout-Element in xml-Layout
     * @param errorCode Errorcode to print Error-Messsage (See in error_code)
     */
    protected void printInputError(int id, String errorCode){
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        TextInputLayout til = (TextInputLayout) findViewById(id);
        til.setErrorEnabled(true);
        til.setError(getString(resId));
    }

    /**
     * Remove some Error from Helper-Field from android.support.design.widget.TextInputLayout
     * @param id Ressource id from Layout-Element in xml-Layout which has the Error
     */
    protected void removeInputError(int id){
        TextInputLayout til = (TextInputLayout) findViewById(id);
        til.setErrorEnabled(false);
    }

    /**
     * Remove all Error from View
     */
    protected abstract void removeAllErrors();

    /**
     * Set Text to a TextView with id
     * @param id id of the TextView
     * @param text text to put in
     */
    protected void setText(int id, String text){
        ((TextView)findViewById(id)).setText(text);
    }

    /**
     * Set Text to a TextView with id
     * @param id id of the TextView
     */
    protected String getTVText(int id){
        return ((EditText)findViewById(id)).getText().toString();
    }
    /**
     * Set Drawable to a ImageView with id
     * @param id id of the ImageView
     * @param drawable id of Drawable
     */
    protected void setPic(int id, int drawable){
        ((ImageView)findViewById(id)).setImageDrawable(getResources().getDrawable(drawable));
    }
    /**
     * Set Bitmap to a ImageView with id
     * @param id id of the ImageView
     * @param bitmap bitmap to put in
     */
    protected void setPic(int id, Bitmap bitmap){
        ((ImageView)findViewById(id)).setImageBitmap(bitmap);
    }
    public void setRefresher(SwipeRefreshLayout refresher){
        this.refresher = refresher;
        refresher.setOnRefreshListener(this);
    }

    public void showProgressBar(boolean flag){
        if(refresher!=null){
            refresher.setRefreshing(flag);
        }
    }

    private void openCrop(Uri uri){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("openCrop", "true");
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 2048);
            cropIntent.putExtra("outputY", 1024);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException e){
            onCameraException(e);
        }
    }
    //protected void openCamera(){
    //    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//
    //        // Create the File where the photo should go
    //        File photoFile = null;
    //        try {
    //        Log.e("CCCamera","HIER");
    //            photoFile = createImageFile();
    //        } catch (IOException ex) {
    //            ex.printStackTrace();
    //            onCameraException(ex);
    //        }
    //        // Continue only if the File was successfully created
    //        if (photoFile != null) {
    //            Uri photoURI = FileProvider.getUriForFile(this, "com.omgproduction.dsport_application.fileprovider", photoFile);
    //            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
    //            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
    //        }
    //    }
    //    else{
    //        onCameraException(new NullPointerException());
    //    }
    //}
//
    //private File createImageFile() throws IOException {
    //    // Create an image file name
    //    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    //    String imageFileName = "JPEG_" + timeStamp + "_";
    //    File storageDir = new File(Environment.getExternalStoragePublicDirectory(
    //            Environment.DIRECTORY_DCIM), "Camera");
    //    File image = File.createTempFile(imageFileName, ".jpg", storageDir);
//
    //    mCurrentPhotoPath = image.getAbsolutePath();
    //    return image;
    //}
//
    //@Override
    //public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
    //        addPicToGallery();
    //        onBitmapResult(getScaledBitmap());
    //    }
    //}
    //private Bitmap getScaledBitmap() {
//
    //    // Get the dimensions of the bitmap
    //    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    //    bmOptions.inJustDecodeBounds = false;
//
    //    return BitmapFactory.decodeFile(mCurrentPhotoPath, null);
    //}
    //private void addPicToGallery() {
    //    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    //    File f = new File(mCurrentPhotoPath);
    //    Uri contentUri = Uri.fromFile(f);
    //    mediaScanIntent.setData(contentUri);
    //    this.sendBroadcast(mediaScanIntent);
    //}

    protected void onCameraException(Exception e){};

    protected void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), SELECT_PICTURE);
    }

    protected void openCamera() {
        try{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
        }catch(ActivityNotFoundException e){
            onCameraException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            final Bundle extras = data.getExtras();
            if (requestCode == CAMERA_REQUEST_CODE || requestCode == SELECT_PICTURE) {
                //TODO FIX QUALITY OF BITMAP
                openCrop(data.getData());
            }else if(requestCode == PIC_CROP){
                //TODO FIX QUALITY OF BITMAP
                Bitmap thePic = extras.getParcelable("data");
                onBitmapResult(thePic);
            }
        }
    }

    protected void onBitmapResult(Bitmap bitmap){

    }

    protected User getLocalUser(){
        User user = userService.getLocalUser();
        if(userService.isAvailable(user)){
            return user;
        }
        Log.e("FragmentActivity", "USER NOT FOUND");
        logoutUser();
        return user;
    }

    protected void logoutUser(){
        sessionService.logout();
    }



}
