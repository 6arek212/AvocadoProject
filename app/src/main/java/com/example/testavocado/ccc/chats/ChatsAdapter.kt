package com.example.testavocado.ccc.chats

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testavocado.ccc.Chat3
import com.example.testavocado.databinding.ChatItemBinding


class ChatsAdapter (val context:Context,val clickListener:(chat: Chat3, state:Boolean)->Unit): ListAdapter<Chat3, ChatsAdapter.ChatViewHolder>(
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
        fun bind(chat: Chat3, onClick:(chat:Chat3, state:Boolean)->Unit){
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



    companion object DiffCallback : DiffUtil.ItemCallback<Chat3>(){
        override fun areItemsTheSame(oldItem: Chat3, newItem: Chat3): Boolean {
            return oldItem.chatId==newItem.chatId
        }

        override fun areContentsTheSame(oldItem: Chat3, newItem: Chat3): Boolean {
            return oldItem==newItem
        }
    }
}