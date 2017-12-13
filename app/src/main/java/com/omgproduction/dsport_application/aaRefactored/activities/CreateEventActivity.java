package com.omgproduction.dsport_application.aaRefactored.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.helper.PictureUtil;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.aaRefactored.services.EventService;
import com.omgproduction.dsport_application.aaRefactored.views.CheckedEditText;
import com.omgproduction.dsport_application.aaRefactored.views.LoadingView;
import com.omgproduction.dsport_application.utils.DateConverter;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.omgproduction.dsport_application.config.CameraOptions.SELECT_PICTURE;


public class CreateEventActivity extends AbstractActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final int PLACE_PICKER_REQUEST = 5;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1234;

    private boolean cameraRequest = false;
    private EventService eventService;

    private GoogleApiClient googleApiClient;

    private Place place;

    private Date currentEventDate;


    private LoadingView loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.eventService = new EventService();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        loadingView = (LoadingView) findViewById(R.id.loading_view);

        DateConverter converter = new DateConverter();
        currentEventDate = new Date();

        findViewById(R.id.create_event_button).setOnClickListener(this);
        findViewById(R.id.create_event_camera_button).setOnClickListener(this);
        findViewById(R.id.create_event_gallery_button).setOnClickListener(this);
        findViewById(R.id.event_location_address_tv).setOnClickListener(this);
        findViewById(R.id.event_location_name_tv).setOnClickListener(this);
        findViewById(R.id.event_date_tv).setOnClickListener(this);

        setText(R.id.event_date_tv, converter.getFormatedDate(currentEventDate, this));

        findCurrentPlace();
        setUpToolbarTitle();
    }

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
    public int getLayout() {
        return R.layout.new_layout_activity_create_event;
    }

    private void setUpToolbarTitle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create a new event");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_event_button:
                loadingView.show();
                saveEvent(cameraRequest);
                break;
            case R.id.create_event_camera_button:
                openCamera();
                break;
            case R.id.create_event_gallery_button:
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateConverter converter = new DateConverter();
        currentEventDate = converter.getDateFromValues(year, month + 1, dayOfMonth);
        setText(R.id.event_date_tv, converter.getFormatedDate(currentEventDate, this));
    }



    protected void findCurrentPlace() {
        // Create the location request
        try{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            cameraRequest = true;
            setPic(this, findViewById(R.id.imageView));
        } else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            cameraRequest = false;
            mCurrentPhotoPath = PictureUtil.getPath(this, data.getData());
            setPic(this, findViewById(R.id.imageView));
        }else if(requestCode == PLACE_PICKER_REQUEST){
            place = PlacePicker.getPlace(data,this);
            setText(R.id.event_location_name_tv, String.valueOf(place.getName()));
            setText(R.id.event_location_address_tv, String.valueOf(place.getAddress()));
        }
    }

    private void saveEvent(final boolean cameraRequest) {
        DateConverter converter = new DateConverter();

        CheckedEditText createEventTitle = (CheckedEditText) findViewById(R.id.create_event_title);
        if (!createEventTitle.checkRequired()) return;

        CheckedEditText createEventText = (CheckedEditText) findViewById(R.id.create_event_text);
        if (!createEventText.checkRequired()) return;

        EventNode eventNode = new EventNode(createEventTitle.getTextString(),
                createEventText.getTextString(),
                converter.convertDate(currentEventDate),
                ((TextView) findViewById(R.id.event_location_name_tv)).getText().toString(),
                ((TextView) findViewById(R.id.event_location_address_tv)).getText().toString());

        eventService.createEvent(this, eventNode, new BackendCallback<EventNode>() {
            @Override
            public void onSuccess(EventNode result, Map<String, String> responseHeader) {
                savePicture(result, cameraRequest);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                Toast.makeText(CreateEventActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void savePicture(EventNode eventNode, final boolean cameraRequest) {
        System.out.println(eventNode.getCreated());
        if (!mCurrentPhotoPath.isEmpty()) {
            final File imgFile = new File(mCurrentPhotoPath);
            eventService.uploadPicture(this, imgFile, eventNode, new BackendCallback<EventNode>() {
                @Override
                public void onSuccess(EventNode result, Map<String, String> responseHeader) {
                    deleteStoredPic(cameraRequest);
                }

                @Override
                public void onFailure(ErrorResponse error) {
                    deleteStoredPic(cameraRequest);
                }
            });
        }

        loadingView.hide();
        finish();
    }
}
