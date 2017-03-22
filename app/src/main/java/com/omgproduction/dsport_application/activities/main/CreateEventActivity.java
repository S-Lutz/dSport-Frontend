package com.omgproduction.dsport_application.activities.main;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.CreateEventStartValues;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.EventService;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.DateConverter;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Strik on 09.03.2017.
 */

public class CreateEventActivity extends AbstractFragmentActivity implements CreateEventStartValues, DatePickerDialog.OnDateSetListener{

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1234;
    private static final int PLACE_PICKER_REQUEST = 5;
    private static final int CODE_TITLE_PIC = 6;
    private static final int CODE_TITLE_PIC_CROP = 7;
    private String eventBoardOwner;

    private int type;

    private Bitmap eventPicture;
    private Bitmap picture;

    private Date currentEventDate;

    private EventService eventService;

    private GoogleApiClient googleApiClient;

    private Place place;

    private AppCompatTextView locationTextView;



    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_create_event);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        currentEventDate = new Date();

        prepareEventData();

        findViewById(R.id.create_event_button).setOnClickListener(this);

        findViewById(R.id.create_event_camera1_button).setOnClickListener(this);
        findViewById(R.id.create_event_gallery1_button).setOnClickListener(this);

        findViewById(R.id.create_event_camera2_button).setOnClickListener(this);
        findViewById(R.id.create_event_gallery2_button).setOnClickListener(this);

        DateConverter converter = new DateConverter();

        findViewById(R.id.event_location_address_tv).setOnClickListener(this);
        findViewById(R.id.event_location_name_tv).setOnClickListener(this);


        setText(R.id.event_date_tv, converter.getFormatedDate(currentEventDate, this));
        findViewById(R.id.event_date_tv).setOnClickListener(this);

        findCurrentPlace();

        eventService = new EventService(this);

        User user = getLocalUser();


        setText(R.id.create_event_username, user.getUsername());
    }

    @Override
    protected void removeAllErrors() {

    }

    @Override
    public void onRefresh() {

    }

    private void saveEvent() {
        final String title = ((AppCompatEditText) findViewById(R.id.create_event_title)).getText().toString();
        final String text = ((AppCompatEditText) findViewById(R.id.create_event_text)).getText().toString();

        DateConverter converter = new DateConverter();

        final String eventDate = converter.convertDate(currentEventDate);
        final String locationName = ((TextView) findViewById(R.id.event_location_name_tv)).getText().toString();
        final String locationAddress = ((TextView) findViewById(R.id.event_location_address_tv)).getText().toString();

        String bmpEventPicture;
        String bmpPicture;

        if (eventPicture == null) {
            bmpEventPicture = "";
        } else {
            bmpEventPicture = BitmapUtils.getStringFromBitmap(eventPicture);
        }
        if (picture == null) {
            bmpPicture = "";
        } else {
            bmpPicture = BitmapUtils.getStringFromBitmap(picture);
        }

        final String pictureEvent = bmpEventPicture;
        final String picture = bmpPicture;

        User user = userService.getLocalUser();
        eventBoardOwner = (eventBoardOwner == null) ? user.getId() : eventBoardOwner;
        eventService.createEvent(user.getId(), eventBoardOwner, (type == CREATE_EVENT_TEXT_VALUE) ? "" : pictureEvent, (type == CREATE_EVENT_TEXT_VALUE) ? "" : picture, text, title, eventDate, Event.toLocationString(locationName, locationAddress), new RequestFuture<Void>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(Void result) {
                CreateEventActivity.super.onBackPressed();
                CreateEventActivity.this.finish();
            }

            @Override
            public void onFailure(int errorCode,String errorMessage) {
                printError(R.id.activity_create_event, errorMessage, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEvent();
                    }
                });
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_event_button:
                saveEvent();
                break;
            case R.id.create_event_camera1_button:
                openCamera(CODE_TITLE_PIC);
                break;
            case R.id.create_event_gallery1_button:
                openGallery(CODE_TITLE_PIC);
                break;
            case R.id.create_event_camera2_button:
                openCamera();
                break;
            case R.id.create_event_gallery2_button:
                openGallery();
                break;
            case R.id.event_date_tv:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentEventDate);
                new DatePickerDialog(
                        this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.event_location_address_tv:
                    startLocationFinder();
                break;
            case R.id.event_location_name_tv:
                startLocationFinder();
                break;
        }
    }

    private void startLocationFinder() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void prepareEventData() {
        eventBoardOwner = getIntent().getStringExtra(INTENT_EVENT_OWNER_ID);
        type = getIntent().getIntExtra(CREATE_EVENT_TYPE_KEY, CREATE_EVENT_TEXT_VALUE);

        switch (type) {
            case CREATE_EVENT_PICTURE_VALUE:
                openCamera();
                break;
            case CREATE_EVENT_GALLERY_VALUE:
                openGallery();
        }
    }

    public void onCameraResult(Bitmap bitmap, File file) {
        type = CREATE_EVENT_PICTURE_VALUE;
        eventPicture = bitmap;
        ((ImageView) findViewById(R.id.create_event_event_picture)).setImageBitmap(bitmap);
        findViewById(R.id.create_event_event_picture).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        DateConverter converter = new DateConverter();
        currentEventDate = converter.getDateFromValues(year, month + 1, dayOfMonth);
        setText(R.id.event_date_tv, converter.getFormatedDate(currentEventDate, this));
    }

    protected void findCurrentPlace() {
        // Create the location request

        try{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                }else{
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){

                }else{
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

            }
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(googleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    place = likelyPlaces.get(0).getPlace();
                    setText(R.id.event_location_name_tv, String.valueOf(place.getName()));
                    setText(R.id.event_location_address_tv, String.valueOf(place.getAddress()));
                    likelyPlaces.release();
                }
            });
        }catch (SecurityException e){
            Toast.makeText(this, R.string.no_gps_permission,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == CAM_REQUEST || requestCode == SELECT_PICTURE) {
                openCrop(getUriFromData(data));
            } else if(requestCode == CODE_TITLE_PIC){
                openCrop(getUriFromData(data), CODE_TITLE_PIC_CROP);
            }else if(requestCode == PIC_CROP){
                onCameraResult(getBitmapFromData( data), new File(getUriFromData(data).getPath()));
            }else if(requestCode == CODE_TITLE_PIC_CROP){
                picture = getBitmapFromData( data);
                ((ImageView) findViewById(R.id.create_event_picture)).setImageBitmap(picture);
            }else if(requestCode == PLACE_PICKER_REQUEST){
                place = PlacePicker.getPlace(data,this);
                setText(R.id.event_location_name_tv, String.valueOf(place.getName()));
                setText(R.id.event_location_address_tv, String.valueOf(place.getAddress()));
            }
        }else {
        }
    }
    public Bitmap getBitmapFromData(Intent data){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), getUriFromData(data));
            return Bitmap.createScaledBitmap(bitmap, CAMERA_DEFAULT_CAPTURE_WIDTH, CAMERA_DEFAULT_CAPTURE_HEIGHT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Uri getUriFromData(Intent data){
        return data.getData();
    }

    public void openCrop(Uri uri) {
        openCrop(uri, PIC_CROP);
    }

    public void openCrop(Uri uri, int CODE) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("openCrop", "true");
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", CAMERA_DEFAULT_CAPTURE_WIDTH);
            cropIntent.putExtra("outputY", CAMERA_DEFAULT_CAPTURE_HEIGHT);
            cropIntent.putExtra("return-data", false);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cropIntent, CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openCamera() {
        openCamera(CAM_REQUEST);
    }

    public void openGallery() {
        openGallery(SELECT_PICTURE);
    }

    private void openGallery(int CODE) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), CODE);
    }

    private void openCamera(int CODE) {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
