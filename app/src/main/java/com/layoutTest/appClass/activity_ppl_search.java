package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anjoyo.liuxiaowei.R;

/**
 * Created by gary on 3/1/2015.
 */
public class activity_ppl_search extends Activity {
    ImageView PplSearchButton;
    TextView PplSearchStrategy;
    TextView PplRiskStrategy;
    TextView PplReturnStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppl_search);

        PplSearchButton = (ImageView) findViewById(R.id.ppl_search_button);


        PplSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity_ppl_search.this, activity_ppl_details.class);
                startActivity(intent);

            }
        });

    }
}




