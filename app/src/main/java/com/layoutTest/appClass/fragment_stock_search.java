package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.liuxiaowei.R;
import com.example.leo.stocktest.CommonAccount;
import com.example.leo.stocktest.StockData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class fragment_stock_search extends ListFragment implements StockData.StockDataUpdateListener {
    ImageView StockSearchButton;
    TextView StockSearchNo;
    TextView StockSearchPrice;
    TextView StockSearchReturn;

    EditText StockSearchNum;
    CustomAdapter customAdapter;

//    ArrayList<StockData> targetData = new ArrayList<StockData>();




    private Handler mHandler = new Handler() {

        public void handleMessage (Message msg)
        {
//            Toast.makeText(MainActivity.this, "OnReceive data in UI thread", Toast.LENGTH_SHORT).show();
            if(msg.what == 0)
            {
                handleStockDataOnUpdate((StockData)msg.obj);
            }
        }

    };


    ArrayList<StockData> stockDataList = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_stock_search, container, false);
        StockSearchButton = (ImageView) v.findViewById(R.id.stock_search_button);
        StockSearchNum = (EditText) v.findViewById(R.id.activity_stock_search_search_bar);

        StockSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(getActivity(), activity_stock_details.class);
//                startActivity(intent);

                try
                {
                    int stockNum = Integer.parseInt(StockSearchNum.getText().toString());

                    if(stockNum > 0 && stockNum < 10000)
                    {
                        StockData returnData = StockData.getStockData(stockNum);

                        if(returnData != null)
                        {
//                            int index = targetData.indexOf(returnData);
                            int index = stockDataList.indexOf(returnData);

                            if(index == -1)
                            {
                                addStockDataView(returnData);

                            }
                            else
                            {
                                //To do: put the new query to the front rather than the end
//                                stockDataList.
                                setStockDataView(index, returnData);
                            }
                            
                            customAdapter.notifyDataSetChanged();

                            return;
                        }

                        addStockDataView(StockData.requestStockData(getActivity().getApplicationContext(),
                                        stockNum, fragment_stock_search.this));
                    }
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
                }


            }
        });

//        addStockData("2037", "KF", "+3%");
//        addStockData("5973", "IZ", "+8%");
//        addStockData("4573", "KQ", "+0%");
//        addStockData("5138", "GL", "+0%");
//        addStockData("1950", "JT", "+9%");
//        addStockData("9556", "EI", "+10%");
//        addStockData("5931", "CQ", "+1%");
//        addStockData("5991", "PT", "+1%");
//        addStockData("1478", "WL", "+8%");
//        addStockData("4258", "NE", "+5%");
//        addStockData("5172", "NK", "+7%");
//        addStockData("6106", "SY", "+9%");
//        addStockData("4349", "EB", "+4%");
//        addStockData("5970", "KN", "+8%");
//        addStockData("8002", "JU", "+8%");
//        addStockData("610", "GI", "+6%");
//        addStockData("5446", "QD", "+7%");
//        addStockData("9115", "CM", "+10%");
//        addStockData("8224", "VE", "+6%");
//        addStockData("8125", "TP", "+5%");

        if (savedInstanceState != null) {
            ArrayList<StockData> values = (ArrayList<StockData>)savedInstanceState.getSerializable("dataList");


            if (values != null) {
//                stockDataList = new ArrayList<String>(Arrays.asList(values));
                stockDataList = values;

                customAdapter = new CustomAdapter(getActivity(), stockDataList);
            }
            else
            {
                stockDataList = new ArrayList<StockData>();
            }
        }
        else {
//            stockDataList = new ArrayList<String>();
            stockDataList = new ArrayList<StockData>();
            customAdapter = new CustomAdapter(getActivity(), stockDataList);
        }
        this.setListAdapter(customAdapter);

