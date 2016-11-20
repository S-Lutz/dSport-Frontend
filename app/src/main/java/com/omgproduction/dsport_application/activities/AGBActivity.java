package com.omgproduction.dsport_application.activities;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.CheckBox;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.controller.ResourceController;
import com.omgproduction.dsport_application.supplements.activities.AdvancedActivity;
import com.omgproduction.dsport_application.utils.ConnectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AGBActivity extends AdvancedActivity {

    private String agb_text, agb_version;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agb);

        this.context = this;

        Intent i = getIntent();
        //String agbString = i.getStringExtra(ApplicationKeys.TEXT);



        //try {
            //JSONObject agb = new JSONObject(agbString);

            //agb_text = agb.getString(ApplicationKeys.TEXT);
            //agb_version = agb.getString(ApplicationKeys.AGB_VERSION);

            findViewById(R.id.btn_goon).setOnClickListener(this);
            ((AppCompatTextView)findViewById(R.id.agb_info)).setText(getString(R.string.version)+" "+agb_version);
            ((AppCompatTextView)findViewById(R.id.agb_text)).setText(agb_text);

        //} catch (JSONException | NullPointerException e) {
        //    e.printStackTrace();
        //    startActivity(new Intent(this,MainActivity.class));
        //}



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_goon:
                processButtonClick();
                break;
        }
    }

    private void processButtonClick() {
        boolean agb_accepted = ((CheckBox)findViewById(R.id.cb_agb)).isChecked();
        if(agb_accepted){
            onAGBAccepted();
        }else{
            onAGBRejected();
        }
    }

    private void onAGBRejected() {
        printError(R.id.agb_layout,"e4");
    }

    private void onAGBAccepted() {
        /*
        Preferences.getInstance(this)
                .putString(ApplicationKeys.AGB_VERSION,agb_version)
                .commit();

        ResourceController resourceController = ResourceController.getInstance();
        resourceController.putAGBVersion(
                Preferences.getInstance(this)
                        .getStringDetail(ApplicationKeys.USERID, "0"),
                agb_version,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ConnectionUtils.Success(jsonObject)){
                            startMainActivity();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        printError(R.id.agb_layout, "e100", R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                processButtonClick();
                            }
                        });
                    }
                }
        );*/
    }

    private void startMainActivity(){
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    @Override
    protected void removeAllErrors() {

    }

    @Override
    public void onRefresh() {

    }
}
