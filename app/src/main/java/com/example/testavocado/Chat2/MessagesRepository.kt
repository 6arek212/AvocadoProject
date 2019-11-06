package com.example.chat

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Database
import androidx.room.Query
import com.example.smartphone.database.*
import com.example.testavocado.R
import com.example.testavocado.Utils.TimeMethods
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import kotlinx.coroutines.*

class MessagesRepository(
        val database: mDatabase,
        val userId: Int,
        val chat: Chat2,
        val application: Application
) {
    val TAG = "MessageRepo"

    var isConnected: Boolean = false


    private val chatId = MutableLiveData<String>()
    val messages = Transformations.switchMap(chatId) {
        database.messageDao.getMessages(chat.chatId)
    }

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val myRef = firebaseDatabase.getReference()
    var isTheSender: Boolean = false

    private val _typing = MutableLiveData<Boolean>()
    val typing: LiveData<Boolean>
        get() = _typing


    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error







    fun checkNetwork() {
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d(TAG, "connected")
                    isConnected = true
                } else {
                    Log.d(TAG, "not connected")
                    isConnected = false
                    _error.postValue(application.getString(R.string.CHECK_INTERNET))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled")
            }
        })

    }


    fun showErrorComplete() {
        _error.value = null
    }


    init {
        checkNetwork()
        _error.value = null
        if (chat.sender == 0) {
            chat.sender = userId
            isTheSender = true
        } else {
            isTheSender = chat.sender == userId
        }
        chatId.value = chat.chatId

        checkIfTyping()
        refreshMessages()
    }


    fun refreshMessages() {
        if (chat.chatId.isEmpty()) {
            return
        }

        myRef.child("chats").child(chat.chatId).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        _error.postValue(application.getString(R.string.CHECK_INTERNET))

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Log.d("MessagesRepository", p0.toString())
                        getData(p0)
                    }
                })

    }

    val job = Job()
    val jobScope = CoroutineScope(job + Dispatchers.IO)


    fun getData(dss: DataSnapshot) {
        jobScope.launch {
            val messageList = ArrayList<Message>()

            for (ds: DataSnapshot in dss.children) {
                Log.d("messageCheck", "${ds.value}")

                val message = ds.getValue(Message::class.java)
                Log.d("message", message.toString())
                message?.let {
                    if (message.senderId != userId) {


                        messageList.add(message)
                        myRef.child("chats").child(chat.chatId).child("messages").child(message._id).removeValue()
                    }
                }

            }
            database.messageDao.insertAll(messageList)
        }
    }


    fun sendMessage(msg: String) {
        if (!isConnected) {
            _error.postValue(application.getString(R.string.CHECK_INTERNET))
            return
        }

        val key = myRef.child("chats").child(chat.chatId).child("messages").push().key

        key?.let {
            val message = Message(key, msg, TimeMethods.getUTCdatetimeAsString(), userId, chat.chatId)
            myRef.child("chats").child(chat.chatId).child("messages").child(key).setValue(message)

            val ref = FirebaseDatabase.getInstance().reference

            val chatSend = ChatSend(chat.chatId, userId, chat.with)

            ref.child("users").child(userId.toString()).child("chats").child(chat.chatId).setValue(chatSend)
            ref.child("users").child(userId.toString()).child("friends").child(chat.with.toString()).setValue(chatSend)

            chatSend.with = userId
            ref.child("users").child(chat.with.toString()).child("chats").child(chat.chatId).setValue(chatSend)
            ref.child("users").child(chat.with.toString()).child("friends").child(userId.toString()).setValue(chatSend)


            ref.child("chats").child(chat.chatId).child("lastMsg").setValue(msg)
            ref.child("chats").child(chat.chatId).child("datetimeLastMsg").setValue(TimeMethods.getUTCdatetimeAsString())

            jobScope.launch {
                database.messageDao.insert(message)
            }

        }
    }


    fun checkIfTyping() {
        if (chat.chatId.isEmpty()) {
            return
        }

        myRef.child("chats").child(chat.chatId).child(
                when (isTheSender) {
                    true -> "r_typing"
                    false -> "s_typing"
                }
        )
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        _error.postValue(application.getString(R.string.CHECK_INTERNET))

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        p0.value?.let {
                            _typing.value = it as Boolean
                        }
                    }
                })
    }


    fun updateTyping(state: Boolean) {
        if (chat.chatId.isEmpty()) {
            return
        }

        if (isTheSender) {
            myRef.child("chats").child(chat.chatId).child("s_typing").setValue(state)
        } else {
            myRef.child("chats").child(chat.chatId).child("r_typing").setValue(state)
        }
    }


    data class ChatSend(var chatId: String = "", var sender: Int = 0, var with: Int = 0)


    fun sendAndCreateChat(msg: String) {
        if (!isConnected) {
            _error.postValue(application.getString(R.string.CHECK_INTERNET))
            return
        }
        val key = myRef.child("chats").push().key

        key?.let {
            val chatA = Chat(it, userId, chat.with, datetime = TimeMethods.getUTCdatetimeAsString())
            myRef.child("chats").child(it).setValue(chatA)
            chat.chatId = key

            val ref = FirebaseDatabase.getInstance().reference

            val chatSend = ChatSend(key, userId, chat.with)

            ref.child("users").child(userId.toString()).child("chats").child(key).setValue(chatSend)
            ref.child("users").child(userId.toString()).child("friends").child(chat.with.toString()).setValue(chatSend)

            chatSend.with = userId
            ref.child("users").child(chat.with.toString()).child("chats").child(key).setValue(chatSend)
            ref.child("users").child(chat.with.toString()).child("friends").child(userId.toString()).setValue(chatSend)


            ref.child("chats").child(chat.chatId).child("lastMsg").setValue(msg)
            ref.child("chats").child(chat.chatId).child("datetimeLastMsg").setValue(TimeMethods.getUTCdatetimeAsString())


            chatId.value = chat.chatId
            val messageKey = myRef.child("chats").child(chat.chatId).child("messages").push().key
            messageKey?.let {
                val message = Message(messageKey, msg, TimeMethods.getUTCdatetimeAsString(), userId, chat.chatId)
                myRef.child("chats").child(chat.chatId).child("messages").child(messageKey).setValue(message)
                checkIfTyping()
                refreshMessages()
                jobScope.launch {
                    database.messageDao.insert(message)
                }
            }

        }
    }


}

