package com.team5.uta.connectifyv1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;

public class SecurityQuestions extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    /*private Spinner spinner01;
    private Spinner spinner02;
    private Spinner spinner03;*/
    private static final String[]paths1 = {"First Crush", "Pet name", "Favourite Cuisine"};
    private static final String[]paths2 = {"Favourite sports", "Mothers Maiden Name", "High School"};
    private static final String[]paths3 = {"First Car Model", "Favourite City", "First Company"};

    public Object output = null;
    private HttpPost httppost;
    private HttpResponse response;
    private InputStream is;
    private HttpWrapper httpWrapper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_questions);

        final Spinner spinner01= (Spinner) findViewById(R.id.sec_ques_spinner1);
        final EditText answer1= (EditText) findViewById(R.id.editText10);
        final Spinner spinner02= (Spinner) findViewById(R.id.sec_ques_spinner2);
        final EditText answer2= (EditText) findViewById(R.id.editText11);
        final Spinner spinner03= (Spinner) findViewById(R.id.sec_ques_spinner3);
        final EditText answer3= (EditText) findViewById(R.id.editText12);
        final Button submit_button = (Button) findViewById(R.id.button3);
        final int duration= Toast.LENGTH_SHORT;
        httpWrapper = new HttpWrapper();


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5793f")));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SecurityQuestions.this,
                android.R.layout.simple_spinner_item,paths1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SecurityQuestions.this,
                android.R.layout.simple_spinner_item,paths2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(SecurityQuestions.this,
                android.R.layout.simple_spinner_item,paths3);


        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner01.setAdapter(adapter1);
        spinner01.setOnItemSelectedListener(this);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner02.setAdapter(adapter2);
        spinner02.setOnItemSelectedListener(this);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner03.setAdapter(adapter3);
        spinner03.setOnItemSelectedListener(this);

        Button next = (Button)findViewById(R.id.button3);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent interestActivity = new Intent(SecurityQuestions.this, AddInterestActivity.class);
                User user = (User)getIntent().getSerializableExtra("user");

                if((answer1.getText().toString().trim().equals(""))||(answer2.getText().toString().trim().equals(""))||
                        (answer3.getText().toString().trim().equals("")))
                {
                    Toast.makeText(getApplicationContext(), "All Fields are Required.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String q1 = spinner01.getSelectedItem().toString();
                    String q2 = spinner02.getSelectedItem().toString();
                    String q3 = spinner03.getSelectedItem().toString();

                    String a1 = answer1.getText().toString();
                    String a2 = answer2.getText().toString();
                    String a3 = answer3.getText().toString();

                    // declare parameters that are passed to PHP script
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                    // define the parameter
                    postParameters.add(new BasicNameValuePair("email",user.getEmail()));
                    postParameters.add(new BasicNameValuePair("q1",q1));
                    postParameters.add(new BasicNameValuePair("a1",a1));
                    postParameters.add(new BasicNameValuePair("q2",q2));
                    postParameters.add(new BasicNameValuePair("a2",a2));
                    postParameters.add(new BasicNameValuePair("q3",q3));
                    postParameters.add(new BasicNameValuePair("a3",a3));

                    httpWrapper.setPostParameters(postParameters);

                    //http post
                    try{
                        httppost = new HttpPost("http://omega.uta.edu/~ssk0590/setSecurityQuestions.php");
                        httpWrapper.setSecurityQuestionsActivity(SecurityQuestions.this);
                        httpWrapper.execute(httppost);
                    }
                    catch(Exception e){
                        Log.e("register_activity", "Error in http connection " + e.toString());
                    }

                }

                startActivity(interestActivity);
            }
        });

        submit_button.setFocusable(true);
    }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever y ou want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_security_questions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
