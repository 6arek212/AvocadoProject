package com.example.chat.NewChat

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.chat.Chats.ChatRepo
import com.example.smartphone.database.mDatabase
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
