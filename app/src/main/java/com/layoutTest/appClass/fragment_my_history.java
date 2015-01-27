package com.layoutTest.appClass;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anjoyo.liuxiaowei.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.YLabels;

/**
 * Created by gary on 11/1/2015.
 */
public class fragment_my_history extends SimpleFragment {

//    public static Fragment newInstance() {
//        return new fragment_1day_chart();
//    }

    private PieChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_history, container, false);

        mChart = (PieChart) v.findViewById(R.id.Portfolo_PieChart);
        mChart.setDescription("");

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        mChart.setValueTypeface(tf);
        mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));
        mChart.setUsePercentValues(true);
        mChart.setCenterText("Quarterly\nRevenue");
        mChart.setCenterTextSize(22f);

        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);

        // enable / disable drawing of x- and y-values
//        mChart.setDrawYValues(false);
//        mChart.setDrawXValues(false);

        mChart.setData(generatePieData());

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

//        TextView tv = new TextView(getActivity());
//
//        tv.setText("Hello");
//        container.addView(tv);


        return v;
    }
}

