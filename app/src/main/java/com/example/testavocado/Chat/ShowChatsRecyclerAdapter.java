package com.example.testavocado.Chat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.testavocado.Models.Chat;
import com.example.testavocado.Models.ChatUser;
import com.example.testavocado.R;

import java.util.ArrayList;

import java.util.List;

import static com.example.testavocado.Chat.ChatActivity.SQL_VER;


public class ShowChatsRecyclerAdapter extends RecyclerView.Adapter<ShowChatsRecyclerAdapter.ViewHolder> {


    public void OnUpdate() {
        Log.d(TAG, "OnUpdate: " + chat_index);
        if (chats.get(chat_index).getChat_sender_id() == current_user_id) {
            chats.get(chat_index).setSender_not_read(0);
        } else {
            chats.get(chat_index).setReceiver_not_read(0);
        }
        notifyDataSetChanged();
    }

    private static final String TAG = "ShowChatsRecyclerAdapte";

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private List<Chat> chats = new ArrayList<Chat>();

    private Context context;
    private int current_user_id;
    private static int chat_index;

    public ShowChatsRecyclerAdapter(Context context, int current_user_id) {
        this.context = context;
        this.current_user_id = current_user_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chat_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat chat = chats.get(i);
        viewBinderHelper.bind(viewHolder.mSwipeLayout, String.valueOf(chat.getChat_id()));
        viewHolder.mUserName.setText(chat.getUser_first_name() + " " + chat.getUser_last_name());
        viewHolder.mLastMessage.setText(chat.getChat_last_message());
        viewHolder.mTime.setText(chat.getChat_last_message_datetime());
        Log.d(TAG, "onBindViewHolder: chat sender_id : " + chat.getChat_sender_id() + "     chat reciver_id : " + chat.getChat_receiver_id());


        viewHolder.mNot_Read.setText((chat.getChat_sender_id() == current_user_id ? chat.getSender_not_read() : chat.getReceiver_not_read()) + "");
        deleteChat(viewHolder.delete,i);

        onClick(viewHolder.frameLayout, i);

    }


    private void onClick(FrameLayout frameLayout, final int index) {
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                chat_index = index;

                ChatUser user = new ChatUser();
                user.setChat_id(chats.get(index).getChat_id());
                user.setUser_first_name(chats.get(index).getUser_first_name());
                user.setUser_last_name(chats.get(index).getUser_last_name());
                user.setProfile_photo(chats.get(index).getUser_profile_photo());

                int user_id = (chats.get(index).getChat_sender_id() == current_user_id ? chats.get(index).getChat_sender_id() :
                        chats.get(index).getChat_receiver_id());

                user.setUser_id(user_id);

                ConversationFragment fragment = new ConversationFragment();
                fragment.chatUser = user;
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainLayoutChatActivity, fragment);
                transaction.addToBackStack(context.getString(R.string.conversation_fragment));
                transaction.commit();
            }
        });

    }


    public void addChat(Chat chat) {
        chats.add(chat);
        notifyDataSetChanged();
    }

    public void addChatSet(List<Chat> chat) {
        chats = chat;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return chats.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mProfileImage;
        TextView mUserName, mTime, mLastMessage, mNot_Read;
        SwipeRevealLayout mSwipeLayout;
        FrameLayout frameLayout,delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mNot_Read = itemView.findViewById(R.id.not_read);
            frameLayout = itemView.findViewById(R.id.front_layout);
            mProfileImage = itemView.findViewById(R.id.profileImage);
            mUserName = itemView.findViewById(R.id.userName);
            mSwipeLayout = itemView.findViewById(R.id.swipe_layout);
            mTime = itemView.findViewById(R.id.time);
            mLastMessage = itemView.findViewById(R.id.lastMessage);
            delete=itemView.findViewById(R.id.delete_layout);
        }
    }



    private void deleteChat(FrameLayout delete, final int adapterPosition) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: current_user_id: "+current_user_id+"  chat iD "+chats.get(adapterPosition).getChat_id());
                ChatMethodsHandler.OnDeletingChat(current_user_id, chats.get(adapterPosition).getChat_id(), new ChatMethodsHandler.OnDeletingChatListener() {
                    @Override
                    public void onDeleted() {
                        Log.d(TAG, "onDeleted: deleted");
                        SQLiteMethods sqLiteMethods=new SQLiteMethods(context,"db1",null,SQL_VER);
                        sqLiteMethods.deleteChat(chats.get(adapterPosition).getChat_id());
                        chats.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                    }

                    @Override
                    public void onServerException(String ex) {
                        Log.d(TAG, "onServerException: "+ex);

                    }

                    @Override
                    public void onFailureListener(String ex) {
                        Log.d(TAG, "onFailureListener: "+ex);

                    }
                });

            }
        });
    }
}
