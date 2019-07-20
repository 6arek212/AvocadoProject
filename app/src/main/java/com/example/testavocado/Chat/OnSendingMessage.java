package com.example.testavocado.Chat;

import com.example.testavocado.Models.Message;

import java.util.List;

public interface OnSendingMessage {
    public void OnSuccess(int message_id);
    public void OnServerException(String exception);
    public void OnFailure(String exception);
}
