package com.layoutTest.appClass;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.*;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anjoyo.liuxiaowei.R;
import com.example.leo.stocktest.CommonAccount;
import com.example.leo.stocktest.StockData;
import com.example.leo.stocktest.StockExchangeRecord;

import com.afollestad.materialdialogs.*;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;
import java.util.Arrays;


public class activity_mframe extends FragmentActivity {

    private LinearLayout MyBottemStatusBtn, MyBottemStockBtn,
            MyBottemPeopleBtn, MyBottemMyBtn, MyBottemMoreBtn;
    private ImageView MyBottemStatusImg, MyBottemStockImg,
            MyBottemPeopleImg, MyBottemMyImg, MyBottemMoreImg;
    private TextView MyBottemStatusTxt, MyBottemStockTxt, MyBottemPeopleTxt,
            MyBottemMyTxt, MyBottemMoreTxt;
    private android.support.v4.view.ViewPager pager;

    private ResideMenu resideMenu;
    private activity_mframe mContext;
    private ResideMenuItem itemChangePW;
    private ResideMenuItem itemFeedSubscription;
    private ResideMenuItem itemHomepage;
    private ResideMenuItem itemContactCustomerService;

    MFrame_pagerAdapter curAdapter;

    CharSequence[] dialogCStrings;


    public static final int REQCODE_LOGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        CommonAccount.getAllAccounts(this);

        popUpCopyFromRecList();

