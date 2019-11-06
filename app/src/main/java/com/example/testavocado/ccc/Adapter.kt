package com.example.testavocado.ccc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testavocado.databinding.*

const val ME_TYPE = 1
const val ME_TYPE_WITH_IMAGE = 2
const val OTHER_TYPE = 3
const val OTHER_TYPE_WITH_IMAGE = 4

class Adapter (val userId:Int, val viewImage:(imageUrl:String?)->Unit) : ListAdapter<Message, RecyclerView.ViewHolder>(
    DiffCallback
) {


    override fun getItemViewType(position: Int): Int {
        val item=getItem(position)

        if(item.senderId==userId && item.pic==null)
            return ME_TYPE
        else if(item.senderId==userId && item.pic!=null){
            return ME_TYPE_WITH_IMAGE
        }
        else if(item.senderId!=userId && item.pic==null){
            return OTHER_TYPE
        }
        else
            return OTHER_TYPE_WITH_IMAGE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ME_TYPE->MessageRightViewHolder.from(parent)
            ME_TYPE_WITH_IMAGE->MessageRightWithPicViewHolder.from(parent)
            OTHER_TYPE->MessageLeftViewHolder.from(parent)
            OTHER_TYPE_WITH_IMAGE->MessageLeftWithPicViewHolder.from(parent)
            else->MessageRightViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(holder){
            is MessageRightViewHolder-> holder.bind(item)
            is MessageLeftViewHolder-> holder.bind(item)
            is MessageRightWithPicViewHolder->holder.bind(item,viewImage)
            is MessageLeftWithPicViewHolder->holder.bind(item,viewImage)
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

    class MessageRightWithPicViewHolder private constructor(val binding: LayoutChatRightWithimageBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,viewImage:(imageUrl:String?)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            binding.imageView3.setOnClickListener{
                viewImage(message.pic)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MessageRightWithPicViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatRightWithimageBinding.inflate(inflater, parent, false)
                return MessageRightWithPicViewHolder(binding)
            }
        }
    }


    class MessageLeftWithPicViewHolder private constructor(val binding: LayoutChatLeftWithimageBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,viewImage:(imageUrl:String?)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            binding.imageView4.setOnClickListener{
                viewImage(message.pic)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MessageLeftWithPicViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatLeftWithimageBinding.inflate(inflater, parent, false)
                return MessageLeftWithPicViewHolder(binding)
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