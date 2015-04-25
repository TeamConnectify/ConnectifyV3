package com.team5.uta.connectifyv1;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.team5.uta.connectifyv1.adapter.Interest;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FriendsListActivity extends ActionBarActivity {

    private TextView user;
    private User otherUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private User currentUser;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5793f")));

        setCurrentUser((User)getIntent().getSerializableExtra("user"));

        Log.i("Current User : ", getCurrentUser().getUid());

        getListOfFriends(getCurrentUser().getUid());
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
            httpWrapper.setFriendsListActivity(FriendsListActivity.this);
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
        httpWrapper1.setFriendsListActivity(FriendsListActivity.this);
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

    public JSONArray getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(JSONArray friendsList) {
        this.friendsList = friendsList;
    }

    private JSONArray friendsList;

    public void getFriendsListCallback(String result) {

        Log.i("Msg","getFriendsListCallback : "+result);
        try {
            JSONObject jObject  = new JSONObject(result);
            JSONArray friends = jObject.getJSONArray("friends");

            setFriendsList(friends);

            renderFriendList();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getListOfFriends(String userId) {

        // declare parameters that are passed to PHP script
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("user_id",userId));

        HttpWrapper httpWrapper1 = new HttpWrapper();
        httpWrapper1.setPostParameters(postParameters);

        HttpPost httppost1 = new HttpPost("http://omega.uta.edu/~ssk0590/getFriendList.php");
        httpWrapper1.setFriendsListActivity(FriendsListActivity.this);
        httpWrapper1.execute(httppost1);
    }

    public void unfriend(String userId) {

        // declare parameters that are passed to PHP script
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("user_id",getCurrentUser().getUid()));
        postParameters.add(new BasicNameValuePair("other_user_id",userId));

        HttpWrapper httpWrapper1 = new HttpWrapper();
        httpWrapper1.setPostParameters(postParameters);

        HttpPost httppost1 = new HttpPost("http://omega.uta.edu/~ssk0590/unfriend.php");
        httpWrapper1.setFriendsListActivity(FriendsListActivity.this);
        httpWrapper1.execute(httppost1);

    }

    public void unfriendCallback(String result) throws JSONException {

        JSONObject res = new JSONObject(result);

        if (res.getString("result").equalsIgnoreCase("success")) {
            Toast.makeText(getApplicationContext(), "Unfriended Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unfriend Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void renderFriendList() {

        ScrollView scroll = (ScrollView) this.findViewById(R.id.friends_list);
        TableRow.LayoutParams scrollparams = new TableRow.LayoutParams(500, 600);
        scroll.setLayoutParams(scrollparams);

        final TableLayout tbl = new TableLayout(this);
        TableLayout.LayoutParams tblparams = new TableLayout.LayoutParams(1000, 500);
        tbl.setLayoutParams(tblparams);

        View.OnClickListener viewProfilelistener = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                getUserDetails("" + v.getId());
                Log.i("Message", "ID : " + v.getId());
            }
        };

        View.OnClickListener unfriendlistener = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                unfriend("" + v.getId());
                tbl.removeView((View)v.getParent());
            }
        };


        JSONArray friendsList = getFriendsList();

        for (int idx = 0; idx < friendsList.length(); idx++) {

            try {

                JSONObject jObj = friendsList.getJSONObject(idx);

                TextView name = new TextView(this);
                name.setText(jObj.getString("username"));

                Button view = new Button(this);
                view.setText("View Profile");
                view.setId(Integer.parseInt(jObj.getString("userId")));
                view.setOnClickListener(viewProfilelistener);

                Button unfriend = new Button(this);
                unfriend.setText("Unfriend");
                unfriend.setId(Integer.parseInt(jObj.getString("userId")));
                unfriend.setOnClickListener(unfriendlistener);

                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                tr.addView(name);
                tr.addView(view);
                tr.addView(unfriend);

                tbl.addView(tr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        scroll.addView(tbl);

        Log.i("Lengths : ", "Scroll - "+scroll.getScrollBarSize() + " ; table - " + tbl.getChildCount());
    }
}
