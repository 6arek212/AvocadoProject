package com.example.testavocado.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.testavocado.BaseActivity;
import com.example.testavocado.Notification.NotificationMethods;
import com.example.testavocado.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import static com.example.testavocado.BaseActivity.StartWorker;
import static com.example.testavocado.Service.App.CHANNEL_1_ID;

public class myWorker extends Worker {
    private static final String TAG = "myWorker";
    private boolean jobCancelled = false;
    private NotificationManagerCompat n1;
    public static String datetime_notification;
    private int current_user_id;
    private int offset;


    public static final int LIKE=1;
    public static final int DISLIKE=2;
    public static final int COMMENT=3;
    public static final int SHARED=4;
    public static final int FRIEND_REQUEST=5;
    public static final int SENT_MESSAGE=6;

    public myWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        getNotified();
        StartWorker();
        Log.d(TAG, "doWork: workin ");
        return Result.success();
    }



    private void getNotified(){
        Log.d(TAG, "getNotified: datetime_notification: "+datetime_notification+"   current_user_id: "+current_user_id
                +"  offset"+offset);
        NotificationMethods.getNotificationService(current_user_id, datetime_notification, offset, new NotificationMethods.OnGettingNotification() {
            @Override
            public void successfullyGettingNotification(String json) {
                Log.d(TAG, "successfullyGettingNotification: datetime_notification: "+datetime_notification+"   current_user_id: "+current_user_id
                        +"  offset"+offset);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    com.example.testavocado.Models.Notification notification;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        notification=new Gson().fromJson(jsonArray.get(i).toString(), com.example.testavocado.Models.Notification.class);
                        popNotification(notification);
                    }

                    BaseActivity.offset+=jsonArray.length();
                    offset+=jsonArray.length();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "successfullyGettingNotification: " + e.getMessage());
                }
            }

            @Override
            public void serverException(String exception) {
                Log.d(TAG, "serverException: "+exception);

            }

            @Override
            public void OnFailure(String exception) {
                Log.d(TAG, "OnFailure: "+exception);

            }
        });
    }




    public void  popNotification(com.example.testavocado.Models.Notification notification1){
        Intent intent=new Intent(getApplicationContext(),BaseActivity.class);
        switch (notification1.getNotification_type()){
            case LIKE:
                intent.putExtra(getApplicationContext().getString(R.string.post_fragment),0);
                break;

            case DISLIKE:
                intent.putExtra(getApplicationContext().getString(R.string.post_fragment),0);
                break;

            case COMMENT:
                intent.putExtra(getApplicationContext().getString(R.string.post_fragment),0);

                break;


            case SHARED:
                intent.putExtra(getApplicationContext().getString(R.string.post_fragment),0);
                break;


            case FRIEND_REQUEST:
                intent.putExtra(getApplicationContext().getString(R.string.connection_requests_fragment),2);
                break;


            case SENT_MESSAGE:
                intent.putExtra(getApplicationContext().getString(R.string.conversation_fragment),1);
                break;
        }



        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,0);


        Notification notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_1_ID)
                .setSmallIcon(R.drawable.avocado)
                .setContentTitle(notification1.getUser_sent_name())
                .setContentText(notification1.getType_txt())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();
        n1.notify(notification1.getNotification_id(), notification);
    }





}
