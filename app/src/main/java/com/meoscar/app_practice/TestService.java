package com.meoscar.app_practice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TestService extends Service {
    private static final String TAG = "app_practice->Service";
    private Timer timer;
    private static int count = 0;
    private static Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static PendingIntent createPendingRadioDialog(Context context) {
        Intent myIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, myIntent, Intent.FILL_IN_ACTION);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate +++");
        context = getApplicationContext();

        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentIntent(createPendingRadioDialog(context))
                .setContentTitle("forground service test")
                .setSmallIcon(R.drawable.icrt_trans)
                .setWhen(System.currentTimeMillis())
                .setContentText("")
                .setColorized(true)
                .setColor(this.getResources().getColor(R.color.colorAccent))
                .build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("meoscar.is.cool".equals(intent.getAction())) {
            Log.d(TAG, "meoscar.is.cool");
            if (timer == null) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new RefreshTask(), 0, 3000);
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy +++");
        try {
            timer.cancel();
            timer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        count = 0;
        stopForeground(true);
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "RefreshTask : Service ha been running for : " + count + " Sec");
            count += 3;
        }
    }
}
