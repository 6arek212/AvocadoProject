package com.example.testavocado.ccc

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testavocado.EditeInfo.ProfilePhotoUploadFragment
import com.example.testavocado.GalleryAndPicSnap.GetaPicActivity
import com.example.testavocado.R
import com.example.testavocado.Utils.HelpMethods
import com.example.testavocado.databinding.MessageFragmentBinding
import com.google.android.gms.location.*
import java.io.File
import java.lang.Exception


class MessageFragment : Fragment() {



    private lateinit var viewModel: MessageViewModel
    private lateinit var userLocationClient: FusedLocationProviderClient
    private lateinit var userLocationCallback: LocationCallback


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
        val chat= MessageFragmentArgs.fromBundle(arg).chat


        val application= requireNotNull(activity).application
        Log.d("chattocheck","$chat  userIDDDDDDD ${HelpMethods.checkSharedPreferencesForUserId(application)}")

        val viewModelFactory=ChatViewModelFactory(application,chat)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MessageViewModel::class.java)
        binding.typing.visibility=View.GONE



        binding.setLifecycleOwner(this)
        binding.viewModel=viewModel
        binding.chat=chat



        val adapter=Adapter(HelpMethods.checkSharedPreferencesForUserId(application),{
            imageUrl ->
            imageUrl?.let {
                findNavController().navigate(MessageFragmentDirections.actionMessageFragmentToFullScreenImageFragment(it))
            }
        },{
            longtit, latit ->
            //open waze

            try {
                val url = "waze://?ll=$latit,$longtit&navigate=yes"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"))
                startActivity(intent)
            }


        })

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
            findNavController().popBackStack()
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


//        viewModel.typing.observe(this, Observer {
//            Log.d("typingtyping","$it")
//            if (it){
//                binding.typing.visibility=View.VISIBLE
//            }else
//            {
//                binding.typing.visibility=View.GONE
//            }
//        })


        viewModel.showMessageEmptyText.observe(this, Observer {
            if(it){
                Toast.makeText(context, getString(R.string.cant_send_empty_message), Toast.LENGTH_SHORT).show()
                viewModel.messageShowComplete()
            }
        })

        binding.pic.setOnClickListener{
            val intent = Intent(context, GetaPicActivity::class.java)
            startActivityForResult(intent, ProfilePhotoUploadFragment.PHOTO_CODE)
        }


        userLocationClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity)




        userLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                Log.i("LocationListener", "$locationResult")
                locationResult ?: return


                val long=locationResult.locations.get(0).longitude
                val latit=locationResult.locations.get(0).latitude

                Log.i("LocationListener", long.toString())
                Log.i("LocationListener", latit.toString())

                viewModel.locationMessage(long,latit)

                userLocationClient.removeLocationUpdates(userLocationCallback)
            }

        }



        binding.location.setOnClickListener{
            alert()
        }

        return binding.root
    }


    fun alert() {
        val alertDialog = AlertDialog.Builder(context,R.style.AlertDialogStyle)

        val userLocationRequest = LocationRequest().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        alertDialog.apply {
            setTitle("location")
            setMessage("send your location ?")

            setPositiveButton("yes"){ dialog, which ->
                userLocationClient.requestLocationUpdates(userLocationRequest,userLocationCallback,null)
            }

            setNegativeButton("No", null)
            setIcon(android.R.drawable.ic_dialog_alert)


            show()
        }
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








    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ProfilePhotoUploadFragment.PHOTO_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                if (data.extras!!.get(getString(R.string.imagePath)) != null) {
                    viewModel.chat?.let {
                        val imagePath = data.extras!!.getString(getString(R.string.imagePath))
                        findNavController().navigate(MessageFragmentDirections.actionMessageFragmentToMessageWithPicFragment(imagePath,it))

                    }

                }

            }
        }
    }



}


class ChatViewModelFactory(
    private val application: Application,
    private val chat: Chat3

) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            return MessageViewModel(application,chat) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}