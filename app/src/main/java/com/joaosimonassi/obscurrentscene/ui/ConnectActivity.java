package com.joaosimonassi.obscurrentscene.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joaosimonassi.obscurrentscene.ObsConfig;
import com.joaosimonassi.obscurrentscene.R;
import com.joaosimonassi.obscurrentscene.SocketCallBacks;
import com.joaosimonassi.obscurrentscene.Storage;
import com.joaosimonassi.obscurrentscene.UdpSocket;

import java.util.List;


public class ConnectActivity extends AppCompatActivity {

    private EditText ip, port;
    private Button connectBtn;
    private TextView helpBtn, downloadScriptBtn;
    private CheckBox savedRequiredCheckBox;
    SocketCallBacks callBacks = new SocketCallBacks() {
        @Override
        public void onEvent(String currentScene) {
        }

        @Override
        public void onAddComplete(List<String> scenes) {
            Log.d("DATAGRAMA", "Recebi uma lista!");
            ObsConfig.current_scenes = scenes;
            startActivity(new Intent(ConnectActivity.this, SelectSceneActivity.class));
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
        downloadScriptBtn = findViewById(R.id.script_download_text);
        savedRequiredCheckBox = findViewById(R.id.saveCheckBox);

        connectBtn.setOnClickListener(v -> initConnect(ip.getText().toString(), port.getText().toString()));
        helpBtn.setOnClickListener(v -> startActivity(new Intent(ConnectActivity.this, HelpActivity.class)));
        downloadScriptBtn.setOnClickListener(v -> {
            //TODO: Pegar URL de alguma APi
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        });

        String dbIp = Storage.getValue("ip", ConnectActivity.this);
        String dbPort = Storage.getValue("port", ConnectActivity.this);

        if(!dbIp.isEmpty() || !dbPort.isEmpty()){
            ip.setText(dbIp);
            port.setText(dbPort);
        }
    }


    private boolean initConnect(String ip, String port){
        if(ip.isEmpty() || port.isEmpty()){
            showMessage("Preencha todas as informações!");
            return false;
        }
        Toast.makeText(this, "Conectando", Toast.LENGTH_LONG).show();
        if(savedRequiredCheckBox.isEnabled()){
            Storage.saveValue("ip", ip, ConnectActivity.this);
            Storage.saveValue("port", port, ConnectActivity.this);
        }else{
            Storage.clearStorage(ConnectActivity.this);
        }
        UdpSocket.initConnection(ip, port, callBacks);
        return true;
    }

    private void showMessage(String msg){
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ops");
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", (arg0, arg1) -> { });
        alert = builder.create();
        alert.show();

    }

    // check for permission again when user grants it from
    // the device settings, and start the service
    @Override
    protected void onResume() {
        super.onResume();
    }
}