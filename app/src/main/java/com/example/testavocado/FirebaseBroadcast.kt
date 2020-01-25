package com.example.testavocado

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.testavocado.Login.RegisterMethods
import com.example.testavocado.Utils.HelpMethods
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


val TAG = "BROADCAST"

class FirebaseBroadcast : FirebaseMessagingService() {


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d(TAG, "From: ${p0?.from}")
        // Check if message contains a notification payload.
        p0?.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            //Message Services handle notification
            val notification = NotificationCompat.Builder(this)
                    .setContentTitle(p0.from)
                    .setSmallIcon(R.drawable.avocado_logo)
                    .setContentText(it.body)
                    .build()
            val manager = NotificationManagerCompat.from(applicationContext)
            manager.notify(/*notification id*/0, notification)
        }
    }

    override fun onNewToken(token: String) {
        HelpMethods.addToken(token, this)
        val fr = FirebaseDatabase.getInstance().reference
        fr.child("users").child(HelpMethods.checkSharedPreferencesForUserId(this).toString()).child("token").setValue(token)
        updateTheToken(token,this)
    }


}



fun updateTheToken(token: String,context: Context){
    RegisterMethods.updateToken(token,HelpMethods.checkSharedPreferencesForUserId(context),object : RegisterMethods.OnNotificationListener{
        override fun onSuccessListener() {
            Log.d(TAG, "token updated")
        }

        override fun onServerException(ex: String?) {
            Log.d(TAG, "$ex")

        }

        override fun onFailureListener(ex: String?) {
            Log.d(TAG, "$ex")

        }
    });
}



