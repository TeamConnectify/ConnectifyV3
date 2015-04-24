package com.team5.uta.connectifyv1.ChangePassword;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import com.team5.uta.connectifyv1.HttpWrapper;
import com.team5.uta.connectifyv1.R;
import com.team5.uta.connectifyv1.User;
import com.team5.uta.connectifyv1.UserProfile;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class ChangeSecurityQuestions extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private HttpPost httpPost;
    private HttpWrapper httpWrapper;
    User user = null;
    private static final String[]paths1 = {"First Crush", "Pet name", "Favourite Cuisine"};
    private static final String[]paths2 = {"Favourite sports", "Mothers Maiden Name", "High School"};
    private static final String[]paths3 = {"First Car Model", "Favourite City", "First Company"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_security_questions);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5793f")));

        final Spinner spinner01 = (Spinner) findViewById(R.id.sec_ques_spinner1);
        final Spinner spinner02 = (Spinner) findViewById(R.id.sec_ques_spinner2);
        final Spinner spinner03 = (Spinner) findViewById(R.id.sec_ques_spinner3);

        final EditText answer_1 = (EditText) findViewById(R.id.editText10);
        final EditText answer_2 = (EditText) findViewById(R.id.editText11);
        final EditText answer_3 = (EditText) findViewById(R.id.editText12);

        final Intent i = getIntent();
        final String e = i.getStringExtra("emailid");
        this.user = (User) getIntent().getSerializableExtra("user");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ChangeSecurityQuestions.this,
                android.R.layout.simple_spinner_item, paths1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ChangeSecurityQuestions.this,
                android.R.layout.simple_spinner_item, paths2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(ChangeSecurityQuestions.this,
                android.R.layout.simple_spinner_item, paths3);


        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner01.setAdapter(adapter1);
        spinner01.setOnItemSelectedListener(this);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner02.setAdapter(adapter2);
        spinner02.setOnItemSelectedListener(this);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner03.setAdapter(adapter3);
        spinner03.setOnItemSelectedListener(this);

        final Button skip = (Button) findViewById(R.id.skipbutton);
        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent skipsecurity = new Intent(ChangeSecurityQuestions.this, UserProfile.class);
                skipsecurity.putExtra("user",user);
                startActivity(skipsecurity);
            }
        });

        final Button submit1 = (Button) findViewById(R.id.button3);
        submit1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ans1 = answer_1.getText().toString();
                String ans2 = answer_2.getText().toString();
                String ans3 = answer_3.getText().toString();

                Intent changesecurity = new Intent(ChangeSecurityQuestions.this, UserProfile.class);
                changesecurity.putExtra("user", user);
                if ((ans1.trim().equals("")) || (ans2.trim().equals("")) ||
                        (ans3.trim().equals(""))) {
                    Toast.makeText(getApplicationContext(), "All Fields are Required.", Toast.LENGTH_SHORT).show();
                } else {
                    String q1 = spinner01.getSelectedItem().toString();
                    String q2 = spinner02.getSelectedItem().toString();
                    String q3 = spinner03.getSelectedItem().toString();

                    Log.i("assad", q1);
                    Log.i("assad", q2);
                    Log.i("assad", q3);
                    Log.i("assad", ans1);
                    Log.i("assad", ans2);
                    Log.i("assad", ans3);

                    // declare parameters that are passed to PHP script
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                    // define the parameter
                    postParameters.add(new BasicNameValuePair("email", e));
                    postParameters.add(new BasicNameValuePair("q1", q1));
                    postParameters.add(new BasicNameValuePair("a1", ans1));
                    postParameters.add(new BasicNameValuePair("q2", q2));
                    postParameters.add(new BasicNameValuePair("a2", ans2));
                    postParameters.add(new BasicNameValuePair("q3", q3));
                    postParameters.add(new BasicNameValuePair("a3", ans3));
                    httpWrapper = new HttpWrapper();
                    httpWrapper.setPostParameters(postParameters);
                    Log.i("assad", q1);
                    Log.i("assad", q2);
                    Log.i("assad", q3);
                    //http post
                    try {
                        httpPost = new HttpPost("http://omega.uta.edu/~mxs1773/updatesecutiy.php");
                        httpWrapper.setChangeSecurityQuestionsActivity(ChangeSecurityQuestions.this);
                        httpWrapper.execute(httpPost);
                    } catch (Exception e) {
                        Log.e("register_activity", "Error in http connection " + e.toString());
                    }

                }
                startActivity(changesecurity);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_security_questions, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
