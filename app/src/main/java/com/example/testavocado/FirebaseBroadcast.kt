package com.example.testavocado

import android.app.ActivityManager
import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.testavocado.Login.RegisterMethods
import com.example.testavocado.Service.App.CHANNEL_1_ID
import com.example.testavocado.Utils.HelpMethods
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


val TAG = "BROADCAST"

class FirebaseBroadcast : FirebaseMessagingService() {


    companion object

    var index = 0;

    val CHANEL_ID = "1"

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d(TAG, "chat num count ${(HelpMethods.getChatNum(this))}")

        Log.d(TAG, "From: ${p0.from}   $index   isAppForground(this")
        if (isAppForground(this)) {
            return
        }


        HelpMethods.addChatNum(this)


        // Check if message contains a notification payload.
        p0.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")


            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_1_ID)
                    .setContentTitle("New Message")
                    .setSmallIcon(R.drawable.avocado_logo)
                    .setContentText(it.body)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .build()


            val manager = NotificationManagerCompat.from(applicationContext)
            manager.notify(/*notification id*/index++, notification)
        }
    }

    override fun onNewToken(token: String) {
        HelpMethods.addToken(token, this)
        val fr = FirebaseDatabase.getInstance().reference
        if (HelpMethods.checkSharedPreferencesForUserId(this) != -1) {
            Log.d("onNewToken", "token $token    ${HelpMethods.checkSharedPreferencesForUserId(this)}")
            fr.child("users").child(HelpMethods.checkSharedPreferencesForUserId(this).toString()).child("token").setValue(token)
            updateTheToken(token, this)

        }

    }


    fun isAppForground(mContext: Context): Boolean {
        val am = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = tasks[0].topActivity
            if (topActivity.packageName != mContext.packageName) {
                return false
            }
        }
        return true
    }


}


fun updateTheToken(token: String, context: Context) {
    RegisterMethods.updateToken(token, HelpMethods.checkSharedPreferencesForUserId(context), object : RegisterMethods.OnNotificationListener {
        override fun onSuccessListener() {
            Log.d(TAG, "token updated")
        }

        override fun onServerException(ex: String?) {
            Log.d(TAG, "onServerException $ex")

        }

        override fun onFailureListener(ex: String?) {
            Log.d(TAG, "onFailureListener $ex")

        }
    });
}



