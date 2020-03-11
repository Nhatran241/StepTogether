package com.nssoft.app.steptogether.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.nssoft.app.steptogether.data.DataBaseManager;
import com.nssoft.app.steptogether.data.SharedPreferencesHelper;
import com.nssoft.app.steptogether.util.DateUtil;

import java.sql.DatabaseMetaData;
import java.util.Date;

import static com.nssoft.app.steptogether.test_sang.AppNotification.CHANNEL_ID;

public class SensorService extends Service implements SensorEventListener {
    public final static int NOTIFICATION_ID = 1;
    private final static long MICROSECONDS_IN_ONE_MINUTE = 60000000;
    private final static long SAVE_OFFSET_TIME = AlarmManager.INTERVAL_HALF_HOUR;
    private final static int SAVE_OFFSET_STEPS = 250;

    private static int steps;
    private static int lastSaveSteps;
    private static long lastSaveTime;

    //BroadcastReceiver

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("SANG_TEST","1 onAccuracyChanged");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("SANG_TEST","2 onSensorChanged: " + event.values[0]);
        steps = (int) event.values[0];
        update();
    }

    private boolean update() {
        Log.d("SANG_TEST","3 update");

        // đi 250 bước hoặc sau 30 phút lưu 1 lần (sang ngày khác)
        if (steps > lastSaveSteps + SAVE_OFFSET_STEPS || (steps > 0 && System.currentTimeMillis() > lastSaveTime + SAVE_OFFSET_TIME)) {
            DataBaseManager db = DataBaseManager.getInstance(this);

            // chưa được lưu
            if (db.getSteps(DateUtil.getToday()) == Integer.MIN_VALUE) {
                int pauseDifference = steps - (int) SharedPreferencesHelper.get(this, "pauseCount", steps);
                db.insertNewDay(DateUtil.getToday(), steps - pauseDifference);
                if (pauseDifference > 0) {
                    SharedPreferencesHelper.put(this, "pauseCount", steps);
                }
            }
            db.saveCurrentSteps(steps);
            db.close();
            lastSaveSteps = steps;
            lastSaveTime = System.currentTimeMillis();
            showNotification();
            //WidgetUpdateService.enqueueUpdate(this);
            return true;
        } else {
            return false;
        }
    }

    private void showNotification() {
        Log.d("SANG_TEST","4 onBind");
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Content Title").build();
        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SANG_TEST","5 onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SANG_TEST", "6 onStartCommand");
        registerSensor();

        if (!update()) {
            showNotification();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SANG_TEST", "7 onCreate");
    }

    private void registerSensor() {
        Log.d("SANG_TEST", "12 registerSensor");
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            Log.d("SANG_TEST", "Catch unregisterListener");
        }

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        try {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL, (int) (5 * MICROSECONDS_IN_ONE_MINUTE));
        } catch (Exception e) {
            Log.d("SANG_TEST", "Catch registerListener");
        }
    }
}
