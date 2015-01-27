package com.layoutTest.appClass;
import com.anjoyo.liuxiaowei.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.*;
import com.example.leo.stocktest.CommonAccount;
import com.example.leo.stocktest.StockData;

import java.util.HashMap;

import info.hoang8f.widget.FButton;

public class activity_stock_purchase extends Activity {

    // Sets the initial values such that the image will be drawn
    private static final int INDIGO_500 = 0xff3f51b5;
    private RangeBar rangebar;
    private FToggleButton buy_button;
    private FToggleButton sell_button;
    private boolean isPressed = false;
    StockData stockData;

    FToggleButton.SingleSelectSet buySellSet;

    RangeBar TradeAmountSeek;

//    private HashMap<View, Boolean> FButtons_HoldPos = new HashMap<View, Boolean>();

    EditText tradeAmount;
    EditText tradePrice;

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_stock_purchase);

        int requestedStockNum = getIntent().getIntExtra("stockNum", -1);

        stockData = StockData.getStockData(requestedStockNum);



        buy_button = (FToggleButton) findViewById(R.id.buy_button);
        buy_button.setButtonColor(0xFFFF8800);

        sell_button = (FToggleButton) findViewById(R.id.sell_button);
        sell_button.setButtonColor(0xFFFF8800);

        buySellSet = new FToggleButton.SingleSelectSet();
        buySellSet.add(buy_button);
        buySellSet.add(sell_button);

        rangebar = (RangeBar) findViewById(R.id.rangebar1);
        rangebar.setBarColor(0xFFFF8800);
        rangebar.setPinColor(0xFFFF8800);
        rangebar.setBarWeight(2);
        rangebar.setConnectingLineColor(0xFFFF8800);
        rangebar.setTickColor(0xFFFFFFFF);
        rangebar.setSelectorColor(0xFFFF8800);
        rangebar.setTickStart(-50);
        rangebar.setTickEnd(50);

        rangebar.setRangePinsByValue(0,0);


//        rangeb

        // Gets the index value TextViews
        final EditText leftIndexValue = (EditText) findViewById(R.id.leftIndexValue);
        final EditText rightIndexValue = (EditText) findViewById(R.id.rightIndexValue);

        leftIndexValue.setText(Double.toString(stockData.getStockPrice()));
        rightIndexValue.setText(Double.toString(stockData.getStockPrice()));
        // Sets the display values of the indices
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                double leftDouble = (Double.parseDouble(leftPinValue)+ 100) * stockData.getStockPrice() * 0.01;
                double rightDouble = (Double.parseDouble(rightPinValue) + 100)* stockData.getStockPrice() * 0.01;



                leftIndexValue.setText("" + leftDouble);
                rightIndexValue.setText("" + rightDouble);
            }

        });


        TextView tv_name = (TextView) findViewById(R.id.stock_name);
        TextView tv_marketPrice = (TextView) findViewById(R.id.stock_market_price);
        final TextView TradeAmount = (TextView) findViewById(R.id.TradeAmount);

        tv_name.setText(stockData.getStockName());
        tv_marketPrice.setText("$" + stockData.getStockPrice());

        TradeAmountSeek = (RangeBar) findViewById(R.id.TradeAmountSeek);


        tradeAmount = (EditText) findViewById(R.id.edit_stock_purchase_quantity);
        tradePrice = (EditText) findViewById(R.id.TradePriceText);
        tradePrice.setText(Double.toString(stockData.getStockPrice()));


        TradeAmountSeek.setRangeBarEnabled(false);
        TradeAmountSeek.setBarColor(0xFFFF8800);
        TradeAmountSeek.setPinColor(0xFFFF8800);
        TradeAmountSeek.setBarWeight(2);
        TradeAmountSeek.setConnectingLineColor(0xFFFF8800);
        TradeAmountSeek.setTickColor(0xFFFFFFFF);
        TradeAmountSeek.setSelectorColor(0xFFFF8800);
        TradeAmountSeek.setRangePinsByValue(0,0);
        TradeAmountSeek.setTickEnd(100);
        TradeAmountSeek.setConnectingLineWeight(0);
//        TradeAmountSeek.setPinRadius(20);


        TradeAmountSeek.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
