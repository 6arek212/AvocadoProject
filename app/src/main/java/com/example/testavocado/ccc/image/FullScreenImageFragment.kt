package com.example.testavocado.ccc.image

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.testavocado.R
import com.example.testavocado.ccc.Chat3
import com.example.testavocado.ccc.ChatViewModelFactory
import com.example.testavocado.ccc.MessageFragmentArgs
import com.example.testavocado.ccc.MessageViewModel
import com.example.testavocado.databinding.FullScreenImageFragmentBinding

class FullScreenImageFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding:FullScreenImageFragmentBinding=DataBindingUtil.inflate(
                layoutInflater,
                R.layout.full_screen_image_fragment,
                container,
                false
        )

        val arg= requireNotNull(arguments)
        val imageUrl= FullScreenImageFragmentArgs.fromBundle(arg).imageUrl
        val application= requireNotNull(activity).application

        binding.setLifecycleOwner(this)
        binding.imageUrl=imageUrl


        return binding.root
    }




}
