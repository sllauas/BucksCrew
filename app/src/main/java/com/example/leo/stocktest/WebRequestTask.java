package com.example.leo.stocktest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.*;
import java.net.*;

/**
 * Created by leo on 29/12/14.
 */
public class WebRequestTask extends AsyncTask<String, Void, String>
{
    public enum  WebRequestState
    {
        READY ,FAIL_NONETWORK, FAIL_NOURL, SUCCESS
    }

    public interface WebRequestListener
    {
        public void webpageRequestFinish(String URL, WebRequestState state, String result);
    }


    Context mCon;
    WebRequestListener mListener;
    WebRequestState state;
    String url;

    public WebRequestTask(Context context, WebRequestListener listener)
    {
        mCon = context;
        mListener = listener;

        state = WebRequestState.READY;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {

            ConnectivityManager connMgr = (ConnectivityManager)mCon.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected())
            {
                url = urls[0];
                return downloadUrl(url);
            }
            else
            {
                state = WebRequestState.FAIL_NONETWORK;
                return "Cannot connect to the network!!!!";
            }

        } catch (IOException e) {
            state = WebRequestState.FAIL_NOURL;
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String contentAsString =  s.hasNext() ? s.next() : "";

            state = WebRequestState.SUCCESS;

//                String contentAsString = ;
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //textView.setText(result);
        //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();

        mListener.webpageRequestFinish(url, state, result);
    }


    public void singleThreadExecute(String url)
    {
        //stupid way to break all rules in AsyncTask
        onPostExecute(doInBackground(url));

    }
}

