package com.team5.uta.connectifyv1;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team5.uta.connectifyv1.adapter.Interest;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NotificationActivity extends ActionBarActivity {

    private TextView user;
    private User otherUser;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5793f")));

        ScrollView scroll = (ScrollView) this.findViewById(R.id.notification_list);
        TableRow.LayoutParams scrollparams = new TableRow.LayoutParams(500, 600);
        scroll.setLayoutParams(scrollparams);

        final TableLayout tbl = new TableLayout(this);
        TableLayout.LayoutParams tblparams = new TableLayout.LayoutParams(1000, 500);
        tbl.setLayoutParams(tblparams);

        View.OnClickListener unlocklistener = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                getUserDetails("" + v.getId());
                Log.i("Message", "ID : " + v.getId());
            }
        };

        View.OnClickListener ignorelistener = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                tbl.removeView((View)v.getParent());
            }
        };

        for (int idx = 0; idx < userList.length; idx++) {

            Button unlock = new Button(this);
            //LinearLayout.LayoutParams unlockparams = new LinearLayout.LayoutParams(100, 50);
            //unlockparams.setMargins(30, 0, 10, 0); //left, top, right, bottom
            //unlockparams.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            //unlock.setLayoutParams(unlockparams);
            unlock.setText("Unlock");
            unlock.setId(Integer.parseInt(userList[idx]));
            unlock.setOnClickListener(unlocklistener);

            Button ignore = new Button(this);
//            LinearLayout.LayoutParams ignoreparams = new LinearLayout.LayoutParams(100, 50);
//            ignoreparams.setMargins(20, 0, 20, 0); //left, top, right, bottom
//            ignoreparams.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            ignore.setLayoutParams(ignoreparams);
            ignore.setText("Ignore");
            ignore.setOnClickListener(ignorelistener);

            TextView counter = new TextView(this);
//            LinearLayout.LayoutParams counterparams = new LinearLayout.LayoutParams(50, 50);
//            counterparams.setMargins(20, 0, 0, 0); //left, top, right, bottom
//            counterparams.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            counter.setLayoutParams(ignoreparams);
            counter.setText("22");

            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            tr.addView(unlock);
            tr.addView(ignore);
            tr.addView(counter);

            tbl.addView(tr);
        }

        scroll.addView(tbl);

        Log.i("Lengths : ", "Scroll - "+scroll.getScrollBarSize() + " ; table - " + tbl.getChildCount());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
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



    public void getUserDetails(String userid)
    {
        // declare parameters that are passed to PHP script
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        // define the parameter
        postParameters.add(new BasicNameValuePair("user_id",userid));

        HttpWrapper httpWrapper = new HttpWrapper();
        httpWrapper.setPostParameters(postParameters);

        //http post
        try{
            HttpPost httppost = new HttpPost("http://omega.uta.edu/~ssk0590/getUserDetailsByUserId.php");
            httpWrapper.setNotificationActivity(NotificationActivity.this);
            httpWrapper.execute(httppost);
        }
        catch(Exception e){
            Log.e("Message", "Error in http connection " + e.toString());
        }
    }

    public User getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }

    public void userDetailsCallback(String data) throws JSONException {

        JSONObject jObj = new JSONObject(data);

        String userid = jObj.getString("userId");
        String firstName = jObj.getString("firstName");
        String lastName = jObj.getString("lastName");
        String email = jObj.getString("email");

        setOtherUser(new User(userid,firstName,lastName,null,email,null,null));

        // declare parameters that are passed to PHP script
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("user_id",userid));

        HttpWrapper httpWrapper1 = new HttpWrapper();
        httpWrapper1.setPostParameters(postParameters);

        HttpPost httppost1 = new HttpPost("http://omega.uta.edu/~ssk0590/get_other_user_interest.php?flag=1");
        httpWrapper1.setNotificationActivity(NotificationActivity.this);
        httpWrapper1.execute(httppost1);
    }

    public void openUserProfile(String result) {
        Log.i("Msg","openUserProfile");
        try {
            JSONObject jObject  = new JSONObject(result);
            String interests = jObject.getString("interests");
            Log.i("Other User Interests", "Final interests: " + interests);
            ArrayList<Interest> selectedInterest = new ArrayList<Interest>(5);

            String[] rawInterests = interests.split(",");
            for(int i = 0;i<rawInterests.length;i++) {
                String[] interestItem = rawInterests[i].split(":");
                String interestText = interestItem[0];
                int interestImageId = Integer.valueOf(interestItem[1].trim());
                Interest interest = new Interest(interestText,interestImageId);
                selectedInterest.add(interest);
            }

            otherUser.setInterests(selectedInterest);
            Intent intent1 = new Intent(this, OtherUserProfile.class);
            intent1.putExtra("user", otherUser);
            startActivity(intent1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String[] getUserList() {
        return userList;
    }

    public void setUserList(String[] userList) {
        this.userList = userList;
    }

    private String[] userList = {"1", "2", "3", "4", "5", "6", "7"};
}
