package com.example.testavocado.ccc.message

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testavocado.R
import com.example.testavocado.Utils.HelpMethods
import com.example.testavocado.Utils.PhotoUpload
import com.example.testavocado.Utils.TimeMethods
import com.example.testavocado.ccc.Chat3
import com.example.testavocado.ccc.MessagesRepository
import com.example.testavocado.ccc.mDatabase
import java.io.File

class MessageWithPicViewModel (val application: Application,val userId:Int,val imageUrl:String,val chat: Chat3) : ViewModel() {

    private val _image= MutableLiveData<String>()
    val image: LiveData<String>
        get()=_image

    private val _error= MutableLiveData<String>()
    val error: LiveData<String>
        get()=_error

    private val _navigateBack= MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get()=_navigateBack

    private val _prog= MutableLiveData<Boolean>()
    val prog: LiveData<Boolean>
        get()=_prog


    val text=MutableLiveData<String>()

    val database= mDatabase.getInstance(application.applicationContext)
    val repo= MessagesRepository(database, HelpMethods.checkSharedPreferencesForUserId(application),chat,application)



    init {
        _prog.value=false
        _error.value=null
        _image.value=imageUrl
    }



    fun navigateComplete(){
        _navigateBack.value=false
    }


    fun errorShowed(){
        _error.value=null
    }

    fun sendMessage(text:String?){
        _prog.value=true
        val uri = Uri.fromFile(File(imageUrl))
        PhotoUpload.uploadNewPhotoFirebase(application.getString(R.string.new_photo),TimeMethods.getUTCdatetimeAsString(),uri,userId,application,object : PhotoUpload.OnUploadingPostListener2{
            override fun onSuccessListener(ImageUrl: String?) {
                if(chat.chatId.isNullOrEmpty()){
                    repo.sendAndCreateChat(text,ImageUrl)
                    _navigateBack.value=true
                    _prog.value=false
                }else{
                    repo.sendMessage(text,ImageUrl)
                    _navigateBack.value=true
                    _prog.value=false
                }
            }

            override fun onFailureListener(ex: String?) {
                _error.value=application.getString(R.string.CHECK_INTERNET)
            }
        })
    }


}
