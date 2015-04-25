package com.team5.uta.connectifyv1;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team5.uta.connectifyv1.ChangePassword.ChangeSecurityQuestions;
import com.team5.uta.connectifyv1.ChangePassword.NewPassword;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *  Wrapper class to send Http requests to the server
 */
public class HttpWrapper extends AsyncTask<HttpPost, Void, InputStream> {

    private BufferedReader reader;
    private StringBuilder sb;
    private String line, result1;
    private ArrayList<NameValuePair> postParameters;
    private InputStream inputStream = null;
    private LoginActivity loginActivity;
    private RegisterActivity registerActivity;
    private AddInterestActivity addInterestActivity;
    private UserProfile userProfileActivity;
    private MapActivity mapActivity;
    private String TAG = "http_wrapper";

    @Override
    protected InputStream doInBackground(HttpPost... httppost) {
        InputStream is = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = httppost[0];
            Log.i(TAG,httpPost.getURI().toString());
            if(postParameters!=null) {
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            }
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }
        catch(Exception e){
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        this.inputStream = is;
        return is;
    }

    @Override
    protected void onPostExecute(InputStream is) {
        String result1 = responseToString(is);
        String status;

        Log.i(TAG,result1.toString());

        if(result1.contains("Register")) {
            if(result1.contains("Success")) {
                status = "Success";
                try {
                    JSONObject jObj = new JSONObject(result1);
                    this.registerActivity.setUser_id(jObj.getString("user_id"));
                    this.registerActivity.registerResult(status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                status = "Fail";
                this.registerActivity.registerResult(status);
            }
        } else if(result1.contains("Login")) {
            if(result1.contains("Success")) {
                status = "Success";
                this.loginActivity.loginResult(result1);
            } else {
                status = "Fail";
                this.loginActivity.loginResult(result1);
            }
        } else if(result1.contains("Get interest")) {
            this.loginActivity.getInterestFromLoginResult(result1);
        }  else if(result1.contains("Get Location Success")) {
            this.mapActivity.getUsersLocationResult(result1);
        } else if(result1.contains("UserDetails")) {
            try {
                if(this.notificationActivity != null) this.notificationActivity.userDetailsCallback(result1);
                else if(this.friendsListActivity != null) this.friendsListActivity.userDetailsCallback(result1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(result1.contains("notification")) {
            try {
                if(this.notificationActivity != null) this.notificationActivity.openUserProfile(result1);
                else if(this.friendsListActivity != null) this.friendsListActivity.openUserProfile(result1);
                Log.i("msg", "notification openUserProfile");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }  else if(result1.contains("Get other user interest")) {
            this.mapActivity.getOtherUserInterestResult(result1);
        } else if(result1.contains("Get Security Questions")) {
            try {
                this.userIdForgotPwd.getSecurityQuestions(result1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(result1.contains("Friends List")) {
                this.friendsListActivity.getFriendsListCallback(result1);
        } else if(result1.contains("unfriend")) {
            try {
                this.friendsListActivity.unfriendCallback(result1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(result1.contains("friend")) {
            try {
                this.notificationActivity.friendCallback(result1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String responseToString(InputStream ins)
    {
        //convert response to string
        try{
            reader = new BufferedReader(new InputStreamReader(ins,"iso-8859-1"),8);
            sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            ins.close();

            result1 = sb.toString();
            return result1;

        }

        catch(Exception e)
        {
            Log.e("log_tag", "Error converting result "+e.toString());
            return "Error converting result "+e.toString();
        }
    }



    public ArrayList<NameValuePair> getPostParameters() {
        return postParameters;
    }

    public void setPostParameters(ArrayList<NameValuePair> postParameters) {
        this.postParameters = postParameters;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public RegisterActivity getRegisterActivity() {
        return registerActivity;
    }

    public void setRegisterActivity(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    public AddInterestActivity getAddInterestActivity() {
        return addInterestActivity;
    }

    public void setAddInterestActivity(AddInterestActivity addInterestActivity) {
        this.addInterestActivity = addInterestActivity;
    }

    public UserProfile getUserProfileActivity() {
        return userProfileActivity;
    }

    public void setUserProfileActivity(UserProfile userProfileActivity) {
        this.userProfileActivity = userProfileActivity;
    }

    public MapActivity getMapActivity() {
        return mapActivity;
    }

    public void setMapActivity(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    public SecurityQuestions getSecurityQuestionsActivity() {
        return securityQuestionsActivity;
    }

    public void setSecurityQuestionsActivity(SecurityQuestions securityQuestionsActivity) {
        this.securityQuestionsActivity = securityQuestionsActivity;
    }

    private SecurityQuestions securityQuestionsActivity;



    private UserIdForgotPwd userIdForgotPwd;
    public UserIdForgotPwd getUserIdForgotPwd() {
        return userIdForgotPwd;
    }

    public void setUserIdForgotPwd(UserIdForgotPwd userIdForgotPwd) {
        this.userIdForgotPwd = userIdForgotPwd;
    }


    private NewPassword newPassword;

    public void setNewPassword(NewPassword newPassword) {
        this.newPassword = newPassword;
    }



    private ChangeSecurityQuestions changesecurityQuestionsActivity;

    public void setChangeSecurityQuestionsActivity(ChangeSecurityQuestions changesecurityQuestionsActivity) {
        this.securityQuestionsActivity = securityQuestionsActivity;
    }


    public NotificationActivity getNotificationActivity() {
        return notificationActivity;
    }

    public void setNotificationActivity(NotificationActivity notificationActivity) {
        this.notificationActivity = notificationActivity;
    }

    private NotificationActivity notificationActivity;

    public FriendsListActivity getFriendsListActivity() {
        return friendsListActivity;
    }

    public void setFriendsListActivity(FriendsListActivity friendsListActivity) {
        this.friendsListActivity = friendsListActivity;
    }

    private FriendsListActivity friendsListActivity;

}
