package com.example.testavocado.Chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import android.util.Log;

import com.example.testavocado.Models.Chat;
import com.example.testavocado.Models.Message;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

public class SQLiteMethods extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteMethods";

    Context context;


    public SQLiteMethods(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table if not exists messages_tbl(message_id int,chat_id int ,message_sender_id int , message_text text ,message_datetime datetime )";
        db.execSQL(query);

        query = "create table if not exists chats_tbl(chat_id int primary key," +
                "user_first_name text ," +
                "user_last_name text," +
                "user_profile_photo text," +
                "chat_sender_id int ," +
                "chat_receiver_id int ," +
                " chat_datetime_created datetime ," +
                "chat_messages_count int," +
                " chat_last_message_user_id int," +
                "chat_last_message text," +
                "chat_last_message_datetime datetime," +
                "sender_not_read int," +
                "receiver_not_read int)";
        db.execSQL(query);
    }


    public void cleatChatsTable() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "delete from chats_tbl";
        db.execSQL(query);
    }




    public void insertChat(Chat chat) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cn1 = new ContentValues();
        cn1.put("chat_id", chat.getChat_id());
        cn1.put("user_first_name", chat.getUser_first_name());
        cn1.put("user_last_name", chat.getUser_last_name());
        cn1.put("user_profile_photo", chat.getUser_profile_photo());
        cn1.put("chat_sender_id", chat.getChat_sender_id());
        cn1.put("chat_receiver_id", chat.getChat_receiver_id());
        cn1.put("chat_datetime_created", chat.getChat_datetime_created());
        cn1.put("chat_messages_count", chat.getChat_messages_count());
        cn1.put("chat_last_message_user_id", chat.getChat_last_message_user_id());
        cn1.put("chat_last_message", chat.getChat_last_message());
        cn1.put("chat_last_message_datetime", chat.getChat_last_message_datetime());
        cn1.put("sender_not_read", chat.getSender_not_read());
        cn1.put("receiver_not_read", chat.getReceiver_not_read());
        db.insert("chats_tbl", null, cn1);
    }


    public List<Chat> getChatsFromSQL() {
        Log.d(TAG, "getChatsFromSQL: ");

        String query = "select * from chats_tbl order by chat_last_message_datetime DESC ";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<Chat> chats = new ArrayList<>();


        if (cursor != null)
            if (cursor.moveToFirst())
                do {
                    Chat chat = new Chat();

                    chat.setChat_id(cursor.getInt(0));
                    chat.setUser_first_name(cursor.getString(1));
                    chat.setUser_last_name(cursor.getString(2));
                    chat.setUser_profile_photo(cursor.getString(3));
                    chat.setChat_sender_id(cursor.getInt(4));
                    chat.setChat_receiver_id(cursor.getInt(5));
                    chat.setChat_datetime_created(TimeMethods.convertDateTimeFormat(cursor.getString(6)));
                    chat.setChat_messages_count(cursor.getInt(7));
                    chat.setChat_last_message_user_id(cursor.getInt(8));
                    chat.setChat_last_message(cursor.getString(9));
                    chat.setChat_last_message_datetime(TimeMethods.convertDateTimeFormat(TimeMethods.getNewLocalDate(cursor.getString(10))));
                    chat.setSender_not_read(cursor.getInt(11));
                    chat.setReceiver_not_read(cursor.getInt(12));

                    chats.add(chat);

                } while (cursor.moveToNext());


        cursor.close();

        return chats;
    }






    public void getChatsFromDb(int user_id, final OnRequestingChatsListener listener) {
        ChatMethodsHandler.gettingChats(user_id, new ChatMethodsHandler.OnSGettingChatsListener() {
            @Override
            public void onSuccessListener(List<Chat> chats) {
                cleatChatsTable();
                for (int i = 0; i < chats.size(); i++) {
                    insertChat(chats.get(i));
                    //chats.get(i).setChat_last_message_datetime(TimeMethods);
                }

                listener.OnSuccessfullyGettingChats(getChatsFromSQL());
            }

            @Override
            public void onServerException(String ex) {
                listener.OnServerException(ex);

            }

            @Override
            public void onFailureListener(String ex) {
                listener.OnServerException(ex);

            }
        });


    }





    public void insetMessage(Message message, int chat_id) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put("message_id", message.getMessage_id());
        cv.put("chat_id", chat_id);
        cv.put("message_sender_id", message.getMessage_sender_id());
        cv.put("message_text", message.getMessage_text());
        cv.put("message_datetime", message.getMessage_datetime());

        db.insert("messages_tbl", null, cv);
    }


    public List<Message> GetMessages(int chat_id, int offset) {
        Log.d(TAG, "GetMessages: chat_id " + chat_id);

        String query = "select message_id,message_sender_id,message_text,message_datetime from messages_tbl where chat_id=" + chat_id + " order by message_datetime DESC " +
                "limit " + offset + ",20";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<Message> messages = new ArrayList<>();


        if (cursor != null)
            if (cursor.moveToFirst())
                do {
                    Message message = new Message();

                    message.setMessage_id(cursor.getInt(0));
                    message.setMessage_sender_id(cursor.getInt(1));
                    message.setMessage_text(cursor.getString(2));
                    Log.d(TAG, "GetMessages: time " + cursor.getString(3));

                    message.setMessage_datetime(TimeMethods.convertDateTimeFormat(TimeMethods.getNewLocalDate(cursor.getString(3))));

                   // message.setMessage_datetime(TimeMethods.convertDateTimeFormat());


                    messages.add(message);

                    Log.d(TAG, "GetMessages: sqlite getting messages " + message);

                } while (cursor.moveToNext());


        return messages;
    }






    public void OnGettingUnreadMessages(int user_id, final int chat_id, final OnRequestingUnrededMessagesListener listener) {

        ChatMethodsHandler.OnGettingUnreadedMessages(user_id, chat_id, new ChatMethodsHandler.OnGettingUnreadMessagesListener() {
            @Override
            public void onSuccessListener(List<Message> messages) {
                for (int k = messages.size() - 1; k >= 0; k--) {
                    Log.d(TAG, "OnSuccessfullyGettingUnrededMessages: getting unread message from the server " + messages.get(k));
                    messages.get(k).setMessage_datetime(messages.get(k).getMessage_datetime());
                    insetMessage(messages.get(k), chat_id);
                }

                listener.OnSuccessfullyGettingUnrededMessages(messages);
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "OnServerException: error getting unreaded messages " + ex);
                listener.OnServerException(ex);
            }

            @Override
            public void onFailureListener(String ex) {
                Log.d(TAG, "OnFailure: error getting unreaded messages " + ex);
                listener.OnFailure(ex);
            }
        });

    }








    public void sendMessage(final int chat_id, final int user_id, final String message_text, final String datetime) {

        ChatMethodsHandler.OnSendingMessage(chat_id, user_id, message_text, datetime, new ChatMethodsHandler.OnSendingMessageListener() {
            @Override
            public void onSuccessListener(int message_id) {
                Message message = new Message(message_id, user_id, message_text, datetime);
                insetMessage(message, chat_id);
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);

            }

            @Override
            public void onFailureListener(String ex) {
                Log.d(TAG, "OnServerException: " + ex);

            }
        });


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
