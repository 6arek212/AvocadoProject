package com.example.chat.Chats

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat.MessagesRepository
import com.example.smartphone.database.Chat2
import com.example.smartphone.database.mDatabase
import com.example.testavocado.Utils.HelpMethods
import kotlinx.coroutines.*

class ChatsViewModel (application: Application) : ViewModel() {

    val job= Job()
    val jobScope= CoroutineScope(job+ Dispatchers.IO)


    val database= mDatabase.getInstance(application.applicationContext)

    val repo=ChatRepo(database, HelpMethods.checkSharedPreferencesForUserId(application))
    val chats=repo.chats




    fun removeChat(chat:Chat2){
        repo.removeChat(chat.chatId,chat.with)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}
