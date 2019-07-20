package com.example.testavocado.Chat;

public interface OnCreatingChat {
    public void OnSuccess(int chat_id,int message_id);
    public void OnServerException(String exception);
    public void OnFailure(String exception);
}
