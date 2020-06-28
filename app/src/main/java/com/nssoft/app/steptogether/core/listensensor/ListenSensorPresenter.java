package com.nssoft.app.steptogether.core.listensensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.nssoft.app.steptogether.data.DataBaseManager;
import com.nssoft.app.steptogether.data.SharedPreferencesHelper;
import com.nssoft.app.steptogether.util.DateUtil;

public class ListenSensorPresenter implements ListenSensorContract.Presenter, SensorEventListener {
    ListenSensorContract.View view;
    Context context;
    private int todayOffset, totalStart, goal, sinceBoot, totalDays, stepSize;

    public ListenSensorPresenter(Context context, ListenSensorContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void resume() {
        DataBaseManager dataBase = DataBaseManager.getInstance(context);

        /**
         * mục tiêu sẻ làm sau mặc định 10.000
         */
        goal = 10000;

        /**
         * độ dài bước chân sẻ làm sau mặc định 50cm
         */
        stepSize = 70;

        todayOffset = dataBase.getSteps(DateUtil.getToday());
        sinceBoot = dataBase.getCurrentSteps();
        int pauseDifference = sinceBoot - (int) SharedPreferencesHelper.get(context, "pauseCount", sinceBoot);

        /**
         * đăng kí sensorlistener để cập nhật cho giao diện
         */
        registerSensor();

        sinceBoot = sinceBoot - pauseDifference;
        totalStart = dataBase.getTotalWithoutToday();
        totalDays = dataBase.getDays();

        view.resume(goal);

        dataBase.close();
    }

    @Override
    public void pause() {
        unRegisterSensor();
        DataBaseManager dataBase = DataBaseManager.getInstance(context);
        dataBase.saveCurrentSteps(sinceBoot);
        dataBase.close();
    }

    @Override
    public void updatePie() {
        view.updatePie(todayOffset, sinceBoot, totalStart, totalDays, goal, stepSize);
    }

    @Override
    public void updateBars() {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (todayOffset == Integer.MIN_VALUE) {
            /**
             * hôm nay chưa có dưa liệu
             */
            todayOffset = -(int) event.values[0];
            DataBaseManager dataBase = DataBaseManager.getInstance(context);
            dataBase.insertNewDay(DateUtil.getToday(), (int) event.values[0]);
            dataBase.close();
        }
        sinceBoot = (int) event.values[0];
        updatePie();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /**
         * không quan tâm hihi $
         */
    }

    private void registerSensor() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        try {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI, 0);
        } catch (Exception e) {
            Log.d("SANG_TEST", "Catch registerListener");
        }
    }

    private void unRegisterSensor() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        try {
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            Log.d("SANG_TEST", "Catch unRegisterListener");
        }
    }
}
