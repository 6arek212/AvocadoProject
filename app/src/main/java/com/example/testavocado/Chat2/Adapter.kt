package com.example.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartphone.database.Message
import com.example.testavocado.databinding.LayoutChatLeft2Binding
import com.example.testavocado.databinding.LayoutChatRight2Binding
import com.example.testavocado.databinding.MessageItemBinding

const val ME_TYPE = 1
const val OTHER_TYPE = 2

class Adapter (val userId:Int) : ListAdapter<Message, RecyclerView.ViewHolder>(
    DiffCallback
) {


    override fun getItemViewType(position: Int): Int {
        if(getItem(position).senderId==userId)
            return ME_TYPE
        else
            return OTHER_TYPE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ME_TYPE->MessageRightViewHolder.from(parent)
            OTHER_TYPE->MessageLeftViewHolder.from(parent)
            else->MessageViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(holder){
            is MessageViewHolder-> holder.bind(item)
            is MessageRightViewHolder-> holder.bind(item)
            is MessageLeftViewHolder-> holder.bind(item)
        }
    }


    class MessageViewHolder private constructor(val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MessageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = MessageItemBinding.inflate(inflater, parent, false)
                return MessageViewHolder(binding)
            }
        }
    }


    class MessageRightViewHolder private constructor(val binding: LayoutChatRight2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MessageRightViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatRight2Binding.inflate(inflater, parent, false)
                return MessageRightViewHolder(binding)
            }
        }
    }



    class MessageLeftViewHolder private constructor(val binding: LayoutChatLeft2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MessageLeftViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatLeft2Binding.inflate(inflater, parent, false)
                return MessageLeftViewHolder(binding)
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}