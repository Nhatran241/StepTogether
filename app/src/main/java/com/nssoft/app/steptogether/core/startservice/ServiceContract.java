package com.nssoft.app.steptogether.core.startservice;

public class ServiceContract {
    public interface View {
        void startSensorService(String notifi);
    }

    public interface Presenter {
        void startSensorService();
    }
}
