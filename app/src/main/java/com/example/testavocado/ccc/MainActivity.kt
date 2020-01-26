package com.example.testavocado.ccc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.testavocado.R
import kotlinx.android.synthetic.main.activity_main.*

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
}
