package com.example.testavocado.post

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testavocado.Models.Post
import com.example.testavocado.R
import com.example.testavocado.Utils.PostTypes
import com.example.testavocado.ccc.newchat.NewChatViewModel
import com.example.testavocado.databinding.UpdatePostFragmentBinding
import java.util.*

class UpdatePostFragment : Fragment() {

    companion object {
        fun newInstance() = UpdatePostFragment()
    }

    private lateinit var viewModel: UpdatePostViewModel
    private lateinit var binding: UpdatePostFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.update_post_fragment,
                container,
                false
        )

        val post = arguments?.get("post")as Post
        Log.d("UpdatePostFragment","$post")

        val factory = UpdatePostViewModelFactory(requireActivity().application, post)
        viewModel = ViewModelProviders.of(this, factory).get(UpdatePostViewModel::class.java)

        binding.setLifecycleOwner (this)
        binding.viewModel=viewModel

        initSpinner()

        binding.postText.setText(viewModel.post.post_text)

        val adapter = UpdateHorizotalRecyclerImageSlide(context)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            this.adapter = adapter
        }


        viewModel.message.observe(this, androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()){
                Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show();
                viewModel.messageShowd()
            }
        })


        binding.close.setOnClickListener{
            fragmentManager?.popBackStack()
        }


        viewModel.navigateBack.observe(this, androidx.lifecycle.Observer {
            if (it){
                fragmentManager?.popBackStack()
                viewModel.navigated()
            }
        })

        adapter.addAllImages(viewModel.post.post_images_url, adapter.itemCount)

        return binding.root
    }


    /**
     * initializing the post type spinner
     *
     *
     * filling the spinner with values
     */
    private fun initSpinner() {
        val postTypes = ArrayList<String>()
        val post_types = PostTypes.getPostTypes(context)
        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, post_types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.setAdapter(arrayAdapter)

        if (viewModel.post.post_type == 0) {
            binding.spinner.setSelection(0)
        } else {
            binding.spinner.setSelection(1)
        }
    }


    class UpdatePostViewModelFactory(
            private val application: Application,
            private val post: Post

    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UpdatePostViewModel::class.java)) {
                return UpdatePostViewModel(application, post) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
