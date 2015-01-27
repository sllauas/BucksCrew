package com.example.leo.stocktest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by leo on 29/12/14.
 */
public class StockUpdateThread extends Thread implements WebRequestTask.WebRequestListener
{
    WebRequestTask callObject;
    String generatedURL;
    ArrayList<Integer> currRequestingStock;
    Context mCon;

    volatile boolean isRunning; // can be stopped in background
    volatile boolean needTerminated = false;
    private Date lastUpdateDate; // read only variable


    public StockUpdateThread(Context context)
    {
        mCon = context;
        callObject = new WebRequestTask(mCon,this);
        generatedURL = "";
        currRequestingStock = new ArrayList<Integer>();
        isRunning = true;

        lastUpdateDate = new Date();

    }

    public void webpageRequestFinish(String URL, WebRequestTask.WebRequestState state, String result)
    {
        if(state == WebRequestTask.WebRequestState.SUCCESS)
        {
            String[] resultPerStock = result.split("\n");

            if(resultPerStock.length != currRequestingStock.size())
            {
                //Toast.makeText(mCon, "Sure something wrong in the web request", Toast.LENGTH_SHORT).show();
                return;
            }

            for(int i = 0 ;i < currRequestingStock.size(); i++)
            {
                StockData.requireUpdateStockData.get(currRequestingStock.get(i)).webpageRequestFinish(null, state, resultPerStock[i]);
            }

            lastUpdateDate = new Date();
        }
    }

    public void setIsRunning(boolean value)
    {
        isRunning = value;
    }

    public void terminateThread(){needTerminated = true;}

    @Override
    public void run()
    {
        while(!needTerminated) {

            if(isRunning) {

                if (generatedURL != "")
                    callObject.singleThreadExecute("http://finance.yahoo.com/d/quotes.csv?s=" + generatedURL + "&f=nd1t1l1c6");

                StringBuilder buildURL = new StringBuilder();

//                generatedURL = "";
                currRequestingStock.clear();

                for (StockData i : StockData.requireUpdateStockData.values()) {
                    currRequestingStock.add(i.stockNumber);

                    if(buildURL.length() != 0)
                        buildURL.append(',');

                    buildURL.append(i.stockNumberString);

//                    if (generatedURL != "")
//                        generatedURL += ",";

//                    generatedURL += i.stockNumberString;


                }

                generatedURL = buildURL.toString();

                //Toast.makeText(mCon, "auto updating.....", Toast.LENGTH_SHORT).show();
                Log.d("auto updater", "auto updating......");
            }

            try {
                   Thread.sleep(50000);
            } catch (Exception e) {
            }

        }
        //should be run forever
    }

    public Date getLastUpdateDate()
    {
        return lastUpdateDate;
    }
}