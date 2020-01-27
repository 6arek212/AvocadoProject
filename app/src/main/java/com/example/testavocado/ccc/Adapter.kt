package com.example.testavocado.ccc

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testavocado.databinding.*

const val ME_TYPE = 1
const val ME_TYPE_WITH_IMAGE = 2
const val ME_TYPE_WITH_LOCATION = 3
const val ME_TYPE_WITH_CONTACT = 4

const val OTHER_TYPE = 5
const val OTHER_TYPE_WITH_IMAGE = 6
const val OTHER_TYPE_WITH_LOCATION = 7
const val OTHER_TYPE_WITH_CONTACT = 8

class Adapter (val userId:Int, val viewImage:(imageUrl:String?,imageView:ImageView,position:Int)->Unit, val viewLocation:(longtit:Double?,latit:Double?)->Unit,
               val removeMessage:(messageId:String)->Unit,val openDialer:(number:String?)->Unit) : ListAdapter<Message, RecyclerView.ViewHolder>(
    DiffCallback
) {


    override fun getItemViewType(position: Int): Int {
        val item=getItem(position)

        if(item.senderId==userId && item.pic==null && item.latitude==null&& item.longitude==null && item.number==null)
            return ME_TYPE
        else if(item.senderId==userId && item.pic!=null){
            return ME_TYPE_WITH_IMAGE
        }
        else if (item.senderId==userId && item.latitude!=null && item.longitude!=null){
            return ME_TYPE_WITH_LOCATION
        }
        else if(item.senderId==userId && item.number!=null){
            return ME_TYPE_WITH_CONTACT
        }

        else if(item.senderId!=userId && item.pic==null && item.latitude==null&& item.longitude==null && item.number==null){
            return OTHER_TYPE
        }
        else if(item.senderId!=userId && item.pic!=null)
            return OTHER_TYPE_WITH_IMAGE

        else if(item.senderId!=userId && item.latitude!=null && item.longitude!=null){
            return OTHER_TYPE_WITH_LOCATION
        }
        else{
            return OTHER_TYPE_WITH_CONTACT
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ME_TYPE->MessageRightViewHolder.from(parent)
            ME_TYPE_WITH_IMAGE->MessageRightWithPicViewHolder.from(parent)
            ME_TYPE_WITH_LOCATION->MessageRightWithLocationViewHolder.from(parent)
            ME_TYPE_WITH_CONTACT->MessageRightWithContactViewHolder.from(parent)
            OTHER_TYPE->MessageLeftViewHolder.from(parent)
            OTHER_TYPE_WITH_IMAGE->MessageLeftWithPicViewHolder.from(parent)
            OTHER_TYPE_WITH_LOCATION->MessageLeftWithLocationViewHolder.from(parent)
            OTHER_TYPE_WITH_CONTACT->MessageLeftWithContactViewHolder.from(parent)
            else->MessageRightViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(holder){
            is MessageRightViewHolder-> holder.bind(item,removeMessage)
            is MessageLeftViewHolder-> holder.bind(item)
            is MessageRightWithPicViewHolder->holder.bind(item,position,viewImage,removeMessage)
            is MessageLeftWithPicViewHolder->holder.bind(item,position,viewImage)
            is MessageRightWithLocationViewHolder->holder.bind(item,viewLocation,removeMessage)
            is MessageLeftWithLocationViewHolder->holder.bind(item,viewLocation)
            is MessageRightWithContactViewHolder->holder.bind(item,removeMessage,openDialer)
            is MessageLeftWithContactViewHolder->holder.bind(item,openDialer)
        }
    }





    class MessageRightViewHolder private constructor(val binding: LayoutChatRight2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,removeMessage:(messageId:String)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            binding.layout.setOnLongClickListener{
                removeMessage(message._id)
                false
            }
        }

        companion object {
            fun from(parent: ViewGroup): MessageRightViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatRight2Binding.inflate(inflater, parent, false)
                return MessageRightViewHolder(binding)
            }
        }
    }


    class MessageRightWithContactViewHolder private constructor(val binding: LayoutChatRightWithcontactBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,removeMessage:(messageId:String)->Unit,openDialer:(number:String?)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            binding.layout.setOnLongClickListener{
                removeMessage(message._id)
                false
            }

            binding.layout.setOnClickListener{
                openDialer(message.number)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MessageRightWithContactViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatRightWithcontactBinding.inflate(inflater, parent, false)
                return MessageRightWithContactViewHolder(binding)
            }
        }
    }


    class MessageRightWithPicViewHolder private constructor(val binding: LayoutChatRightWithimageBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,position: Int,viewImage:(imageUrl:String?,imageView:ImageView,position:Int)->Unit,removeMessage:(messageId:String)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            ViewCompat.setTransitionName(binding.imageView3, "Test_$position")

            binding.imageView3.setOnClickListener{
                viewImage(message.pic,it as @kotlin.ParameterName(name = "imageView") ImageView,position)
            }

            binding.layout.setOnLongClickListener{
                removeMessage(message._id)
                false
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



    class MessageRightWithLocationViewHolder private constructor(val binding: LayoutChatRightWithlocationBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,viewLocation:(longtit:Double?,latit:Double?)->Unit,removeMessage:(messageId:String)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            binding.location.setOnClickListener{
                viewLocation(message.longitude,message.latitude)
            }

            binding.layout.setOnLongClickListener{
                removeMessage(message._id)
                false
            }
        }

        companion object {
            fun from(parent: ViewGroup): MessageRightWithLocationViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatRightWithlocationBinding.inflate(inflater, parent, false)
                return MessageRightWithLocationViewHolder(binding)
            }
        }
    }



    class MessageLeftWithLocationViewHolder private constructor(val binding: LayoutChatLeftWithlocationBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,viewLocation:(longtit:Double?,latit:Double?)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            binding.location.setOnClickListener{
                viewLocation(message.longitude,message.latitude)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MessageLeftWithLocationViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatLeftWithlocationBinding.inflate(inflater, parent, false)
                return MessageLeftWithLocationViewHolder(binding)
            }
        }
    }


    class MessageLeftWithPicViewHolder private constructor(val binding: LayoutChatLeftWithimageBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,position: Int,viewImage:(imageUrl:String?,imageView:ImageView,position:Int)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            ViewCompat.setTransitionName(binding.imageView4, "Test_$position")

            binding.imageView4.setOnClickListener{
                viewImage(message.pic,it as @kotlin.ParameterName(name = "imageView") ImageView,position)
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


    class MessageLeftWithContactViewHolder private constructor(val binding: LayoutChatLeftWithcontactBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,openDialer:(number:String?)->Unit) {
            binding.message = message
            binding.executePendingBindings()

            binding.layout.setOnClickListener{
                openDialer(message.number)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MessageLeftWithContactViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutChatLeftWithcontactBinding.inflate(inflater, parent, false)
                return MessageLeftWithContactViewHolder(binding)
            }
        }
    }



    class MessageLeftViewHolder private constructor(val binding: LayoutChatLeft2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            Log.d("messagemessage","$message")
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