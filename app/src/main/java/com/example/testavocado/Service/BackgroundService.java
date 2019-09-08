package com.example.testavocado.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.testavocado.BaseActivity;
import com.example.testavocado.Notification.NotificationMethods;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import static com.example.testavocado.Service.App.CHANNEL_1_ID;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";

    private int current_user_id;
    private NotificationManager mNM;
    private String datetime_notification;
    private int offset;
    private Context mContext;


    private static final int LIKE = 1;
    private static final int DISLIKE = 2;
    private static final int COMMENT = 3;
    private static final int SHARED = 4;
    private static final int FRIEND_REQUEST = 5;
    private static final int SENT_MESSAGE = 6;


   static Handler handler;
  static   Runnable runnable;


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
        initVars();
        startService();
    }




    private void initVars() {
        mContext = this;
        current_user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);
        datetime_notification = TimeMethods.getUTCdatetimeAsString();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY ;

    }

    public void startService() {
        handler = new Handler(getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                getNotified();
                handler.postDelayed(runnable, 2000); //100 ms you should do it 4000
            }
        };
        handler.postDelayed(runnable, 0);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
     //   handler.removeCallbacks(runnable);
    }



    public static void stopThis() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }


    private void getNotified() {
        Log.d(TAG, "getNotified: datetime_notification: " + datetime_notification + "   current_user_id: " + current_user_id
                + "  offset" + offset);
        NotificationMethods.getNotificationService(current_user_id, datetime_notification, offset, new NotificationMethods.OnGettingNotification() {
            @Override
            public void successfullyGettingNotification(String json) {
                Log.d(TAG, "successfullyGettingNotification: datetime_notification: " + datetime_notification + "   current_user_id: " + current_user_id
                        + "  offset" + offset);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    com.example.testavocado.Models.Notification notification;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        notification = new Gson().fromJson(jsonArray.get(i).toString(), com.example.testavocado.Models.Notification.class);
                        popNotification(notification);
                    }

                    BaseActivity.offset += jsonArray.length();
                    offset += jsonArray.length();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "successfullyGettingNotification: " + e.getMessage());
                }
            }

            @Override
            public void serverException(String exception) {
                Log.d(TAG, "serverException: " + exception);

            }

            @Override
            public void OnFailure(String exception) {
                Log.d(TAG, "OnFailure: " + exception);

            }
        });
    }


    public void popNotification(com.example.testavocado.Models.Notification notification1) {
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        switch (notification1.getNotification_type()) {
            case LIKE:
                intent.putExtra(getApplicationContext().getString(R.string.post_fragment), 0);
                break;

            case DISLIKE:
                intent.putExtra(getApplicationContext().getString(R.string.post_fragment), 0);
                break;

            case COMMENT:
                intent.putExtra(getApplicationContext().getString(R.string.post_fragment), 0);

                break;


            case SHARED:
                intent.putExtra(getApplicationContext().getString(R.string.post_fragment), 0);
                break;


            case FRIEND_REQUEST:
                intent.putExtra(getApplicationContext().getString(R.string.connection_requests_fragment), 2);
                break;


            case SENT_MESSAGE:
                intent.putExtra(getApplicationContext().getString(R.string.conversation_fragment), 1);
                break;
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);


        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
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
        mNM.notify(notification1.getNotification_id(), notification);
    }


}
