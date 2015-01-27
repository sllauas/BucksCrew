package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.anjoyo.liuxiaowei.R;

/**
 * Created by gary on 3/1/2015.
 */
public class activity_ppl_status extends Activity {
    LinearLayout WriteSthButton; // change button name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppl_details); // change to the shown xml file name

        WriteSthButton = (LinearLayout) findViewById(R.id.write_sth_botton); // change button id


        WriteSthButton.setOnClickListener(new View.OnClickListener() { // change button name

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity_ppl_status.this, activity_ppl_status_comment.class); // (this file name).this , (linked file name).class
                startActivity(intent);

            }
        });

    }
}




