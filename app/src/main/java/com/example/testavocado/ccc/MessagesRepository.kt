package com.example.testavocado.ccc

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.testavocado.R
import com.example.testavocado.Utils.TimeMethods
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*


class MessagesRepository(
        val database: mDatabase,
        val userId: Int,
        val chat: Chat3,
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

    private val _seen = MutableLiveData<Boolean>()
    val seen: LiveData<Boolean>
        get() = _seen


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
        _seen.value = false
        checkIfTyping()
        refreshMessages()
        seenMessage()
        removedMessagesListener()
    }

    lateinit var seenLister: ValueEventListener
    lateinit var refreshMessagesListener: ValueEventListener
    lateinit var typingEventListener: ValueEventListener
    lateinit var removedChatListener:ValueEventListener

    fun removeListeners() {
        myRef.child("chats").child(chat.chatId).child(when (isTheSender) {
            true -> "r_seen"
            false -> "s_seen"
        }).removeEventListener(seenLister)

        myRef.child("chats").child(chat.chatId).child(
                when (isTheSender) {
                    true -> "r_typing"
                    false -> "s_typing"
                }
        ).removeEventListener(typingEventListener)
        myRef.child("chats").child(chat.chatId).child("messages").removeEventListener(refreshMessagesListener)
        myRef.child("chats").child(chat.chatId).child("deleted Messages").removeEventListener(removedChatListener)
    }


    fun seenMessage() {
        if (chat.chatId.isEmpty()) {
            return
        }

        seenLister = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val state = p0.getValue(Boolean::class.java)
                _seen.value = state
                Log.d("seendState", "$state")
            }
        }


        myRef.child("chats").child(chat.chatId).child(when (isTheSender) {
            true -> "r_seen"
            false -> "s_seen"
        }).addValueEventListener(seenLister)

    }


    fun refreshMessages() {
        if (chat.chatId.isEmpty()) {
            return
        }

        refreshMessagesListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                _error.postValue(application.getString(R.string.CHECK_INTERNET))

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("MessagesRepository", p0.toString())
                getData(p0)
            }
        }

        myRef.child("chats").child(chat.chatId).child("messages")
                .addValueEventListener(refreshMessagesListener)

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


            if (isTheSender) {
                myRef.child("chats").child(chat.chatId).child("s_seen").setValue(true)
            } else {
                myRef.child("chats").child(chat.chatId).child("r_seen").setValue(true)
            }
        }
    }


    fun sendMessage(msg: String? = null, pic: String? = null, long: Double? = null, latit: Double? = null) {
        if (!isConnected) {
            _error.postValue(application.getString(R.string.CHECK_INTERNET))
            return
        }

        val key = myRef.child("chats").child(chat.chatId).child("messages").push().key

        key?.let {
            val message = Message(key, msg, TimeMethods.getUTCdatetimeAsString(), userId, chat.chatId, pic, longitude = long, latitude = latit)
            myRef.child("chats").child(chat.chatId).child("messages").child(key).setValue(message)

            val ref = FirebaseDatabase.getInstance().reference

            val chatSend = ChatSend(chat.chatId, chat.sender, chat.with)

            ref.child("users").child(userId.toString()).child("chats").child(chat.chatId).setValue(chatSend)
            ref.child("users").child(userId.toString()).child("friends").child(chat.with.toString()).setValue(chatSend)

            chatSend.with = userId
            ref.child("users").child(chat.with.toString()).child("chats").child(chat.chatId).setValue(chatSend)
            ref.child("users").child(chat.with.toString()).child("friends").child(userId.toString()).setValue(chatSend)

            ref.child("chats").child(chat.chatId).child("lastMsg").setValue(msg)
            ref.child("chats").child(chat.chatId).child("datetimeLastMsg").setValue(TimeMethods.getUTCdatetimeAsString())

            ref.child("chats").child(chat.chatId).child("lastMsgId").setValue(it)


            if (isTheSender) {
                ref.child("chats").child(chat.chatId).child("r_seen").setValue(false)
            } else {
                ref.child("chats").child(chat.chatId).child("s_seen").setValue(false)
            }

            jobScope.launch {
                database.messageDao.insert(message)
            }

        }
    }


    fun checkIfTyping() {
        if (chat.chatId.isEmpty()) {
            return
        }

        typingEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                _error.postValue(application.getString(R.string.CHECK_INTERNET))

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.value?.let {
                    _typing.value = it as Boolean
                }
            }
        }


        myRef.child("chats").child(chat.chatId).child(
                when (isTheSender) {
                    true -> "r_typing"
                    false -> "s_typing"
                }
        )
                .addValueEventListener(typingEventListener)
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


    data class ChatSend(var chatId: String = "", var sender: Int? = 0, var with: Int = 0)


    fun sendAndCreateChat(msg: String? = null, pic: String? = null, long: Double? = null, latit: Double? = null) {
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
                val message = Message(messageKey, msg, TimeMethods.getUTCdatetimeAsString(), userId, chat.chatId, pic, long, latit)

                ref.child("chats").child(chat.chatId).child("lastMsgId").setValue(it)

                myRef.child("chats").child(chat.chatId).child("messages").child(messageKey).setValue(message)
                checkIfTyping()
                refreshMessages()
                seenMessage()
                removedMessagesListener()
                jobScope.launch {
                    database.messageDao.insert(message)
                }
            }

        }
    }



    fun removedMessagesListener() {
         removedChatListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (ds: DataSnapshot in p0.children) {
                    val dMsg = ds.getValue(RemovedMessage::class.java)

                    dMsg?.let {
                        if (dMsg.sender != userId) {
                            jobScope.launch {
                                it.messageId?.let {
                                    database.messageDao.deleteMessage(it)
                                    myRef.child("chats").child(chat.chatId).child("deleted Messages").child(it).removeValue()

                                }

                            }
                        }
                    }
                }


            }
        }

        myRef.child("chats").child(chat.chatId).child("deleted Messages").addValueEventListener(removedChatListener)
    }


    data class RemovedMessage(val sender: Int?=null, val messageId: String?=null)

    suspend fun removeMessage(messageId: String) {
        withContext(Dispatchers.IO) {
            val rm = RemovedMessage(userId, messageId)

            database.messageDao.deleteMessage(messageId)
            myRef.child("chats").child(chat.chatId).child("deleted Messages").child(messageId).setValue(rm)
            myRef.child("chats").child(chat.chatId).child("messages").child(messageId).child("text").setValue("deleted message")
            myRef.child("chats").child(chat.chatId).child("messages").child(messageId).child("pic").removeValue()
            myRef.child("chats").child(chat.chatId).child("messages").child(messageId).child("latitude").removeValue()
            myRef.child("chats").child(chat.chatId).child("messages").child(messageId).child("longitude").removeValue()

            myRef.child("chats").child(chat.chatId).child("lastMsgId").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val msgId=p0.getValue(String::class.java)
                    messageId.let {
                        if (it==messageId){
                            myRef.child("chats").child(chat.chatId).child("lastMsg").setValue("deleted message")
                        }
                    }
                }
            })
        }
    }


}

