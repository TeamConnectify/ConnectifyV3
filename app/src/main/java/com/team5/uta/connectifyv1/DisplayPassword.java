package com.team5.uta.connectifyv1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DisplayPassword extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_password);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5793f")));
        Intent i = getIntent();
        String pwd = i.getStringExtra("pwd1");
        final TextView pass = (TextView) findViewById(R.id.pwd);
        pass.setText(pwd);
        final Button submit1=(Button) findViewById(R.id.loginpage);
        submit1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginActivity = new Intent(DisplayPassword.this, LoginActivity.class);
                startActivity(loginActivity);
            }

        });}
                @Override
                public boolean onCreateOptionsMenu(Menu menu) {
                    // Inflate the menu; this adds items to the action bar if it is present.
                    getMenuInflater().inflate(R.menu.menu_display_password, menu);
                    return true;
                }

                @Override
                public boolean onOptionsItemSelected(MenuItem item) {
                    // Handle action bar item clicks here. The action bar will
                    // automatically handle clicks on the Home/Up button, so long
                    // as you specify a parent activity in AndroidManifest.xml.
                    int id = item.getItemId();

                    //noinspection SimplifiableIfStatement
                    if (id == R.id.action_settings) {
                        return true;
                    }

                    return super.onOptionsItemSelected(item);
                }
            }
