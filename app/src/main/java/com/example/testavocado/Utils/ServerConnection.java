package com.example.testavocado.Utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ServerConnection {
    private static final String TAG = "ServerConnection";

    //FOR INSERTING / UPDATE / CREATE

    public static String TASK_DONE="1";
    public static String TASK_ERROR="0";
    public static String TASK_FAIL_EXCEPTION="-1";


   // private static final String BASE_URL = "http://tarik221-001-site1.btempurl.com//home/";
   private static final String BASE_URL = "http://tarik775-001-site1.itempurl.com/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    public static void   disableThreads(Context context){
        client.cancelAllRequests(true);

        client.cancelRequests(context,true);


    }
}
