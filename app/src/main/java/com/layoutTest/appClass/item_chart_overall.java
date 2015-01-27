package com.layoutTest.appClass;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.anjoyo.liuxiaowei.R;

public class item_chart_overall extends ChartItem {

    private Typeface mTf;

    public item_chart_overall(ChartData cd, Context c) {
        super(cd);

        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null|| !convertView.getTag().getClass().equals(ViewHolder.class)) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
            holder.chart = (LineChart) convertView.findViewById(R.id.linechart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        holder.chart.setDrawYValues(false);
        holder.chart.setDescription("");
        holder.chart.setDrawVerticalGrid(false);
        holder.chart.setDrawGridBackground(false);

        XLabels xl = holder.chart.getXLabels();
        xl.setCenterXLabelText(true);
        xl.setPosition(XLabels.XLabelPosition.BOTTOM);
        xl.setTypeface(mTf);

        YLabels yl = holder.chart.getYLabels();
        yl.setTypeface(mTf);
        yl.setLabelCount(5);

        // set data
        holder.chart.setData((LineData) mChartData);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateX(1000);

        return convertView;
    }

    private static class ViewHolder {
        LineChart chart;
    }
}
