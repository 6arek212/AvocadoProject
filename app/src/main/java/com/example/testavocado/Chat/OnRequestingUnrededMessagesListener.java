package com.example.testavocado.Chat;

import com.example.testavocado.Models.Message;

import java.util.List;

public interface OnRequestingUnrededMessagesListener {
    public void OnSuccessfullyGettingUnrededMessages(List<Message> messages);
    public void OnServerException(String exception);
    public void OnFailure(String exception);
}
