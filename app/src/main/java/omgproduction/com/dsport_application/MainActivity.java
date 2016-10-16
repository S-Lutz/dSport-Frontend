package omgproduction.com.dsport_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import omgproduction.com.dsport_application.activities.LoginActivity;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, LoginActivity.class));

    }
}
