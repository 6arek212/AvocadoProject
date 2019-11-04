package com.example.chat.Chats

import android.content.Context
import android.text.method.Touch.onTouchEvent
import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartphone.database.Chat2
import com.example.smartphone.database.Message
import android.view.MotionEvent
import android.view.GestureDetector
import com.example.testavocado.databinding.ChatItemBinding


class ChatsAdapter (val context:Context,val clickListener:(chat:Chat2,state:Boolean)->Unit): ListAdapter<Chat2, ChatsAdapter.ChatViewHolder>(
    DiffCallback
){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item=getItem(position)
        holder.bind(item,clickListener)
    }




    class ChatViewHolder private constructor(val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(chat: Chat2,onClick:(chat:Chat2,state:Boolean)->Unit){
            binding.chat=chat
            binding.executePendingBindings()



            binding.layout.setOnLongClickListener{
                onClick(chat,false)
                true
            }

            binding.layout.setOnClickListener(){
                onClick(chat,true)
            }

        }





        companion object{
            fun from(parent:ViewGroup): ChatViewHolder {
                val inflater= LayoutInflater.from(parent.context)
                val binding = ChatItemBinding.inflate(inflater,parent,false)
                return ChatViewHolder(binding)
            }
        }
    }



    companion object DiffCallback : DiffUtil.ItemCallback<Chat2>(){
        override fun areItemsTheSame(oldItem: Chat2, newItem: Chat2): Boolean {
            return oldItem.chatId==newItem.chatId
        }

        override fun areContentsTheSame(oldItem: Chat2, newItem: Chat2): Boolean {
            return oldItem==newItem
        }
    }
}