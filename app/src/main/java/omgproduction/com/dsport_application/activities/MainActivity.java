package omgproduction.com.dsport_application.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import omgproduction.com.dsport_application.R;

/**
 * Created by Florian on 17.10.2016.
 *
 * MainActivity for the first Access when user open the App and Login
 *
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, LoginActivity.class));

    }
}
