package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.anjoyo.liuxiaowei.R;
import com.example.leo.stocktest.BasicPost;
import com.example.leo.stocktest.CommonAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 4/1/2015.
 */

public class fragment_status extends Fragment {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<BasicPost> statusItems=new ArrayList<BasicPost>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    StatusAdapter statusAdapter;

    ListView statusList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if(container != null)
//            container.removeAllViews();

        View v = null;

        if(CommonAccount.getCurrentAccount() != null) {



            v = inflater.inflate(R.layout.activity_status, container, false);

            statusList = (ListView) v.findViewById(R.id.listView3);

            for (int i = 0; i < 20; i++) {
                statusItems.add(new BasicPost(123));
            }

            statusAdapter = new StatusAdapter(getActivity(),
                    android.R.layout.simple_list_item_1,
                    statusItems);
            statusList.setAdapter(statusAdapter);
//            }
//            catch (Exception e)
//            {
//                Log.e("status", e.getMessage());
//            }


        }
        else
        {
            v = inflater.inflate(R.layout.fragment_status_notlogin, container, false);

            TextView loginBut = (TextView) v.findViewById(R.id.frag_status_notlogin_but);
            loginBut.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    activity_mframe act = (activity_mframe)getActivity();

                    Intent i = new Intent(act, activity_login.class);
                    act.startActivityForResult(i, act.generateRequestCode(activity_mframe.REQCODE_LOGIN, 0));

                }

            });
        }

//        LinearLayout performanceList = (LinearLayout)v.findViewById(R.id.My_TradePerform);
//        inflater.inflate(R.layout.activity_my_detail, performanceList, false);


        return  v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Log.d("status return comment", data.getStringExtra("content"));
        }
    }

    public class StatusAdapter extends ArrayAdapter<BasicPost>
    {
        replyBoxOnClick impl = new replyBoxOnClick();

        StatusAdapter(Context context, int id, List<BasicPost> posts)
        {
            super(context, id, posts);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent)
        {
            BasicPost post = getItem(position);


            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_status_item, parent, false);
            }

//            TextView tv = (TextView) convertView.findViewById();

//            ImageButton but = (ImageButton) convertView.findViewById(R.id.status_general_item_imgButton);
//            but.setTag(position);
//            but.setOnClickListener(impl);

            return convertView;

        }

        private class replyBoxOnClick implements View.OnClickListener
        {
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), activity_status_reply.class);
                i.putExtra("oIndex", ((Integer)v.getTag()).intValue());

                fragment_status.this.startActivityForResult(i, 1);

            }
        }


    }

}
