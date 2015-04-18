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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserIdForgotPwd extends ActionBarActivity {

    public Object output = null;
    private HttpPost httpPost;
    private HttpWrapper httpWrapper;
    private String[] questions = new String[3];
    private String[] answers = new String[3];
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id_forgot_pwd);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5793f")));
        final EditText email_id = (EditText) findViewById(R.id.email_id);
        final Button submit=(Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                if (checkEmail(email_id.getText().toString()))
                {
                    String uemail = email_id.getText().toString();
                    // declare parameters that are passed to PHP script
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                    postParameters.add(new BasicNameValuePair("email_id",uemail));

                    httpWrapper = new HttpWrapper();

                    httpWrapper.setPostParameters(postParameters);

                    //http post
                    try{
                        httpPost = new HttpPost("http://omega.uta.edu/~ssk0590/getUserSecurityQuestions.php");
                        httpWrapper.setUserIdForgotPwd(UserIdForgotPwd.this);
                        httpWrapper.execute(httpPost);
                    }
                    catch(Exception e){
                        Log.e("register_activity", "Error in http connection " + e.toString());
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email id.", Toast.LENGTH_SHORT).show();
                }
            }

            public boolean checkEmail(String email) {
                Pattern pattern = Pattern.compile("[A-Za-z]+\\.[A-Za-z]+@mavs\\.uta\\.edu$");
                Matcher matcher = pattern.matcher(email);
                return matcher.matches();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_id_forgot_pwd, menu);
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

    public void getSecurityQuestions(String data) throws JSONException {

        Log.i("Message","Inside getSecurityQuestions : Success");
        Log.i("Data", data);

        JSONObject jObj = new JSONObject(data);
        String result = jObj.getString("result");

        if(result.equalsIgnoreCase("success")) {

            JSONArray jArr = jObj.getJSONArray("questionaire");

            for (int i = 0; i < jArr.length(); i++) {
                JSONObject questionaire = jArr.getJSONObject(i);
                questions[i] = questionaire.getString("q");
                answers[i] = questionaire.getString("a");
            }

            for (int i = 0; i < questions.length; i++) {
                Log.i("q",questions[i]);
            }

            for (int i = 0; i < answers.length; i++) {
                Log.i("a",answers[i]);
            }

            password = jObj.getString("password");

            Log.i("password",password);

            Intent securityQuestionsActivity = new Intent(UserIdForgotPwd.this,ForgotPassword.class);
            securityQuestionsActivity.putExtra("qa",questions);
            securityQuestionsActivity.putExtra("aa",answers);
            securityQuestionsActivity.putExtra("pwd",password);
            startActivity(securityQuestionsActivity);

        } else {
            Toast.makeText(getApplicationContext(),"Invalid Email ID",Toast.LENGTH_SHORT).show();
        }
    }
}
