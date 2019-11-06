package com.example.testavocado.ccc.message

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.testavocado.R
import com.example.testavocado.Utils.HelpMethods
import com.example.testavocado.ccc.Chat3
import com.example.testavocado.databinding.MessageWithPicFragmentBinding

class MessageWithPicFragment : Fragment() {


    private lateinit var viewModel: MessageWithPicViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding:MessageWithPicFragmentBinding=DataBindingUtil.inflate(
                layoutInflater,
                R.layout.message_with_pic_fragment,
                container,
                false
        )

        val arg= requireNotNull(arguments)
        val imageUrl=MessageWithPicFragmentArgs.fromBundle(arg).imageUrl
        val chat=MessageWithPicFragmentArgs.fromBundle(arg).chat

        val application= requireNotNull(activity).application
        val userId=HelpMethods.checkSharedPreferencesForUserId(application)

        val viewModelFactory=MessageWithImageViewModelFactory(application,imageUrl,chat,userId)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MessageWithPicViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.viewModel=viewModel



        binding.close.setOnClickListener{
            findNavController().popBackStack()
        }

        viewModel.navigateBack.observe(this, Observer {
            if (it){
                findNavController().popBackStack()
                viewModel.navigateComplete()
            }
        })


        viewModel.error.observe(this, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show();
                viewModel.errorShowed()
            }
        })


        return binding.root
    }



    class MessageWithImageViewModelFactory(
            private val application: Application,
            private val imageUrl: String,
            private val chat:Chat3,
            private val userId:Int

    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MessageWithPicViewModel::class.java)) {
                return MessageWithPicViewModel(application,userId,imageUrl,chat) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

