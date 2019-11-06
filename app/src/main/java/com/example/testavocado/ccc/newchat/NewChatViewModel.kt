package com.example.testavocado.ccc.newchat

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.testavocado.ccc.chats.ChatRepo
import com.example.testavocado.ccc.mDatabase
import com.example.testavocado.Utils.HelpMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class NewChatViewModel (application: Application) : ViewModel() {
    val job= Job()
    val jobScope= CoroutineScope(job+ Dispatchers.Main)


    val database= mDatabase.getInstance(application.applicationContext)

    val repo= ChatRepo(database, HelpMethods.checkSharedPreferencesForUserId(application))

    val chats=repo.chatsToStart

}
