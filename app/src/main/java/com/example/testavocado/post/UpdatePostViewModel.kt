package com.example.testavocado.post

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testavocado.Models.Post
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.lang.Exception

class UpdatePostViewModel(application: Application, val post: Post) : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message


    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack


    var text: String? = null
    var type: Int = 0

    init {
        text=post.post_text
    }



    val selectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.d("UpdatePostViewModel", "${position}")
            type = position
        }
    }




    init {
        _progress.value = false
        _navigateBack.value = false
    }


    val job = Job()
    val scope = CoroutineScope(job + Dispatchers.IO)

    fun onClick() {
        scope.launch {
            try {
                _progress.postValue(true)
                val data = Network.post.updatePost(UpdatePostData(text, type, post.post_id)).await()

                if (data) {
                    _message.postValue("Updated")
                    _navigateBack.postValue(true)
                } else {
                    _message.postValue("Error updating")
                }
            } catch (e: Exception) {
                _message.postValue("Error")
            }
            _progress.postValue(false)
        }
    }


    fun messageShowd() {
        _message.value = null
    }

    fun navigated() {
        _navigateBack.value = false
    }


}

data class UpdatePostData(val text: String?, val type: Int, val post_id: Int)


object Network {
    private val retrofit = Retrofit.Builder()
            //.client(getUnsafeOkHttpClient())
            .baseUrl("http://ahmadgabarin-001-site1.ftempurl.com/")
            //.addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val post: UpdatePost by lazy {
        retrofit.create(UpdatePost::class.java)
    }
}

interface UpdatePost {
    @POST("api/Post/updatePost")
    fun updatePost(@Body updatePost: UpdatePostData): Deferred<Boolean>
}