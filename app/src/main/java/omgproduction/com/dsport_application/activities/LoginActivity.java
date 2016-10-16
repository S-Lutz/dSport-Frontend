package omgproduction.com.dsport_application.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import omgproduction.com.dsport_application.R;

public class LoginActivity extends Activity implements View.OnClickListener{

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        findViewById(R.id.registration_link).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registration_link :
                startActivity(new Intent(context, RegisterActivity.class));
        }
    }
}
