package com.example.leo.stocktest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by leo on 29/12/14.
 */
public class StockData implements WebRequestTask.WebRequestListener, Serializable
{
    //if require dynamic update, your apps should link the updater

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mma", Locale.ENGLISH);
    static SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mma", Locale.ENGLISH);

    static HashMap<Integer, StockData> requireUpdateStockData = new HashMap<Integer, StockData>();
    static StockUpdateThread stockUpdateThread = null;

    public static void stopUpdateStock()
    {
        if(stockUpdateThread != null)
        {
            stockUpdateThread.setIsRunning(false);
        }
    }

    public static void resumeUpdateStock()
    {
        if(stockUpdateThread != null)
        {
            stockUpdateThread.setIsRunning(true);
        }
    }

    public static void endUpdateStock()
    {
        if(stockUpdateThread != null)
        {
            stockUpdateThread.terminateThread();
            stockUpdateThread = null;
        }
    }

    public static Date getLastUpdateStockDate()
    {
        if(stockUpdateThread != null)
        {
            return stockUpdateThread.getLastUpdateDate();
        }

        return null;  // null if NO thread is running
    }

    Date stockDataTime; // time which represent the data (the nearest update time from YAHOO)
//    Date lastUpdateTime;
    //last update time is replaced by a general time (save in StockUpdateThread class)

    String stockName;
    int stockNumber; // 0001 = 1.....
    String stockNumberString;

    int slotSize; //should be known in real application

    double stockPrice; // last trade stock price
    Context mCon;

    // data structure for saving other figures
    // And this part will be skipped when creating
    HashMap<String, Double> otherFigures;

    public interface StockDataUpdateListener
    {
        public void stockDataOnUpdate(StockData data);
    }

    ArrayList<StockDataUpdateListener> updateListeners = null;

    public StockData(Context context, int sNumber)
    {
        stockNumber = sNumber;
        mCon = context;

        stockNumberString = String.format("%04d", sNumber) + ".HK";

        otherFigures = new HashMap<String, Double>();

        slotSize = -1;
    }

    public StockData createFixedCopy()
    {
        StockData data = new StockData(null, stockNumber);
        data.stockDataTime = stockDataTime;
        data.stockName = stockName;
        data.stockNumberString = stockNumberString;
        data.slotSize = slotSize;
        data.stockPrice = stockPrice;
//        data.lastUpdateTime = lastUpdateTime;

        return data;
    }

//    public void setOnUpdateListener(StockDataUpdateListener lis)
//    {
//        updateListener = lis;
//    }

    public void listenToNextOnUpdate(StockDataUpdateListener lis)
    {
        synchronized (this) {
            if (updateListeners == null)
                updateListeners = new ArrayList<StockDataUpdateListener>();
            updateListeners.add(lis);
        }
    }


    public void webpageRequestFinish(String URL, WebRequestTask.WebRequestState state, String result)
    {
        Log.d("Result", result);
        if(state == WebRequestTask.WebRequestState.SUCCESS)
        {
            //update data
            result = result.replaceAll("\"", "");
            String[] results = result.split(",");

            stockName = results[0];

            try {

                Log.d("two date com sum", results[1] + " " + results[2]);
                Date timeOnly = dateFormatter.parse("2/2/2000 " + results[2]);
                Calendar c = Calendar.getInstance();
                c.setTime(timeOnly);
                c.add(Calendar.HOUR_OF_DAY, 13);
                String newTimeString = timeFormatter.format(c.getTime());
                stockDataTime = dateFormatter.parse(results[1] + " " + newTimeString);

                //stockDataTime = dateFormatter.parse(results[1] + " " + results[2]);
//                Calendar c = Calendar.getInstance();
//                c.setTime(stockDataTime);
//                c.add(Calendar.HOUR_OF_DAY, -11);
//                stockDataTime = c.getTime();

            }
            catch (Exception e)
            {
                //Toast.makeText(mCon, "fail To convert date time!", Toast.LENGTH_SHORT).show();
                Log.d("Stock data updater", "fail to update!!");
                stockDataTime = new Date();
            }


            stockPrice = Double.parseDouble(results[3]);

//            lastUpdateTime = new Date();

            synchronized (this) {

                if (updateListeners != null) {
                    for (StockDataUpdateListener lis : updateListeners) {
                        lis.stockDataOnUpdate(this);
                    }

                    updateListeners.clear();
                }
            }
        }

    }

    public String getStockName()
    {
        return stockName;
    }
    public double getStockPrice() {return stockPrice;}
    public int getStockNumber(){return  stockNumber;}
    public String getStockNumberString(){return stockNumberString;}

    public boolean hasGotData()
    {
        return stockNumber != 0; // == zero only when no data got from the server
    }


    public void requestDataFromServer()
    {
        String requestURL = "http://finance.yahoo.com/d/quotes.csv?s=" + stockNumberString + "&f=nd1t1l1c6";

        WebRequestTask nTask = new WebRequestTask(mCon, this);
        nTask.execute(requestURL);
    }


    //for all stock data which should be updated regular, you should use this function rather than
    //new StockData

    public static StockData requestStockData(Context context, int sNumber)
    {
        //nghj3vy
        //Yahoo finance stock API

        if(stockUpdateThread == null)
        {
            stockUpdateThread = new StockUpdateThread(context);
            stockUpdateThread.start();
        }

        Integer sNum = new Integer(sNumber);

        if(requireUpdateStockData.containsKey(sNum))
        {
            return requireUpdateStockData.get(sNum);
        }
        else
        {
            //data request from server
            StockData newData = new StockData(context, sNumber);
            requireUpdateStockData.put(sNum, newData);
            newData.requestDataFromServer();
            return newData;
        }


    }

    public static StockData requestStockData(Context context, int sNumber, StockDataUpdateListener lis)
    {
        StockData data = requestStockData(context, sNumber);
        data.listenToNextOnUpdate(lis);
        return data;
    }

    public static StockData getStockData(int sNumber)
    {
        //only for created stock data
        Integer sNum = new Integer(sNumber);

        if(requireUpdateStockData.containsKey(sNum))
        {
            return requireUpdateStockData.get(sNum);
        }

        return null;
    }


    @Override
    public String toString()
    {
        return StorageUtils.<String>objectToLetterString(stockNumberString + "," + stockName + "," + dateFormatter.format(stockDataTime) + "," + Double.toString(stockPrice));
    }

    public String getDescription()
    {
        return stockNumberString + "," + stockName  + ", price: $" + Double.toString(stockPrice);
    }

    public static StockData createDataFromString(String s)
    {
        s = StorageUtils.<String>letterStringToObj(s);
        String strings[] = s.split(",", -2);
        StockData n = new StockData(null, Integer.parseInt(strings[0]));

        n.stockName = strings[1];
        try {
            n.stockDataTime = dateFormatter.parse(strings[2]);
        }
        catch (Exception e)
        {
            return null;
        }

        n.stockPrice = Integer.parseInt(strings[3]);

        return n;

    }



}
