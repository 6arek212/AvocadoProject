package com.example.testavocado.ccc

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.Gson


@Dao
interface ChatDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(chats: List<Chat3>)

    @Query("select * from chats_tbl order by lastMsgDatetime DESC")
    fun getChats():LiveData<List<Chat3>>


    @Query("delete from chats_tbl where chatId=:id")
    fun deleteChatById(id:String)

    @Query("delete from chats_tbl")
    fun clear()
}



@Dao
interface MessagesDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<Message>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages: Message)


    @Query("select * from messages_tbl where chat_id=:chat order by datetime DESC")
    fun getMessages(chat:String):LiveData<List<Message>>

    @Query("delete from messages_tbl where chat_id=:chatId")
    fun clearMessages(chatId:String)

    @Query("delete from messages_tbl")
    fun clear()

    @Query("update messages_tbl set text='deleted message',longitude=null,latitude=null,pic=null,number=null where message_id=:messageId")
    fun deleteMessage(messageId:String)
}


@Database(entities = [Chat3::class,Message::class],version = 19)
@TypeConverters(Converters::class)
abstract class mDatabase : RoomDatabase(){

    abstract val chatDao:ChatDao
    abstract val messageDao:MessagesDao


    companion object{
        @Volatile
        private var INSTANCE:mDatabase?=null

        fun getInstance(context: Context):mDatabase{
            synchronized(this){
                var instance= INSTANCE

                if(instance==null){
                    instance=Room.databaseBuilder(
                        context.applicationContext,
                        mDatabase::class.java,
                        "db"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
                INSTANCE=instance

                return instance
            }
        }
    }


}


class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        val list = objects.toList()
        return list
    }
}