package com.nssoft.app.steptogether.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nssoft.app.steptogether.R;
import com.nssoft.app.steptogether.core.listensensor.ListenSensorContract;
import com.nssoft.app.steptogether.core.listensensor.ListenSensorPresenter;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentStep extends Fragment implements ListenSensorContract.View{
    View view;
    Unbinder unbinder;

    @BindView(R.id.pc_step)
    PieChart pcStep;
    @BindView(R.id.tv_steps)
    TextView tvSteps;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_average)
    TextView tvAverage;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.averageandtotal)
    LinearLayout averageandtotal;
    @BindView(R.id.ll_average_total_text)
    LinearLayout llAverageTotalText;
    @BindView(R.id.bc_step)
    BarChart bcStep;
    private PieModel sliceGoal, sliceCurrent;

    ListenSensorPresenter listenSensorPresenter;
    private int goal;
    private boolean showSteps = true;
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_step, container, false);
        initView();
        return view;
    }

    private void initView() {
        unbinder = ButterKnife.bind(this, view);

        sliceCurrent = new PieModel("", 0, Color.parseColor("#FF00B3"));
        pcStep.addPieSlice(sliceCurrent);
        sliceGoal = new PieModel("", goal, Color.parseColor("#00FFE1"));
        pcStep.addPieSlice(sliceGoal);
        pcStep.setDrawValueInPie(false);
        pcStep.setUsePieRotation(true);
        pcStep.startAnimation();

        listenSensorPresenter = new ListenSensorPresenter(getActivity(), this);
    }

    @OnClick({R.id.pc_step, R.id.bc_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pc_step:
                break;
            case R.id.bc_step:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listenSensorPresenter.resume();
    }

    @Override
    public void resume(int mucTieu) {
        this.goal = mucTieu;
        stepsDistanceChanged();
    }

    @Override
    public void updatePie(int todayOffSet, int sinceBoot, int totalStart, int totalDays, int mucTieu, int stepSize) {
        goal = mucTieu;
        int stepsToday = Math.max(todayOffSet + sinceBoot, 0);
        sliceCurrent.setValue(stepsToday);
        Log.d("SANG_TEST", todayOffSet + " / " + sinceBoot);
        if (goal - stepsToday > 0) {
            if (pcStep.getData().size() == 1) {
                pcStep.addPieSlice(sliceGoal);
            }
            sliceGoal.setValue(goal - stepsToday);
        } else {
            pcStep.clearChart();
            pcStep.addPieSlice(sliceCurrent);
        }

        pcStep.update();
        if (showSteps) {
            tvSteps.setText(formatter.format(stepsToday));
            tvTotal.setText(formatter.format(totalStart + stepsToday));
            tvAverage.setText(formatter.format((totalStart + stepsToday) / totalDays));
        } else {
            float distanceToday = stepsToday * stepSize;
            float distanceTotal = (totalStart + stepsToday) * stepSize;
            distanceToday /= 100000;
            distanceTotal /= 100000;
            tvSteps.setText(formatter.format(distanceToday));
            tvTotal.setText(formatter.format(distanceTotal));
            tvAverage.setText(formatter.format(distanceTotal / totalDays));
        }
    }

    @Override
    public void updateBars(List<Pair<Long, Integer>> last) {

    }

    private void stepsDistanceChanged() {
        if (showSteps) {
            tvUnit.setText("Steps");
        } else {
            tvUnit.setText("Km");
        }
        listenSensorPresenter.updatePie();
        listenSensorPresenter.updateBars();
    }

    @Override
    public void onPause() {
        super.onPause();
        listenSensorPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
