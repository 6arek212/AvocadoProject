package com.example.chat.Chats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import com.example.chat.Friend
import com.example.smartphone.database.Chat
import com.example.smartphone.database.Chat2
import com.example.smartphone.database.mDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatRepo(val database: mDatabase, val userId: Int) {

    val chats = database.chatDao.getChats()

    val job = Job()
    val jobScope = CoroutineScope(job + Dispatchers.IO)

    val chatsToStart = MutableLiveData<List<Chat2>>()


    init {
        getUsersAndChats()
        getUsersA()
    }


    //new chat
    fun getUsersA() {
        val ref = FirebaseDatabase.getInstance().reference

        var i = 0

        ref.child("users").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("ChatRepo2", p0.toString())


                jobScope.launch {

                    val chatList = ArrayList<Chat2>()

                    for (ds: DataSnapshot in p0.child(userId.toString()).child("friends").children) {
                        val chatId = ds.child("chatId").getValue(String::class.java)
                        val sender = ds.child("sender").getValue(Int::class.java)
                        val with = ds.child("with").getValue(Int::class.java)
                        val lastMsgDate=ds.child("lastMsgDate").getValue(String::class.java)

                        val profileImage=p0.child(with.toString()).child("profilePic").getValue(String::class.java)


                        with?.let {
                            val name =
                                p0.child(with.toString()).child("name").getValue(String::class.java)
                            Log.d("ChatRepo2", "${chatId}  ${sender}  $with $name  profileImage $profileImage")

                            val chat2:Chat2

                            if (chatId != null) {
                                 chat2 = Chat2(name!!, chatId, sender!!, with,profileImage,lastMsgDate)
                            } else {
                                 chat2 = Chat2(name!!, with = with,profileImg = profileImage)
                            }

                            chatList.add(chat2)
                        }

                    }
                    chatsToStart.postValue(chatList)
                }
            }
        })

    }




    //chats made
    fun getUsersAndChats() {
        val ref = FirebaseDatabase.getInstance().reference

        var i = 0

        ref.addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("ChatRepoooooo", p0.toString())

                jobScope.launch {
                    database.chatDao.clear()

                    val chatList = ArrayList<Chat2>()

                    for (ds: DataSnapshot in p0.child("users").child(userId.toString()).child("chats").children) {

                        val chatId = ds.child("chatId").getValue(String::class.java)
                        val sender = ds.child("sender").getValue(Int::class.java)
                        val with = ds.child("with").getValue(Int::class.java)
                        val profileImage=p0.child("users").child(with.toString()).child("profilePic").getValue(String::class.java)



                        val lastMsgDate=p0.child("chats").child(chatId.toString()).child("datetimeLastMsg").getValue(String::class.java)
                        val lastMsg=p0.child("chats").child(chatId.toString()).child("lastMsg").getValue(String::class.java)
                        val name = p0.child("users").child(with.toString()).child("name").getValue(String::class.java)

                        with?.let {
                            val chat2 = Chat2(name, chatId!!, sender, with,profileImage,lastMsgDate,lastMsg)
                            Log.d("ChatRepoooooo22222", ds.toString()+"  $chatId   $sender $with  $chat2")

                            chatList.add(chat2)
                        }
                    }
                    database.chatDao.insertAll(chatList)
                }
            }
        })

    }






    fun removeChat(chatId:String,with:Int){
        val ref = FirebaseDatabase.getInstance().reference
        ref.child("users").child(userId.toString()).child("chats").child(chatId).removeValue()
        ref.child("users").child(userId.toString()).child("friends").child(with.toString()).child(chatId).removeValue()

        jobScope.launch {
            database.messageDao.clearMessages(chatId)
        }
    }


}