//                TradeAmount.setText("Trade Amount = " + rightPinIndex);

                tradeAmount.setText("" + rightPinIndex);
            }

        });

        TextView confirmButton = (TextView) findViewById(R.id.tv_stock_purchase_confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(buySellSet.getCurrentSelected() == null)
                {
                    Toast.makeText(activity_stock_purchase.this, "Please select buy / sell!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int amountIndex = TradeAmountSeek.getRightIndex();

                if(buySellSet.getCurrentSelected() == sell_button)
                {
                    amountIndex = -amountIndex;
                }

                if(CommonAccount.getCurrentAccount()
                        .stockBuySell(stockData.getStockNumber(), amountIndex * 100)) {

                    activity_stock_purchase.this.finish();
                }
                else if(amountIndex >= 0)
                {
                    Toast.makeText(activity_stock_purchase.this, "You do not have enough money!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity_stock_purchase.this, "You cannot sell this much!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        TradeAmountSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar tickCountSeek, int progress, boolean fromUser) {
//
//                TradeAmount.setText("Trade Amount = " + progress / 10.0f);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });


//
//        registerFButton_HoldAtt(buy_button);
//
//        buy_button.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                if(isPressed==false){
//
//                    buy_button.setBackgroundResource(R.drawable.your_pressed_image);
//                    isPressed=true;
//
//                }else if(isPressed==true){
//
//                    buy_button.setBackgroundResource(R.drawable.your_default_image);
//                    isPressed=false;
//
//                }
//            }
//        });


    }

//    public void registerFButton_HoldAtt(View v) {
//        FButtons_HoldPos.put(v, false);
//    }
//
//    public boolean toggleFButton_HoldAtt(View v) {
//        boolean x = FButtons_HoldPos.get(v);
//        x = !x;
//        FButtons_HoldPos.put(v, x);
//
//        return x;
//    }
//
//    public void FButton_setDrawable(FButton but, Drawable background) {
//        if (Build.VERSION.SDK_INT >= 16) {
//            but.setBackground(background);
//        } else {
//            but.setBackgroundDrawable(background);
//        }
//    }

}

//package com.layoutTest.appClass;
//import com.anjoyo.liuxiaowei.R;
//import android.app.Activity;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.appyvet.rangebar.*;
//import com.example.leo.stocktest.CommonAccount;
//import com.example.leo.stocktest.StockData;
//
//public class activity_stock_purchase extends Activity {
//
//    // Sets the initial values such that the image will be drawn
//    private static final int INDIGO_500 = 0xff3f51b5;
//    private RangeBar rangebar;
//
//    RangeBar TradeAmountSeek;
//    EditText tradePriceText;
//    EditText tradeAmount;
//
//    StockData stockData;
//    @Override
//    protected void onSaveInstanceState(Bundle bundle) {
//        super.onSaveInstanceState(bundle);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_stock_purchase);
//
//        int requestedStockNum = getIntent().getIntExtra("stockNum", -1);
//
//        stockData = StockData.requestStockData(requestedStockNum);
//
//        rangebar = (RangeBar) findViewById(R.id.rangebar1);
//
//        rangebar.setBarColor(0xFFFF8800);
//        rangebar.setPinColor(0xFFFF8800);
//        rangebar.setBarWeight(2);
//        rangebar.setConnectingLineColor(0xFFFF8800);
//        rangebar.setTickColor(0xFFFFFFFF);
//        rangebar.setSelectorColor(0xFFFF8800);
//
//        rangebar.setRangePinsByValue(0,0);
//
//        TextView tv_name = (TextView) findViewById(R.id.stock_name);
//        TextView tv_marketPrice = (TextView) findViewById(R.id.stock_market_price);
//        tradeAmount = (EditText) findViewById(R.id.edt_stock_purchase_tradeamount);
//
//        tv_name.setText(stockData.getStockName());
//        tv_marketPrice.setText("market price: " + stockData.getStockPrice());
//
//        tradePriceText = (EditText) findViewById(R.id.TradePriceText);
//        tradePriceText.setText(Double.toString(stockData.getStockPrice()));
//
//
////        rangeb
//
//        // Gets the index value TextViews
//        final EditText leftIndexValue = (EditText) findViewById(R.id.leftIndexValue);
//        final EditText rightIndexValue = (EditText) findViewById(R.id.rightIndexValue);
//
//        leftIndexValue.setText(Double.toString(stockData.getStockPrice()));
//        rightIndexValue.setText(Double.toString(stockData.getStockPrice()));
//
//        // Sets the display values of the indices
//        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
//            @Override
//            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
//                                              int rightPinIndex,
//                                              String leftPinValue, String rightPinValue) {
//                double leftDouble = (Double.parseDouble(leftPinValue)+ 100) * stockData.getStockPrice() * 0.01;
//                double rightDouble = (Double.parseDouble(rightPinValue) + 100)* stockData.getStockPrice() * 0.01;
//
//
//
//                leftIndexValue.setText("" + leftDouble);
//                rightIndexValue.setText("" + rightDouble);
//            }
//
//        });
//
//
//        final TextView TradeAmount = (TextView) findViewById(R.id.TradeAmount);
//        TradeAmountSeek = (RangeBar) findViewById(R.id.rangebar_tradeamount);
//
//        TradeAmountSeek.setBarColor(0xFFFF8800);
//        TradeAmountSeek.setPinColor(0xFFFF8800);
//        TradeAmountSeek.setBarWeight(2);
//        TradeAmountSeek.setConnectingLineColor(0xFFFF8800);
//        TradeAmountSeek.setTickColor(0xFFFFFFFF);
//        TradeAmountSeek.setSelectorColor(0xFFFF8800);
//        TradeAmountSeek.setRangePinsByValue(0,0);
//
//
//        TradeAmountSeek.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
//            @Override
//            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
////                TradeAmount.setText("Trade Amount = " + rightPinIndex);
//                tradeAmount.setText(Integer.toString(rightPinIndex));
//            }
//        });
//
//
//        TextView confirmButton = (TextView) findViewById(R.id.tv_stock_purchase_confirm);
//
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//
//                if(CommonAccount.getCurrentAccount()
//                        .stockBuySell(stockData.getStockNumber(), TradeAmountSeek.getRightIndex() * 100)) {
//
//                    activity_stock_purchase.this.finish();
//                }
//                else
//                {
//                    Toast.makeText(activity_stock_purchase.this, "You do not have enough money!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//
//}
