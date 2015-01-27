package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.anjoyo.liuxiaowei.R;
import com.example.leo.stocktest.CommonAccount;
import com.example.leo.stocktest.StockData;

import java.util.Arrays;

/**
 * Created by gary on 3/1/2015.
 */
public class activity_ppl_details extends Activity {
    LinearLayout wallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppl_details);

        wallButton = (LinearLayout) findViewById(R.id.ppl_details_bottomlayout);

        StockData.requestStockData(this, 1);

//        if(CommonAccount.getCurrentAccount() != null)
//            Log.d("Copy from record list", Arrays.toString(CommonAccount.getCurrentAccount().getCopyFromNewestRecord().toArray()));


        wallButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //TEST ONLY!!!!!!!!!!!!!!!

//                Log.d("WTF Buy stock at ppl detail", "stealing money......");
                Log.d("WTF Buy stock at ppl detail", "0001 data:" + StockData.getStockData(1).getDescription());
                Log.d("WTF Buy stock at ppl detail", "Current M: " + CommonAccount.getCurrentAccount().getCurrentCash());

                CommonAccount.getCurrentAccount().stockBuySell(1, 100);
                Log.d("WTF Buy stock at ppl detail", "After M: " + CommonAccount.getCurrentAccount().getCurrentCash());

                Log.d("WTF Buy stock at ppl detail", "Amount of 0001:" + CommonAccount.getCurrentAccount().getStockInventory(1));


                try {
                    CommonAccount.updateCurrentAccount(activity_ppl_details.this);
                }
                catch (Exception e)
                {
                    Log.e("Cannot stock buy", e.getMessage());
                }

//                Intent intent = new Intent(activity_ppl_details.this, activity_ppl_status.class);
//                startActivity(intent);

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);

                finish();

            }
        });

    }
}




