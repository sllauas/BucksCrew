package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anjoyo.liuxiaowei.R;

/**
 * Created by gary on 4/1/2015.
 */

public class activity_forget_pw extends Activity {
    TextView ForgetpwOKButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pw);

        ForgetpwOKButton = (TextView) findViewById(R.id.forget_pw_OK);

        ForgetpwOKButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity_forget_pw.this, activity_mframe.class);
                startActivity(intent);

            }
        });


    }
}
