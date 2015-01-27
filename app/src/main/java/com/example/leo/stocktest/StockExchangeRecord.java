package com.example.leo.stocktest;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by leo on 29/12/14.
 */
public class StockExchangeRecord implements Serializable
{
    StockData dataRecord; // this record should not be updated(fixed)
    int purchaseAmountOfStock; // lot * (num per lot)
    double totalAmountOfMoney;
    Date bTime;


    private  StockExchangeRecord()
    {

    }

    public StockExchangeRecord(int stockNum, int purchaseAmount, Date buyTime)
    {
        dataRecord = StockData.getStockData(stockNum).createFixedCopy();

        if(dataRecord == null)
        {
            throw new RuntimeException("No stock data!!");
        }

        purchaseAmountOfStock = purchaseAmount;
        totalAmountOfMoney = dataRecord.stockPrice * purchaseAmount;
        bTime = buyTime;
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(dataRecord.toString()).append(';').append(purchaseAmountOfStock).append(',').append(totalAmountOfMoney)
                .append(',').append(StockData.dateFormatter.format(bTime));

        return builder.toString();
    }

    public String getDescription()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(dataRecord.getDescription()).append(", amount=").append(purchaseAmountOfStock).append(',')
                .append(StockData.dateFormatter.format(bTime));

        return builder.toString();
    }

    public static StockExchangeRecord createRecordFromString(String s)
    {
        StockExchangeRecord d = new StockExchangeRecord();
        String[] strings = s.split(";", -2);
        String[] classStrings = strings[1].split(",", -2);

        d.dataRecord = StockData.createDataFromString(strings[0]);
        d.purchaseAmountOfStock = Integer.parseInt(classStrings[0]);
        d.totalAmountOfMoney = Double.parseDouble(classStrings[1]);

        try
        {
            d.bTime = StockData.dateFormatter.parse(classStrings[2]);
        }
        catch (Exception e)
        {
            return null;
        }

        return d;

    }


}
