package com.example.chat.Chats

import android.app.AlertDialog
import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.ChatViewModelFactory
import com.example.chat.MessageViewModel
import com.example.smartphone.database.Chat2
import com.example.testavocado.R
import com.example.testavocado.databinding.ChatsFragmentBinding


class ChatsFragment : Fragment() {


    private lateinit var viewModel: ChatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ChatsFragmentBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.chats_fragment,
            container,
            false
        )
        binding.setLifecycleOwner(this)
        val application= requireNotNull(activity).application
        val viewModelFactory= ChatModelFactory(application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(ChatsViewModel::class.java)


        binding.add.setOnClickListener{
            findNavController().navigate(ChatsFragmentDirections.actionChatsFragmentToNewChatFragment())
        }



        val adapter=ChatsAdapter(application.applicationContext){
            chat, state ->
            if (state){
                findNavController().navigate(ChatsFragmentDirections.actionChatsFragmentToMessageFragment(chat))
            }
            else{
                alert(chat)
            }
        }

        binding.chatList.layoutManager=LinearLayoutManager(context)
        binding.chatList.adapter=adapter



        viewModel.chats.observe(this, Observer {
            adapter.submitList(it)
        })


        return binding.root
    }


    fun alert(chat:Chat2) {
        val alertDialog = AlertDialog.Builder(context,R.style.AlertDialogStyle)

        alertDialog.apply {
            setTitle(getString(R.string.delete_chat))
            setMessage(getString(R.string.you_sure_delete_chat) + chat.name)

            setPositiveButton("yes"){ dialog, which ->
                viewModel.removeChat(chat)
            }

            setNegativeButton("No", null)
            setIcon(android.R.drawable.ic_dialog_alert)


            show()
        }
    }

}
class ChatModelFactory(
    private val application: Application

) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatsViewModel::class.java)) {
            return ChatsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}