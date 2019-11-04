package com.example.chat

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartphone.database.Chat
import com.example.smartphone.database.Chat2
import com.example.testavocado.R
import com.example.testavocado.Utils.HelpMethods
import com.example.testavocado.databinding.MessageFragmentBinding


class MessageFragment : Fragment() {



    private lateinit var viewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: MessageFragmentBinding =DataBindingUtil.inflate(
            layoutInflater,
            R.layout.message_fragment,
            container,
            false
        )
        val arg= requireNotNull(arguments)
        val chat=MessageFragmentArgs.fromBundle(arg).chat

        val application= requireNotNull(activity).application
        val viewModelFactory=ChatViewModelFactory(application,chat)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MessageViewModel::class.java)
        binding.typing.visibility=View.GONE



        binding.setLifecycleOwner(this)
        binding.viewModel=viewModel
        binding.chat=chat



        val adapter=Adapter(HelpMethods.checkSharedPreferencesForUserId(application))
        val ln=LinearLayoutManager(context)
        ln.reverseLayout=true
        binding.recyclerView.layoutManager=ln
        binding.recyclerView.adapter=adapter
        binding.recyclerView.scrollToPosition(0)


        viewModel.error.observe(this, Observer {
            Log.d("errorMeSSAGE","$it")
                if (!it.isNullOrEmpty()){
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show();
                    viewModel.showErrorComplete()
                }
        })


        //close click
        binding.close.setOnClickListener{
            findNavController().navigate(MessageFragmentDirections.actionMessageFragmentToChatsFragment())
        }


        //adding messages
        viewModel.messages.observe(this, Observer {
            adapter.submitList(it)
        })



        //scroll to the top
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                binding.recyclerView.scrollToPosition(0)
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                binding.recyclerView.scrollToPosition(0)
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                binding.recyclerView.scrollToPosition(0)
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.recyclerView.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                binding.recyclerView.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                binding.recyclerView.scrollToPosition(0)
            }
        })



        viewModel.clearText.observe(this, Observer {
            if(it){
                binding.text.setText("")
                viewModel.clearTextComplete()
            }
        })


        viewModel.typing.observe(this, Observer {
            Log.d("typingtyping","$it")
            if (it){
                binding.typing.visibility=View.VISIBLE
            }else
            {
                binding.typing.visibility=View.GONE
            }
        })


        viewModel.showMessageEmptyText.observe(this, Observer {
            if(it){
                Toast.makeText(context, getString(R.string.cant_send_empty_message), Toast.LENGTH_SHORT).show()
                viewModel.messageShowComplete()
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewModel.textToSend.observe(this, Observer {
            if(it.isNullOrEmpty()){
                viewModel.typing(false)
            }
            else{
                viewModel.typing(true)
            }
        })
    }

}


class ChatViewModelFactory(
    private val application: Application,
    private val chat: Chat2

) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            return MessageViewModel(application,chat) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}