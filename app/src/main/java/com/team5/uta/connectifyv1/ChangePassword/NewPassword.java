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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team5.uta.connectifyv1.ForgotPassword;
import com.team5.uta.connectifyv1.HttpWrapper;
import com.team5.uta.connectifyv1.R;
import com.team5.uta.connectifyv1.User;
import com.team5.uta.connectifyv1.UserProfile;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewPassword extends ActionBarActivity {
    private HttpPost httpPost;
    private HttpWrapper httpWrapper;
    private String password = "";
    User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5793f")));

        this.user = (User) getIntent().getSerializableExtra("user");
        final EditText password = (EditText) findViewById(R.id.resetpwd);
        final EditText confirm_password = (EditText) findViewById(R.id.confirmresetpwd);
        final EditText emailid = (EditText) findViewById(R.id.emailid);
        final Button resetbttn=(Button) findViewById(R.id.resetpwdbttn);
        final String emailid1 = emailid.toString();


        resetbttn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd = password.getText().toString();
                if (!(pwd.equals(confirm_password.getText().toString()))) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent profile = new Intent(NewPassword.this, ChangeSecurityQuestions.class);
                    profile.putExtra("user", user);
                    profile.putExtra("emailid",emailid1);
                    startActivity(profile);
                }
                //Send new password to database and update
                //user current user object,get email and send email+new pwd to DB

                if (checkEmail(emailid.getText().toString()))
                {
                    String uemail = emailid.getText().toString();
                    // declare parameters that are passed to PHP script
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    Log.i("msg",pwd);
                    postParameters.add(new BasicNameValuePair("email_id",uemail));
                    postParameters.add(new BasicNameValuePair("pwd",pwd));

                    httpWrapper = new HttpWrapper();

                    httpWrapper.setPostParameters(postParameters);

                    //http post
                    try{
                        httpPost = new HttpPost("http://omega.uta.edu/~mxs1773/updatepassword.php");
                        httpWrapper.setNewPassword(NewPassword.this);
                        httpWrapper.execute(httpPost);
                        Toast.makeText(getApplicationContext(), "Password Changed!.", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_new_password, menu);
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
