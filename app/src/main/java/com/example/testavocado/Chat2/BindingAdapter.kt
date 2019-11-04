package com.example.testavocado.Chat2

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testavocado.R
import com.example.testavocado.Utils.TimeMethods

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Log.d("BindingAdapter", "${imgUrl}")

        Glide.with(imgView.context)
                .load(imgUrl)
                .centerCrop()
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.profile_ic)
                )
                .into(imgView)



}

@BindingAdapter("time")
fun time(view: TextView, time: String?) {
    time?.let {
        view.text = TimeMethods.convertDateTimeFormat2(time)
    }
}