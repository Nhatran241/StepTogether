package com.nssoft.app.steptogether.core.startservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nssoft.app.steptogether.service.SensorService;
import com.nssoft.app.steptogether.service.notification.API26Wrapper;

public class ServicePresenter implements ServiceContract.Presenter {
    ServiceContract.View view;
    Context context;

    public ServicePresenter(Context context) {
        this.view = (ServiceContract.View) context;
        this.context = context;
    }

    @Override
    public void startSensorService() {
        if (Build.VERSION.SDK_INT >= 26) {
            API26Wrapper.startForegroundService(context,
                    new Intent(context, SensorService.class));
            view.startSensorService("API >= 26");
        } else {
            view.startSensorService("API < 26");
        }
    }
}
