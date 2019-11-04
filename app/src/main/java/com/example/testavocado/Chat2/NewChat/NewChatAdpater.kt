package com.example.chat.NewChat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartphone.database.Chat2
import com.example.testavocado.databinding.ChatItemBinding

class NewChatAdpater (val clickListener:(chat:Chat2)->Unit): ListAdapter<Chat2, NewChatAdpater.ChatViewHolder>(
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
        fun bind(chat: Chat2,onClick:(chat:Chat2)->Unit){
            binding.chat=chat
            binding.executePendingBindings()

            binding.layout.setOnClickListener{
                onClick(chat)
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