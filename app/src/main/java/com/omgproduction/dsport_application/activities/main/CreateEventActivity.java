package com.omgproduction.dsport_application.activities.main;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.CreateEventStartValues;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.EventService;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.DateConverter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Strik on 09.03.2017.
 */

public class CreateEventActivity extends AbstractFragmentActivity implements CreateEventStartValues, DatePickerDialog.OnDateSetListener{

    private String eventBoardOwner;

    private int type;

    private Bitmap eventPicture;
    private Bitmap picture;

    private Date currentEventDate;

    private EventService eventService;

    private GoogleApiClient googleApi;

    @Override
    protected void onStart() {
        googleAPI.connect();
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_create_event);

        currentEventDate = new Date();

        prepareEventData();

        findViewById(R.id.create_event_button).setOnClickListener(this);

        findViewById(R.id.create_event_camera1_button).setOnClickListener(this);
        findViewById(R.id.create_event_gallery1_button).setOnClickListener(this);

        findViewById(R.id.create_event_camera2_button).setOnClickListener(this);
        findViewById(R.id.create_event_gallery2_button).setOnClickListener(this);

        DateConverter converter = new DateConverter();

        setText(R.id.event_date_tv, converter.getFormatedDate(currentEventDate, this));
        findViewById(R.id.event_date_tv).setOnClickListener(this);

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
        final String title = ((AppCompatEditText)findViewById(R.id.create_event_title)).getText().toString();
        final String text = ((AppCompatEditText)findViewById(R.id.create_event_text)).getText().toString();

        DateConverter converter = new DateConverter();

        final String eventDate = converter.convertDate(currentEventDate);
        final String location = ((AppCompatEditText)findViewById(R.id.event_location_tv)).getText().toString();

        String bmpEventPicture;
        String bmpPicture;

        if(eventPicture==null){
            bmpEventPicture = "";
        }else {
            bmpEventPicture = BitmapUtils.getStringFromBitmap(eventPicture);
        }
        if (picture==null){
            bmpPicture="";
        }else{
            bmpPicture= BitmapUtils.getStringFromBitmap(picture);
        }

        final String pictureEvent = bmpEventPicture;
        final String picture = bmpPicture;

        User user = userService.getLocalUser();
        eventBoardOwner = (eventBoardOwner==null)?user.getId():eventBoardOwner;
        eventService.createEvent(user.getId(), eventBoardOwner,(type == CREATE_EVENT_TEXT_VALUE)?"":pictureEvent,(type == CREATE_EVENT_TEXT_VALUE)?"":picture,text,title, eventDate, location, new RequestFuture<Void>(){
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
            public void onFailure(String errorCode) {
                printError(R.id.activity_create_event, errorCode, R.string.retry, new View.OnClickListener() {
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
        switch (v.getId()){
            case R.id.create_event_button:
                saveEvent();
                break;
            case R.id.create_event_camera1_button:
                openCamera();
                break;
            case R.id.create_event_gallery1_button:
                openGallery();
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
        }
    }

    public void prepareEventData() {
        eventBoardOwner = getIntent().getStringExtra(INTENT_EVENT_OWNER_ID);
        type = getIntent().getIntExtra(CREATE_EVENT_TYPE_KEY, CREATE_EVENT_TEXT_VALUE);

        switch (type){
            case CREATE_EVENT_PICTURE_VALUE:
                openCamera();
                break;
            case CREATE_EVENT_GALLERY_VALUE:
                openGallery();
        }
    }

    @Override
    protected void onBitmapResult(Bitmap bitmap) {
        type = CREATE_EVENT_PICTURE_VALUE;
        eventPicture = bitmap;
        ((ImageView)findViewById(R.id.create_event_event_picture)).setImageBitmap(bitmap);
        findViewById(R.id.create_event_event_picture).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        DateConverter converter = new DateConverter();
        currentEventDate = converter.getDateFromValues(year,month+1,dayOfMonth);
        setText(R.id.event_date_tv, converter.getFormatedDate(currentEventDate,this));
    }
}