//        StockData.requestStockData(getActivity().getApplicationContext(), 0001);
//
//        if(targetData == null)
//            targetData = new ArrayList<StockData>();
//        else
//            targetData.clear();

        return v;

    }

    public void addStockDataView(StockData data) {
//        if(stockDataList.size() != 0)

        stockDataList.add(0, data);
//        else
//            stockDataList.add(data);

        //stockNum0, stockName0, stockPC0, stockNum1, stockName1, stockPC1, stockNum2, stockName2, stockPC2, stockNum3, stockName3, stockPC3,
    }

    public void setStockDataView(int index, StockData data)
    {
        stockDataList.remove(index);
        addStockDataView(data);
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        // Note: getValues() is a method in your ArrayAdaptor subclass
//        String[] array = new Foo[list.size()];
//        list.toArray(array);

//        StockData[] values = new StockData[stockDataList.size()];
//        stockDataList.toArray(values);
//        savedState.putSerializable("dataList" ,stockDataList);

    }




    @Override
    public void onListItemClick (ListView l, View v, int position, long id)
    {
        Intent i = new Intent(getActivity(), activity_stock_purchase.class);
        i.putExtra("stockNum", 1); // should be the real stockNum
        startActivityForResult(i, 0);
    }

    @Override
    public void stockDataOnUpdate(StockData data)
    {
        mHandler.obtainMessage(0, data).sendToTarget();
    }

    public void handleStockDataOnUpdate(StockData data)
    {
        // linear search, slow but ok for these kind of application
        int pos = stockDataList.indexOf(data);

        if(pos == -1)
        {
            addStockDataView(data);
        }
        else
        {
            setStockDataView(pos, data);
        }

        customAdapter.notifyDataSetChanged();
    }


//    private static class SampleListAdapter extends ArrayAdapter<String> {
//
//        public SampleListAdapter(Context context, List<String> objects) {
//            super(context, R.layout.list_item, objects);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            String name = getItem(position);
//            if (convertView == null) {
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.stock_item_detail, parent, false);
//            }
//
//            TextView StockNo = (TextView) convertView.findViewById(R.id.people_search_stockno);
//            StockNo.setText(StockNo);
//
//            TextView price = (TextView) convertView.findViewById(R.id.people_search_price);
//            price.setText(name);
//
//            TextView PriceChange = (TextView) convertView.findViewById(R.id.people_search_price_change);
//            PriceChange.setText(name);
//
//
//
//            return convertView;
//        }
//    }

    public class CustomAdapter extends ArrayAdapter<StockData> {

        private final Context context;
        private final List<StockData> items;

        public CustomAdapter(Context context, List<StockData> items) {
            super(context, R.layout.stock_item_detail, items);
            this.context = context;
            this.items = items;

        }

        @Override
        public int getCount()
        {
            return items.size(); //
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {


            StockData targetData = stockDataList.get(position);

            LayoutInflater inflater = LayoutInflater.from(context);
            View rowView = inflater.inflate(R.layout.stock_item_detail, null, true);

            TextView StockNo = (TextView) rowView.findViewById(R.id.people_search_stockno);
            StockNo.setText(targetData.getStockName());

            TextView price = (TextView) rowView.findViewById(R.id.people_search_price);
            price.setText(String.format("%3f", targetData.getStockPrice()));

            TextView PriceChange = (TextView) rowView.findViewById(R.id.people_search_price_change);
            PriceChange.setText("Nan");


            return rowView;
        }
//
    }
}



//package com.layoutTest.appClass;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.anjoyo.liuxiaowei.R;
//import com.example.leo.stocktest.CommonAccount;
//
//public class fragment_stock_search extends Fragment {
//    ImageView StockSearchButton;
//    TextView StockSearchNo;
//    TextView StockSearchPrice;
//    TextView StockSearchReturn;
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//
//        View v = inflater.inflate(R.layout.activity_stock_search, container, false);
//        StockSearchButton = (ImageView) v.findViewById(R.id.stock_search_button);
//        StockSearchButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
////                Intent intent = new Intent(getActivity(), activity_stock_details.class);
////                startActivity(intent);
//
//            }
//        });
//
//
//        return  v;
//
//    }
//}
