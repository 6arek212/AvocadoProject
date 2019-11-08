package com.example.testavocado.ccc.image

import android.app.Application
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater

import com.example.testavocado.R
import com.example.testavocado.ccc.Chat3
import com.example.testavocado.ccc.MessageFragmentArgs
import com.example.testavocado.ccc.MessageViewModel
import com.example.testavocado.databinding.FullScreenImageFragmentBinding
import kotlinx.android.synthetic.main.full_screen_image_fragment.*

class FullScreenImageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
//        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding:FullScreenImageFragmentBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.full_screen_image_fragment,
                container,
                false
        )

        val arg = requireNotNull(arguments)
        val imageUrl = FullScreenImageFragmentArgs.fromBundle(arg).imageUrl
        val application = requireNotNull(activity).application
        val position=FullScreenImageFragmentArgs.fromBundle(arg).position

//        ViewCompat.setTransitionName(binding.imageView6, "Test_$position")



        binding.setLifecycleOwner(this)
        binding.imageUrl = imageUrl


        return binding.root

    }




}