        setUpMenu();
        initView();

//        ViewPager pager = (ViewPager) findViewById(R.id.MyFramePager);
//
//        pager.setAdapter(new MFrame_pagerAdapter(getSupportFragmentManager()
//                , new Fragment[]{new fragment_status(), new fragment_stock_search(),
//        new fragment_ppl_search(), new Fragment_my()}));
    }


    void popUpCopyFromRecList()
    {
        CommonAccount curAcc = CommonAccount.getCurrentAccount();

        if(curAcc != null)
        {
            ArrayList<StockExchangeRecord> copyFromList = curAcc.getCopyFromNewestRecord();
            if(copyFromList != null)
            {
                Log.d("Copy from record list", Arrays.toString(copyFromList.toArray()));
                //show dialog
                ArrayList<CharSequence> listDisplay = new ArrayList<CharSequence>();

                for(StockExchangeRecord rec : copyFromList)
                {
                    listDisplay.add(rec.getDescription());
                }

                dialogCStrings = listDisplay.toArray(new CharSequence[listDisplay.size()]);


                new MaterialDialog.Builder(this)
                        .title("Here is all the exchange record from your subscribers:")
                        .items(dialogCStrings)
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text)
                            {


                            }
                        })
                        .positiveText("select")
                        .show();
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        int oRequestCode = getOriginalCodeFromReqCode(requestCode);
        int fragIndex = getFragIndexFromReqCode(requestCode);

        Log.d("mFrame_onActResult", "Hi, length=" + curAdapter.allFragments.length + ", mixCode=" + requestCode + "," + oRequestCode + "," + fragIndex);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (oRequestCode == REQCODE_LOGIN) {

            popUpCopyFromRecList();

            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            Fragment newFragment;
//            try {
//                newFragment = curAdapter.getItem(fragIndex).getClass().newInstance();
//            }
//            catch (Exception e)
//            {
//                Log.e("Unsupported new Instance", "T_T");
//                return;
//            }
//
//            int pViewGroupId = ((ViewGroup) curAdapter.getItem(fragIndex).getView().getParent()).getId();
//
//
//            fragmentTransaction.replace(pViewGroupId, newFragment);



            try {
                fragmentTransaction.detach(curAdapter.getItem(fragIndex))
                        .attach(curAdapter.getItem(fragIndex)).commitAllowingStateLoss();
            }
            catch (Exception e)
            {
                Log.e("Fragment refresh", e.getMessage());
            }


            Log.d("Refresh", "^_^");


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_mframe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        StockData.resumeUpdateStock();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        StockData.stopUpdateStock();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        StockData.endUpdateStock();
    }


    private void initView() {

        MyBottemStatusBtn = (LinearLayout) findViewById(R.id.MyBottemStatusBtn);
        MyBottemStockBtn = (LinearLayout) findViewById(R.id.MyBottemStockBtn);
        MyBottemPeopleBtn = (LinearLayout) findViewById(R.id.MyBottemPeopleBtn);
        MyBottemMyBtn = (LinearLayout) findViewById(R.id.MyBottemMyBtn);
        MyBottemMoreBtn = (LinearLayout) findViewById(R.id.MyBottemMoreBtn);

        MyBottemStatusImg = (ImageView) findViewById(R.id.MyBottemStatusImg);
        MyBottemStockImg = (ImageView) findViewById(R.id.MyBottemStockImg);
        MyBottemPeopleImg = (ImageView) findViewById(R.id.MyBottemPeopleImg);
        MyBottemMyImg = (ImageView) findViewById(R.id.MyBottemMyImg);
        MyBottemMoreImg = (ImageView) findViewById(R.id.MyBottemMoreImg);

        MyBottemStatusTxt = (TextView) findViewById(R.id.MyBottemStatusTxt);
        MyBottemStockTxt = (TextView) findViewById(R.id.MyBottemStockTxt);
        MyBottemPeopleTxt = (TextView) findViewById(R.id.MyBottemPeopleTxt);
        MyBottemMyTxt = (TextView) findViewById(R.id.MyBottemMyTxt);
        MyBottemMoreTxt = (TextView) findViewById(R.id.MyBottemMoreTxt);

        MyBtnOnclick mytouchlistener = new MyBtnOnclick();
        MyBottemStatusBtn.setOnClickListener(mytouchlistener);
        MyBottemStockBtn.setOnClickListener(mytouchlistener);
        MyBottemPeopleBtn.setOnClickListener(mytouchlistener);
        MyBottemMyBtn.setOnClickListener(mytouchlistener);
        MyBottemMoreBtn.setOnClickListener(mytouchlistener);
        pager = (ViewPager) findViewById(R.id.MyFramePager);

        //,new fragment_stock_search()

        curAdapter = new MFrame_pagerAdapter(getSupportFragmentManager()
                , new Fragment[]{new fragment_status(),new fragment_stock_search(),
                new fragment_ppl_search(), new Fragment_my()});
        pager.setAdapter(curAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

                initBottemBtn();
//                int flag = (Integer) list.get((arg0)).getTag();

                int flag = arg0;
                if (flag == 0) {
                    MyBottemStatusImg
                            .setImageResource(R.drawable.main_index_search_pressed);
                    MyBottemStatusTxt.setTextColor(Color.parseColor("#FF8C00"));
                } else if (flag == 1) {
                    MyBottemStockImg
                            .setImageResource(R.drawable.main_index_tuan_pressed);
                    MyBottemStockTxt.setTextColor(Color.parseColor("#FF8C00"));
                } else if (flag == 2) {
                    MyBottemPeopleImg
                            .setImageResource(R.drawable.main_index_checkin_pressed);
                    MyBottemPeopleTxt.setTextColor(Color
                            .parseColor("#FF8C00"));
                } else if (flag == 3) {
                    MyBottemMyImg
                            .setImageResource(R.drawable.main_index_my_pressed);
                    MyBottemMyTxt.setTextColor(Color.parseColor("#FF8C00"));
                } else if (flag == 4) {
                    MyBottemMoreImg
                            .setImageResource(R.drawable.main_index_more_pressed);
                    MyBottemMoreTxt.setTextColor(Color.parseColor("#FF8C00"));
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }


    private class MyBtnOnclick implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            int mBtnid = arg0.getId();

            if (mBtnid == R.id.MyBottemStatusBtn) {//
                pager.setCurrentItem(0);
                initBottemBtn();
                MyBottemStatusImg
                        .setImageResource(R.drawable.main_index_search_pressed);
                MyBottemStatusTxt.setTextColor(Color.parseColor("#FF8C00"));

            } else if (mBtnid == R.id.MyBottemStockBtn) {
                pager.setCurrentItem(1);
                initBottemBtn();
                MyBottemStockImg
                        .setImageResource(R.drawable.main_index_tuan_pressed);
                MyBottemStockTxt.setTextColor(Color.parseColor("#FF8C00"));

            } else if (mBtnid == R.id.MyBottemPeopleBtn) {
                pager.setCurrentItem(2);
                initBottemBtn();
                MyBottemPeopleImg
                        .setImageResource(R.drawable.main_index_checkin_pressed);
                MyBottemPeopleTxt.setTextColor(Color.parseColor("#FF8C00"));

            } else if (mBtnid == R.id.MyBottemMyBtn) {
                pager.setCurrentItem(3);
                initBottemBtn();
                MyBottemMyImg
                        .setImageResource(R.drawable.main_index_my_pressed);
                MyBottemMyTxt.setTextColor(Color.parseColor("#FF8C00"));

            } else if (mBtnid == R.id.MyBottemMoreBtn) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                initBottemBtn();
                MyBottemMoreImg
                        .setImageResource(R.drawable.main_index_more_pressed);
                MyBottemMoreTxt.setTextColor(Color.parseColor("#FF8C00"));

            } else if (mBtnid == itemChangePW.getId()) {
                resideMenu.clearIgnoredViewList();
                resideMenu.closeMenu();

            } else if (mBtnid == itemFeedSubscription.getId()) {
                resideMenu.clearIgnoredViewList();
                resideMenu.closeMenu();

            } else if (mBtnid == itemHomepage.getId()) {
                resideMenu.clearIgnoredViewList();
                resideMenu.closeMenu();

            } else if (mBtnid == itemContactCustomerService.getId()) {
                resideMenu.clearIgnoredViewList();
                resideMenu.closeMenu();

            }


        }

    }

    private void initBottemBtn() {
        MyBottemStatusImg.setImageResource(R.drawable.search_bottem_search);
        MyBottemStockImg.setImageResource(R.drawable.search_bottem_tuan);
        MyBottemPeopleImg.setImageResource(R.drawable.search_bottem_checkin);
        MyBottemMyImg.setImageResource(R.drawable.search_bottem_my);
        MyBottemMoreImg.setImageResource(R.drawable.search_bottem_more);
        MyBottemStatusTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        MyBottemStockTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        MyBottemPeopleTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        MyBottemMyTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        MyBottemMoreTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemChangePW     = new ResideMenuItem(this, R.drawable.icon_home,     "Change password");
        itemFeedSubscription  = new ResideMenuItem(this, R.drawable.icon_profile,  "Feed subscription");
        itemHomepage = new ResideMenuItem(this, R.drawable.icon_calendar, "Homepage settings");
        itemContactCustomerService = new ResideMenuItem(this, R.drawable.icon_settings, "Contact customer service");

        resideMenu.addMenuItem(itemChangePW, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemFeedSubscription, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemHomepage, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemContactCustomerService, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);


        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_mframe.this);
                builder.setTitle("Notice");
                builder.setMessage("Do you want to quit??");
                builder.setIcon(R.drawable.ic_launcher);

                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if (arg1 == DialogInterface.BUTTON_POSITIVE) {
                            arg0.cancel();
                        } else if (arg1 == DialogInterface.BUTTON_NEGATIVE) {
                            activity_mframe.this.finish();
                        }
                    }
                };
                builder.setPositiveButton("Cancel", dialog);
                builder.setNegativeButton("Yes", dialog);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }
        return false;
    }

    public int generateRequestCode(int originalCode, int fragIndex)
    {
        int code = originalCode * curAdapter.allFragments.length + fragIndex;
//        Log.d("generateReqCode", "code= " + code);

        return code;
    }

    public int generateRequestCode(int originalCode, Fragment frag)
    {
        //linear search, ok for short length
        int i;
        for(i = 0; i < curAdapter.allFragments.length; i++)
        {
            if(curAdapter.allFragments[i] == frag)
                return i;
        }

        return  -1;
    }

    public int getFragIndexFromReqCode(int code)
    {
        return code % curAdapter.allFragments.length;
    }

    public int getOriginalCodeFromReqCode(int code)
    {
        return code / curAdapter.allFragments.length;
    }



    public class MFrame_pagerAdapter extends FragmentPagerAdapter
    {
        Fragment[] allFragments;
        String[] allTitles;

        public MFrame_pagerAdapter(FragmentManager fm, Fragment[] fragments)
        {
            super(fm);

            allFragments = fragments;
        }

        public MFrame_pagerAdapter(FragmentManager fm, Fragment[] fragments
                , String[] titles)
        {
            super(fm);

            allTitles = titles;
            allFragments = fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if(allTitles == null || position >= allTitles.length )
                return "";

            return allTitles[position];
        }


        @Override
        public Fragment getItem(int position) {

            return allFragments[position];
        }


        public int getCount() {
            return allFragments.length;
        }




    }
}
