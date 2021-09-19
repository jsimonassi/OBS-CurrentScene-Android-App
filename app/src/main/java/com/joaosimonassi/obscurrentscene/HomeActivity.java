package com.joaosimonassi.obscurrentscene;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button finishServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        ForegroundService.activity = HomeActivity.this;

        startService();
        finishServiceBtn = findViewById(R.id.finish_btn);
        finishServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeService();
            }
        });
    }

    private void closeService(){
        ForegroundService.window.close();
        ForegroundService.window = null;
        stopService( new Intent(HomeActivity.this, ForegroundService.class));
    }


    public void startService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if(Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(this, ForegroundService.class));
                } else {
                    startService(new Intent(this, ForegroundService.class));
                }
            }
        }else{
            startService(new Intent(this, ForegroundService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeService();
    }
}