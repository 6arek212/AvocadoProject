package com.example.testavocado.ccc

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.*
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize


@Keep
data class Chat(
    @ColumnInfo(name = "chat_id")
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val sender: Int,
    val receiver: Int,
    val s_typing: Boolean=false,
    val r_typing: Boolean=false,
    val datetime:String

)


@Entity(tableName = "chats_tbl")
@Parcelize
data class Chat3(val name:String?="",
                 @PrimaryKey(autoGenerate = false)
                 var chatId:String="", var sender:Int?=0, val with:Int=0, val profileImg:String?="", val lastMsgDatetime:String?="", val lastMsg:String?=""):Parcelable



@Keep
@IgnoreExtraProperties
@Entity(tableName = "messages_tbl")
data class Message(@ColumnInfo(name = "message_id")
                    @PrimaryKey(autoGenerate = false)
                    val _id: String="",
                   val text: String?="",
                   val datetime: String="",
                   val senderId: Int=0,
                   val chat_id:String="",
                   val pic:String?=null)










