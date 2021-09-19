package com.joaosimonassi.obscurrentscene;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.List;

public class ForegroundService extends Service {

    static Window window;
    Context context;
    public static Activity activity;

    SocketCallBacks callBacks = new SocketCallBacks() {
        @Override
        public void onEvent(String currentScene) {
            Log.d("DATAGRAMA", "Recebi esse event: " + currentScene);
            if(currentScene.contains(ObsConfig.myScene)){
                window.close();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        window = new Window(context, R.layout.popup_live_window);
                        window.open();
                    }
                });

            }else{
                window.close();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        window = new Window(context, R.layout.popup_free_window);
                        window.open();
                    }
                });
            }
        }

        @Override
        public void onAddComplete(List<String> scenes) {

        }
    };


    public ForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        UdpSocket.callbacks = callBacks;
        context = this;
        window=new Window(this, R.layout.popup_hold_window);
        window.open();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.joaosimonassi.obscurrentscene";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_MIN);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Service running")
                .setContentText("Displaying over other apps")

                // this is important, otherwise the notification will show the way
                // you want i.e. it will show some default notification
                .setSmallIcon(R.drawable.ic_launcher_foreground)

                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


}
