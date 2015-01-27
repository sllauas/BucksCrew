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

public class activity_registration extends Activity {
    ImageView RegistrationBackButton;
    TextView RegistrationOK;

    EditText new_ID;
    EditText new_Name;
    EditText new_pw;
    EditText new_pw_re;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        RegistrationBackButton = (ImageView) findViewById(R.id.registration_back);
        RegistrationOK = (TextView) findViewById(R.id.registration_OK);

        new_ID = (EditText) findViewById(R.id.registration_id);
        new_Name = (EditText) findViewById(R.id.registration_name);
        new_pw = (EditText) findViewById(R.id.registration_pw);
        new_pw_re = (EditText) findViewById(R.id.registration_pw2);

        RegistrationBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity_registration.this, activity_mframe.class);
                startActivity(intent);

            }
        });
        RegistrationOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String id = new_ID.getText().toString();
                String name = new_Name.getText().toString();
                String pw1 = new_pw.getText().toString();
                String pw2 = new_pw_re.getText().toString();



                if(id == "" || name == "" || pw1 == "" || pw2 == "")
                {
                    Toast.makeText(activity_registration.this, "Please fill in all the" +
                            "forms!!", Toast.LENGTH_SHORT).show();;
                    return;
                }

                if(!pw1.equals(pw2))
                {
                    Toast.makeText(activity_registration.this, "Two passwords" +
                            "are not equal!!", Toast.LENGTH_SHORT).show();;
                    return;
                }

                if(CommonAccount.createNewAccount(activity_registration.this, pw1, id, name, "") == null)
                {
                    Toast.makeText(activity_registration.this, "Exist ID!", Toast.LENGTH_SHORT).show();
                    return;
                }



                Intent intent = new Intent(activity_registration.this, activity_mframe.class);
                startActivity(intent);

            }
        });

    }
}
