package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.anjoyo.liuxiaowei.R;
import com.example.leo.stocktest.CommonAccount;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 4/1/2015.
 */

public class Fragment_my extends Fragment {
    TextView MyRegisterButton;
    TextView MyLoginButton;
//    TextView MyName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = null;

        {

            v = inflater.inflate(R.layout.fragment_my, container, false);

            MyRegisterButton = (TextView) v.findViewById(R.id.My_register);
            MyLoginButton = (TextView) v.findViewById(R.id.My_login);
//            MyName = (TextView) v.findViewById(R.id.My_logintoast);

//            if (CommonAccount.getCurrentAccount() != null)
//                MyName.setText(CommonAccount.getCurrentAccount().getPrivateName());

            MyRegisterButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), activity_registration.class);
                    startActivity(intent);

                }
            });


            MyLoginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), activity_login.class);
                    int reqCode = ((activity_mframe)getActivity())
                            .generateRequestCode(activity_mframe.REQCODE_LOGIN, 3);

                    Log.d("generateReqCode", "code= " + reqCode);

                    getActivity().startActivityForResult(intent, reqCode);

                }
            });


        }

        ListView lv = (ListView) v.findViewById(R.id.my_chartlist);
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        list.add(new item_chart_1day(generateDataLine(1), getActivity().getApplicationContext()));
        list.add(new item_chart_1month(generateDataLine(2), getActivity().getApplicationContext()));
        list.add(new item_chart_3month(generateDataLine(3), getActivity().getApplicationContext()));
        list.add(new item_chart_6month(generateDataLine(4), getActivity().getApplicationContext()));
        list.add(new item_chart_12month(generateDataLine(5), getActivity().getApplicationContext()));
        list.add(new item_chart_overall(generateDataLine(6), getActivity().getApplicationContext()));
        list.add(new item_my_piechart(generateDataPie(7), getActivity().getApplicationContext()));

        ChartDataAdapter cda = new ChartDataAdapter(getActivity().getApplicationContext(), list);
        lv.setAdapter(cda);
        return  v;

    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 2; // we have 2 different item-types
        }
    }




    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> Yvaluelist1 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            Yvaluelist1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d1 = new LineDataSet(Yvaluelist1, "HSI Index");
        d1.setLineWidth(3f);
        d1.setCircleSize(5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<Entry> Yvaluelist2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            Yvaluelist2.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d2 = new LineDataSet(Yvaluelist2, "Your Portfolio");
        d2.setLineWidth(3f);
        d2.setCircleSize(5f);
        d2.setHighLightColor(0xFFFF8800);
        d2.setColor(0xFFFF8800);
        d2.setCircleColor(0xFFFF8800);

        ArrayList<LineDataSet> datasetlist = new ArrayList<LineDataSet>();
        datasetlist.add(d1);
        datasetlist.add(d2);

        LineData cd = new LineData(getMonths(), datasetlist);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry((int) (Math.random() * 70) + 30, i));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(getMonths(), d);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(int cnt) {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new Entry((int) (Math.random() * 70) + 30, i));
        }

        PieDataSet piedataset = new PieDataSet(entries, "");

        // space between slices
        piedataset.setSliceSpace(5f);
        piedataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(getQuarters(), piedataset);
        return cd;
    }

    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("CHEUNG KONG 0001");
        q.add("CLP HOLDINGS 0002");
        q.add("CHINA GAS CO LTD 0003");
        q.add("HYSAN DEV 0014");

        return q;
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }
}



//package com.layoutTest.appClass;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.anjoyo.liuxiaowei.R;
//import com.example.leo.stocktest.CommonAccount;
//
///**
// * Created by gary on 4/1/2015.
// */
//
//public class Fragment_my extends Fragment {
//    TextView MyRegisterButton;
//    TextView MyLoginButton;
//    TextView MyChartButton;
//    TextView MyHistoryButton;
//    TextView MyName;
//    ViewPager mViewPager;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View v = null;
//
//        {
//
//            v = inflater.inflate(R.layout.fragment_my, container, false);
//
//            MyRegisterButton = (TextView) v.findViewById(R.id.My_register);
//            MyLoginButton = (TextView) v.findViewById(R.id.My_login);
//            MyChartButton = (TextView) v.findViewById(R.id.My_chart);
//            MyHistoryButton = (TextView) v.findViewById(R.id.My_history) ;
//            MyName = (TextView) v.findViewById(R.id.My_logintoast);
//
//            if (CommonAccount.getCurrentAccount() != null)
//                MyName.setText(CommonAccount.getCurrentAccount().getPrivateName());
//
//            MyRegisterButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(getActivity(), activity_registration.class);
//                    startActivity(intent);
//
//                }
//            });
//
//
//            MyLoginButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(getActivity(), activity_login.class);
//                    int reqCode = ((activity_mframe)getActivity())
//                            .generateRequestCode(activity_mframe.REQCODE_LOGIN, 3);
//
//                    Log.d("generateReqCode", "code= " + reqCode);
//
//                    getActivity().startActivityForResult(intent, reqCode);
//
//                }
//            });
//            MyChartButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    mViewPager.setCurrentItem(0);
//
//                }
//            });
//            MyHistoryButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    mViewPager.setCurrentItem(1);
//
//                }
//            });
//
//            LinearLayout performanceList = (LinearLayout) v.findViewById(R.id.My_TradePerform);
//            Log.d("ShowPerformList", "Hii");
//            performanceList.addView(inflater.inflate(R.layout.activity_my_detail, null));
//
//        }
//
//
//        mViewPager = (ViewPager) v.findViewById(R.id.fragment_my_pager);
//
//        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
//
////        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
////        pager.setOffscreenPageLimit(3);
//
//
//
//
//
//
//        return  v;
//
//    }
//
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
////        mViewPager = (ViewPager) view.findViewById(R.id.pager);
////
////        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
//
//    }
//
//    public static class MyAdapter extends FragmentPagerAdapter {
//
//        public MyAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int pos) {
//            Fragment f = null;
//
//            Log.d("Fragment my chart frag", "pos=" + pos);
//
//            switch(pos) {
//                case 0:
//                    f = new fragment_my_chart();
//                    break;
//                case 1:
//                    f = new fragment_my_history();
//                    break;
//
//            }
//
//            return f;
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    }
//
//}
