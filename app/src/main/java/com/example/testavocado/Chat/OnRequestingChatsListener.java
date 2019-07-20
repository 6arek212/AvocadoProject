package com.example.testavocado.Chat;

import com.example.testavocado.Models.Chat;

import java.util.List;

public interface OnRequestingChatsListener {
    public void OnSuccessfullyGettingChats(List<Chat> chats);
    public void OnServerException(String exception);
    public void OnFailure(String exception);
}
