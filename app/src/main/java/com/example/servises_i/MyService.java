package com.example.servises_i;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class MyService extends Service {
    String name = " ";
    
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            saveToFile();
        }
    };

    private void saveToFile() {
        try {
            File directory = new File(getFilesDir() + File.separator + "NameFolder");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory, "name.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream=new FileOutputStream(file,true);
            OutputStreamWriter writer=new OutputStreamWriter(fileOutputStream);
            writer.append(name+"\n");
            writer.close();

            Intent intent =new Intent("com.xyz.service");
            intent.putExtra("data","write Done");
            sendBroadcast(intent);
        } catch (Exception e) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            name = intent.getStringExtra("name");
        }
        launchBackGroundThread();
        return super.onStartCommand(intent, flags, startId);
    }
    private void launchBackGroundThread(){
        Thread thread = new Thread(task);
        thread.start();
    }

    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotificationAndStartForeGround();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    private void showNotificationAndStartForeGround() {
        createChannel();

        NotificationCompat.Builder notificationBuilder = null;
        notificationBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Service is running : yaay")
                .setContentText("name is android")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification = null;
        notification = notificationBuilder.build();
        startForeground(1, notification);
    }


    /*
Create noticiation channel if OS version is greater than or eqaul to Oreo
*/
    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications");
            Objects.requireNonNull(this.getSystemService(NotificationManager.class)).createNotificationChannel(channel);
        }
    }
}