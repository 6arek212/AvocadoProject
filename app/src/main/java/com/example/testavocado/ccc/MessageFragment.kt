package com.example.testavocado.ccc

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testavocado.BaseActivity
import com.example.testavocado.EditeInfo.ProfilePhotoUploadFragment
import com.example.testavocado.EditeInfo.ProfilePhotoUploadFragment.PHOTO_CODE
import com.example.testavocado.GalleryAndPicSnap.GetaPicActivity
import com.example.testavocado.R
import com.example.testavocado.Utils.HelpMethods
import com.example.testavocado.Utils.Permissions
import com.example.testavocado.databinding.MessageFragmentBinding
import com.google.android.gms.location.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
import com.theartofdev.edmodo.cropper.CropImageView


class MessageFragment : Fragment() {


    private lateinit var viewModel: MessageViewModel
    private lateinit var userLocationClient: FusedLocationProviderClient
    private lateinit var userLocationCallback: LocationCallback


    private val PICK_CONTACT = 11
    var stopped: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: MessageFragmentBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.message_fragment,
                container,
                false
        )
        val arg = requireNotNull(arguments)
        val chat = MessageFragmentArgs.fromBundle(arg).chat


        val application = requireNotNull(activity).application
        Log.d("chattocheck", "$chat  userIDDDDDDD ${HelpMethods.checkSharedPreferencesForUserId(application)}")

        val viewModelFactory = ChatViewModelFactory(application, chat)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MessageViewModel::class.java)
        binding.typing.visibility = View.GONE



        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.chat = chat


        val adapter = Adapter(HelpMethods.checkSharedPreferencesForUserId(application), { imageUrl, imageView, position ->
            imageUrl?.let {
                Log.d("imageViewAnimation", "$imageUrl $imageView")
                findNavController().navigate(MessageFragmentDirections.actionMessageFragmentToFullScreenImageFragment(it, position),
                        FragmentNavigator.Extras.Builder()
                                .addSharedElement(imageView, ViewCompat.getTransitionName(imageView)!!)
                                .build())
            }
        }, { longtit, latit ->
            //open waze

            try {
                val url = "waze://?ll=$latit,$longtit&navigate=yes"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"))
                startActivity(intent)
            }


        }, { messageId ->

            //remove Message
            alertRemoveMessage(messageId)
        }, { number ->
            //open dialer
            number?.let {
                openDialer(number)
            }
        })

        val ln = LinearLayoutManager(context)
        ln.reverseLayout = true
        binding.recyclerView.layoutManager = ln
        binding.recyclerView.adapter = adapter
        binding.recyclerView.scrollToPosition(0)
        binding.recyclerView.apply {
            postponeEnterTransition()
            viewTreeObserver
                    .addOnPreDrawListener {
                        startPostponedEnterTransition()
                        true
                    }
        }


        viewModel.error.observe(this, Observer {
            Log.d("errorMeSSAGE", "$it")
            if (!it.isNullOrEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show();
                viewModel.showErrorComplete()
            }
        })


        //close click
        binding.close.setOnClickListener {
            if (activity?.intent?.extras != null)
                activity?.finish()
            else
                findNavController().popBackStack()
        }


        //adding messages
        viewModel.messages.observe(this, Observer {
            adapter.submitList(it)
        })


        //scroll to the top
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                //binding.recyclerView.scrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                //binding.recyclerView.scrollToPosition(0)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                //binding.recyclerView.scrollToPosition(0)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (stopped) {
                    stopped = false
                } else {
                    binding.recyclerView.scrollToPosition(0)
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                //binding.recyclerView.scrollToPosition(0)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                //binding.recyclerView.scrollToPosition(0)
            }
        })



        viewModel.clearText.observe(this, Observer {
            if (it) {
                binding.text.setText("")
                viewModel.clearTextComplete()
            }
        })





        viewModel.showMessageEmptyText.observe(this, Observer {
            if (it) {
                Toast.makeText(context, getString(R.string.cant_send_empty_message), Toast.LENGTH_SHORT).show()
                viewModel.messageShowComplete()
            }
        })

        binding.pic.setOnClickListener {
            val intent = Intent(context, GetaPicActivity::class.java)
            startActivityForResult(intent, ProfilePhotoUploadFragment.PHOTO_CODE)
        }


        when (activity) {
            is MainActivity -> userLocationClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity)
            is BaseActivity -> userLocationClient = LocationServices.getFusedLocationProviderClient(activity as BaseActivity)
        }




        userLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                Log.i("LocationListener", "$locationResult")
                if (locationResult == null) {
                    Toast.makeText(context, getString(R.string.GPS_ERROR), Toast.LENGTH_SHORT).show();
                    return
                }


                val long = locationResult.locations.get(0).longitude
                val latit = locationResult.locations.get(0).latitude

                Log.i("LocationListener", long.toString())
                Log.i("LocationListener", latit.toString())

                viewModel.locationMessage(long, latit)

                userLocationClient.removeLocationUpdates(userLocationCallback)
            }

        }

        binding.contact.setOnClickListener {
            openContact()
        }



        binding.location.setOnClickListener {
            alert()
        }




        return binding.root
    }


    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            activity?.let {
                Toast.makeText(it, "got location", Toast.LENGTH_SHORT).show()
            }

            val long = location.longitude
            val latit = location.latitude

            Log.i("LocationListener", long.toString())
            Log.i("LocationListener", latit.toString())

            viewModel.locationMessage(long, latit)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.d("LocationListener", "onStatusChanged $status  $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.d("LocationListener", "onProviderEnabled   $provider")

        }

        override fun onProviderDisabled(provider: String) {
            Log.d("LocationListener", "onProviderDisabled   $provider")
        }
    }



    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }


    fun location() {
        context?.let {
            if (ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
                val str = arrayOf(Permissions.GPS, Permissions.GPS2)
                Permissions.verifyPermission(str, activity)
                return
            }
            val mLocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
            }

            mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null)
            Toast.makeText(context, "We are getting location it may take few seconds", Toast.LENGTH_SHORT).show();
        }
    }


    private fun launchImageCrop(uri: Uri) {
        context?.let {
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE) // default is rectangle
                    .start(it, this)
        }
    }


    fun alert() {
        val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogStyle)

        val userLocationRequest = LocationRequest().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        alertDialog.apply {
            setTitle(getString(R.string.location))
            setMessage(getString(R.string.send_location))

            setPositiveButton(getString(R.string.yes)) { dialog, which ->
                //userLocationClient.requestLocationUpdates(userLocationRequest, userLocationCallback, null)
                location()
            }

            setNegativeButton(getString(R.string.no), null)
            setIcon(android.R.drawable.ic_dialog_alert)


            show()
        }
    }


    fun alertRemoveMessage(messageId: String) {
        val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogStyle)

        val userLocationRequest = LocationRequest().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        alertDialog.apply {
            setTitle("Delete Message")
            setMessage("are you sure to delete the message ?")

            setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.removeMessage(messageId)
            }

            setNegativeButton(getString(R.string.no), null)
            setIcon(android.R.drawable.ic_dialog_alert)


            show()
        }
    }


    class Contact(val name: String, val number: String)

    fun sendContactAlert(contact: Contact) {
        val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogStyle)

        alertDialog.apply {
            setTitle(getString(R.string.send_contact))
            setMessage("${getString(R.string.are_you_sure_send_contact)} ${contact.name} ?")

            setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.sendContact(contact)
            }

            setNegativeButton(getString(R.string.no), null)
            setIcon(android.R.drawable.ic_dialog_alert)


            show()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.textToSend.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                viewModel.typing(false)
            } else {
                viewModel.typing(true)
            }
        })
    }


    fun openContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)
        startActivityForResult(intent, PICK_CONTACT)
    }

    fun openDialer(number: String) {
        val intent = Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:$number"));
        startActivity(intent);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PHOTO_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data?.extras!!.get(getString(R.string.imagePath)) != null) {
                        val imagePath = data.extras!!.getString(getString(R.string.imagePath))
                        imagePath?.let {
                            val uri = Uri.parse("file://" + imagePath)
                            Log.d("gotImage", "$uri")
                            launchImageCrop(uri)
                        }
                    }
                }
            }

            CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val imagePath = result.uri

                    Log.d("gotImage", "$imagePath")
                    viewModel.chat?.let {
                        if (activity?.intent?.extras != null) {
                            val bundle=Bundle()
                            bundle.putParcelable("imageUrl",imagePath)
                            bundle.putParcelable("chat",it)
                            findNavController().navigate(R.id.action_messageFragment2_to_messageWithPicFragment2,bundle)

                        } else
                            findNavController().navigate(MessageFragmentDirections.actionMessageFragmentToMessageWithPicFragment(imagePath, it))
                    }
                }
            }

            PICK_CONTACT -> {

                val TAG = "MessageFragment"

                if (resultCode == Activity.RESULT_OK) {
                    val contactsData = data?.getData()
                    if (contactsData != null) {
                        val loader = CursorLoader(requireNotNull(context), contactsData, null, null, null, null);
                        val c = loader.loadInBackground();

                        c?.let {
                            if (c.moveToFirst()) {
                                val contact = Contact(name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                        number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
                                sendContactAlert(contact)
                            }
                        }
                    }

                }


            }

        }


    }


    override fun onStop() {
        super.onStop()
        Log.d("MessageFragment", "onStop")
        stopped = true
    }


    class ChatViewModelFactory(
            private val application: Application,
            private val chat: Chat3

    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
                return MessageViewModel(application, chat) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}