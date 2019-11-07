package com.example.testavocado.ccc.chats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.testavocado.ccc.Chat3
import com.example.testavocado.ccc.mDatabase

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

    val chatsToStart = MutableLiveData<List<Chat3>>()
    val myRef = FirebaseDatabase.getInstance().getReference()


    init {
        getUsersAndChats()
        getUsersA()
    }


    lateinit var getChatsMade:ValueEventListener
    lateinit var getNewChats : ValueEventListener


    fun onClear(){
        myRef.child("users").removeEventListener(getNewChats)
        myRef.removeEventListener(getChatsMade)
    }



    //new chat
    fun getUsersA() {
        getNewChats=object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("ChatRepo2", p0.toString())


                jobScope.launch {

                    val chatList = ArrayList<Chat3>()

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

                            val chat2:Chat3

                            if (chatId != null) {
                                chat2 = Chat3(name!!, chatId, sender!!, with,profileImage,lastMsgDate)
                            } else {
                                chat2 = Chat3(name!!, with = with,profileImg = profileImage)
                            }

                            chatList.add(chat2)
                        }

                    }
                    chatsToStart.postValue(chatList)
                }            }
        }
        Log.d("CHAT REPO","$getNewChats   $myRef")

        myRef.child("users").addValueEventListener(getNewChats)

    }




    //chats made
    fun getUsersAndChats() {
        getChatsMade=object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                jobScope.launch {
                    val chatList = ArrayList<Chat3>()

                    for (ds: DataSnapshot in p0.child("users").child(userId.toString()).child("chats").children) {

                        val chatId = ds.child("chatId").getValue(String::class.java)
                        val sender = ds.child("sender").getValue(Int::class.java)
                        val with = ds.child("with").getValue(Int::class.java)
                        val profileImage=p0.child("users").child(with.toString()).child("profilePic").getValue(String::class.java)



                        val lastMsgDate=p0.child("chats").child(chatId.toString()).child("datetimeLastMsg").getValue(String::class.java)
                        val lastMsg=p0.child("chats").child(chatId.toString()).child("lastMsg").getValue(String::class.java)
                        val name = p0.child("users").child(with.toString()).child("name").getValue(String::class.java)

                        with?.let {
                            val chat2 = Chat3(name, chatId!!, sender, with,profileImage,lastMsgDate,lastMsg)
                            Log.d("ChatRepoooooo22222", " ${ds.toString()}")

                            chatList.add(chat2)
                        }
                    }
                    database.chatDao.insertAll(chatList)
                }            }
        }


        myRef.addValueEventListener(getChatsMade)

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