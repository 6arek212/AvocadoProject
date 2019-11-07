package com.example.testavocado.ccc

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.testavocado.Utils.HelpMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MessageViewModel (application: Application,val chat: Chat3): ViewModel() {

    val job= Job()
    val jobScope= CoroutineScope(job+Dispatchers.Main)


    val database= mDatabase.getInstance(application.applicationContext)
    val repo=MessagesRepository(database,HelpMethods.checkSharedPreferencesForUserId(application),chat,application)

    val messages=repo.messages
    val error=repo.error
    val seen=repo.seen

    val textToSend=MutableLiveData<String>()

    private val _clearText=MutableLiveData<Boolean>()
    val clearText:LiveData<Boolean>
    get()=_clearText


    private val _showMessageEmptyText=MutableLiveData<Boolean>()
    val showMessageEmptyText:LiveData<Boolean>
        get()=_showMessageEmptyText



    val typing=repo.typing

    init {
        _showMessageEmptyText.value=false
        Log.d("ididid","${HelpMethods.checkSharedPreferencesForUserId(application)}")
    }


    fun showErrorComplete(){
        repo.showErrorComplete()
    }



    fun messageShowComplete(){
        _showMessageEmptyText.value=false
    }


    fun sendMsg(){
        if(textToSend.value.isNullOrEmpty()){
            _showMessageEmptyText.value=true
            return
        }

        if(chat.chatId.isEmpty()){
            textToSend.value?.let {
                repo.sendAndCreateChat(it)
            }
            _clearText.value=true
            return
        }

        textToSend.value?.let {
            repo.sendMessage(it)
        }
        _clearText.value=true
    }

    fun clearTextComplete(){
        _clearText.value=false
    }


    fun typing(state:Boolean){
        repo.updateTyping(state)
    }

    override fun onCleared() {
        super.onCleared()
        repo.removeListeners()
        job.cancel()
        Log.d("cleared","cleared")
    }

}
