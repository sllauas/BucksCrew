package com.layoutTest.appClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.liuxiaowei.R;
import com.example.leo.stocktest.CommonAccount;

/**
 * Created by gary on 4/1/2015.
 */

public class activity_login extends Activity {
    ImageView LoginBackButton;
    TextView LoginOK;
    TextView LoginForgetPW;
    TextView LoginRegistration;

    EditText loginID;
    EditText loginPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CommonAccount.logout();


        LoginBackButton = (ImageView) findViewById(R.id.Login_back);
        LoginOK = (TextView) findViewById(R.id.forget_pw_OK);
        LoginForgetPW= (TextView) findViewById(R.id.Login_forget_pw);
        LoginRegistration= (TextView) findViewById(R.id.Login_registration);

        loginID = (EditText) findViewById(R.id.login_user);
        loginPW = (EditText) findViewById(R.id.login_password);

        LoginBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(activity_login.this, activity_mframe.class);
//                startActivity(intent);

                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();

            }
        });
        LoginOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String id = loginID.getText().toString();
                String pw = loginPW.getText().toString();

                if(CommonAccount.loginIn(activity_login.this,id,pw) == null)
                {
                    Toast.makeText(activity_login.this, "Wrong password, or invalid account id!",
                            Toast.LENGTH_SHORT).show();

                    return;
                }


//                Intent intent = new Intent(activity_login.this, activity_mframe.class);
//                startActivity(intent);

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });
        LoginForgetPW.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity_login.this, activity_forget_pw.class);
                startActivity(intent);

//                finish();

            }
        });
        LoginRegistration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity_login.this, activity_registration.class);
                startActivity(intent);

//                finish();

            }
        });
    }
}
