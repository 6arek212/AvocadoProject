package com.example.testavocado.Chat;

import com.example.testavocado.Models.Chat;
import com.example.testavocado.Models.ChatUser;

import java.util.List;

public interface OnRequestingUserAndChatsListener {
    public void OnSuccessfullyGettingUsersAndChats(List<ChatUser> chats);
    public void OnServerException(String exception);
    public void OnFailure(String exception);
}
