package com.example.testavocado.ccc.chats

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.testavocado.ccc.Chat3
import com.example.testavocado.ccc.mDatabase
import com.example.testavocado.Utils.HelpMethods

import kotlinx.coroutines.*

class ChatsViewModel (application: Application) : ViewModel() {

    val job= Job()
    val jobScope= CoroutineScope(job+ Dispatchers.IO)


    val database= mDatabase.getInstance(application.applicationContext)

    val repo= ChatRepo(database, HelpMethods.checkSharedPreferencesForUserId(application))
    val chats=repo.chats




    fun removeChat(chat: Chat3){
        repo.removeChat(chat.chatId,chat.with)
    }

    override fun onCleared() {
        super.onCleared()
        repo.onClear()
        job.cancel()
    }

}
