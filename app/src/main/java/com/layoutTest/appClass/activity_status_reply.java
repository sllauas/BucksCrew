package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anjoyo.liuxiaowei.R;

/**
 * Created by leo on 10/1/15.
 */
public class activity_status_reply extends Activity
{
    EditText replyText;
    TextView replyOk;

    int oid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_writecomment);

        oid = getIntent().getIntExtra("oIndex", -1);

        replyText = (EditText) findViewById(R.id.activity_status_replybox);
        replyOk = (TextView) findViewById(R.id.activity_status_replyok);

        replyOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("oIndex", oid);
                i.putExtra("content", replyText.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed ()
    {
        Intent i = new Intent();
        i.putExtra("oIndex", oid);
        setResult(RESULT_CANCELED, i);
        finish();
    }


}
