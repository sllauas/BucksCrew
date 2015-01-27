package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.liuxiaowei.R;
import com.example.leo.stocktest.CommonAccount;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fragment_ppl_search extends ListFragment{
    ImageView PplSearchButton;
    TextView PplSearchStrategy;
    TextView PplRiskStrategy;
    TextView PplReturnStrategy;
    ExpandTabView expandTabView;
    ArrayList<View> mViewArray;
    ArrayList<TextView> areas;

    ViewLeft viewLeft;
    ViewMiddle viewMiddle;
    ViewRight viewRight;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_ppl_search, container, false);
        PplSearchButton = (ImageView) v.findViewById(R.id.ppl_search_button);
        PplSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), activity_ppl_details.class);
                startActivity(intent);

            }
        });

        expandTabView = (ExpandTabView)v.findViewById(R.id.expandtab_view);
        viewLeft = new ViewLeft(getActivity());
        viewMiddle = new ViewMiddle(getActivity());
        viewRight = new ViewRight(getActivity());

        areas = new ArrayList<TextView>();
        areas.add((TextView)v.findViewById(R.id.people_search_title_strategy));
        areas.add((TextView)v.findViewById(R.id.people_search_title_risk));
        areas.add((TextView)v.findViewById(R.id.people_search_title_return));

        initValue();
        initListener();
        
        
//        ListAdapter listAdapter = new CommonAccListAdapter(getActivity(), Arrays.asList(getResources().getStringArray(R.array.names)));
        List<CommonAccount> cList = CommonAccount.getAccountList();
//        cList.remove(CommonAccount.getCurrentAccount());

        ListAdapter listAdapter = new CommonAccListAdapter(getActivity(), cList);
        setListAdapter(listAdapter);
        
        return v;
}

    private void initValue() {

//        ArrayList<String> mTextArray = new ArrayList<String>();
//        mTextArray.add("Strategy");
//        mTextArray.add("Risk");
//        mTextArray.add("Return");
//
//        if(mViewArray != null) {
////            expandTabView.setValue(mTextArray, mViewArray);
//
////            return;
//
////            mViewArray = new
//        }


        mViewArray = new ArrayList<View>();

        mViewArray.add(viewLeft);
        mViewArray.add(viewMiddle);
        mViewArray.add(viewRight);

//        expandTabView.setValue(mTextArray, mViewArray);


        expandTabView.setByCustomTab(areas, mViewArray);

//        expandTabView.setTitle(viewLeft.getShowText(), 0);
//        expandTabView.setTitle(viewMiddle.getShowText(), 1);
//        expandTabView.setTitle(viewRight.getShowText(), 2);

    }

    private void initListener() {

        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewLeft, showText);
            }
        });

        viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {

                onRefresh(viewMiddle,showText);

            }
        });

        viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewRight, showText);
            }
        });

    }

    private void onRefresh(View view, String showText) {

        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
        }
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();

    }
    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id)
    {
        Intent i = new Intent(getActivity(), activity_ppl_details.class);
        startActivityForResult(i, 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(isRemoving()){
            if (!expandTabView.onPressBack()) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }
    
    private static class CommonAccListAdapter extends ArrayAdapter<CommonAccount> {
        
        public CommonAccListAdapter(Context context, List<CommonAccount> objects) {
            super(context, R.layout.list_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CommonAccount pAcc = getItem(position);
//            if(pAcc == CommonAccount.getCurrentAccount()) return null;

            String name = pAcc.getPublicName();


            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            TextView gainView = (TextView) convertView.findViewById(R.id.tv_TestGain);
            gainView.setText("+" + String.format("%.2f", Math.random()) + "%");

            
            LetterImageView letterImageView = (LetterImageView) convertView.findViewById(R.id.iv_avatar);
            letterImageView.setOval(true);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_name);
            letterImageView.setLetter(name.charAt(0));
            textView.setText(name);
            
            return convertView;
        }
    }


}
