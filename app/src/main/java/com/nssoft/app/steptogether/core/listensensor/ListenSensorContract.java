package com.nssoft.app.steptogether.core.listensensor;

import android.util.Pair;

import java.util.List;

public class ListenSensorContract {
    public interface View {
        void resume(int mucTieu);
        void updatePie(int todayOffSet, int sinceBoot, int totalStart, int totalDays, int mucTieu, int stepSize);
        void updateBars(List<Pair<Long, Integer>> last);
    }

    public interface Presenter {
        void resume();
        void pause();
        void updatePie();
        void updateBars();
    }
}
