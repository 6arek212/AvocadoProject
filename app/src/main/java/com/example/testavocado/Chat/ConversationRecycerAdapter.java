package com.example.testavocado.Chat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testavocado.Models.Message;
import com.example.testavocado.R;

import java.util.ArrayList;
import java.util.List;

public class ConversationRecycerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ConversationRecycerAdap";

   private static List<Message> messages=new ArrayList<Message>();
   private int current_user_id;


    public ConversationRecycerAdapter(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void addMessage(Message message){
       messages.add(message);
       notifyDataSetChanged();
       Log.d(TAG, "addMessage: added a message "+message);
   }

    public void addMessageTop(Message message){
        messages.add(0,message);
        notifyDataSetChanged();
        Log.d(TAG, "addMessageTop: recyclerview updated with new message ");
    }

    public void setMessagesList(List<Message> messagesList){
        messages=messagesList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=null;

        switch (i){
            case 0:
                Log.d(TAG, "onCreateViewHolder: right");
                view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chat_right,viewGroup,false);
                break ;

            default:
                Log.d(TAG, "onCreateViewHolder: left");
                view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chat_left,viewGroup,false);
                break;
        }


        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).mMessage_text.setText(messages.get(i).getMessage_text());
        ((ViewHolder) viewHolder).mTime.setText(messages.get(i).getMessage_datetime());
    }




    @Override
    public int getItemCount() {
        return messages.size();
    }




    @Override
    public int getItemViewType(int position) {
        return ((messages.get(position).getMessage_sender_id()==current_user_id) ? 0 :1 );
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mMessage_text,mTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessage_text=itemView.findViewById(R.id.messageText);
            mTime=itemView.findViewById(R.id.time);
        }
    }


}
