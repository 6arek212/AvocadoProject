package com.example.testavocado.ccc

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ClearData (application: Application) {
    val database= mDatabase.getInstance(application)

    val job= Job()
    val scop= CoroutineScope(job+Dispatchers.IO)

    fun clearDb(){
        scop.launch {
            database.messageDao.clear()
            database.chatDao.clear()
        }
    }


}