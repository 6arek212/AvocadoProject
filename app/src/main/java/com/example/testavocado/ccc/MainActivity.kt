package com.example.testavocado.ccc

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.testavocado.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.extras!=null){
            val b1=intent?.extras?.getBundle("chat")
            b1?.let {
                val chat=b1.getParcelable<Chat3>("chat")

                Log.d("MainActivity1212","chat $chat")

                val bundle = Bundle()
                bundle.putParcelable("chat",chat)
                findNavController(R.id.my_nav_host_fragment)
                        .setGraph(R.navigation.nav2, bundle)

            }
        }

    }

    override fun onResume() {
        super.onResume()
        val mNotificationManager: NotificationManager
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancelAll()
    }
}
