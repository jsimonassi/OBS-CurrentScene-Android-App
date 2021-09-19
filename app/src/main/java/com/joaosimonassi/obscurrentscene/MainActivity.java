package com.joaosimonassi.obscurrentscene;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText ip, port;
    private Button connectBtn;
    private TextView helpBtn;

    SocketCallBacks callBacks = new SocketCallBacks() {
        @Override
        public void onEvent(String currentScene) {
        }

        @Override
        public void onAddComplete(List<String> scenes) {
            Log.d("DATAGRAMA", "Recebi uma lista!");
            ObsConfig.current_scenes = scenes;
            startActivity(new Intent(MainActivity.this, SelectSceneActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ip = findViewById(R.id.ip_input);
        port = findViewById(R.id.port_input);
        connectBtn = findViewById(R.id.connect_btn);
        helpBtn = findViewById(R.id.helpBtn);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initConnect(ip.getText().toString(), port.getText().toString());
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
            }
        });

        checkOverlayPermission();
    }


    private boolean initConnect(String ip, String port){
        if(ip.isEmpty() || port.isEmpty()){
            showMessage("Preencha todas as informações!");
            return false;
        }
        Toast.makeText(this, "Conectando", Toast.LENGTH_LONG).show();
        UdpSocket.initConnection(ip, port, callBacks);
        return true;
    }

    private void showMessage(String msg){
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ops");
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) { }
        });

        alert = builder.create();
        alert.show();

    }

    // method to ask user to grant the Overlay permission
    public void checkOverlayPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }

    // check for permission again when user grants it from
    // the device settings, and start the service
    @Override
    protected void onResume() {
        super.onResume();
    }
}