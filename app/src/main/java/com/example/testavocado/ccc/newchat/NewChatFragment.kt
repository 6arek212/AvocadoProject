package com.example.testavocado.ccc.newchat

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testavocado.R
import com.example.testavocado.databinding.NewChatFragmentBinding

class NewChatFragment : Fragment() {

    private lateinit var viewModel: NewChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: NewChatFragmentBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.new_chat_fragment,
            container,
            false
        )
        binding.setLifecycleOwner(this)
        val application= requireNotNull(activity).application
        val viewModelFactory= ChatViewModelFactory(application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(NewChatViewModel::class.java)



        val adapter=NewChatAdpater{
            findNavController().navigate(NewChatFragmentDirections.actionNewChatFragmentToMessageFragment(it))
        }


        viewModel.chats.observe(this, Observer {
            adapter.submitList(it)
        })



        binding.list.adapter=adapter

        return binding.root
    }


}
class ChatViewModelFactory(
    private val application: Application

) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewChatViewModel::class.java)) {
            return NewChatViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}