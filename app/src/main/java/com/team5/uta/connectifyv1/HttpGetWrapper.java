package com.team5.uta.connectifyv1;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *  Wrapper class to send Http requests to the server
 */
public class HttpGetWrapper extends AsyncTask<HttpGet, Void, InputStream> {

    private BufferedReader reader;
    private StringBuilder sb;
    private String line, result1;
    private InputStream inputStream = null;

    public AddInterestActivity getAddInterestActivity() {
        return addInterestActivity;
    }

    public void setAddInterestActivity(AddInterestActivity addInterestActivity) {
        this.addInterestActivity = addInterestActivity;
    }

    private AddInterestActivity addInterestActivity;

    public SecurityQuestions getSecurityQuestionsActivity() {
        return securityQuestionsActivity;
    }

    public void setSecurityQuestionsActivity(SecurityQuestions securityQuestionsActivity) {
        this.securityQuestionsActivity = securityQuestionsActivity;
    }

    private SecurityQuestions securityQuestionsActivity;

    @Override
    protected InputStream doInBackground(HttpGet... httpget) {
        Log.i("http_wrapper","Inside doInBackground");
        InputStream is = null;
        String result = "";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpget[0]);
            // receive response as inputStream
            is = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(is != null) result = convertInputStreamToString(is);
            else result = "Did not work!";

            Log.i("http_wrapper",httpget.toString());
            Log.i("http_wrapper",result.toString());
        }
        catch(Exception e){
            Log.e("http_wrapper", "Error in http connection " + e.toString());
        }
        this.inputStream = is;
        return is;
    }

    // convert inputstream to String
    protected String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        return result;
    }

    @Override
    protected void onPostExecute(InputStream is) {
        Log.i("http_wrapper","Inside onPostExecute");
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

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

}
