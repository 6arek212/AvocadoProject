package com.example.testavocado.methods.emil_methods;

import android.content.Context;

import com.example.testavocado.R;
import com.example.testavocado.objects.result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class check_if_emil_exists {
    private static final String TAG = "check_if_emil_exists";
    private Context mycontext;

    //ctor ----------------------------------------------------------------------------------------->

    public check_if_emil_exists(Context mycontext) {
        this.mycontext = mycontext;
    }

    public check_if_emil_exists() {
    }
    //set get----------------------------------------------------------------------------------------->

    public static String getTAG() {
        return TAG;
    }

    public Context getMycontext() {
        return mycontext;
    }

    public void setMycontext(Context mycontext) {
        this.mycontext = mycontext;
    }
    //method check----------------------------------------------------------------------------------------->

    static private AsyncHttpClient myclient = new AsyncHttpClient();
    static private RequestParams myparam = new RequestParams();

    //method check if emil exists
    // 1 emil used  0-emil not used
    static public void chceck_if_emil_exists(String emil, final Context context1, final on_checking_emil_exists listner) {
        myparam.put("emil", emil);

        myclient.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.check_if_emil_exists), myparam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                result myresult = new result();
                try {
                    myresult.setState(response.getString(context1.getString(R.string.State)));
                    myresult.setException(response.getString(context1.getString(R.string.Exception)));
                    myresult.setJson_data(response.getString(context1.getString(R.string.Json_data)));
                    if (myresult.getState().equals("1")) {
                        listner.onSuccess_checking_emil_exists(myresult.getState());
                    } else if (myresult.getState().equals("0")) {
                        listner.onSuccess_checking_emil_exists(myresult.getState());

                    } else if (myresult.getState().equals("-1")) {
                        listner.onSuccess_checking_emil_exists(myresult.getState());
                    }
                } catch (Exception ex) {
                    listner.onserverException(ex.getMessage().toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                listner.onFailure(throwable.getMessage().toString());
            }
        });
    }
}
