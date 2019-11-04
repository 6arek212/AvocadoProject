package com.example.smartphone.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.chat.Chats.ChatRepo
import com.google.gson.Gson


@Dao
interface ChatDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(chats: List<Chat2>)

    @Query("select * from chats_tbl")
    fun getChats():LiveData<List<Chat2>>


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
}


@Database(entities = [Chat2::class,Message::class],version = 9)
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