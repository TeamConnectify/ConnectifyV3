package com.team5.uta.connectifyv1;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

public class ForgotPassword extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5793f")));

        final EditText answer_1 = (EditText) findViewById(R.id.a1);
        final EditText answer_2= (EditText) findViewById(R.id.a2);
        final EditText answer_3 = (EditText) findViewById(R.id.a3);
        final TextView question_1 = (TextView) findViewById(R.id.q1);
        final TextView question_2 = (TextView) findViewById(R.id.q2);
        final TextView question_3 = (TextView) findViewById(R.id.q3);
        final Intent i = getIntent();
        final String []q1 = i.getStringArrayExtra("qa");
        final String pwd = i.getStringExtra("pwd");
        question_1.setText(q1[0]);
        question_2.setText(q1[1]);
        question_3.setText(q1[2]);


        final String []a1 = i.getStringArrayExtra("aa");
            Log.i("as",a1[0]);
            Log.i("as",a1[1]);
            Log.i("as",a1[2]);



            final Button submit1=(Button) findViewById(R.id.submit_button_1);
            submit1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    String ans1 = answer_1.getText().toString();
                    String ans2 = answer_2.getText().toString();
                    String ans3 = answer_3.getText().toString();
                    Log.i("answer1",ans1);
                    Log.i("answer2",ans2);
                    Log.i("answer3",ans3);
                    if(a1[0].equals(ans1) && a1[1].equals(ans2) && a1[2].equals(ans3))
                    {
                        Intent forgotpasswordActivity = new Intent(ForgotPassword.this,DisplayPassword.class);
                        forgotpasswordActivity.putExtra("pwd1",pwd);
                        startActivity(forgotpasswordActivity);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Wrong details entered!You will be reported", Toast.LENGTH_SHORT).show();
                    }
        }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot__password, menu);
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
